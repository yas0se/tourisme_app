package com.gestion.tourisme_app.repository;

import com.gestion.tourisme_app.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserIdOrderByCreatedDateDesc(Long userId);

    Optional<CartItem> findByUserIdAndProductIdAndSelectedColorAndSelectedSize(
            Long userId, Long productId, String selectedColor, String selectedSize);

    void deleteByUserId(Long userId);
}