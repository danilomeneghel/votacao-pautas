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

import java.time.LocalDate;
import java.util.Date;

@Entity
@RegisterForReflection
public class Sessao extends PanacheEntity {

	@FormParam("idPauta")
	public Long idPauta;

	@FormParam("nome")
	public String nome;

	@FormParam("duracao")
	@NotNull(message = "Informe a duração")
	public Integer duracao;

	@NotNull(message = "Informe a data de inicio da sessão")
	@Column(name = "data_inicio_sessao")
	@FormParam("data_inicio_sessao")
	public LocalDate dataInicioSessao;

	@NotNull(message = "Informe a data de fim da sessão")
	@Column(name = "data_fim_sessao")
	@FormParam("data_fim_sessao")
	public LocalDate dataFimSessao;

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

	public Long getIdPauta() {
		return idPauta;
	}

	public void setIdPauta(Long idPauta) {
		this.idPauta = idPauta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getDuracao() {
		return duracao;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	public LocalDate getDataInicioSessao() {
		return dataInicioSessao;
	}

	public void setDataInicioSessao(LocalDate dataInicioSessao) {
		this.dataInicioSessao = dataInicioSessao;
	}

	public LocalDate getDataFimSessao() {
		return dataFimSessao;
	}

	public void setDataFimSessao(LocalDate dataFimSessao) {
		this.dataFimSessao = dataFimSessao;
	}

	public StatusSessaoEnum getStatus() {
		return status;
	}

	public void setStatus(StatusSessaoEnum status) {
		this.status = status;
	}

}
