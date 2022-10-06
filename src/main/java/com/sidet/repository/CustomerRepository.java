package com.sidet.repository;

import com.sidet.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAllByStatusAndFullNameContaining(String status, String fullName, Pageable pageable);
    Optional<Customer> findByIdAndStatus(Long id, String status);
    Boolean existsByIdAndFullNameAndStatus(Long id,String fullName, String status);
}
