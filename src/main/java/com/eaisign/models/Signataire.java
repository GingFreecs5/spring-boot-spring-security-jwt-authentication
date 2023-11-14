package com.eaisign.models;

import javax.persistence.*;

import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "aa_sg_signataires")
public class Signataire {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private Long id;
	private String email;
	private String nom;
	private String prenom;
	@OneToOne(targetEntity = Document.class,fetch = FetchType.LAZY)
	@JsonIgnore
	@ToString.Exclude
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Document document;
	public Signataire(String email,String nom,String prenom){
		this.email=email;
		this.prenom=prenom;
		this.nom=nom;
	}
}
