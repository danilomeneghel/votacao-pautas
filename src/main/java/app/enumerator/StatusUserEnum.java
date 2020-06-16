package app.enumerator;

import java.util.Arrays;
import java.util.Objects;

public enum StatusUserEnum {

	ABLE_TO_VOTE("ABLE_TO_VOTE", "Habilitado"), UNABLE_TO_VOTE("UNABLE_TO_VOTE", "Desabilitado");

	private String valor;
	private String descricao;

	StatusUserEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static StatusUserEnum getValor(String valor) {
		return Arrays.stream(StatusUserEnum.values()).filter(status -> Objects.equals(status.getValor(), valor))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("Valor não existe" + valor));
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
