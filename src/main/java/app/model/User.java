package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@RegisterForReflection
public class User extends PanacheEntity {

	@Size(min = 3, max = 100)
	@FormParam("name")
	public String name;

	@Size(min = 3, max = 100)
	@FormParam("email")
	public String email;

	@NotNull(message = "Digite o CPF (somente números)")
	@FormParam("cpf")
	public Long cpf;

	@Size(min = 3, max = 50)
	@FormParam("username")
	public String username;

	@Size(min = 4, max = 80)
	@FormParam("password")
	public String password;

	@Transient
	@Size(min = 4, max = 80)
	@Column(nullable = false)
	@FormParam("passwordCheck")
	public String passwordCheck;

	@FormParam("role")
	public String role;

	@FormParam("status")
	public String status;

	@CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_criacao")
    public Date dtCreated;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_alteracao")
    public Date dtUpdated;
    
	public User() {
	}

	public User(String username, String password, String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(@Size(min = 3, max = 100) String name, @Size(min = 3, max = 100) String email, @NotNull Long cpf,
			@Size(min = 3, max = 50) String username, @Size(min = 4, max = 80) String password, String role,
			String status) {
		super();
		this.name = name;
		this.email = email;
		this.cpf = cpf;
		this.username = username;
		this.password = password;
		this.role = role;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordCheck() {
		return passwordCheck;
	}

	public void setPasswordCheck(String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
