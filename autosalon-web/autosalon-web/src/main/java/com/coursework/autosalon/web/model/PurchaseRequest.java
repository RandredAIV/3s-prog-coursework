package com.coursework.autosalon.web.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchase_requests")
public class PurchaseRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Кто заказал
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Какой болид
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    // Снэпшоты названия и цены на момент заявки
    @Column(name = "car_brand_snapshot", nullable = false, length = 100)
    private String carBrandSnapshot;

    @Column(name = "price_snapshot", nullable = false, precision = 15, scale = 2)
    private BigDecimal priceSnapshot;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Новый статус заявки
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private PurchaseRequestStatus status = PurchaseRequestStatus.PENDING;

    // ===== геттеры/сеттеры =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getCarBrandSnapshot() {
        return carBrandSnapshot;
    }

    public void setCarBrandSnapshot(String carBrandSnapshot) {
        this.carBrandSnapshot = carBrandSnapshot;
    }

    public BigDecimal getPriceSnapshot() {
        return priceSnapshot;
    }

    public void setPriceSnapshot(BigDecimal priceSnapshot) {
        this.priceSnapshot = priceSnapshot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public PurchaseRequestStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseRequestStatus status) {
        this.status = status;
    }

    // Удобные методы
    public boolean isPending() {
        return status == PurchaseRequestStatus.PENDING;
    }

    public boolean isInProgress() {
        return status == PurchaseRequestStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return status == PurchaseRequestStatus.COMPLETED;
    }
}
