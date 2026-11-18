package com.kelompok8.inventaris.dao;

import com.kelompok8.inventaris.exception.PenyimpananException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas dasar abstrak untuk operasi baca/tulis file teks secara permanen.
 */
public abstract class BaseFileDAO<T> {
    protected static final String DATA_DIR = "data";
    protected final Path filePath;

    protected BaseFileDAO(String fileName) {
        this.filePath = Paths.get(DATA_DIR, fileName);
    }

    protected List<String> bacaSemuaBaris() throws PenyimpananException {
        List<String> hasil = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return hasil;
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    hasil.add(line);
                }
            }
            return hasil;
        } catch (IOException e) {
            throw new PenyimpananException("Gagal membaca file data: " + filePath, e);
        }
    }

    protected void tulisSemuaBaris(List<String> baris) throws PenyimpananException {
        try {
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                for (String b : baris) {
                    writer.write(b);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new PenyimpananException("Gagal menyimpan file data: " + filePath, e);
        }
    }

    public abstract List<T> muatSemua() throws PenyimpananException;

    public abstract void simpanSemua(List<T> daftar) throws PenyimpananException;
}
