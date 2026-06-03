package com.coursework.autosalon.repository;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.coursework.autosalon.io.BinaryCarFile;
import com.coursework.autosalon.model.CarRecord;

/**
 * Класс-репозиторий для работы с бинарным файлом "Автосалон".
 * Здесь будут операции:
 *  - записать список записей в файл (создать/пересоздать файл);
 *  - прочитать все записи из файла.
 */
public class CarFileRepository {

    private final String fileName;

    public CarFileRepository(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Перезаписать файл полным списком записей.
     * Старое содержимое файла полностью заменяется.
     */
    public void writeAll(List<CarRecord> records) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName))) {
            for (CarRecord record : records) {
                BinaryCarFile.writeRecord(out, record);
            }
        }
    }

    /**
     * Прочитать все записи из файла.
     */
    public List<CarRecord> readAll() throws IOException {
        List<CarRecord> result = new ArrayList<>();

        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    CarRecord record = BinaryCarFile.readRecord(in);
                    result.add(record);
                } catch (IOException e) {
                    // Когда достигнут конец файла, read* бросит исключение.
                    // Мы выходим из цикла.
                    break;
                }
            }
        }

        return result;
    }
}
