package com.eaisign.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "aa_sg_users", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
public class User {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private Long id;

	@NotBlank
	@Size(max = 20)
	@Column(name = "NOM")
	private String username;
	@Size(max = 20)
	private String prenom;
	@Size(max = 20)
	private String numTelephone;
	@Size(max = 20)
	private String pieceJusticatif;
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "aa_sg_user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	
	
	public User() {
	}

	public String getNumTelephone() {
		return numTelephone;
	}

	public void setNumTelephone(String numTelephone) {
		this.numTelephone = numTelephone;
	}

	public String getPieceJusticatif() {
		return pieceJusticatif;
	}

	public void setPieceJusticatif(String pieceJusticatif) {
		this.pieceJusticatif = pieceJusticatif;
	}



	public User(String username, String prenom, String email, String piece_justicatif, String num_telephone) {
		this.username = username;
		this.email = email;
		this.prenom = prenom;
		this.pieceJusticatif = piece_justicatif;
		this.numTelephone = num_telephone;
	}

	public User(String username, String prenom, String email, String piece_justicatif, String num_telephone,
			String password) {
		this.username = username;
		this.email = email;
		this.prenom = prenom;
		this.pieceJusticatif = piece_justicatif;
		this.numTelephone = num_telephone;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


}