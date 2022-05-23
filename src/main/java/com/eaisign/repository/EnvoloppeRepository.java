package com.eaisign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eaisign.models.Enveloppe;
import com.eaisign.models.User;

@Repository
public interface EnvoloppeRepository extends JpaRepository<Enveloppe, Long> {
	List<Enveloppe> findByUser(User user);
	List<Enveloppe> findByStatusAndUser(String status,User user);
	
}
