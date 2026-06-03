package com.coursework.autosalon.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coursework.autosalon.web.model.PurchaseRequest;
import com.coursework.autosalon.web.model.User;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

    // пригодится потом: заявки конкретного пользователя
    List<PurchaseRequest> findByUser(User user);

    List<PurchaseRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<PurchaseRequest> findById(Long id);

    boolean existsByCarId(Long carId);

    long countByCar_Id(Long carId);

}
