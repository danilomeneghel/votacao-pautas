package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import app.model.Votacao;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
@Transactional(REQUIRED)
public class VotacaoService {
	
	@Transactional(SUPPORTS)
    public List<Votacao> findAllVotacaos() {
        return Votacao.listAll();
    }

    @Transactional(SUPPORTS)
    public Votacao findVotacao(Long id) {
        return Votacao.findById(id);
    }
    
    @Transactional(SUPPORTS)
    public Votacao findByIdpautaAndIduser(Long idpauta, Long iduser) {
    	return Votacao.find("idpauta = :idpauta and iduser = :iduser", 
    			Parameters.with("idpauta", idpauta).and("iduser", iduser)).firstResult();
    }

    public Votacao insert(Votacao votacao) {
    	votacao.persist();
        return votacao;
    }

    public Votacao update(Votacao loaded, Votacao votacao) {
    	loaded.idpauta = votacao.idpauta;
    	//loaded.iduser = votacao.iduser;
    	loaded.voto = votacao.voto;
        return loaded;
    }

    public void delete(Long id) {
        Votacao votacao = Votacao.findById(id);
        if(votacao!=null) {
            votacao.delete();
        }
    }
}
