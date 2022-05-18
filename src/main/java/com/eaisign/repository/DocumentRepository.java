package com.eaisign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eaisign.models.Document;

@Repository
public interface DocumentRepository extends	JpaRepository<Document, Long>{
	
}
