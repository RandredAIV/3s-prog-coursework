package com.coursework.autosalon.web.controller;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.coursework.autosalon.web.dto.RegistrationForm;
import com.coursework.autosalon.web.service.RegistrationService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // страница входа
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // страница регистрации
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "auth/register";
    }

    // обработка регистрации
    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("form") RegistrationForm form,
                             BindingResult bindingResult,
                             Model model) {

        // Дополнительная валидация даты рождения
        if (form.getBirthDate() != null) {
            LocalDate birthDate = form.getBirthDate();
            LocalDate today = LocalDate.now();

            // год совсем "левый"
            if (birthDate.getYear() < 1900) {
                bindingResult.rejectValue(
                        "birthDate",
                        "error.birthDate",
                        "Год рождения не может быть меньше 1900"
                );
            } else if (birthDate.isAfter(today)) {
                bindingResult.rejectValue(
                        "birthDate",
                        "error.birthDate",
                        "Дата рождения не может быть в будущем"
                );
            } else {
                int age = Period.between(birthDate, today).getYears();
                if (age < 18) {
                    bindingResult.rejectValue(
                            "birthDate",
                            "error.birthDate",
                            "Возраст для регистрации должен быть не менее 18 лет"
                    );
                }
            }
        }

        // Дополнительная валидация пароля (подтверждение)
        if (form.getConfirmPassword() != null
                && !form.getConfirmPassword().equals(form.getPassword())) {
            bindingResult.rejectValue(
                    "confirmPassword",
                    "error.confirmPassword",
                    "Пароли не совпадают"
            );
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            registrationService.registerNewUser(form);
        } catch (IllegalArgumentException ex) {
            // Определяем к какому полю относится ошибка
            if (ex.getMessage().contains("e-mail")) {
                bindingResult.rejectValue("email", "email.exists", ex.getMessage());
            } else if (ex.getMessage().contains("телефон")) {
                bindingResult.rejectValue("phone", "phone.exists", ex.getMessage());
            } else {
                model.addAttribute("error", ex.getMessage());
            }
            return "auth/register";
        }

        return "redirect:/login?registered";
    }
}
