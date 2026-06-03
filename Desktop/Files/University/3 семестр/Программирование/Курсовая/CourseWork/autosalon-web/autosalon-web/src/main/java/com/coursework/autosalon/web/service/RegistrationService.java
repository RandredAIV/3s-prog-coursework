package com.coursework.autosalon.web.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coursework.autosalon.web.dto.RegistrationForm;
import com.coursework.autosalon.web.model.User;
import com.coursework.autosalon.web.repository.UserRepository;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerNewUser(RegistrationForm form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким e-mail уже существует");
        }

        if (userRepository.existsByPhone(form.getPhone())) {
            throw new IllegalArgumentException("Пользователь с таким номером телефона уже существует");
        }

        User user = new User();
        user.setEmail(form.getEmail());
        user.setPasswordHash(passwordEncoder.encode(form.getPassword()));

        user.setLastName(form.getLastName());
        user.setFirstName(form.getFirstName());
        user.setMiddleName(form.getMiddleName());
        user.setBirthDate(form.getBirthDate());
        user.setPhone(form.getPhone());
        user.setGender(form.getGender());

        user.setRole("ROLE_USER");
        user.setEnabled(true);

        userRepository.save(user);
    }
}