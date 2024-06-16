package com.app.security.repository;

import com.app.security.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail( String email);

    Optional<User> findByUsernameOrEmail( String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsernameOrEmail(String username,String email);

    Page<User> findByProfilePowerGreaterThanEqual( int power, Pageable page);

    Page<User>findByEmailContainsIgnoreCaseAndProfilePowerGreaterThanEqual( Pageable page, String email, int power);
}