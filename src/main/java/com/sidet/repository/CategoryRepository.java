package com.sidet.repository;

import com.sidet.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAllByStatusAndNameContaining(String status, String name, Pageable pageable);
    Optional<Category> findByStatusAndId(String status, Long id);
    Boolean existsByNameAndIdAndStatus(String name,Long id, String status);
}
