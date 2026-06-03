package com.coursework.autosalon.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.coursework.autosalon.web.model.Car;
import com.coursework.autosalon.web.model.Team;
import com.coursework.autosalon.web.repository.CarRepository;
import com.coursework.autosalon.web.repository.PurchaseRequestRepository;
import com.coursework.autosalon.web.repository.TeamRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final CarRepository carRepository;
    private final TeamRepository teamRepository;
    private final PurchaseRequestRepository purchaseRequestRepository;

    private static final BigDecimal MAX_PRICE = new BigDecimal("100000000000");

    public AdminCarController(CarRepository carRepository,
                              TeamRepository teamRepository,
                              PurchaseRequestRepository purchaseRequestRepository) {
        this.carRepository = carRepository;
        this.teamRepository = teamRepository;
        this.purchaseRequestRepository = purchaseRequestRepository;
    }

    @GetMapping("/new")
    public String showCarForm(Model model) {
        List<Team> teams = teamRepository.findAll();
        model.addAttribute("car", new Car());
        model.addAttribute("teams", teams);
        return "admin/car-form";
    }

    @PostMapping("/new")
    public String createCar(@Valid @ModelAttribute Car car,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        // Дополнительная валидация цены
        if (car.getPrice() != null && car.getPrice().compareTo(MAX_PRICE) > 0) {
            bindingResult.rejectValue("price", "error.car", "Цена не может превышать 100 000 000 000 ₽");
        }

        // Проверка соответствия даты выпуска и сезона (rudiment, убрано)
        /*
        if (car.getReleaseDate() != null && car.getSeasonYear() != null) {
            if (!car.isReleaseDateMatchesSeason()) {
                bindingResult.rejectValue("releaseDate", "error.car",
                    "Год даты выпуска должен совпадать с годом сезона (" + car.getSeasonYear() + ")");
            }
        }
        */

        if (bindingResult.hasErrors()) {
            List<Team> teams = teamRepository.findAll();
            model.addAttribute("teams", teams);
            model.addAttribute("org.springframework.validation.BindingResult.car", bindingResult);
            return "admin/car-form";
        }

        try {
            // Дефолтные значения
            if (car.getAvailable() == null) {
                car.setAvailable(true);
            }
            if (car.getConstructorsChampion() == null) {
                car.setConstructorsChampion(false);
            }
            if (car.getPoints() == null) {
                car.setPoints(0);
            }
            if (car.getWins() == null) {
                car.setWins(0);
            }
            if (car.getMileageKm() == null) {
                car.setMileageKm(0);
            }
            // if (car.getReleaseDate() == null) { car.setReleaseDate(LocalDate.now()); }
            if (car.getHidden() == null) {
                car.setHidden(false);
            }

            carRepository.save(car);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Болид " + car.getBrand() + " успешно добавлен!"
            );

            return "redirect:/cars";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Ошибка при добавлении болида: " + e.getMessage()
            );
            return "redirect:/admin/cars/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditCarForm(@PathVariable("id") Long id, Model model) {
        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Болид не найден: " + id));

            List<Team> teams = teamRepository.findAll();
            model.addAttribute("car", car);
            model.addAttribute("teams", teams);
            model.addAttribute("isEdit", true);

            return "admin/car-form";

        } catch (Exception e) {
            return "redirect:/cars";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateCar(@PathVariable("id") Long id,
                            @Valid @ModelAttribute Car car,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        // Дополнительная валидация цены
        if (car.getPrice() != null && car.getPrice().compareTo(MAX_PRICE) > 0) {
            bindingResult.rejectValue("price", "error.car", "Цена не может превышать 100 000 000 000 ₽");
        }

        // Проверка соответствия даты выпуска и сезона (rudiment, убрано)
        /*
        if (car.getReleaseDate() != null && car.getSeasonYear() != null) {
            if (!car.isReleaseDateMatchesSeason()) {
                bindingResult.rejectValue("releaseDate", "error.car",
                    "Год даты выпуска должен совпадать с годом сезона (" + car.getSeasonYear() + ")");
            }
        }
        */

        if (bindingResult.hasErrors()) {
            List<Team> teams = teamRepository.findAll();
            model.addAttribute("teams", teams);
            model.addAttribute("isEdit", true);
            model.addAttribute("org.springframework.validation.BindingResult.car", bindingResult);
            return "admin/car-form";
        }

        try {
            Car existingCar = carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Болид не найден: " + id));

            // Обновляем поля существующего болида
            existingCar.setBrand(car.getBrand());
            existingCar.setPrice(car.getPrice());
            existingCar.setMileageKm(car.getMileageKm());
            // existingCar.setReleaseDate(car.getReleaseDate());
            existingCar.setTeam(car.getTeam());
            existingCar.setSeasonYear(car.getSeasonYear());
            existingCar.setWins(car.getWins());
            existingCar.setPoints(car.getPoints());
            existingCar.setBestResult(car.getBestResult());
            existingCar.setDescription(car.getDescription());
            existingCar.setAvailable(car.getAvailable());
            existingCar.setConstructorsChampion(car.getConstructorsChampion());
            existingCar.setHidden(car.getHidden());

            carRepository.save(existingCar);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Болид " + existingCar.getBrand() + " успешно обновлён!"
            );

            return "redirect:/cars/" + id;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Ошибка при обновлении болида: " + e.getMessage()
            );
            return "redirect:/admin/cars/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCar(@PathVariable("id") Long id,
                            RedirectAttributes redirectAttributes) {

        try {
            Car car = carRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Болид не найден: " + id));

            String carName = car.getBrand();

            // === Проверка через репозиторий заявок ===
            long count = purchaseRequestRepository.countByCar_Id(id);

            if (count > 0) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Нельзя удалить болид \"" + carName +
                                "\", потому что он используется в " + count +
                                " заявках. Удалите или завершите эти заявки."
                );
                return "redirect:/cars";
            }

            carRepository.delete(car);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Болид \"" + carName + "\" успешно удалён!"
            );

        } catch (Exception e) {

            // Перехват FK ошибок
            if (e.getMessage() != null &&
                    e.getMessage().contains("violates foreign key constraint")) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Удаление невозможно — болид связан с заявками. " +
                                "Сначала удалите заявки."
                );
                return "redirect:/cars";
            }

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Ошибка: " + e.getMessage()
            );
        }

        return "redirect:/cars";
    }

}
