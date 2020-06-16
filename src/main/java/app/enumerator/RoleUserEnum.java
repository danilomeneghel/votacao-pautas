package app.enumerator;

import java.util.Arrays;
import java.util.Objects;

public enum RoleUserEnum {

	ADMIN(1, "Administrador"), USER(2, "Usuário"), ASSOC(3, "Associado");

	private Integer valor;
	private String descricao;

	RoleUserEnum(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static RoleUserEnum getValor(Integer valor) {
		return Arrays.stream(RoleUserEnum.values()).filter(status -> Objects.equals(status.getValor(), valor))
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
