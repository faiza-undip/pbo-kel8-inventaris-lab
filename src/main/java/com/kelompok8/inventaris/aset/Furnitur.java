package com.kelompok8.inventaris.aset;

import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.Validator;

/**
 * Kelas turunan Aset untuk furnitur laboratorium (meja lab, kursi, lemari alat).
 */
public class Furnitur extends Aset {
    private static final double LAJU_PENYUSUTAN_TAHUNAN = 0.10;
    private static final double MAKS_PENYUSUTAN = 0.80;

    private String bahan;
    private String dimensi;

    public Furnitur(
            String kodeAset,
            String namaAset,
            String merk,
            int tahunPerolehan,
            double hargaPerolehan,
            String kondisi,
            String kodeRuangan,
            boolean dapatDipinjam,
            String bahan,
            String dimensi
    ) throws ValidasiException {
        super(kodeAset, namaAset, merk, tahunPerolehan, hargaPerolehan, kondisi, kodeRuangan, dapatDipinjam);
        setBahan(bahan);
        setDimensi(dimensi);
    }

    @Override
    public String getKategori() {
        return "Furnitur";
    }

    @Override
    public double hitungPenyusutan(int tahunAcuan) {
        int usia = hitungUsiaTahun(tahunAcuan);
        double rasio = Math.min(MAKS_PENYUSUTAN, usia * LAJU_PENYUSUTAN_TAHUNAN);
        return getHargaPerolehan() * rasio;
    }

    @Override
    public String getSpesifikasiRingkas() {
        return "Bahan: " + bahan + ", Dimensi: " + dimensi;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) throws ValidasiException {
        this.bahan = Validator.wajibIsi(bahan, "Bahan Furnitur");
    }

    public String getDimensi() {
        return dimensi;
    }

    public void setDimensi(String dimensi) throws ValidasiException {
        this.dimensi = Validator.wajibIsi(dimensi, "Dimensi Furnitur");
    }
}
