package com.kelompok8.inventaris.peminjaman;

import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.Validator;

import java.time.LocalDate;

/**
 * Model transaksi peminjaman aset laboratorium.
 */
public class Peminjaman {
    private String idPeminjaman;
    private String kodeAset;
    private String namaPeminjam;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
    private String status; // Dipinjam, Dikembalikan
    private String keperluan;

    public Peminjaman(
            String idPeminjaman,
            String kodeAset,
            String namaPeminjam,
            LocalDate tanggalPinjam,
            LocalDate tanggalKembali,
            String status,
            String keperluan
    ) throws ValidasiException {
        setIdPeminjaman(idPeminjaman);
        setKodeAset(kodeAset);
        setNamaPeminjam(namaPeminjam);
        setTanggalPinjam(tanggalPinjam);
        setTanggalKembali(tanggalKembali);
        setStatus(status);
        setKeperluan(keperluan);
    }

    public String getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(String idPeminjaman) throws ValidasiException {
        this.idPeminjaman = Validator.wajibIsi(idPeminjaman, "ID Peminjaman").toUpperCase();
    }

    public String getKodeAset() {
        return kodeAset;
    }

    public void setKodeAset(String kodeAset) throws ValidasiException {
        this.kodeAset = Validator.wajibIsi(kodeAset, "Kode Aset").toUpperCase();
    }

    public String getNamaPeminjam() {
        return namaPeminjam;
    }

    public void setNamaPeminjam(String namaPeminjam) throws ValidasiException {
        this.namaPeminjam = Validator.wajibIsi(namaPeminjam, "Nama Peminjam");
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(LocalDate tanggalPinjam) throws ValidasiException {
        if (tanggalPinjam == null) {
            throw new ValidasiException("Tanggal pinjam tidak boleh kosong.");
        }
        this.tanggalPinjam = tanggalPinjam;
    }

    public LocalDate getTanggalKembali() {
        return tanggalKembali;
    }

    public void setTanggalKembali(LocalDate tanggalKembali) throws ValidasiException {
        if (tanggalKembali == null) {
            throw new ValidasiException("Tanggal kembali tidak boleh kosong.");
        }
        if (tanggalPinjam != null && tanggalKembali.isBefore(tanggalPinjam)) {
            throw new ValidasiException("Tanggal kembali tidak boleh sebelum tanggal pinjam.");
        }
        this.tanggalKembali = tanggalKembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) throws ValidasiException {
        this.status = Validator.wajibIsi(status, "Status Peminjaman");
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) throws ValidasiException {
        this.keperluan = Validator.wajibIsi(keperluan, "Keperluan");
    }
}
