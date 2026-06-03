// src/main/java/com/coursework/autosalon/web/model/User.java
package com.coursework.autosalon.web.model;

import java.time.LocalDate;

import com.coursework.autosalon.web.security.StringEncryptConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // email = логин
    @Convert(converter = StringEncryptConverter.class)
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Convert(converter = StringEncryptConverter.class)
    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @Convert(converter = StringEncryptConverter.class)
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Convert(converter = StringEncryptConverter.class)
    @Column(name = "middle_name", length = 255)
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Convert(converter = StringEncryptConverter.class)
    @Column(length = 50)
    private String phone;

    @Column(nullable = false, length = 50)
    private String role; // 'ROLE_USER' или 'ROLE_ADMIN'

    @Column(length = 16)
    private String gender; // 'MALE' / 'FEMALE'

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
