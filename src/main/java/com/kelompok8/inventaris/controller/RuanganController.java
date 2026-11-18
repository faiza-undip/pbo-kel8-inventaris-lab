package com.kelompok8.inventaris.controller;

import com.kelompok8.inventaris.dao.RuanganDAO;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.ruangan.Ruangan;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller CRUD untuk entitas Ruangan Laboratorium (pola MVC).
 */
public class RuanganController {
    private final RuanganDAO ruanganDAO;
    private List<Ruangan> cacheRuangan;

    public RuanganController() throws PenyimpananException {
        this.ruanganDAO = new RuanganDAO();
        muatUlang();
    }

    public void muatUlang() throws PenyimpananException {
        this.cacheRuangan = new ArrayList<>(ruanganDAO.muatSemua());
    }

    public List<Ruangan> getSemua() {
        return new ArrayList<>(cacheRuangan);
    }

    public List<Ruangan> cari(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getSemua();
        }
        String k = keyword.trim().toLowerCase();
        List<Ruangan> hasil = new ArrayList<>();
        for (Ruangan r : cacheRuangan) {
            if (r.getKodeRuangan().toLowerCase().contains(k)
                    || r.getNamaRuangan().toLowerCase().contains(k)
                    || r.getGedung().toLowerCase().contains(k)
                    || r.getPenanggungJawab().toLowerCase().contains(k)) {
                hasil.add(r);
            }
        }
        return hasil;
    }

    public void tambah(Ruangan ruangan) throws ValidasiException, PenyimpananException {
        for (Ruangan r : cacheRuangan) {
            if (r.getKodeRuangan().equalsIgnoreCase(ruangan.getKodeRuangan())) {
                throw new ValidasiException("Kode ruangan '" + ruangan.getKodeRuangan() + "' sudah terdaftar.");
            }
        }
        cacheRuangan.add(ruangan);
        ruanganDAO.simpanSemua(cacheRuangan);
    }

    public void ubah(String kodeLama, Ruangan dataBaru) throws ValidasiException, PenyimpananException {
        int idx = cariIndeks(kodeLama);
        if (idx < 0) {
            throw new ValidasiException("Ruangan dengan kode '" + kodeLama + "' tidak ditemukan.");
        }
        if (!kodeLama.equalsIgnoreCase(dataBaru.getKodeRuangan())) {
            for (Ruangan r : cacheRuangan) {
                if (r.getKodeRuangan().equalsIgnoreCase(dataBaru.getKodeRuangan())) {
                    throw new ValidasiException("Kode ruangan '" + dataBaru.getKodeRuangan() + "' sudah digunakan.");
                }
            }
        }
        cacheRuangan.set(idx, dataBaru);
        ruanganDAO.simpanSemua(cacheRuangan);
    }

    public void hapus(String kodeRuangan) throws ValidasiException, PenyimpananException {
        int idx = cariIndeks(kodeRuangan);
        if (idx < 0) {
            throw new ValidasiException("Ruangan dengan kode '" + kodeRuangan + "' tidak ditemukan.");
        }
        cacheRuangan.remove(idx);
        ruanganDAO.simpanSemua(cacheRuangan);
    }

    private int cariIndeks(String kodeRuangan) {
        for (int i = 0; i < cacheRuangan.size(); i++) {
            if (cacheRuangan.get(i).getKodeRuangan().equalsIgnoreCase(kodeRuangan)) {
                return i;
            }
        }
        return -1;
    }
}
