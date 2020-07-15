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
import javax.ws.rs.core.Response.Status;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Set;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import app.enumerator.StatusSessaoEnum;
import app.model.Message;
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
    Template sessaoForm;
    
    @Inject
    Template sessaoView;
    
    @Inject
    Template sessoes;
    
    @Inject
    Template sessoesList;
    
    @Inject
    JsonWebToken jwt;
    
    public Message message = new Message();
    
    @GET
    @PermitAll
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance sessoes() {
        return sessoes.instance();
    }
    
    @GET
    @RolesAllowed({"ADMIN", "ASSOC"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/content")
    public Object listSessoes(@QueryParam("filter") String filter) {
        if(jwt.getName() == null)
            return Response.status(Status.UNAUTHORIZED).build();

        Set<String> groups = jwt.getGroups();
        String role = String.join(", ", groups);
        
        return sessoesList.data("sessoes", find(filter))
            .data("role", role)
            .data("filter", filter)
            .data("filtered", filter != null && !filter.isEmpty());
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<Sessao> listSessoesJson() {
        return sessaoService.findAllSessoes();
    }
    
    public List<Sessao> find(String filter) {
        Sort sort = Sort.ascending("nome");

        if (filter != null && !filter.isEmpty()) {
            return Sessao.find("LOWER(nome) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        } else {
            return Sessao.findAll(sort).list();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Object getIdJson(@PathParam("id") Long id) {
    	return sessaoService.findSessao(id);
    }
    
    @GET
    @Path("/view/{id}")
    public TemplateInstance getId(@PathParam("id") Long id) {
        Sessao sessao = sessaoService.findSessao(id);
        if (sessao == null) {
            return sessaoView.data("error", "Sessao com id " + id + " não encontrada.");
        }
        
        return sessaoView.data("sessao", sessao);
    }

    @GET
    @Path("/new")
    public TemplateInstance addForm() {
        String error = null;
    	List<Pauta> pautas = pautaService.findPautasAtivas();
    	if(pautas.isEmpty()) {
    		error = "É preciso ter pelo menos uma Pauta ativa";
        }
        return sessaoForm.data("status", StatusSessaoEnum.values())
                        .data("pautas", pautas)
                        .data("error", error);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/new")
    public Object addSessao(@MultipartForm @Valid Sessao sessao) {
        if (sessao.idpauta != null) {
            Sessao loaded = sessaoService.findSessaoPauta(sessao.idpauta);
            if (loaded != null) {
                return sessaoForm.data("error", "Sessao já criada para essa Pauta de id " + sessao.idpauta);
            }
            sessaoService.insert(sessao);
        } else {
            return sessaoForm.data("error", "É necessário ter uma Pauta selecionada");
    	}
    	
    	message.type = "success";
        message.title = "Sucesso!";
        message.description = "Sessão cadastrada com sucesso.";
        return Response.ok(message).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/add")
    public Object add(Sessao sessao) {
    	sessaoService.insert(sessao);    	
    	
    	return sessao;
    }
    
    @GET
    @Path("/edit/{id}")
    public TemplateInstance updateForm(@PathParam("id") long id) {
    	List<Pauta> pautas = pautaService.findPautasAtivas();
    	if(pautas.isEmpty()) {
    		return sessaoForm.data("error", "É preciso ter pelo menos uma Pauta ativa");
        }

        Sessao loaded = sessaoService.findSessao(id);
        if (loaded == null) {
            return sessaoForm.data("error", "Sessao com id " + id + " não encontrada.");
        }

        return sessaoForm.data("sessao", loaded).data("status", StatusSessaoEnum.values()).data("pautas", pautas)
                .data("update", true);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/edit/{id}")
    public Object updateSessao(@PathParam("id") long id, @MultipartForm @Valid Sessao sessao) {
        Sessao loaded = sessaoService.findSessao(id);
        if (loaded == null) {
            return sessaoForm.data("error", "Sessao com id " + id + " não encontrada.");
        }

        sessaoService.update(loaded, sessao);

        message.type = "success";
        message.title = "Sucesso!";
        message.description = "Sessão editada com sucesso.";
        return Response.ok(message).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/delete/{id}")
    public Response deleteSessao(@PathParam("id") long id) {
        Sessao.delete("id", id);

        message.type = "success";
        message.title = "Sucesso!";
        message.description = "Sessão excluida com sucesso.";
        return Response.ok(message).build();
    }
}
