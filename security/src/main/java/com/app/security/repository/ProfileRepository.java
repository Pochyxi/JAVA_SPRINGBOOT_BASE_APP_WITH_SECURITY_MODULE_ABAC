package com.app.security.repository;

import com.app.security.entity.Profile;
import com.app.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, User> {

    Profile findByUserId( Long userId);
}
