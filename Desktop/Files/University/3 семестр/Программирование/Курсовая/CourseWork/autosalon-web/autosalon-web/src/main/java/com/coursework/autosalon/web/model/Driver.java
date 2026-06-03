package com.coursework.autosalon.web.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @Column(name = "championships_count", nullable = false)
    private Integer championshipsCount = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Driver() {
    }

    public Driver(String fullName, String country, Integer championshipsCount, Boolean isActive) {
        this.fullName = fullName;
        this.country = country;
        this.championshipsCount = championshipsCount;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getChampionshipsCount() {
        return championshipsCount;
    }

    public void setChampionshipsCount(Integer championshipsCount) {
        this.championshipsCount = championshipsCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    /**
     * Строка из эмодзи⭐ по количеству титулов.
     * 0 → "" ; 1 → "⭐" ; 4 → "⭐⭐⭐⭐"
     */
    @Transient
    public String getChampionshipStars() {
        int count = championshipsCount != null ? championshipsCount : 0;
        if (count <= 0) {
            return "";
        }
        return "⭐".repeat(count);
    }
}
