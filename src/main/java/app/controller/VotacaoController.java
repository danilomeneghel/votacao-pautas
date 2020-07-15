package app.controller;

import io.quarkus.qute.Template;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import app.model.Votacao;
import app.model.Message;
import app.model.Sessao;
import app.model.User;
import app.service.VotacaoService;
import app.service.PautaService;
import app.service.SessaoService;
import app.service.UserService;
import app.enumerator.StatusUserEnum;
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
    Template pautaView;
    
    @Inject
    Template pautas;
    
    public Message message = new Message();

	@GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<Votacao> listVotacoesJson() {
        return votacaoService.findAllVotacoes();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/votar")
    public Object votar(@MultipartForm @Valid Votacao votacao) {
        User user = userService.findUserCpf(votacao.cpf);
        Sessao sessao = sessaoService.findSessaoPauta(votacao.idpauta);
        
        if(sessao != null && sessao.status.getValor() == 1) {
            LocalDate dtIn = sessao.dataInicioSessao;
            LocalDate dtFi = sessao.dataFimSessao;
            LocalDate dtAtual = LocalDate.now();

            if((dtIn.isBefore(dtAtual) || dtIn.isEqual(dtAtual)) && (dtFi.isAfter(dtAtual) || dtFi.isEqual(dtAtual))) {
                //Pega o status do CPF da API externa para verificar se está habilitado para votar
                StatusUserEnum statusCpf = ExternalApi.getStatusCpf(votacao.cpf);
                
                if(statusCpf != null && statusCpf.toString().equals("ABLE_TO_VOTE")) {
                    Votacao votacaoDetalhes = votacaoService.findByIdpautaAndIduser(votacao.idpauta, user.id);
                    if (votacaoDetalhes == null) {
                        votacao.iduser = user.id;					
                        votacaoService.insert(votacao);
                        pautaService.updateVoto(votacao.idpauta, votacao.voto.getValor());
                        
                        message.type = "success";
                        message.title = "Sucesso!";
                        message.description = "Voto confirmado com sucesso.";
                        return Response.ok(message).build();
                    } else {
                        message.type = "warning";
                        message.title = "Alerta!";
                        message.description = "Essa Pauta já foi votada.";
                        return Response.ok(message).build();
                    }
                } else {
                    message.type = "warning";
                    message.title = "Alerta!";
                    message.description = "Usuário não habilitado para votar.";
                    return Response.ok(message).build();
                }
            } else {
                message.type = "warning";
                message.title = "Alerta!";
                message.description = "Essa Pauta está fora do prazo para votar.";
                return Response.ok(message).build();
            }
        } else {
            message.type = "warning";
            message.title = "Alerta!";
            message.description = "Sessão não está aberta para votar nessa Pauta.";
            return Response.ok(message).build();
        }
    }
    
}
