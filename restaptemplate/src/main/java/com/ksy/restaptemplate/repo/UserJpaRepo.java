package com.ksy.restaptemplate.repo;

import com.ksy.restaptemplate.entity.Test;
import com.ksy.restaptemplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
