package com.eaisign.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
public class FileDB {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String name;
	private String b64;
	private String type;
	@Lob
	private byte[] data;

	
	 public FileDB(String name, String type, byte[] data,String b64) {
		    this.name = name;
		    this.type = type;
		    this.data = data;
		    this.b64=b64;
		  }
}
