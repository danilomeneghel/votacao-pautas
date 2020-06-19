package app.controller;

import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import app.enumerator.StatusSessaoEnum;
import app.model.Pauta;
import app.model.Sessao;
import app.service.PautaService;
import app.service.SessaoService;

@Path("/sessoes")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class SessaoController {

    @Inject
    SessaoService sessaoService;
    
    @Inject
    PautaService pautaService;

    @Inject
    Template error;
    
    @Inject
    Template sessaoForm;
    
    @Inject
    Template sessaoView;
    
    @Inject
    Template sessoes;
        
    @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listSessoes(@QueryParam("filter") String filter) {
        return sessoes.data("sessoes", find(filter))
            .data("filter", filter)
            .data("filtered", filter != null && !filter.isEmpty());
    }

    @GET
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sessao> listSessoesJson() {
        return sessaoService.findAllSessoes();
    }
    
    private List<Sessao> find(String filter) {
        Sort sort = Sort.ascending("nome");

        if (filter != null && !filter.isEmpty()) {
            return Sessao.find("LOWER(nome) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        }
        else {
            return Sessao.findAll(sort).list();
        }
    }

    @GET
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object getIdJson(@PathParam("id") Long id) {
    	Sessao sessao = sessaoService.findSessao(id);
    	if (sessao == null) {
            return error.data("error", "Sessao com id " + id + " não encontrada.");
        }
    	
        return Response.status(Response.Status.OK).build();
    }
    
    @GET
    @Path("/view/{id}")
    public TemplateInstance getId(@PathParam("id") Long id) {
        Sessao sessao = sessaoService.findSessao(id);
        if (sessao == null) {
            return error.data("error", "Sessao com id " + id + " não encontrada.");
        }
        
        return sessaoView.data("sessao", sessao)
        		.data("status", StatusSessaoEnum.values());
    }

    @GET
    @Path("/new")
    public TemplateInstance addForm() {
    	List<Pauta> pautas = pautaService.findPautasAtivas();
    	if(pautas.isEmpty()) {
    		return error.data("error", "É preciso ter pelo menos uma Pauta ativa");
    	}
        return sessaoForm.data("status", StatusSessaoEnum.values())
        		.data("pautas", pautas);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/new")
    public Object addSessao(@MultipartForm @Valid Sessao sessao) {
    	if(sessao.idpauta != null) {
	    	Sessao loaded = sessaoService.findSessaoPauta(sessao.idpauta);
	    	if (loaded != null) {
	            return error.data("error", "Sessao já criada para essa Pauta de id " + sessao.idpauta);
	        }    	
	    	sessaoService.insert(sessao);
    	} else {
    		return error.data("error", "É necessário ter uma Pauta selecionada");
    	}
    	
    	return Response.seeOther(URI.create("/sessoes")).build();
    }
    
    @GET
    @Path("/edit/{id}")
    public TemplateInstance updateForm(@PathParam("id") long id) {
    	List<Pauta> pautas = pautaService.findPautasAtivas();
    	if(pautas.isEmpty()) {
    		return error.data("error", "É preciso ter pelo menos uma Pauta ativa");
    	}
    	
        Sessao loaded = sessaoService.findSessao(id);
        if (loaded == null) {
            return error.data("error", "Sessao com id " + id + " não encontrada.");
        }
		
        return sessaoForm.data("sessao", loaded)
        	.data("status", StatusSessaoEnum.values())
        	.data("pautas", pautas)
            .data("update", true);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/edit/{id}")
    public Object updateSessao(@PathParam("id") long id, @MultipartForm @Valid Sessao sessao) {
    	Sessao loaded = sessaoService.findSessao(id);    	
        if (loaded == null) {
            return error.data("error", "Sessao com id " + id + " não encontrada.");
        }

        sessaoService.update(loaded, sessao);

        return Response.seeOther(URI.create("/sessoes")).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/delete/{id}")
    public Response deleteSessao(@PathParam("id") long id) {
        Sessao.delete("id", id);

        return Response.seeOther(URI.create("/sessoes")).build();
    }
}
