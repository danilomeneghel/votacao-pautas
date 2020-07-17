package app.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.annotation.security.PermitAll;

import java.util.Set;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import at.favre.lib.crypto.bcrypt.BCrypt;


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
    Template login;
    
    @Inject
    JsonWebToken jwt;
        
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @PermitAll
    @Path("/cadastrar")
    public Object cadastrarUser(@MultipartForm @Valid User user) {
    	User loaded = userService.findUsernameCpf(user);
    	if (loaded != null) {
            return login.data("error", "CPF ou Usuário já cadastrado.");
        } else {
        	boolean validaCpf = CpfValidate.isCPF(user.cpf.toString());
        	if(validaCpf) {
                user.password = BCrypt.withDefaults().hashToString(12, user.password.toCharArray());
                user.role = RoleUserEnum.ASSOC;
                userService.insert(user);
                return login.data("success", "Cadastro realizado com sucesso!");
            } else {
                return login.data("error", "Usuário com CPF inválido!");
            }
        }
    }

    @GET
    @PermitAll
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
        if(user != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(log.password.toCharArray(), user.password);

            if(result.verified) {
                String token = service.generateUserToken(user.email, log.username, user.role.toString());
                log.token = token;
                log.cpf = user.cpf.toString();
                return log;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/user")
    public Object userLogged() {
        Login log = new Login();
        if(jwt.getName() == null)
            return Response.status(Status.UNAUTHORIZED).build();
        
        log.username = jwt.getName();
        Set<String> groups = jwt.getGroups();
        log.role = String.join(", ", groups);

        return log;
    }
    
}