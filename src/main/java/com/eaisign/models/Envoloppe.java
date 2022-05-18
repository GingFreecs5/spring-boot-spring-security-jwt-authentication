package com.eaisign.models;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "aa_sg_envoloppes")

public class Envoloppe {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	private Long id;
	private String nom;
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateAjout;
	@Temporal(TemporalType.TIMESTAMP)
	private Date derniereModification;
	private Boolean favoris;
	@ManyToOne
	private User user;
	@OneToMany(mappedBy = "envoloppe")
	private List<Document> documents;
	@PrePersist
	@PreUpdate
	public void updateTimeStamps() {
		 derniereModification=new Date();
		if(dateAjout==null) {
			dateAjout=new Date();
		}
	}
	public Envoloppe(String nom,String status,List<Document> docs) {
		this.nom=nom;
		this.status=status;
		this.documents=docs;
	}
	

}
