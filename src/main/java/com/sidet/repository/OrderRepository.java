package com.sidet.repository;

import com.sidet.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> getAllByStatus(String status, Pageable pageable);
    Optional<Order> getByIdAndStatus(Long id, String status);
}
