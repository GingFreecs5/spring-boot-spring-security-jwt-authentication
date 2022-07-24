package com.eaisign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eaisign.models.Document;
import com.eaisign.models.Enveloppe;

@Repository
public interface DocumentRepository extends	JpaRepository<Document, Long>{
	List<Document> findByEnveloppe(Enveloppe enveloppe);
}
