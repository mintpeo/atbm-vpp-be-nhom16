package com.atbm.appvppbe.repository;

import com.atbm.appvppbe.dto.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRep extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
}
