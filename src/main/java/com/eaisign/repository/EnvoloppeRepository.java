package com.eaisign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eaisign.models.Envoloppe;
import com.eaisign.models.User;

@Repository
public interface EnvoloppeRepository extends JpaRepository<Envoloppe, Long> {
	List<Envoloppe> findByUser(User user);
	List<Envoloppe> findByStatusAndUser(String status,User user);
	
}
