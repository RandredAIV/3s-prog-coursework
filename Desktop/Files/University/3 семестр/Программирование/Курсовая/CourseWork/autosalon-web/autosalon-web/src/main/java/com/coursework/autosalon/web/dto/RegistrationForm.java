package com.coursework.autosalon.web.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Фамилия обязательна")
    @Pattern(regexp = "^[A-Za-zА-Яа-яёЁ\\- ]+$", message = "Фамилия может содержать только буквы и дефис")
    @Size(min = 1, max = 50, message = "Фамилия должна быть от 1 до 50 символов")
    private String lastName;

    @NotBlank(message = "Имя обязательно")
    @Pattern(regexp = "^[A-Za-zА-Яа-яёЁ\\- ]+$", message = "Имя может содержать только буквы и дефис")
    @Size(min = 1, max = 50, message = "Имя должно быть от 1 до 50 символов")
    private String firstName;

    @Pattern(regexp = "^$|^[A-Za-zА-Яа-яёЁ\\- ]+$", message = "Отчество может содержать только буквы и дефис")
    @Size(max = 50, message = "Отчество не может превышать 50 символов")
    private String middleName;

    @NotNull(message = "Дата рождения обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+7\\d{10}$", message = "Телефон должен быть в формате +7XXXXXXXXXX (10 цифр после +7)")
    private String phone;

    @NotBlank(message = "Пол обязателен")
    private String gender;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @NotBlank(message = "Подтверждение пароля обязательно")
    private String confirmPassword;

    // геттеры и сеттеры
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}