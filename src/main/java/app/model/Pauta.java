package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@RegisterForReflection
public class Pauta extends PanacheEntity {
	
	@FormParam("iduser")
	public Long iduser;

	@Size(min = 3, max = 100)
	@FormParam("titulo")
	public String titulo;

	@Size(min = 3)
	@FormParam("descricao")
	public String descricao;

	@FormParam("votoSim")
	public Integer votoSim = 0;

	@FormParam("votoNao")
	public Integer votoNao = 0;

	@FormParam("status")
	public String status;
	
	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_criacao")
    public Date dtCreated;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_alteracao")
    public Date dtUpdated;

	public Pauta() {
	}

	public Long getIduser() {
		return iduser;
	}

	public void setIduser(Long iduser) {
		this.iduser = iduser;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getVotoSim() {
		return votoSim;
	}

	public void setVotoSim(Integer votoSim) {
		this.votoSim = votoSim;
	}

	public Integer getVotoNao() {
		return votoNao;
	}

	public void setVotoNao(Integer votoNao) {
		this.votoNao = votoNao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}