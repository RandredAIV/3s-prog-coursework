package com.coursework.autosalon;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.coursework.autosalon.model.CarRecord;
import com.coursework.autosalon.model.CarShortRecord;
import com.coursework.autosalon.repository.CarFileRepository;
import com.coursework.autosalon.repository.CarShortFileRepository;
import com.coursework.autosalon.service.AutosalonService;

public class App {

    private static final String MAIN_FILE_NAME = "autosalon.dat";
    private static final String SHORT_FILE_NAME = "autosalon_short.dat";

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   Программа \"Автосалон\" (консоль)");
        System.out.println("   Вариант №43 - файл \"Автосалон\"");
        System.out.println("====================================");
        System.out.println();

        CarFileRepository mainRepository = new CarFileRepository(MAIN_FILE_NAME);
        CarShortFileRepository shortRepository = new CarShortFileRepository(SHORT_FILE_NAME);

        AutosalonService service = new AutosalonService(mainRepository, shortRepository);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Выберите пункт меню: ");
                String choice = scanner.nextLine().trim();
                System.out.println();

                switch (choice) {
                    case "1":
                        handleCreateDemoFile(service);
                        break;
                    case "2":
                        handleShowAll(service);
                        break;
                    case "3":
                        handleShowByMaxPrice(service, scanner);
                        break;
                    case "4":
                        handleCreateShortFile(service);
                        break;
                    case "5":
                        handleShowShortFile(service);
                        break;
                    case "6":
                        handleAddNewRecord(service, scanner);
                        break;
                    case "7":
                        handleDeleteByManufacturer(service, scanner);
                        break;
                    case "8":
                        handleUpdatePriceByBrand(service, scanner);
                        break;
                    case "0":
                        running = false;
                        System.out.println("Выход из программы. До свидания!");
                        break;
                    default:
                        System.out.println("Неизвестный пункт меню. Попробуйте ещё раз.");
                        System.out.println();
                        break;
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("Меню:");
        System.out.println("  1 - Создать/перезаписать файл демо-записями");
        System.out.println("  2 - Показать всё содержимое файла");
        System.out.println("  3 - Показать автомобили с ценой не выше заданной");
        System.out.println("  4 - Сформировать второй файл (марка, пробег, цена)");
        System.out.println("  5 - Показать содержимое второго файла");
        System.out.println("  6 - Добавить новую запись в основной файл");
        System.out.println("  7 - Удалить все записи указанной фирмы-изготовителя");
        System.out.println("  8 - Изменить цену для заданной марки автомобиля");
        System.out.println("  0 - Выход");
        System.out.println();
    }

    // ---- Пункт 1 ----
    private static void handleCreateDemoFile(AutosalonService service) {
        try {
            service.createDemoFile();
            System.out.println("Файл \"" + MAIN_FILE_NAME + "\" успешно создан.");
        } catch (Exception e) {
            System.out.println("Ошибка при создании файла: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Пункт 2 ----
    private static void handleShowAll(AutosalonService service) {
        try {
            List<CarRecord> records = service.loadAll();
            printRecordsFull(records);
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Пункт 3 ----
    private static void handleShowByMaxPrice(AutosalonService service, Scanner scanner) {
        try {
            System.out.print("Введите максимальную цену: ");
            String input = scanner.nextLine().trim().replace(',', '.');
            double maxPrice = Double.parseDouble(input);

            List<CarRecord> filtered = service.findCarsWithPriceNotGreaterThan(maxPrice);
            System.out.println("Автомобили с ценой не выше " + formatPrice(maxPrice) + ":");
            printRecordsForPriceFilter(filtered);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: некорректное число.");
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Пункт 4 ----
    private static void handleCreateShortFile(AutosalonService service) {
        try {
            service.createShortFile();
            System.out.println("Второй файл успешно сформирован: \"" + SHORT_FILE_NAME + "\"");
        } catch (Exception e) {
            System.out.println("Ошибка при формировании второго файла: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Пункт 5 ----
    private static void handleShowShortFile(AutosalonService service) {
        try {
            List<CarShortRecord> list = service.loadShortFile();
            System.out.println("Содержимое второго файла:");
            printShortRecords(list);
        } catch (Exception e) {
            System.out.println("Ошибка при чтении второго файла: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Пункт 6: добавить новую запись ----
    private static void handleAddNewRecord(AutosalonService service, Scanner scanner) {
        try {
            System.out.println("Добавление новой записи в файл \"Автосалон\".");
            System.out.print("Введите марку (модель болида): ");
            String brand = scanner.nextLine().trim();

            System.out.print("Введите фирму-изготовителя (команду): ");
            String manufacturer = scanner.nextLine().trim();

            System.out.print("Введите дату выпуска (в формате ГГГГ-ММ-ДД, например 2023-02-15): ");
            String dateStr = scanner.nextLine().trim();
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Ошибка: некорректный формат даты.");
                System.out.println();
                return;
            }

            System.out.print("Введите пробег (км, целое число): ");
            String mileageStr = scanner.nextLine().trim();
            int mileage;
            try {
                mileage = Integer.parseInt(mileageStr);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: пробег должен быть целым числом.");
                System.out.println();
                return;
            }

            System.out.print("Введите цену (рублей): ");
            String priceStr = scanner.nextLine().trim().replace(',', '.');
            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: цена должна быть числом.");
                System.out.println();
                return;
            }

            CarRecord newRecord = new CarRecord(brand, manufacturer, date, mileage, price);
            service.addRecord(newRecord);

            System.out.println("Запись успешно добавлена.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении записи: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Пункт 7: удалить записи по фирме ----
    private static void handleDeleteByManufacturer(AutosalonService service, Scanner scanner) {
        try {
            System.out.print("Введите фирму-изготовителя, чьи записи нужно удалить: ");
            String manufacturer = scanner.nextLine().trim();

            int removed = service.deleteByManufacturer(manufacturer);
            if (removed == 0) {
                System.out.println("Записей с фирмой \"" + manufacturer + "\" не найдено.");
            } else {
                System.out.println("Удалено записей: " + removed);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении записей: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Пункт 8: изменить цену по марке ----
    private static void handleUpdatePriceByBrand(AutosalonService service, Scanner scanner) {
        try {
            System.out.print("Введите марку автомобиля (модель болида), для которой нужно изменить цену: ");
            String brand = scanner.nextLine().trim();

            System.out.print("Введите новую цену: ");
            String priceStr = scanner.nextLine().trim().replace(',', '.');
            double newPrice;
            try {
                newPrice = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: некорректная цена.");
                System.out.println();
                return;
            }

            int updated = service.updatePriceByBrand(brand, newPrice);
            if (updated == 0) {
                System.out.println("Записей с маркой \"" + brand + "\" не найдено.");
            } else {
                System.out.println("Обновлено записей: " + updated);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении цены: " + e.getMessage());
        }
        System.out.println();
    }

    // ---- Выводы ----

    private static void printRecordsFull(List<CarRecord> records) {
        if (records.isEmpty()) {
            System.out.println("(файл пуст)");
            return;
        }
        int i = 1;
        for (CarRecord r : records) {
            System.out.println(i + ".");
            System.out.println("  Марка          : " + r.getBrand());
            System.out.println("  Фирма          : " + r.getManufacturer());
            System.out.println("  Дата выпуска   : " + r.getReleaseDate());
            System.out.println("  Пробег (км)    : " + r.getMileageKm());
            System.out.println("  Цена           : " + formatPrice(r.getPrice()));
            System.out.println();
            i++;
        }
    }

    private static void printRecordsForPriceFilter(List<CarRecord> records) {
        if (records.isEmpty()) {
            System.out.println("(нет подходящих автомобилей)");
            return;
        }
        int i = 1;
        for (CarRecord r : records) {
            System.out.println(i + ".");
            System.out.println("  Цена           : " + formatPrice(r.getPrice()));
            System.out.println("  Марка          : " + r.getBrand());
            System.out.println("  Дата выпуска   : " + r.getReleaseDate());
            System.out.println();
            i++;
        }
    }

    private static void printShortRecords(List<CarShortRecord> list) {
        if (list.isEmpty()) {
            System.out.println("(второй файл пуст)");
            return;
        }
        int i = 1;
        for (CarShortRecord r : list) {
            System.out.println(i + ".");
            System.out.println("  Марка          : " + r.getBrand());
            System.out.println("  Пробег (км)    : " + r.getMileageKm());
            System.out.println("  Цена           : " + formatPrice(r.getPrice()));
            System.out.println();
            i++;
        }
    }

    private static String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("#,###");
        String raw = df.format(price);
        return raw.replace(',', ' ') + " $";
    }
}
