package app.model;

import javax.ws.rs.FormParam;

public class Login {

	@FormParam("username")
	public String username;

	@FormParam("password")
	public String password;

	@FormParam("cpf")
	public String cpf;

	@FormParam("role")
	public String role;

	@FormParam("token")
	public String token;

}
