package com.coursework.autosalon.model;

import java.time.LocalDate;

/**
 * Одна запись файла "Автосалон".
 * В методичке: марка, фирма, дата выпуска, пробег, цена.
 * Здесь:
 *  - brand        -> марка автомобиля (модель болида)
 *  - manufacturer -> фирма-изготовитель (команда)
 *  - releaseDate  -> дата выпуска
 *  - mileageKm    -> пробег, км
 *  - price        -> цена
 */
public class CarRecord {

    private String brand;         // марка
    private String manufacturer;  // фирма-изготовитель
    private LocalDate releaseDate;// дата выпуска
    private int mileageKm;        // пробег, км
    private double price;         // цена

    public CarRecord(String brand,
                     String manufacturer,
                     LocalDate releaseDate,
                     int mileageKm,
                     double price) {
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.releaseDate = releaseDate;
        this.mileageKm = mileageKm;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getMileageKm() {
        return mileageKm;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMileageKm(int mileageKm) {
        this.mileageKm = mileageKm;
    }

    @Override
    public String toString() {
        return "CarRecord{" +
                "brand='" + brand + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", releaseDate=" + releaseDate +
                ", mileageKm=" + mileageKm +
                ", price=" + price +
                '}';
    }
}
