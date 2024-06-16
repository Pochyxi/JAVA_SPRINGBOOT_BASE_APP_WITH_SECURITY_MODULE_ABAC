package com.app.security.repository;

import com.app.security.entity.Permission;
import com.app.security.enumerated.PermissionList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {


    Optional<Permission>findByName( PermissionList name);
}
