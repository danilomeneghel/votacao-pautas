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

import app.enumerator.StatusPautaEnum;
import app.enumerator.VotoEnum;
import app.model.Message;
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
    Template pautaForm;
    
    @Inject
    Template pautaView;
    
    @Inject
    Template pautas;
    
    @Inject
    Template pautasList;
    
    @Inject
    JsonWebToken jwt;
    
    public Message message = new Message();

    @GET
    @PermitAll
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance pautas() {
        return pautas.instance();
    }
    
    @GET
    @RolesAllowed({"ADMIN", "ASSOC"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/content")
    public Object listPautas(@QueryParam("filter") String filter) {
        if(jwt.getName() == null)
            return Response.status(Status.UNAUTHORIZED).build();

        String username = jwt.getName();
        Set<String> groups = jwt.getGroups();
        String role = String.join(", ", groups);
        
        User user = userService.findUsername(username);
        Long cpf = (user != null) ? user.cpf : 0;

        return pautasList.data("pautas", find(filter))
            .data("role", role)
            .data("cpf", cpf)
            .data("filter", filter)
            .data("filtered", filter != null && !filter.isEmpty());
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<Pauta> listPautasJson() {
        return pautaService.findAllPautas();
    }
    
    public List<Pauta> find(String filter) {
        Sort sort = Sort.ascending("titulo");

        if (filter != null && !filter.isEmpty()) {
            return Pauta.find("LOWER(titulo) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        } else {
            return Pauta.findAll(sort).list();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/id/{id}")
    public Object getIdJson(@PathParam("id") Long id) {
    	return pautaService.findPauta(id);
    }
    
    @GET
    @Path("/view/{id}/{cpf}")
    public TemplateInstance getId(@PathParam("id") Long id, @PathParam("cpf") Long cpf) {
        Pauta pauta = pautaService.findPauta(id);
        if (pauta == null) {
            return pautaView.data("error", "Pauta com id " + id + " não encontrada.");
        }
        
        return pautaView.data("pauta", pauta)
                .data("cpf", cpf)
        		.data("voto", VotoEnum.values());
    }

    @GET
    @Path("/new")
    public TemplateInstance addForm() {
    	List<User> user = userService.findAllUsers();
    	if(user.isEmpty()) {
    		return pautaForm.data("error", "É necessário primeiro criar um usuário para depois votar na Pauta.");
    	}
    	
        return pautaForm.data("status", StatusPautaEnum.values());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/new")
    public Object addPauta(@MultipartForm @Valid Pauta pauta) {
    	Pauta loaded = pautaService.findPautaTitulo(pauta.titulo);
    	if (loaded != null) {
            return pautaForm.data("error", "Pauta com título '" + pauta.titulo + "' já cadastrada.");
        }
    	pautaService.insert(pauta);    	
    	
    	message.type = "success";
        message.title = "Sucesso!";
        message.description = "Pauta cadastrada com sucesso.";
        return Response.ok(message).build();
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
            return pautaForm.data("error", "Pauta com id " + id + " não encontrada.");
        }
		
        return pautaForm.data("pauta", loaded)
        	.data("status", StatusPautaEnum.values())
            .data("update", true);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/edit/{id}")
    public Object updatePauta(@PathParam("id") long id, @MultipartForm @Valid Pauta pauta) {
    	Pauta loaded = pautaService.findPauta(id);    	
        if (loaded == null) {
            return pautaForm.data("error", "Pauta com id " + id + " não encontrada.");
        }

        pautaService.update(loaded, pauta);

        message.type = "success";
        message.title = "Sucesso!";
        message.description = "Pauta editada com sucesso.";
        return Response.ok(message).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/delete/{id}")
    public Response deletePauta(@PathParam("id") long id) {
        Pauta.delete("id", id);

        message.type = "success";
        message.title = "Sucesso!";
        message.description = "Pauta excluida com sucesso.";
        return Response.ok(message).build();
    }
}
