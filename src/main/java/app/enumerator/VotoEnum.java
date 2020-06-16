package app.enumerator;

import java.util.Arrays;
import java.util.Objects;

public enum VotoEnum {

	SIM(1, "Sim"), NAO(0, "Não");

	private Integer valor;
	private String descricao;

	VotoEnum(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static VotoEnum getValor(Integer valor) {
		return Arrays.stream(VotoEnum.values()).filter(status -> Objects.equals(status.getValor(), valor)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Valor não existe" + valor));
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
