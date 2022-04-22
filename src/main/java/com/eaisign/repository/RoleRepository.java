package com.eaisign.repository;

import java.util.Optional;

import com.eaisign.models.ERole;
import com.eaisign.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
