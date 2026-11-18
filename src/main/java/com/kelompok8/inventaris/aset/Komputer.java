package com.kelompok8.inventaris.aset;

import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.Validator;

/**
 * Kelas turunan Aset untuk perangkat komputer laboratorium.
 * Menerapkan pewarisan, interface Maintainable, dan polimorfisme hitungPenyusutan().
 */
public class Komputer extends Aset implements Maintainable {
    private static final double LAJU_PENYUSUTAN_TAHUNAN = 0.20;
    private static final double MAKS_PENYUSUTAN = 0.90;

    private String processor;
    private int ramGb;
    private int storageGb;

    public Komputer(
            String kodeAset,
            String namaAset,
            String merk,
            int tahunPerolehan,
            double hargaPerolehan,
            String kondisi,
            String kodeRuangan,
            boolean dapatDipinjam,
            String processor,
            int ramGb,
            int storageGb
    ) throws ValidasiException {
        super(kodeAset, namaAset, merk, tahunPerolehan, hargaPerolehan, kondisi, kodeRuangan, dapatDipinjam);
        setProcessor(processor);
        setRamGb(ramGb);
        setStorageGb(storageGb);
    }

    @Override
    public String getKategori() {
        return "Komputer";
    }

    @Override
    public double hitungPenyusutan(int tahunAcuan) {
        int usia = hitungUsiaTahun(tahunAcuan);
        double rasio = Math.min(MAKS_PENYUSUTAN, usia * LAJU_PENYUSUTAN_TAHUNAN);
        return getHargaPerolehan() * rasio;
    }

    @Override
    public String getSpesifikasiRingkas() {
        return processor + ", RAM " + ramGb + " GB, Storage " + storageGb + " GB";
    }

    @Override
    public boolean perluPerawatan(int tahunSekarang) {
        return hitungUsiaTahun(tahunSekarang) >= 1 || !"Baik".equalsIgnoreCase(getKondisi());
    }

    @Override
    public String getJadwalPerawatan() {
        return "Setiap 6 bulan (pembersihan hardware & pembaruan OS)";
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) throws ValidasiException {
        this.processor = Validator.wajibIsi(processor, "Processor");
    }

    public int getRamGb() {
        return ramGb;
    }

    public void setRamGb(int ramGb) throws ValidasiException {
        this.ramGb = Validator.minimalSatu(ramGb, "Kapasitas RAM (GB)");
    }

    public int getStorageGb() {
        return storageGb;
    }

    public void setStorageGb(int storageGb) throws ValidasiException {
        this.storageGb = Validator.minimalSatu(storageGb, "Kapasitas Storage (GB)");
    }
}
