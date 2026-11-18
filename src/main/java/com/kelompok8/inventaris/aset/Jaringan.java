package com.kelompok8.inventaris.aset;

import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.Validator;

/**
 * Kelas turunan Aset untuk perangkat jaringan laboratorium (router, switch, access point).
 */
public class Jaringan extends Aset implements Maintainable {
    private static final double LAJU_PENYUSUTAN_TAHUNAN = 0.15;
    private static final double MAKS_PENYUSUTAN = 0.85;

    private String tipePerangkat;
    private int jumlahPort;
    private int kecepatanMbps;

    public Jaringan(
            String kodeAset,
            String namaAset,
            String merk,
            int tahunPerolehan,
            double hargaPerolehan,
            String kondisi,
            String kodeRuangan,
            boolean dapatDipinjam,
            String tipePerangkat,
            int jumlahPort,
            int kecepatanMbps
    ) throws ValidasiException {
        super(kodeAset, namaAset, merk, tahunPerolehan, hargaPerolehan, kondisi, kodeRuangan, dapatDipinjam);
        setTipePerangkat(tipePerangkat);
        setJumlahPort(jumlahPort);
        setKecepatanMbps(kecepatanMbps);
    }

    @Override
    public String getKategori() {
        return "Jaringan";
    }

    @Override
    public double hitungPenyusutan(int tahunAcuan) {
        int usia = hitungUsiaTahun(tahunAcuan);
        double rasio = Math.min(MAKS_PENYUSUTAN, usia * LAJU_PENYUSUTAN_TAHUNAN);
        return getHargaPerolehan() * rasio;
    }

    @Override
    public String getSpesifikasiRingkas() {
        return tipePerangkat + ", " + jumlahPort + " port, " + kecepatanMbps + " Mbps";
    }

    @Override
    public boolean perluPerawatan(int tahunSekarang) {
        return hitungUsiaTahun(tahunSekarang) >= 2 || !"Baik".equalsIgnoreCase(getKondisi());
    }

    @Override
    public String getJadwalPerawatan() {
        return "Setiap 12 bulan (audit firmware, port, dan konektivitas)";
    }

    public String getTipePerangkat() {
        return tipePerangkat;
    }

    public void setTipePerangkat(String tipePerangkat) throws ValidasiException {
        this.tipePerangkat = Validator.wajibIsi(tipePerangkat, "Tipe Perangkat Jaringan");
    }

    public int getJumlahPort() {
        return jumlahPort;
    }

    public void setJumlahPort(int jumlahPort) throws ValidasiException {
        this.jumlahPort = Validator.minimalSatu(jumlahPort, "Jumlah Port");
    }

    public int getKecepatanMbps() {
        return kecepatanMbps;
    }

    public void setKecepatanMbps(int kecepatanMbps) throws ValidasiException {
        this.kecepatanMbps = Validator.minimalSatu(kecepatanMbps, "Kecepatan (Mbps)");
    }
}
