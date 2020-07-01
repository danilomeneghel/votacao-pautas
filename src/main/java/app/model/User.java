package app.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import javax.json.bind.annotation.JsonbTransient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import app.enumerator.RoleUserEnum;
import app.enumerator.StatusUserEnum;
import app.enumerator.converter.RoleUserEnumConverter;
import app.enumerator.converter.StatusUserEnumConverter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@RegisterForReflection
public class User  extends PanacheEntityBase {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long id;
	
	@Size(min = 3, max = 60)
	@FormParam("name")
	public String name;

	@Size(min = 3, max = 100)
	@FormParam("email")
	public String email;

	@NotNull(message = "Digite o CPF (somente números)")
	@FormParam("cpf")
	public Long cpf;

	@Size(min = 3, max = 60)
	@FormParam("username")
	public String username;

	@Size(min = 3, max = 60)
	@FormParam("password")
	public String password;

	@Convert(converter = RoleUserEnumConverter.class)
	@FormParam("role")
	public RoleUserEnum role;

	@Transient
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
	
	@JsonbTransient
    @OneToMany
    public List<Pauta> pautas;
}
