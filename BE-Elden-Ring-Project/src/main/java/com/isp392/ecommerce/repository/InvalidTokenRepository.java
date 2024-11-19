package com.isp392.ecommerce.repository;

import com.isp392.ecommerce.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
}