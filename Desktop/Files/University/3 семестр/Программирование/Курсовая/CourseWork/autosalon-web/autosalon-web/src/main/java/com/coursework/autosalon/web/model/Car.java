package com.coursework.autosalon.web.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand", nullable = false, length = 100)
    @NotNull(message = "Название болида обязательно")
    @Size(min = 1, max = 100, message = "Название болида должно быть от 1 до 100 символов")
    private String brand;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "Цена обязательна")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private BigDecimal price;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    @Column(name = "mileage_km", nullable = false)
    @NotNull(message = "Пробег обязателен")
    @Min(value = 0, message = "Пробег не может быть отрицательным")
    @Max(value = 1000000, message = "Пробег не может превышать 1 000 000 км")
    private Integer mileageKm;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    @NotNull(message = "Команда обязательна")
    private Team team;

    @Column(name = "season_year", nullable = false)
    @NotNull(message = "Год сезона обязателен")
    @Min(value = 2020, message = "Год сезона должен быть не меньше 2020")
    @Max(value = 2024, message = "Год сезона должен быть не больше 2024")
    private Integer seasonYear = 2024;

    @Column(name = "points", nullable = false)
    @NotNull(message = "Количество очков обязательно")
    @Min(value = 0, message = "Количество очков не может быть отрицательным")
    @Max(value = 1000, message = "Количество очков не может превышать 1000")
    private Integer points = 0;

    @Column(name = "wins", nullable = false)
    @NotNull(message = "Количество побед обязательно")
    @Min(value = 0, message = "Количество побед не может быть отрицательным")
    @Max(value = 50, message = "Количество побед не может превышать 50")
    private Integer wins = 0;

    @Column(name = "description")
    @Size(max = 1000, message = "Описание не может превышать 1000 символов")
    private String description;

    @Column(name = "constructors_champion", nullable = false)
    private Boolean constructorsChampion = false;

    @Column(name = "best_result", length = 100)
    @Size(max = 100, message = "Лучший результат не может превышать 100 символов")
    private String bestResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "best_driver_id")
    private Driver bestDriver;

    public Car() {
    }

    // ---------- Геттеры и сеттеры ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Integer getMileageKm() {
        return mileageKm;
    }

    public void setMileageKm(Integer mileageKm) {
        this.mileageKm = mileageKm;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public String getBestResult() {
        return bestResult;
    }

    public void setBestResult(String bestResult) {
        this.bestResult = bestResult;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getConstructorsChampion() {
        return constructorsChampion;
    }

    public void setConstructorsChampion(Boolean constructorsChampion) {
        this.constructorsChampion = constructorsChampion;
    }

    public Driver getBestDriver() {
        return bestDriver;
    }

    public void setBestDriver(Driver bestDriver) {
        this.bestDriver = bestDriver;
    }

    @Transient
    public String getConstructorsChampionStar() {
        return Boolean.TRUE.equals(constructorsChampion) ? "⭐" : "";
    }
}
