package com.coursework.autosalon.web.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coursework.autosalon.web.model.Car;
import com.coursework.autosalon.web.repository.CarRepository;

/**
 * Сервисный слой для работы с сущностью Car.
 * Здесь инкапсулируем логику получения данных из репозитория.
 */
@Service
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    /**
     * Конструктор с внедрением зависимости CarRepository.
     * Spring сам подставит сюда бин репозитория.
     */
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Получить список всех болидов.
     */
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    /**
     * Получить список тестовых (скрытых) болидов
     */
    public List<Car> findHiddenCars() {
        return carRepository.findByHiddenTrue();
    }

    /**
     * Найти все болиды по конкретному сезону.
     * Если seasonYear == null, обычно вызывающий код сам решает,
     * что делать (например, вызвать findAll()).
     */
    public List<Car> findBySeasonYear(Integer seasonYear) {
        if (seasonYear == null) {
            return findAll();
        }
        return carRepository.findBySeasonYear(seasonYear);
    }

    /**
     * Получить список всех доступных сезонов (для плиток сезонов и фильтрации).
     */
    public List<Integer> findDistinctSeasonYears() {
        return carRepository.findDistinctSeasonYears();
    }

    /**
     * Чемпионские болиды конструкторов в диапазоне 1980–1999.
     * Используется для блока "чемпионские болиды" на странице каталога.
     */
    public List<Car> findChampionCars1980to1999() {
    return carRepository.findByConstructorsChampionTrueAndSeasonYearBetween(
            1980,
            1999,
            Sort.by(Sort.Direction.ASC, "seasonYear")   // сортировка по году, старые → новые
    );
}

    /**
     * Найти болид по его уникальному идентификатору.
     * Это понадобится для страницы подробной информации /cars/{id}.
     */
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    // Возвращаем только болиды 2020–2024
    public List<Car> findSeason2020to2024() {
        return carRepository.findBySeasonYearBetween(2020, 2024);
    }
    public List<Car> findCars(Integer season,
                            String sortField,
                            String direction,
                            BigDecimal minPrice,
                            BigDecimal maxPrice,
                            Boolean filterAvail,
                            Boolean filterUnavail,
                            boolean championsOnly,
                            List<Long> teamIds,
                            Boolean showHidden) {

        Sort sort = buildSort(sortField, direction);

        return carRepository.findAll(sort)
                .stream()
                .filter(car -> {
                    // ----- САМЫЙ ВАЖНЫЙ ФИЛЬТР ПЕРВЫМ: скрытые болиды -----
                    // Если showHidden = false (по умолчанию) - скрываем тестовые болиды
                    // Если showHidden = true - показываем ВСЕ болиды (включая скрытые)
                    if (!Boolean.TRUE.equals(showHidden) && Boolean.TRUE.equals(car.getHidden())) {
                        return false;
                    }

                    // ----- фильтр по сезону -----
                    // 1) Если сезон задан (2020, 2021, 2022, 2023, 2024) — показываем только его
                    if (season != null && !season.equals(car.getSeasonYear())) {
                        return false;
                    }

                    // 2) Если сезон НЕ задан (вкладка "Все сезоны") — показываем только 2020–2024
                    // НО: скрытые болиды уже отфильтрованы выше, так что здесь они не появятся
                    if (season == null) {
                        Integer year = car.getSeasonYear();
                        if (year == null || year < 2020 || year > 2024) {
                            return false; // отсеиваем чемпионские 1980–1999 и вообще всё вне диапазона
                        }
                    }

                    // ----- фильтр по производителю (команде) -----
                    if (teamIds != null && !teamIds.isEmpty()) {
                        if (car.getTeam() == null || !teamIds.contains(car.getTeam().getId())) {
                            return false;
                        }
                    }

                    // ----- фильтр по минимальной цене -----
                    if (minPrice != null && car.getPrice() != null
                            && car.getPrice().compareTo(minPrice) < 0) {
                        return false;
                    }

                    // ----- фильтр по максимальной цене -----
                    if (maxPrice != null && car.getPrice() != null
                            && car.getPrice().compareTo(maxPrice) > 0) {
                        return false;
                    }
                    
                    // ----- фильтр по наличию -----
                    boolean available = Boolean.TRUE.equals(car.getAvailable());

                    // только "В наличии"
                    if (Boolean.TRUE.equals(filterAvail)
                            && !Boolean.TRUE.equals(filterUnavail)
                            && !available) {
                        return false;
                    }

                    // только "Проданы"
                    if (!Boolean.TRUE.equals(filterAvail)
                            && Boolean.TRUE.equals(filterUnavail)
                            && available) {
                        return false;
                    }

                    // ----- фильтр "только чемпионы" -----
                    if (championsOnly && !Boolean.TRUE.equals(car.getConstructorsChampion())) {
                        return false;
                    }

                    // обе галочки включены или обе выключены — не фильтруем по наличию
                    return true;
                })
                .toList();
    }

    private Sort buildSort(String sortField, String direction) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        String property;
        switch (sortField) {
            case "name":
                property = "brand";
                break;
            case "price":
                property = "price";
                break;
            case "year":
            default:
                property = "seasonYear";
                break;
        }

        return Sort.by(dir, property);
    }

}