package com.atbm.appvppbe.repository;

import com.atbm.appvppbe.dto.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRep extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(long orderId);
}
