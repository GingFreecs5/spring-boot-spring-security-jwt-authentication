package com.eaisign.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "aa_sg_docs")
public class Document implements Serializable {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private Long id;
	private String nom;
	private String canalUtilise;
	@ManyToOne
	@JsonIgnore
	 @JoinColumn(name = "enveloppe_id", nullable = false)
	private Enveloppe enveloppe;
	@OneToOne(targetEntity= Signataire.class)
	@JoinColumn(name="signataire_id")
	private Signataire signataire;


	public Document(String nom,Enveloppe enveloppe) {
		this.nom=nom;
		this.enveloppe=enveloppe;
		
	}
	public Document(String originalFilename) {
		this.nom=originalFilename;
	}
	public Document(String nom, Enveloppe enveloppe, String canalUtilise, Signataire signataire) {
		this.nom=nom;
		this.enveloppe=enveloppe;
		this.canalUtilise=canalUtilise;
		this.signataire=signataire;

}

}