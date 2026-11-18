package com.kelompok8.inventaris.aset;

import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.Validator;

import java.time.Year;

/**
 * Kelas abstrak induk untuk seluruh aset inventaris laboratorium.
 * Menerapkan enkapsulasi, validasi pada setter, metode abstrak, dan overloading.
 */
public abstract class Aset {
    private String kodeAset;
    private String namaAset;
    private String merk;
    private int tahunPerolehan;
    private double hargaPerolehan;
    private String kondisi; // Baik, Rusak Ringan, Rusak Berat
    private String kodeRuangan;
    private boolean dapatDipinjam;

    protected Aset(
            String kodeAset,
            String namaAset,
            String merk,
            int tahunPerolehan,
            double hargaPerolehan,
            String kondisi,
            String kodeRuangan,
            boolean dapatDipinjam
    ) throws ValidasiException {
        setKodeAset(kodeAset);
        setNamaAset(namaAset);
        setMerk(merk);
        setTahunPerolehan(tahunPerolehan);
        setHargaPerolehan(hargaPerolehan);
        setKondisi(kondisi);
        setKodeRuangan(kodeRuangan);
        setDapatDipinjam(dapatDipinjam);
    }

    public abstract String getKategori();

    public abstract double hitungPenyusutan(int tahunAcuan);

    public abstract String getSpesifikasiRingkas();

    /**
     * Overloading metode hitungPenyusutan menggunakan tahun kalender saat ini.
     */
    public double hitungPenyusutan() {
        return hitungPenyusutan(Year.now().getValue());
    }

    public double hitungNilaiSekarang(int tahunAcuan) {
        double sisa = hargaPerolehan - hitungPenyusutan(tahunAcuan);
        return Math.max(0, sisa);
    }

    public double hitungNilaiSekarang() {
        return hitungNilaiSekarang(Year.now().getValue());
    }

    protected int hitungUsiaTahun(int tahunAcuan) {
        return Math.max(0, tahunAcuan - tahunPerolehan);
    }

    public String getKodeAset() {
        return kodeAset;
    }

    public void setKodeAset(String kodeAset) throws ValidasiException {
        this.kodeAset = Validator.wajibIsi(kodeAset, "Kode Aset").toUpperCase();
    }

    public String getNamaAset() {
        return namaAset;
    }

    public void setNamaAset(String namaAset) throws ValidasiException {
        this.namaAset = Validator.wajibIsi(namaAset, "Nama Aset");
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) throws ValidasiException {
        this.merk = Validator.wajibIsi(merk, "Merk/Produsen");
    }

    public int getTahunPerolehan() {
        return tahunPerolehan;
    }

    public void setTahunPerolehan(int tahunPerolehan) throws ValidasiException {
        int tahunMaks = Year.now().getValue() + 1;
        this.tahunPerolehan = Validator.rentangTahun(tahunPerolehan, 1990, tahunMaks, "Tahun Perolehan");
    }

    public double getHargaPerolehan() {
        return hargaPerolehan;
    }

    public void setHargaPerolehan(double hargaPerolehan) throws ValidasiException {
        this.hargaPerolehan = Validator.minimalNol(hargaPerolehan, "Harga Perolehan");
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) throws ValidasiException {
        this.kondisi = Validator.wajibIsi(kondisi, "Kondisi Aset");
    }

    public String getKodeRuangan() {
        return kodeRuangan;
    }

    public void setKodeRuangan(String kodeRuangan) throws ValidasiException {
        this.kodeRuangan = Validator.wajibIsi(kodeRuangan, "Ruangan");
    }

    public boolean isDapatDipinjam() {
        return dapatDipinjam;
    }

    public void setDapatDipinjam(boolean dapatDipinjam) {
        this.dapatDipinjam = dapatDipinjam;
    }

    @Override
    public String toString() {
        return kodeAset + " - " + namaAset + " (" + getKategori() + ")";
    }
}
