package com.gauravk.oauth2.repository;

import com.gauravk.oauth2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByEmail(String email);
}
