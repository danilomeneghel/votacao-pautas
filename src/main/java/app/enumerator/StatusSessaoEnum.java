package app.enumerator;

import java.util.Arrays;
import java.util.Objects;

public enum StatusSessaoEnum {

	ABERTO(1, "Aberto"), ANDAMENTO(2, "Andamento"), FECHADO(3, "Fechado");

	private Integer valor;
	private String descricao;

	StatusSessaoEnum(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static StatusSessaoEnum getValor(Integer valor) {
		return Arrays.stream(StatusSessaoEnum.values()).filter(status -> Objects.equals(status.getValor(), valor))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Valor não existe" + valor));
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
