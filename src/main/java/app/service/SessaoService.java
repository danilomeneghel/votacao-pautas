package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.apache.james.mime4j.dom.datetime.DateTime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public Sessao findSessaoTitulo(String nome) {
    	return Sessao.find("nome", nome).firstResult();
    }
    
    public Sessao convertDate(Sessao sessao) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataInicioSessao = LocalDate.parse(sessao.dataInicioSessao.toString(), formatter);
        LocalDate dataFimSessao = LocalDate.parse(sessao.dataFimSessao.toString(), formatter);
        
        sessao.dataInicioSessao = dataInicioSessao;
        sessao.dataFimSessao = dataFimSessao;    	
    	return sessao;
    }
    
    public Sessao insert(Sessao sessao) {
    	sessao = convertDate(sessao);
    	sessao.persist();
        return sessao;
    }

    public Sessao update(Sessao loaded, Sessao sessao) {
    	loaded.dataInicioSessao = convertDate(sessao).dataInicioSessao;
    	loaded.dataFimSessao = convertDate(sessao).dataFimSessao;
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
