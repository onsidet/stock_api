package com.sidet.repository;

import com.sidet.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {
    Page<Store> findAllByNameContainingAndStatus(String name, String status, Pageable pageable);
    Optional<Store> findByIdAndStatus(Long id, String status);
    Boolean existsByIdAndNameAndStatus(Long id, String name,String status);
}
