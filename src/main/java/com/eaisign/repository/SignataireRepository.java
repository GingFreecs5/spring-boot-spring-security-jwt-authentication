package com.eaisign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eaisign.models.Signataire;

@Repository
public interface SignataireRepository extends JpaRepository<Signataire, Long> {

}
