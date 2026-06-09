package com.atbm.appvppbe.repository;

import com.atbm.appvppbe.dto.entity.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureRep extends JpaRepository<Signature, Long> {
}
