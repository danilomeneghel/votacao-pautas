package app.controller;

import io.quarkus.panache.common.Sort;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.google.common.collect.ImmutableMap;

import app.model.User;
import app.service.UserService;

@Path("/users")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class UserController {

    @Inject
    UserService userService;

    @Inject
    Template error;
    
    @Inject
    Template userForm;
    
    @Inject
    Template userView;
    
    @Inject
    Template users;

    public List<String> role = Arrays.asList("Administrador", "Usuário", "Associado");
    public Map<String, String> status = ImmutableMap.of("ABLE_TO_VOTE", "Ativo", "UNABLE_TO_VOTE", "Inativo");
    
    @GET
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listUsers(@QueryParam("filter") String filter) {
        return users.data("users", find(filter))
            .data("filter", filter)
            .data("filtered", filter != null && !filter.isEmpty());
    }

    private List<User> find(String filter) {
        Sort sort = Sort.ascending("name");

        if (filter != null && !filter.isEmpty()) {
            return User.find("LOWER(name) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        }
        else {
            return User.findAll(sort).list();
        }
    }

    @GET
    @Path("/id/{id}")
    public TemplateInstance getId(@PathParam("id") Long id) {
        User user = userService.findUser(id);
        if (user == null) {
            return error.data("error", "Usuário com id " + id + " não encontrado.");
        }
        
        return userView.data("user", user);
    }
    
    @GET
    @Path("/{cpf}")
    public TemplateInstance getCpf(@PathParam("cpf") Long cpf) {
    	User user = userService.findUserCpf(cpf);
    	if (user == null) {
            return error.data("error", "Usuário com cpf " + cpf + " não encontrado.");
        }
    	
    	return userView.data("user", user);
    }

    @GET
    @Path("/new")
    public TemplateInstance addForm() {
        return userForm.data("role", role)
        	.data("status", status);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    @Path("/new")
    public Response addUser(@MultipartForm User user) {
    	userService.insert(user);

    	return Response.seeOther(URI.create("/users")).build();
    }
    
    @GET
    @Path("/{id}/edit")
    public TemplateInstance updateForm(@PathParam("id") long id) {
        User loaded = userService.findUser(id);
        if (loaded == null) {
            return error.data("error", "Usuário com id " + id + " não encontrado.");
        }
		
        return userForm.data("user", loaded)
        	.data("role", role)
        	.data("status", status)
            .data("update", true);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    @Path("/{id}/edit")
    public Object updateUser(@PathParam("id") long id, @MultipartForm User user) {
    	User loaded = userService.findUser(id);
    	
        if (user == null) {
            return error.data("error", "Usuário com id " + id + " não encontrado.");
        }

        userService.update(loaded, user);

        return Response.seeOther(URI.create("/users")).build();
    }

    @POST
    @Transactional
    @Path("/{id}/delete")
    public Response deleteUser(@PathParam("id") long id) {
        User.delete("id", id);

        return Response.seeOther(URI.create("/users")).build();
    }
}
