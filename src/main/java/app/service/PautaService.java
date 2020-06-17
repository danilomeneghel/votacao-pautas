package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;

import app.enumerator.StatusPautaEnum;
import app.model.Pauta;

@ApplicationScoped
@Transactional(REQUIRED)
public class PautaService {
	
	@Transactional()
    public List<Pauta> findAllPautas() {
        return Pauta.listAll();
    }

    @Transactional()
    public Pauta findPauta(Long id) {
        return Pauta.findById(id);
    }
    
    @Transactional()
    public List<Pauta> findPautasAtivas() {
    	return Pauta.find("status", StatusPautaEnum.ATIVO).list();
    }
    
    @Transactional()
    public Pauta findPautaTitulo(String titulo) {
    	return Pauta.find("titulo", titulo).firstResult();
    }

    public Pauta insert(Pauta pauta) {
    	pauta.persist();
        return pauta;
    }

    public Pauta update(Pauta loaded, Pauta pauta) {
    	loaded.titulo = pauta.titulo;
    	loaded.descricao = pauta.descricao;
    	loaded.status = pauta.status;
    	loaded.persist();
        return loaded;
    }

    public Pauta updateVoto(Long id, Integer voto) {
    	Pauta pauta = Pauta.findById(id);
    	if(voto == 1) 
    		pauta.votoSim = pauta.votoSim + 1;
    	else 
    		pauta.votoNao = pauta.votoNao + 1;
    	pauta.persist();
    	return pauta;
    }
    
    public void delete(Long id) {
        Pauta pauta = Pauta.findById(id);
        if(pauta!=null) {
            pauta.delete();
        }
    }
}
