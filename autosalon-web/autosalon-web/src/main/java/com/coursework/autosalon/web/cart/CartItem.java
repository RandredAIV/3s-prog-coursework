package com.coursework.autosalon.web.cart;

import java.math.BigDecimal;

import com.coursework.autosalon.web.model.Car;

public class CartItem {

    private Car car;
    private int quantity;

    public CartItem(Car car) {
        this.car = car;
        this.quantity = 1; // у нас по ТЗ один болид в единственном экземпляре
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        if (car == null || car.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return car.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
