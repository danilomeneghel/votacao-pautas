package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;

import app.model.Sessao;

@ApplicationScoped
@Transactional(REQUIRED)
public class SessaoService {
	
	@Transactional()
    public List<Sessao> findAllSessoes() {
        return Sessao.listAll();
    }

    @Transactional()
    public Sessao findSessao(Long id) {
        return Sessao.findById(id);
    }
    
    @Transactional()
    public Sessao findSessaoPauta(Long idpauta) {
        return Sessao.find("idpauta", idpauta).firstResult();
    }
    
    @Transactional()
    public Sessao findSessaoNome(String nome) {
    	return Sessao.find("nome", nome).firstResult();
    }
    
    public Sessao insert(Sessao sessao) {
    	sessao.persist();
        return sessao;
    }

    public Sessao update(Sessao loaded, Sessao sessao) {
    	loaded.dataInicioSessao = sessao.dataInicioSessao;
    	loaded.dataFimSessao = sessao.dataFimSessao;
    	loaded.nome = sessao.nome;
    	loaded.duracao = sessao.duracao;
    	loaded.status = sessao.status;
    	loaded.persist();
        return loaded;
    }

    public void delete(Long id) {
        Sessao sessao = Sessao.findById(id);
        if(sessao!=null) {
            sessao.delete();
        }
    }
}
