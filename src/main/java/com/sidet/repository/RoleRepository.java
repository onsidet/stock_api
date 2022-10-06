package com.sidet.repository;

import com.sidet.entity.Role;
import com.sidet.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> getByName(UserRole name);
}
