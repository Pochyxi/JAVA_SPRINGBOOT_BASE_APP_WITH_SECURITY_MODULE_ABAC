package com.app.security.repository;

import com.app.security.entity.ProfilePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProfilePermissionRepository extends JpaRepository<ProfilePermission,Long> {

    Set<ProfilePermission> findByProfileId( Long userId);
}
