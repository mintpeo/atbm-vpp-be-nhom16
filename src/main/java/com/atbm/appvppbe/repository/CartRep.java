package com.atbm.appvppbe.repository;

import com.atbm.appvppbe.dto.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRep extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByUserId(long userId);
}
