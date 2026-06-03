package com.coursework.autosalon.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coursework.autosalon.web.model.PurchaseRequest;
import com.coursework.autosalon.web.model.PurchaseRequestStatus;
import com.coursework.autosalon.web.repository.PurchaseRequestRepository;

@Controller
@RequestMapping("/admin/requests")
public class AdminRequestController {

    private final PurchaseRequestRepository purchaseRequestRepository;

    public AdminRequestController(PurchaseRequestRepository purchaseRequestRepository) {
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    private String redirectToUser(PurchaseRequest request) {
        return "redirect:/admin/users/" + request.getUser().getId();
    }

    @PostMapping("/{id}/to-pending")
    public String setPending(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes) {

        PurchaseRequest request = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена: " + id));

        request.setStatus(PurchaseRequestStatus.PENDING);
        purchaseRequestRepository.save(request);

        redirectAttributes.addFlashAttribute("successMessage",
                "Заявка #" + id + " переведена в статус «На рассмотрении»");

        return redirectToUser(request);
    }

    @PostMapping("/{id}/to-in-progress")
    public String setInProgress(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {

        PurchaseRequest request = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена: " + id));

        request.setStatus(PurchaseRequestStatus.IN_PROGRESS);
        purchaseRequestRepository.save(request);

        redirectAttributes.addFlashAttribute("successMessage",
                "Заявка #" + id + " переведена в статус «В процессе оформления»");

        return redirectToUser(request);
    }

    @PostMapping("/{id}/to-completed")
    public String setCompleted(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {

        PurchaseRequest request = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена: " + id));

        request.setStatus(PurchaseRequestStatus.COMPLETED);
        purchaseRequestRepository.save(request);

        redirectAttributes.addFlashAttribute("successMessage",
                "Заявка #" + id + " переведена в статус «Оформлено»");

        return redirectToUser(request);
    }
}
