package com.atbm.appvppbe.repository;

import com.atbm.appvppbe.dto.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRep extends JpaRepository<Order, Long> {
}
