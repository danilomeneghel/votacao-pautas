package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.hibernate.annotations.CreationTimestamp;

import app.enumerator.VotoEnum;
import app.enumerator.converter.VotoEnumConverter;

import java.util.Date;

@Entity
@Table(name = "votacao")
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

	@NotNull
	@Convert(converter = VotoEnumConverter.class)
	@FormParam("voto")
	public VotoEnum voto;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao")
	public Date dtCreated;

	@ManyToOne
	private Pauta pauta;

	@OneToOne
	private User user;

}
