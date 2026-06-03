package com.coursework.autosalon.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.coursework.autosalon.model.CarRecord;
import com.coursework.autosalon.model.CarShortRecord;
import com.coursework.autosalon.repository.CarFileRepository;
import com.coursework.autosalon.repository.CarShortFileRepository;

/**
 * Сервисный класс, который инкапсулирует логику работы с файлами.
 */
public class AutosalonService {

    private final CarFileRepository mainRepository;
    private final CarShortFileRepository shortRepository;

    public AutosalonService(CarFileRepository mainRepository, CarShortFileRepository shortRepository) {
        this.mainRepository = mainRepository;
        this.shortRepository = shortRepository;
    }

    // ---- Пункт 1: создать файл ----
    public void createDemoFile() throws IOException {
        List<CarRecord> records = createSampleRecords();
        mainRepository.writeAll(records);
    }

    // ---- Пункт 2: вывести всё содержимое ----
    public List<CarRecord> loadAll() throws IOException {
        return mainRepository.readAll();
    }

    // ---- Пункт 3: фильтр по максимальной цене ----
    public List<CarRecord> findCarsWithPriceNotGreaterThan(double maxPrice) throws IOException {
        List<CarRecord> all = mainRepository.readAll();
        List<CarRecord> result = new ArrayList<>();
        for (CarRecord r : all) {
            if (r.getPrice() <= maxPrice) {
                result.add(r);
            }
        }
        return result;
    }

    // ---- Пункт 4: сформировать второй файл (марка, пробег, цена) ----
    public void createShortFile() throws IOException {
        List<CarRecord> all = mainRepository.readAll();
        List<CarShortRecord> shorts = new ArrayList<>();

        for (CarRecord r : all) {
            shorts.add(new CarShortRecord(
                    r.getBrand(),
                    r.getMileageKm(),
                    r.getPrice()
            ));
        }

        shortRepository.writeAll(shorts);
    }

    // ---- Пункт 5: вывести второй файл ----
    public List<CarShortRecord> loadShortFile() throws IOException {
        return shortRepository.readAll();
    }

    // ---- Пункт 6: добавить новую запись в основной файл ----
    public void addRecord(CarRecord newRecord) throws IOException {
        List<CarRecord> all = mainRepository.readAll();
        all.add(newRecord);
        mainRepository.writeAll(all);
    }

    // ---- Пункт 7: удалить все записи указанной фирмы-изготовителя ----
    /**
     * Удаляет все записи, у которых фирма-изготовитель совпадает (без учёта регистра)
     * с переданным значением. Возвращает количество удалённых записей.
     */
    public int deleteByManufacturer(String manufacturer) throws IOException {
        List<CarRecord> all = mainRepository.readAll();
        List<CarRecord> filtered = new ArrayList<>();
        int removed = 0;

        for (CarRecord r : all) {
            if (r.getManufacturer().equalsIgnoreCase(manufacturer)) {
                removed++;
            } else {
                filtered.add(r);
            }
        }

        mainRepository.writeAll(filtered);
        return removed;
    }

    // ---- Пункт 8: изменить цену для заданной марки автомобиля ----
    /**
     * Изменяет цену всех автомобилей с заданной маркой (без учёта регистра).
     * Возвращает количество изменённых записей.
     */
    public int updatePriceByBrand(String brand, double newPrice) throws IOException {
        List<CarRecord> all = mainRepository.readAll();
        int updated = 0;

        for (CarRecord r : all) {
            if (r.getBrand().equalsIgnoreCase(brand)) {
                r.setPrice(newPrice);
                updated++;
            }
        }

        mainRepository.writeAll(all);
        return updated;
    }

    // ---- Демо-данные ----
    private List<CarRecord> createSampleRecords() {
        CarRecord r1 = new CarRecord(
                "Mercedes W11",
                "Mercedes-AMG Petronas",
                LocalDate.of(2020, 2, 1),
                15000,
                25_000_000.0
        );

        CarRecord r2 = new CarRecord(
                "Ferrari SF-23",
                "Scuderia Ferrari",
                LocalDate.of(2023, 2, 15),
                8000,
                23_000_000.0
        );

        CarRecord r3 = new CarRecord(
                "Red Bull RB19",
                "Oracle Red Bull Racing",
                LocalDate.of(2023, 2, 20),
                10000,
                27_000_000.0
        );

        return Arrays.asList(r1, r2, r3);
    }
}
