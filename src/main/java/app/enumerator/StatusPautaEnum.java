package app.enumerator;

import java.util.Arrays;
import java.util.Objects;

public enum StatusPautaEnum {

	ATIVO(1, "Ativo"), INATIVO(0, "Inativo");

	private Integer valor;
	private String descricao;

	StatusPautaEnum(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static StatusPautaEnum getValor(Integer valor) {
		return Arrays.stream(StatusPautaEnum.values()).filter(status -> Objects.equals(status.getValor(), valor))
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
