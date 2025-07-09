package com.uca.parcialfinalncapas.repository;

import com.uca.parcialfinalncapas.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iTokenRepository extends JpaRepository<Token, Long> {

    Token findByToken(String token);
}