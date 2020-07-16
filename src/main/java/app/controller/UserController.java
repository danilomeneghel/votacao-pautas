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

import at.favre.lib.crypto.bcrypt.BCrypt;

import app.enumerator.RoleUserEnum;
import app.enumerator.StatusUserEnum;
import app.model.Message;
import app.model.User;
import app.service.UserService;
import app.util.CpfValidate;
import app.util.ExternalApi;
import app.security.TokenService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserController {

    @Inject
    UserService userService;

    @Inject
    TokenService service;
    
    @Inject
    Template userForm;
    
    @Inject
    Template userView;
    
    @Inject
    Template users;

    @Inject
    Template usersList;
    
    @Inject
    Template perfil;
    
    @Inject
    Template perfilForm;
    
    @Inject
    JsonWebToken jwt;
    
    public Message message = new Message();
    
    @GET
    @PermitAll
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance users() {
        return users.instance();
    }
    
    @GET
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/content")
    public Object listUsers(@QueryParam("filter") String filter) {
        if(jwt.getName() == null)
            return Response.status(Status.UNAUTHORIZED).build();

        Set<String> groups = jwt.getGroups();
        String role = String.join(", ", groups);

        return usersList.data("users", find(filter))
            .data("role", role)
            .data("filter", filter)
            .data("filtered", filter != null && !filter.isEmpty());
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<User> listUsersJson() {
        return userService.findAllUsers();
    }
    
    public List<User> find(String filter) {
        Sort sort = Sort.ascending("name");

        if (filter != null && !filter.isEmpty()) {
            return User.find("LOWER(name) LIKE LOWER(?1)", sort, "%" + filter + "%").list();
        } else {
            return User.findAll(sort).list();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cpf/{cpf}")
    public Object getCpfJson(@PathParam("cpf") Long cpf) {
    	User user = userService.findUserCpf(cpf);
    	if (user == null) {
            return userForm.data("error", "Usuário com CPF " + cpf + " não encontrado.");
        }
    	StatusUserEnum statusCpf = ExternalApi.getStatusCpf(user.cpf);
    	user.status = statusCpf;
    	
        return user;
    }
    
    @GET
    @Path("/view/{id}")
    public TemplateInstance getId(@PathParam("id") Long id) {
    	User user = userService.findUser(id);
    	if (user == null) {
            return userView.data("error", "Usuário com id " + id + " não encontrado.");
        }
    	StatusUserEnum statusCpf = ExternalApi.getStatusCpf(user.cpf);
    	user.status = statusCpf;
    	
    	return userView.data("user", user);
    }

    @GET
    @Path("/new")
    public TemplateInstance addForm() {
        return userForm.data("roles", RoleUserEnum.values());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/new")
    public Object addUser(@MultipartForm @Valid User user) {
    	User loaded = userService.findUserCpf(user.cpf);
    	if (loaded != null) {
            return userForm.data("error", "CPF " + user.cpf + " já cadastrado.");
        } else {
            user.password = BCrypt.withDefaults().hashToString(12, user.password.toCharArray());
        	boolean validaCpf = CpfValidate.isCPF(user.cpf.toString());
        	if(validaCpf)
        		userService.insert(user);
        	else
        		return userForm.data("error", "Usuário com CPF inválido!");
        }
    	
    	message.type = "success";
        message.title = "Sucesso!";
        message.description = "Usuário cadastrado com sucesso.";
        return Response.ok(message).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/add")
    public Object add(User user) {
        user.password = BCrypt.withDefaults().hashToString(12, user.password.toCharArray());
    	userService.insert(user);    	
    	
    	return user;
    }
    
    @GET
    @Path("/perfil")
    public TemplateInstance perfil() {
        return perfil.instance();
    }
    
    @GET
    @RolesAllowed({"ADMIN", "ASSOC"})
    @Path("/perfil/content")
    public Object perfilForm() {
        if(jwt.getName() == null)
            return Response.status(Status.UNAUTHORIZED).build();
        
        String username = jwt.getName();
        Set<String> groups = jwt.getGroups();
        String role = String.join(", ", groups);

        User loaded = userService.findUsername(username);
        if (loaded == null) {
            return perfilForm.data("error", "Usuário não encontrado.");
        }
		
        return perfilForm.data("user", loaded)
            .data("role", role)
            .data("roles", RoleUserEnum.values())
            .data("error", null);
    }
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/perfil/edit/")
    public Object updatePerfil(@MultipartForm @Valid User user) {
        User loaded = userService.findUsername(user.username);

        if (loaded == null) {
            return perfil.data("error", "Usuário não encontrado.");
        } else {
            user.password = BCrypt.withDefaults().hashToString(12, user.password.toCharArray());
            userService.update(loaded, user);
            return perfil.data("success", "Perfil atualizado com sucesso!")
                .data("error", null);
        }
    }

    @GET
    @Path("/edit/{id}")
    public TemplateInstance updateForm(@PathParam("id") long id) {
        User loaded = userService.findUser(id);
        if (loaded == null) {
            return userForm.data("error", "Usuário com id " + id + " não encontrado.");
        }
		
        return userForm.data("user", loaded)
            .data("roles", RoleUserEnum.values())
            .data("update", true);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/edit/{id}")
    public Object updateUser(@PathParam("id") long id, @MultipartForm @Valid User user) {
    	User loaded = userService.findUser(id);    	
        if (loaded == null) {
            return userForm.data("error", "Usuário com id " + id + " não encontrado.");
        } else {
            user.password = BCrypt.withDefaults().hashToString(12, user.password.toCharArray());
        	boolean validaCpf = CpfValidate.isCPF(user.cpf.toString());
        	if(validaCpf)
        		userService.update(loaded, user);
        	else
        		return userForm.data("error", "Usuário com CPF inválido!");
        }
    	
        message.type = "success";
        message.title = "Sucesso!";
        message.description = "Usuário editado com sucesso.";
        return Response.ok(message).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/delete/{id}")
    public Response deleteUser(@PathParam("id") long id) {
        User.delete("id", id);

        message.type = "success";
        message.title = "Sucesso!";
        message.description = "Usuário excluído com sucesso.";
        return Response.ok(message).build();
    }
    
}
