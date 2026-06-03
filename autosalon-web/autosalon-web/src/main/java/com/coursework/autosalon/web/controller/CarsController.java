package com.coursework.autosalon.web.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.coursework.autosalon.web.model.Car;
import com.coursework.autosalon.web.service.CarService;

/**
 * Контроллер для работы со страницами, связанными с болидами F1.
 * Отвечает за:
 *  - список болидов /cars (с фильтром по сезону),
 *  - страницу подробной информации /cars/{id}.
 */
@Controller
public class CarsController {

    private final CarService carService;

    /**
     * Внедряем сервис через конструктор.
     * Spring автоматически подставит сюда бин CarService.
     */
    public CarsController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Страница каталога болидов.
     *
     * URL:
     *   GET /cars
     *   GET /cars?season=2023
     *
     * Параметры:
     *   season (опционально) — если указан, показываем болиды только этого сезона.
     *
     * Модель:
     *   cars           — список болидов для отображения;
     *   championCars   — список чемпионских болидов 1980–1999;
     *   selectedSeason — выбранный сезон (может быть null).
     */
    @GetMapping("/cars")
    public String listCars(@RequestParam(name = "season", required = false) Integer season,
                           @RequestParam(name = "sort", required = false, defaultValue = "year") String sort,
                           @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir,
                           @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                           @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                           @RequestParam(name = "showAvailable", required = false) Boolean showAvailable,
                           @RequestParam(name = "showUnavailable", required = false) Boolean showUnavailable,
                           @RequestParam(name = "teamIds", required = false) List<Long> teamIds,
                           @RequestParam(name = "championsOnly", required = false, defaultValue = "false") boolean championsOnly,
                           @RequestParam(name = "showHidden", required = false) Boolean showHiddenParam,
                           Authentication authentication,
                           Model model) {

        // Проверяем, является ли пользователь админом
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        // Устанавливаем значение по умолчанию для showHidden
        Boolean showHidden = showHiddenParam;
        if (showHidden == null) {
            showHidden = false; // по умолчанию скрываем тестовые болиды
        }

        // Если пользователь не админ, всегда скрываем тестовые болиды
        if (!isAdmin) {
            showHidden = false;
        }

        // болиды с учётом сезона, сортировки, цены, наличия и производителя
        var cars = carService.findCars(
                season,
                sort,
                dir,
                minPrice,
                maxPrice,
                showAvailable,
                showUnavailable,
                championsOnly,
                teamIds,
                showHidden
        );

        // ---------- Компаратор для чемпионской секции ----------
        Comparator<Car> championsComparator;
        switch (sort) {
            case "name":
                championsComparator = Comparator.comparing(
                        Car::getBrand,
                        String.CASE_INSENSITIVE_ORDER
                );
                break;
            case "price":
                championsComparator = Comparator.comparing(
                        Car::getPrice,
                        Comparator.nullsLast(BigDecimal::compareTo)
                );
                break;
            case "year":
            default:
                championsComparator = Comparator.comparing(
                        Car::getSeasonYear,
                        Comparator.nullsLast(Integer::compareTo)
                );
                break;
        }

        if ("desc".equalsIgnoreCase(dir)) {
            championsComparator = championsComparator.reversed();
        }

        // чемпионы 1980–1999: применяем те же фильтры (цена, наличие, производитель)
        var championCars = carService.findChampionCars1980to1999()
                .stream()
                .filter(car -> {
                    // --- фильтр по производителю ---
                    if (teamIds != null && !teamIds.isEmpty()) {
                        if (car.getTeam() == null || !teamIds.contains(car.getTeam().getId())) {
                            return false;
                        }
                    }

                    // --- фильтр по минимальной цене ---
                    if (minPrice != null && car.getPrice() != null
                            && car.getPrice().compareTo(minPrice) < 0) {
                        return false;
                    }

                    // --- фильтр по максимальной цене ---
                    if (maxPrice != null && car.getPrice() != null
                            && car.getPrice().compareTo(maxPrice) > 0) {
                        return false;
                    }

                    // --- фильтр по наличию ---
                    boolean available = Boolean.TRUE.equals(car.getAvailable());

                    // только "В наличии"
                    if (Boolean.TRUE.equals(showAvailable)
                            && !Boolean.TRUE.equals(showUnavailable)
                            && !available) {
                        return false;
                    }

                    // только "Проданы"
                    if (!Boolean.TRUE.equals(showAvailable)
                            && Boolean.TRUE.equals(showUnavailable)
                            && available) {
                        return false;
                    }

                    // обе включены или обе выключены — не режем по наличию
                    return true;
                })
                .sorted(championsComparator)
                .toList();

        model.addAttribute("cars", cars);
        model.addAttribute("championCars", championCars);
        model.addAttribute("selectedSeason", season);

        // чтобы сохранить состояние фильтров в форме
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("showAvailable", showAvailable);
        model.addAttribute("showUnavailable", showUnavailable);
        model.addAttribute("championsOnly", championsOnly);
        model.addAttribute("showHidden", showHidden);
        model.addAttribute("isAdmin", isAdmin);

        // выбранные производители
        model.addAttribute("teamIds", teamIds);

        // удобные флаги под каждую команду (если где-то ещё используются)
        model.addAttribute("teamRedBull",
                teamIds != null && teamIds.contains(1L));
        model.addAttribute("teamMercedes",
                teamIds != null && teamIds.contains(2L));
        model.addAttribute("teamFerrari",
                teamIds != null && teamIds.contains(3L));
        model.addAttribute("teamWilliams",
                teamIds != null && teamIds.contains(4L));
        model.addAttribute("teamMcLaren",
                teamIds != null && teamIds.contains(5L));
        model.addAttribute("teamBenetton",
                teamIds != null && teamIds.contains(6L));

        return "cars/list";
    }

    /**
     * Страница подробной информации о конкретном болиде.
     *
     * URL:
     *   GET /cars/{id}
     *
     * Пример:
     *   /cars/1
     *
     * Модель:
     *   car — объект Car, который будет использоваться в шаблоне cars/detail.html.
     */
    @GetMapping("/cars/{id}")
    public String showCarDetails(@PathVariable Long id, Model model) {

        return carService.findById(id)
                .map(car -> {
                    model.addAttribute("car", car);
                    return "cars/detail";
                })
                .orElse("redirect:/cars");
    }
}
