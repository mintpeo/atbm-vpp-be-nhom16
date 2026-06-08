package com.atbm.appvppbe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureRep extends JpaRepository<SignatureRep, Long> {
}
