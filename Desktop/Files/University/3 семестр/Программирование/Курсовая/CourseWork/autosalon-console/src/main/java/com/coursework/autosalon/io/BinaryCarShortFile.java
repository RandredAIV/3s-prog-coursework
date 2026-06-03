package com.coursework.autosalon.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.coursework.autosalon.model.CarShortRecord;

/**
 * Запись и чтение сокращённых записей (марка, пробег, цена)
 * для второго файла "autosalon_short.dat".
 */
public class BinaryCarShortFile {

    public static void writeRecord(DataOutputStream out, CarShortRecord r) throws IOException {
        out.writeUTF(r.getBrand());
        out.writeInt(r.getMileageKm());
        out.writeDouble(r.getPrice());
    }

    public static CarShortRecord readRecord(DataInputStream in) throws IOException {
        String brand = in.readUTF();
        int mileage = in.readInt();
        double price = in.readDouble();
        return new CarShortRecord(brand, mileage, price);
    }
}
