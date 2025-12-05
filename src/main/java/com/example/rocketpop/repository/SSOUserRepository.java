package com.example.rocketpop.repository;

import com.example.rocketpop.entity.SSOUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SSOUserRepository extends JpaRepository<SSOUser, Integer> {
    
    Optional<SSOUser> findByUsername(String username);
    
    Optional<SSOUser> findByEmail(String email);
    
    List<SSOUser> findByRole(String role);
    
    List<SSOUser> findByUsernameContainingIgnoreCase(String username);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
