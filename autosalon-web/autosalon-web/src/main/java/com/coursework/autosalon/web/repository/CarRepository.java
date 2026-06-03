package com.coursework.autosalon.web.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coursework.autosalon.web.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findBySeasonYear(int seasonYear, Sort sort);

    List<Car> findByConstructorsChampionTrueAndSeasonYearBetween(int fromYear, int toYear, Sort sort);

    // Все болиды определенного сезона
    List<Car> findBySeasonYear(Integer seasonYear);

    // Уникальные сезоны для фильтра
    @Query("select distinct c.seasonYear from Car c order by c.seasonYear")
    List<Integer> findDistinctSeasonYears();

    // Чемпионские болиды (1980-1999)
    List<Car> findByConstructorsChampionTrueAndSeasonYearBetween(int fromYear, int toYear);

    // Между другими методами репозитория
    List<Car> findBySeasonYearBetween(int fromYear, int toYear);

    // Все чемпионские болиды
    List<Car> findByConstructorsChampionTrue();

    // Чемпионские болиды в диапазоне сезонов
    List<Car> findByConstructorsChampionTrueAndSeasonYearBetween(Integer fromYear, Integer toYear);

    // НОВЫЙ МЕТОД: найти все скрытые болиды
    List<Car> findByHiddenTrue();
}