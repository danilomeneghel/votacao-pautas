package app.controller;

import java.security.Principal;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import app.enumerator.RoleUserEnum;
import app.model.Login;
import app.model.User;
import app.security.TokenService;
import app.service.UserService;
import app.util.CpfValidate;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthController {
    
    @Inject
    UserService userService;

    @Inject
    TokenService service;
    
    @Inject
    Template cadastro;
    
    @Inject
    Template login;
    
    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("/cadastro")
    public TemplateInstance cadastroForm() {
        return cadastro.instance();
    }
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PermitAll
    @Path("/cadastrar")
    public Object cadastrarUser(@MultipartForm @Valid User user) {
    	User loaded = userService.findUserCpf(user.cpf);
    	if (loaded != null) {
            return cadastro.data("error", "CPF " + user.cpf + " já cadastrado.");
        } else {
        	boolean validaCpf = CpfValidate.isCPF(user.cpf.toString());
        	if(validaCpf) {
                user.role = RoleUserEnum.ASSOC;
                userService.insert(user);
                return login.data("success", "Cadastro realizado com sucesso!");
            } else {
                return cadastro.data("error", "Usuário com CPF inválido!");
            }
        }
    }

    @GET
    @PermitAll
    @Path("/login")
    public TemplateInstance login() {
        return login.instance();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PermitAll
    @Path("/logar")
    public Object logar(Login log) {
        User user = userService.findUsername(log.username);
        if(user == null || !user.password.equals(log.password)) {
            return false;
        } else {
            String token = service.generateUserToken(user.email, log.username, user.role.toString());
            log.token = token;
            log.cpf = user.cpf.toString();
            return log;
        }
    }

    @GET
    @Path("/user")
    public Object userLogged() {
        Principal caller =  securityContext.getUserPrincipal(); 
        String username = caller == null ? "anonymous" : caller.getName();

        return username;
    }
}