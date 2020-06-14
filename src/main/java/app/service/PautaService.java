package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import app.model.Pauta;

@ApplicationScoped
@Transactional(REQUIRED)
public class PautaService {
	
	@Transactional(SUPPORTS)
    public List<Pauta> findAllPautas() {
        return Pauta.listAll();
    }

    @Transactional(SUPPORTS)
    public Pauta findPauta(Long id) {
        return Pauta.findById(id);
    }

    public Pauta insert(Pauta pauta) {
    	pauta.persist();
        return pauta;
    }

    public Pauta update(Pauta loaded, Pauta pauta) {
    	//loaded.iduser = pauta.iduser;
    	loaded.titulo = pauta.titulo;
    	loaded.descricao = pauta.descricao;
    	loaded.status = pauta.status;
        return loaded;
    }

    public Pauta updateVoto(Long id, Integer voto) {
    	Pauta pauta = Pauta.findById(id);
    	if(voto == 1) 
    		pauta.votoSim = pauta.votoSim + 1;
    	else 
    		pauta.votoNao = pauta.votoNao + 1;
    	return pauta;
    }
    
    public void delete(Long id) {
        Pauta pauta = Pauta.findById(id);
        if(pauta!=null) {
            pauta.delete();
        }
    }
}
