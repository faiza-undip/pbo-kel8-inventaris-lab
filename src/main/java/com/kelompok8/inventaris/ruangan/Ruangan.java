package com.kelompok8.inventaris.ruangan;

import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.Validator;

/**
 * Model entitas ruangan laboratorium tempat aset ditempatkan.
 */
public class Ruangan {
    private String kodeRuangan;
    private String namaRuangan;
    private String gedung;
    private int kapasitas;
    private String penanggungJawab;

    public Ruangan(
            String kodeRuangan,
            String namaRuangan,
            String gedung,
            int kapasitas,
            String penanggungJawab
    ) throws ValidasiException {
        setKodeRuangan(kodeRuangan);
        setNamaRuangan(namaRuangan);
        setGedung(gedung);
        setKapasitas(kapasitas);
        setPenanggungJawab(penanggungJawab);
    }

    public String getKodeRuangan() {
        return kodeRuangan;
    }

    public void setKodeRuangan(String kodeRuangan) throws ValidasiException {
        this.kodeRuangan = Validator.wajibIsi(kodeRuangan, "Kode Ruangan").toUpperCase();
    }

    public String getNamaRuangan() {
        return namaRuangan;
    }

    public void setNamaRuangan(String namaRuangan) throws ValidasiException {
        this.namaRuangan = Validator.wajibIsi(namaRuangan, "Nama Ruangan");
    }

    public String getGedung() {
        return gedung;
    }

    public void setGedung(String gedung) throws ValidasiException {
        this.gedung = Validator.wajibIsi(gedung, "Gedung");
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) throws ValidasiException {
        this.kapasitas = Validator.minimalSatu(kapasitas, "Kapasitas Ruangan");
    }

    public String getPenanggungJawab() {
        return penanggungJawab;
    }

    public void setPenanggungJawab(String penanggungJawab) throws ValidasiException {
        this.penanggungJawab = Validator.wajibIsi(penanggungJawab, "Penanggung Jawab");
    }

    @Override
    public String toString() {
        return kodeRuangan + " - " + namaRuangan;
    }
}
