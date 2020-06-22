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

import app.enumerator.StatusPautaEnum;
import app.enumerator.VotoEnum;
import app.model.Pauta;
import app.model.User;
import app.service.PautaService;
import app.service.UserService;

@Path("/pautas")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class PautaController {

    @Inject
    PautaService pautaService;

    @Inject
    UserService userService;
    
    @Inject
    Template error;
    
    @Inject
    Template pautaForm;
    
    @Inject
    Template pautaView;
    
    @Inject
    Template pautas;
    
    @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listPautas(@QueryParam("filter") String filter) {
        return pautas.data("pautas", find(filter))
            .data("filter", filter)
            .data("filtered", filter != null && !filter.isEmpty());
    }

    @GET
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pauta> listPautasJson() {
        return pautaService.findAllPautas();
    }
    
    public List<Pauta> find(String filter) {
        Sort sort = Sort.ascending("titulo");

        if (filter != null && !filter.isEmpty()) {
            return Pauta.find("LOWER(titulo) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        }
        else {
            return Pauta.findAll(sort).list();
        }
    }

    @GET
    @Path("/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object getIdJson(@PathParam("id") Long id) {
    	return pautaService.findPauta(id);
    }
    
    @GET
    @Path("/view/{id}")
    public TemplateInstance getId(@PathParam("id") Long id) {
        Pauta pauta = pautaService.findPauta(id);
        if (pauta == null) {
            return error.data("error", "Pauta com id " + id + " não encontrada.");
        }
        
        //Localiza o primeiro usuário cadastrado para pegar o CPF (teste para votar)
        User user = userService.findFirstUser();
                
        return pautaView.data("pauta", pauta)
        		.data("cpf", user.cpf)
        		.data("voto", VotoEnum.values());
    }

    @GET
    @Path("/new")
    public TemplateInstance addForm() {
    	List<User> user = userService.findAllUsers();
    	if(user.isEmpty()) {
    		return error.data("error", "É necessário primeiro criar um usuário para depois votar na Pauta.");
    	}
    	
        return pautaForm.data("status", StatusPautaEnum.values());
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/new")
    public Object addPauta(@MultipartForm @Valid Pauta pauta) {
    	Pauta loaded = pautaService.findPautaTitulo(pauta.titulo);
    	if (loaded != null) {
            return error.data("error", "Pauta com título '" + pauta.titulo + "' já cadastrada.");
        }
    	pautaService.insert(pauta);    	
    	
    	return Response.seeOther(URI.create("/pautas")).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/add")
    public Object add(Pauta pauta) {
    	pautaService.insert(pauta);    	
    	
    	return pauta;
    }
    
    @GET
    @Path("/edit/{id}")
    public TemplateInstance updateForm(@PathParam("id") long id) {
        Pauta loaded = pautaService.findPauta(id);
        if (loaded == null) {
            return error.data("error", "Pauta com id " + id + " não encontrada.");
        }
		
        return pautaForm.data("pauta", loaded)
        	.data("status", StatusPautaEnum.values())
            .data("update", true);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/edit/{id}")
    public Object updatePauta(@PathParam("id") long id, @MultipartForm @Valid Pauta pauta) {
    	Pauta loaded = pautaService.findPauta(id);    	
        if (loaded == null) {
            return error.data("error", "Pauta com id " + id + " não encontrada.");
        }

        pautaService.update(loaded, pauta);

        return Response.seeOther(URI.create("/pautas")).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/delete/{id}")
    public Response deletePauta(@PathParam("id") long id) {
        Pauta.delete("id", id);

        return Response.seeOther(URI.create("/pautas")).build();
    }
}
