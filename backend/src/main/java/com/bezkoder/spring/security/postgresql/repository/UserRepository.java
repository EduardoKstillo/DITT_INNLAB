package com.bezkoder.spring.security.postgresql.repository;

import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.security.postgresql.models.ERole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.security.postgresql.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = "roles")
  List<User> findAll();

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  Boolean existsByDni(String dni);

  List<User> findByEmailContainingIgnoreCase(String email);

  List<User> findByRoles_Name(ERole role);

}
