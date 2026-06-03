//AdminUserController.java
package com.coursework.autosalon.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coursework.autosalon.web.model.Car;
import com.coursework.autosalon.web.model.PurchaseRequest;
import com.coursework.autosalon.web.model.User;
import com.coursework.autosalon.web.repository.PurchaseRequestRepository;
import com.coursework.autosalon.web.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final UserRepository userRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;

    public AdminUserController(UserRepository userRepository,
                               PurchaseRequestRepository purchaseRequestRepository) {
        this.userRepository = userRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    @GetMapping("/users/{id}")
    public String userDetails(@PathVariable("id") Long id,
                            Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: id=" + id));

        List<PurchaseRequest> requests = purchaseRequestRepository
                .findByUserIdOrderByCreatedAtDesc(id);

        model.addAttribute("user", user);
        model.addAttribute("requests", requests);

        return "admin/user-details";
    }

    @PostMapping("/users/{userId}/requests/{requestId}/delete")
    public String deletePurchaseRequest(@PathVariable("userId") Long userId,
                                    @PathVariable("requestId") Long requestId) {
        
        // Находим заявку
        PurchaseRequest request = purchaseRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));
        
        // Находим связанный болид
        Car car = request.getCar();
        
        // Меняем статус болида обратно на доступен
        car.setAvailable(true);
        // Здесь может понадобиться carRepository.save(car) если у тебя есть репозиторий для Car
        
        // Удаляем заявку
        purchaseRequestRepository.delete(request);
        
        return "redirect:/admin/users/" + userId;
    }

    @GetMapping("/users")
    public String listUsersWithRequests(Model model) {

        List<User> users = userRepository.findAll();
        List<PurchaseRequest> allRequests = purchaseRequestRepository.findAll();

        // Группируем заявки по id пользователя
        Map<Long, List<PurchaseRequest>> requestsByUser = allRequests.stream()
                .filter(pr -> pr.getUser() != null && pr.getUser().getId() != null)
                .collect(Collectors.groupingBy(pr -> pr.getUser().getId()));

        model.addAttribute("users", users);
        model.addAttribute("requestsByUser", requestsByUser);

        return "admin/users";   // шаблон admin/users.html
    }
}
