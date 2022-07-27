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
@Table(name = "aa_sg_docs")
public class Document {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private Long id;
	private String nom;
	private String canalUtilise;
	@ManyToOne(  cascade = CascadeType.ALL)
	 @JoinColumn(name = "enveloppe_id", nullable = false)
	 @JsonIgnore
	private Enveloppe enveloppe;
	@OneToOne(mappedBy = "document", cascade = CascadeType.ALL)
	@JoinColumn(name="signataire_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
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