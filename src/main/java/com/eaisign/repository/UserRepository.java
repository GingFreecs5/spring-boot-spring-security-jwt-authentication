package com.eaisign.repository;

import java.util.Optional;

import com.eaisign.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);
  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
