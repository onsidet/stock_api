package com.sidet.repository;

import com.sidet.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByStatusAndNameContaining(String status, String name, Pageable pageable);
    Optional<Product> findByIdAndStatus(Long id, String status);
    Boolean existsByNameAndIdAndStatus(String name, Long id, String status);
}
