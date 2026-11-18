package com.kelompok8.inventaris;

import com.kelompok8.inventaris.aset.Aset;
import com.kelompok8.inventaris.aset.Furnitur;
import com.kelompok8.inventaris.aset.Jaringan;
import com.kelompok8.inventaris.aset.Komputer;
import com.kelompok8.inventaris.aset.Maintainable;
import com.kelompok8.inventaris.controller.AsetController;
import com.kelompok8.inventaris.controller.AuthController;
import com.kelompok8.inventaris.controller.PeminjamanController;
import com.kelompok8.inventaris.controller.RuanganController;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.peminjaman.Peminjaman;
import com.kelompok8.inventaris.ruangan.Ruangan;

import java.time.LocalDate;

/**
 * Pengujian otomatis untuk memverifikasi konsep OOP, validasi, DAO, dan Controller MVC.
 */
public class InventarisTest {
    private static int lulus = 0;

    public static void main(String[] args) throws Exception {
        ujiValidasiEnkapsulasi();
        ujiPolimorfismeDanAbstraksi();
        ujiDaoDanController();
        System.out.println("Semua pengujian lulus! Total asersi berhasil: " + lulus);
    }

    private static void ujiValidasiEnkapsulasi() {
        boolean gagalHargaNegatif = false;
        try {
            new Komputer("AST-T1", "PC Uji", "Dell", 2024, -500000, "Baik", "LAB-RPL", true, "i5", 8, 256);
        } catch (ValidasiException e) {
            gagalHargaNegatif = true;
        }
        assertBenar(gagalHargaNegatif, "Harga negatif harus melempar ValidasiException");

        boolean gagalTanggalKembali = false;
        try {
            new Peminjaman("PJM-T1", "AST-PC01", "Budi", LocalDate.of(2026, 5, 10), LocalDate.of(2026, 5, 8), "Dipinjam", "Uji");
        } catch (ValidasiException e) {
            gagalTanggalKembali = true;
        }
        assertBenar(gagalTanggalKembali, "Tanggal kembali sebelum tanggal pinjam harus melempar ValidasiException");
    }

    private static void ujiPolimorfismeDanAbstraksi() throws ValidasiException {
        Aset pc = new Komputer("K1", "PC Lab", "HP", 2022, 10_000_000, "Baik", "LAB-RPL", true, "i7", 16, 512);
        Aset sw = new Jaringan("J1", "Switch", "Cisco", 2022, 10_000_000, "Baik", "LAB-JAR", false, "Switch", 24, 1000);
        Aset mj = new Furnitur("F1", "Meja", "Informa", 2022, 10_000_000, "Baik", "LAB-RPL", false, "Kayu", "120x60");

        // Dalam 2 tahun (2024): Komputer 40%, Jaringan 30%, Furnitur 20%
        assertSama(4_000_000.0, pc.hitungPenyusutan(2024), "Penyusutan Komputer 2 tahun = 40%");
        assertSama(3_000_000.0, sw.hitungPenyusutan(2024), "Penyusutan Jaringan 2 tahun = 30%");
        assertSama(2_000_000.0, mj.hitungPenyusutan(2024), "Penyusutan Furnitur 2 tahun = 20%");

        assertBenar(pc instanceof Maintainable, "Komputer mengimplementasikan Maintainable");
        assertBenar(sw instanceof Maintainable, "Jaringan mengimplementasikan Maintainable");
        assertBenar(!(mj instanceof Maintainable), "Furnitur tidak mengimplementasikan Maintainable");
    }

    private static void ujiDaoDanController() throws Exception {
        AuthController auth = new AuthController();
        assertBenar(auth.login("admin", "admin123") != null, "Login admin berhasil");

        RuanganController rc = new RuanganController();
        assertBenar(rc.getSemua().size() >= 3, "Data awal ruangan termuat");

        AsetController ac = new AsetController();
        assertBenar(ac.getSemua().size() >= 6, "Data awal aset termuat");
        assertBenar(!ac.cariAset("Komputer", "Semua", "").isEmpty(), "Overloading cariAset bekerja");

        PeminjamanController pc = new PeminjamanController(ac);
        assertBenar(pc.getSemua().size() >= 2, "Data awal peminjaman termuat");

        // Uji tambah + hapus ruangan sementara agar data tetap bersih
        Ruangan rTemp = new Ruangan("LAB-TEST", "Lab Pengujian", "Gedung Uji", 20, "Tester");
        rc.tambah(rTemp);
        assertBenar(rc.cari("LAB-TEST").size() == 1, "Ruangan baru tersimpan");
        rc.hapus("LAB-TEST");
        assertBenar(rc.cari("LAB-TEST").isEmpty(), "Ruangan uji berhasil dihapus");
    }

    private static void assertBenar(boolean kondisi, String pesan) {
        if (!kondisi) {
            throw new AssertionError("GAGAL: " + pesan);
        }
        lulus++;
    }

    private static void assertSama(double expected, double actual, String pesan) {
        if (Math.abs(expected - actual) > 0.01) {
            throw new AssertionError("GAGAL: " + pesan + " (expected=" + expected + ", actual=" + actual + ")");
        }
        lulus++;
    }
}
