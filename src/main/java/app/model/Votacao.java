package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.ws.rs.FormParam;

import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;

@Entity
@RegisterForReflection
public class Votacao extends PanacheEntity {
	
	@Column(name = "idpauta", nullable = false)
	@FormParam("idpauta")
	public Long idpauta;

	@Column(name = "iduser", nullable = false)
	@FormParam("iduser")
	public Long iduser;

	@Transient
	@Column(updatable = false)
	@FormParam("cpf")
	public Long cpf;
	
	@Column(name = "voto", nullable = false)
	@FormParam("voto")
	public Integer voto;

	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_criacao")
	public Date dtCreated;

	public Votacao() {
	}

	public Votacao(Long idpauta, Long iduser, Integer voto) {
		super();
		this.idpauta = idpauta;
		this.iduser = iduser;
		this.voto = voto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdpauta() {
		return idpauta;
	}

	public void setIdpauta(Long idpauta) {
		this.idpauta = idpauta;
	}

	public Long getIduser() {
		return iduser;
	}

	public void setIduser(Long iduser) {
		this.iduser = iduser;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public Integer getVoto() {
		return voto;
	}

	public void setVoto(Integer voto) {
		this.voto = voto;
	}

}
