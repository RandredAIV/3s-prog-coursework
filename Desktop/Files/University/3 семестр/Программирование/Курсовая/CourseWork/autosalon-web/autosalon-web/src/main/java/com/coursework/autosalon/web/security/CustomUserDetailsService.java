package com.coursework.autosalon.web.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coursework.autosalon.web.model.User;
import com.coursework.autosalon.web.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("Пустой email");
        }

        // JPA + AttributeConverter сами зашифруют email при запросе
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Пользователь с email " + email + " не найден"));

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole())); // ROLE_USER / ROLE_ADMIN

        return new org.springframework.security.core.userdetails.User(
                email.trim(),
                user.getPasswordHash(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}
