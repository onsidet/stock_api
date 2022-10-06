package com.sidet.repository;

import com.sidet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> getByUsernameAndStatus(String username, String status);
    Optional<User> getByPhoneAndStatus(String phone, String status);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}
