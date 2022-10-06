package com.sidet.repository;

import com.sidet.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> getAllByStatus(String status, Pageable pageable);
    Optional<OrderItem> getByIdAndStatus(Long id, String status);
}
