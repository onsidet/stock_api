package com.sidet.repository;

import com.sidet.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff , Long> {
    Page<Staff> findAllByStatusAndFullNameContaining(String status, String fullName, Pageable pageable);
    Optional<Staff> findByIdAndStatus(Long id, String status);
    Boolean existsByIdAndFullNameAndStatus(Long id, String fullName, String status);
}
