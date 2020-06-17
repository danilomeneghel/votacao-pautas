package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import app.enumerator.StatusSessaoEnum;
import app.enumerator.converter.StatusSessaoEnumConverter;

import java.util.Date;

@Entity
@RegisterForReflection
public class Sessao extends PanacheEntity {

	@Column(name = "idpauta", nullable = false)
	@FormParam("idpauta")
	public Long idpauta;

	@FormParam("nome")
	public String nome;

	@FormParam("duracao")
	@NotNull(message = "Informe a duração")
	public Integer duracao;

	@Column(name = "data_inicio_sessao")
	@FormParam("data_inicio_sessao")
	public String dataInicioSessao;

	@Column(name = "data_fim_sessao")
	@FormParam("data_fim_sessao")
	public String dataFimSessao;

	@Convert(converter = StatusSessaoEnumConverter.class)
	@FormParam("status")
	public StatusSessaoEnum status;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao")
	public Date dtCreated;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_alteracao")
	public Date dtUpdated;

	public Sessao() {
	}

}
