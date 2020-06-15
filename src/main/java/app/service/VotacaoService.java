package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import app.model.Votacao;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
@Transactional(REQUIRED)
public class VotacaoService {
	
	@Transactional()
    public List<Votacao> findAllVotacaos() {
        return Votacao.listAll();
    }

    @Transactional()
    public Votacao findVotacao(Long id) {
        return Votacao.findById(id);
    }
    
    @Transactional()
    public Votacao findByIdpautaAndIduser(Long idpauta, Long iduser) {
    	return Votacao.find("idpauta = :idpauta and iduser = :iduser", 
    			Parameters.with("idpauta", idpauta).and("iduser", iduser)).firstResult();
    }

    public Votacao insert(Votacao votacao) {
    	votacao.persist();
        return votacao;
    }

}
