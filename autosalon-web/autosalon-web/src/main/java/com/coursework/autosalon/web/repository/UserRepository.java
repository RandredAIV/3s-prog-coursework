package com.coursework.autosalon.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coursework.autosalon.web.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // используется login'ом + AttributeConverter шифрует email
    Optional<User> findByEmail(String email);

    // используются регистрацией — тоже проходят через конвертер
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
