package app.controller;

import io.quarkus.qute.Template;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import app.model.Votacao;
import app.model.Sessao;
import app.model.User;

import app.service.VotacaoService;
import app.service.PautaService;
import app.service.SessaoService;
import app.service.UserService;

@Path("/votacao")
@Produces(MediaType.TEXT_HTML)
@ApplicationScoped
public class VotacaoController {

    @Inject
    VotacaoService votacaoService;
    
    @Inject
    SessaoService sessaoService;
    
    @Inject
    PautaService pautaService;
    
    @Inject
    UserService userService;

    @Inject
    Template error;
    
    @Inject
    Template pautas;
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    @Path("/votar")
    public Object votar(@MultipartForm @Valid Votacao votacao) {
    	Sessao sessao = sessaoService.findSessaoPauta(votacao.idpauta);
    	User user = userService.findUserCpf(votacao.cpf);
    	
    	if(sessao != null && sessao.status.getValor() == 1) {
			if(user.status.getValor().equals("ABLE_TO_VOTE")) {
				Votacao votacaoDetalhes = votacaoService.findByIdpautaAndIduser(votacao.idpauta, user.id);
				if (votacaoDetalhes != null) {
					return error.data("error", "Pauta já votada!");
				} else {
					votacao.iduser = user.id;					
					votacaoService.insert(votacao);
					pautaService.updateVoto(votacao.idpauta, votacao.voto.getValor());
				}
			} else {
				return error.data("error", "Usuário não habilitado para votar.");
	    	}
    	} else {
    		return error.data("error", "Sessão não está aberta para votar.");
    	}

		return Response.seeOther(URI.create("/pautas")).build();
    }
    
}
