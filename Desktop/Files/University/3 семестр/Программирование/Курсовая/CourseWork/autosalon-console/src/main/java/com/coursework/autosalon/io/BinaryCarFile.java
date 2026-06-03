package com.coursework.autosalon.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import com.coursework.autosalon.model.CarRecord;

/**
 * Вспомогательные методы для записи и чтения одной записи CarRecord
 * в двоичном формате.
 */
public class BinaryCarFile {

    /**
     * Записать одну запись в поток вывода.
     *
     * Порядок полей:
     *  1) brand (UTF-строка)
     *  2) manufacturer (UTF-строка)
     *  3) year (int)
     *  4) month (int)
     *  5) day (int)
     *  6) mileageKm (int)
     *  7) price (double)
     */
    public static void writeRecord(DataOutputStream out, CarRecord record) throws IOException {
        out.writeUTF(record.getBrand());
        out.writeUTF(record.getManufacturer());

        LocalDate date = record.getReleaseDate();
        out.writeInt(date.getYear());
        out.writeInt(date.getMonthValue());
        out.writeInt(date.getDayOfMonth());

        out.writeInt(record.getMileageKm());
        out.writeDouble(record.getPrice());
    }

    /**
     * Прочитать одну запись из потока ввода.
     *
     * ВАЖНО: порядок чтения должен совпадать с порядком записи.
     */
    public static CarRecord readRecord(DataInputStream in) throws IOException {
        String brand = in.readUTF();
        String manufacturer = in.readUTF();

        int year = in.readInt();
        int month = in.readInt();
        int day = in.readInt();
        LocalDate date = LocalDate.of(year, month, day);

        int mileageKm = in.readInt();
        double price = in.readDouble();

        return new CarRecord(brand, manufacturer, date, mileageKm, price);
    }
}
