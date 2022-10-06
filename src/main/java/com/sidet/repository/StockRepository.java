package com.sidet.repository;

import com.sidet.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {
    Page<Stock> findAllByStatus(String status, Pageable pageable);
    Optional<Stock> findByIdAndStatus(Long id, String status);
}
