package com.rippletalk.rippletalk_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rippletalk.rippletalk_backend.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByAuthority(String authority);

}
