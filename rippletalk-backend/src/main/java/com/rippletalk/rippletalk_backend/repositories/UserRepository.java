package com.rippletalk.rippletalk_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rippletalk.rippletalk_backend.models.ApplicationUser;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {

  Optional<ApplicationUser> findByUsername(String username);

}
