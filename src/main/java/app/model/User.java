package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import app.enumerator.RoleUserEnum;
import app.enumerator.StatusUserEnum;
import app.enumerator.converter.RoleUserEnumConverter;
import app.enumerator.converter.StatusUserEnumConverter;

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

	@Convert(converter = RoleUserEnumConverter.class)
	@FormParam("role")
	public RoleUserEnum role;

	@Convert(converter = StatusUserEnumConverter.class)
	@FormParam("status")
	public StatusUserEnum status;

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

	public RoleUserEnum getRole() {
		return role;
	}

	public void setRole(RoleUserEnum role) {
		this.role = role;
	}

	public StatusUserEnum getStatus() {
		return status;
	}

	public void setStatus(StatusUserEnum status) {
		this.status = status;
	}
}
