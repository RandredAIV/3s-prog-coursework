package com.coursework.autosalon.web.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coursework.autosalon.web.model.PurchaseRequest;
import com.coursework.autosalon.web.model.User;
import com.coursework.autosalon.web.repository.PurchaseRequestRepository;
import com.coursework.autosalon.web.repository.UserRepository;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;

    public ProfileController(UserRepository userRepository,
                             PurchaseRequestRepository purchaseRequestRepository) {
        this.userRepository = userRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    @GetMapping
    public String profile(Model model, Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            // теоретически не должны сюда попадать, но на всякий случай
            return "redirect:/login";
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден: " + email));

        List<PurchaseRequest> requests =
                purchaseRequestRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("requests", requests);

        return "profile";
    }
}
