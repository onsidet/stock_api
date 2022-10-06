package com.sidet.repository;

import com.sidet.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Page<Brand> findAllByStatusAndNameContaining(String status, String name, Pageable pageable);
    Optional<Brand> findByIdAndStatus(Long id, String status);
    Boolean existsByNameAndIdAndStatus(String name, Long id, String status);
}
