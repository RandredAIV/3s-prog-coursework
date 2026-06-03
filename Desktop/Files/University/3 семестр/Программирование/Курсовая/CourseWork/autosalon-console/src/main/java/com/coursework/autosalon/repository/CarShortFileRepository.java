package com.coursework.autosalon.repository;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.coursework.autosalon.io.BinaryCarShortFile;
import com.coursework.autosalon.model.CarShortRecord;

public class CarShortFileRepository {

    private final String fileName;

    public CarShortFileRepository(String fileName) {
        this.fileName = fileName;
    }

    public void writeAll(List<CarShortRecord> records) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(fileName))) {
            for (CarShortRecord r : records) {
                BinaryCarShortFile.writeRecord(out, r);
            }
        }
    }

    public List<CarShortRecord> readAll() throws IOException {
        List<CarShortRecord> list = new ArrayList<>();

        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    list.add(BinaryCarShortFile.readRecord(in));
                } catch (IOException e) {
                    break;
                }
            }
        }

        return list;
    }
}
