package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.ws.rs.FormParam;

@Entity
@RegisterForReflection
public class Login extends PanacheEntity {

	@FormParam("username")
	public String username;

	@FormParam("password")
	public String password;

	@Transient
	@FormParam("cpf")
	public String cpf;

	@Transient
	@FormParam("role")
	public String role;

	@Transient
	@FormParam("token")
	public String token;

}
