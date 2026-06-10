package com.atbm.appvppbe.repository;

import com.atbm.appvppbe.dto.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRep extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(long cartId);
}
