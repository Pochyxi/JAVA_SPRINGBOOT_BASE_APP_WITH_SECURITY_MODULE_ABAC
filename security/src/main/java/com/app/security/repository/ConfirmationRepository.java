package com.app.security.repository;

import com.app.security.entity.Confirmation;
import com.app.security.entity.User;
import com.app.security.enumerated.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    Confirmation findByToken( String token);

    Set<Confirmation> findByTokenTypeAndUserId( TokenType tokenType, Long userId);

    Set<Confirmation> findByUserId( Long userId);

    boolean existsConfirmationByUser( User user);
}
