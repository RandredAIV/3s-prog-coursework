package com.coursework.autosalon.model;

/**
 * Урезанная запись (только марка, пробег и цена).
 * Используется для второго файла (пункт 4 методички).
 */
public class CarShortRecord {

    private final String brand;
    private final int mileageKm;
    private final double price;

    public CarShortRecord(String brand, int mileageKm, double price) {
        this.brand = brand;
        this.mileageKm = mileageKm;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public int getMileageKm() {
        return mileageKm;
    }

    public double getPrice() {
        return price;
    }
}
