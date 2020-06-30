package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import app.enumerator.StatusPautaEnum;
import app.enumerator.converter.StatusPautaEnumConverter;

import java.util.Date;

@Entity
@Table(name = "pauta")
@RegisterForReflection
public class Pauta extends PanacheEntity {

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

	@Convert(converter = StatusPautaEnumConverter.class)
	@FormParam("status")
	public StatusPautaEnum status;

	@JsonbDateFormat(value = "dd/MM/yyyy")
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao")
	public Date dtCreated;

	@JsonbDateFormat(value = "dd/MM/yyyy")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_alteracao")
	public Date dtUpdated;

	@OneToOne(mappedBy = "pauta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Sessao sessao;

	@ManyToOne
    @JoinColumn(name = "iduser")
    public User user;
}
