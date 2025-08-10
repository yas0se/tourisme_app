package com.gestion.tourisme_app.repository;

import com.gestion.tourisme_app.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserIdOrderByCreatedDateDesc(Long userId, Pageable pageable);
    Order findByOrderNumber(String orderNumber);
}
