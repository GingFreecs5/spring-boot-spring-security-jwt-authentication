package com.eaisign.models;


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "aa_sg_enveloppes")

public class Enveloppe {
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
	@ManyToOne( cascade = CascadeType.ALL)
	 @JoinColumn(name = "user_id", nullable = false)
	 @JsonIgnore
	private User user;
	@PrePersist
	@PreUpdate
	public void updateTimeStamps() {
		 derniereModification=new Date();
		if(dateAjout==null) {
			dateAjout=new Date();
		}
	}
	public Enveloppe(String nom,String status,User user) {
		this.nom=nom;
		this.status=status;
		this.user=user;
	}
	

}
