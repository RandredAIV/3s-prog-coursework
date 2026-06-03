package com.coursework.autosalon.web.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.coursework.autosalon.web.model.Car;

public class Cart {

    private final List<CartItem> items = new ArrayList<>();

    // ===== базовые геттеры =====

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalCount() {
        return items.size();
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(ci -> {
                    Car c = ci.getCar();
                    return (c != null && c.getPrice() != null)
                            ? c.getPrice()
                            : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear() {
        items.clear();
    }

    // ===== работа с болидами =====

    /**
     * Проверка: есть ли болид с таким id в корзине
     */
    public boolean contains(Long carId) {
        if (carId == null) return false;

        return items.stream()
                .map(CartItem::getCar)
                .filter(Objects::nonNull)
                .anyMatch(c -> Objects.equals(c.getId(), carId));
    }

    /**
     * Добавление болида в корзину.
     * Если болид уже есть — просто ничего не делаем.
     */
    public void addItem(Car car) {
        if (car == null || car.getId() == null) {
            return;
        }

        if (contains(car.getId())) {
            // уже есть в корзине — не дублируем
            return;
        }

        items.add(new CartItem(car));
    }

    /**
     * Удаление болида по id
     */
    public void removeItem(Long carId) {
        if (carId == null) return;

        items.removeIf(ci ->
                ci.getCar() != null &&
                        Objects.equals(ci.getCar().getId(), carId)
        );
    }
}
