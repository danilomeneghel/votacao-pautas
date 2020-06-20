package app.controller;

import io.quarkus.qute.Template;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import app.model.Votacao;
import app.enumerator.StatusUserEnum;
import app.model.Sessao;
import app.model.User;

import app.service.VotacaoService;
import app.service.PautaService;
import app.service.SessaoService;
import app.service.UserService;
import app.util.ExternalApi;

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
    Template success;
    
    @Inject
    Template pautas;
	
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/votar")
    public Object votar(@MultipartForm @Valid Votacao votacao) {
    	Sessao sessao = sessaoService.findSessaoPauta(votacao.idpauta);
    	User user = userService.findUserCpf(votacao.cpf);
    	
    	if(sessao != null && sessao.status.getValor() == 1) {
    		//Pega o status do CPF da API externa para verificar se está habilitado para votar
    		StatusUserEnum statusCpf = ExternalApi.getStatusCpf(votacao.cpf);
    		
			if(statusCpf != null && statusCpf.toString().equals("ABLE_TO_VOTE")) {
				Votacao votacaoDetalhes = votacaoService.findByIdpautaAndIduser(votacao.idpauta, user.id);
				if (votacaoDetalhes != null) {
					return error.data("error", "Pauta já votada!");
				} else {
					votacao.iduser = user.id;					
					votacaoService.insert(votacao);
					pautaService.updateVoto(votacao.idpauta, votacao.voto.getValor());
					return success.data("success", "Voto realizado com sucesso!");
				}
			} else {
				return error.data("error", "Usuário não habilitado para votar.");
	    	}
    	} else {
    		return error.data("error", "Sessão não está aberta para votar.");
    	}
    }
    
}
