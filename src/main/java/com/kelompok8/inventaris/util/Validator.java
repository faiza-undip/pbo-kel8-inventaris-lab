package com.kelompok8.inventaris.util;

import com.kelompok8.inventaris.exception.ValidasiException;

/**
 * Utilitas validasi input agar pesan kesalahan konsisten dan tidak duplikat.
 */
public final class Validator {
    private Validator() {
    }

    public static String wajibIsi(String nilai, String namaField) throws ValidasiException {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new ValidasiException(namaField + " tidak boleh kosong.");
        }
        return nilai.trim();
    }

    public static double minimalNol(double nilai, String namaField) throws ValidasiException {
        if (nilai < 0) {
            throw new ValidasiException(namaField + " tidak boleh bernilai negatif.");
        }
        return nilai;
    }

    public static int minimalSatu(int nilai, String namaField) throws ValidasiException {
        if (nilai < 1) {
            throw new ValidasiException(namaField + " harus minimal 1.");
        }
        return nilai;
    }

    public static int rentangTahun(int tahun, int min, int max, String namaField) throws ValidasiException {
        if (tahun < min || tahun > max) {
            throw new ValidasiException(namaField + " harus berada di antara " + min + " dan " + max + ".");
        }
        return tahun;
    }
}
