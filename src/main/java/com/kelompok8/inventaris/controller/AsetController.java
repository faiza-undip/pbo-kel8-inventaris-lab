package com.kelompok8.inventaris.controller;

import com.kelompok8.inventaris.aset.Aset;
import com.kelompok8.inventaris.aset.Maintainable;
import com.kelompok8.inventaris.dao.AsetDAO;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller CRUD, pencarian (overloading), dan kalkulasi penyusutan (polimorfisme) untuk Aset.
 */
public class AsetController {
    private final AsetDAO asetDAO;
    private List<Aset> cacheAset;

    public AsetController() throws PenyimpananException {
        this.asetDAO = new AsetDAO();
        muatUlang();
    }

    public void muatUlang() throws PenyimpananException {
        this.cacheAset = new ArrayList<>(asetDAO.muatSemua());
    }

    public List<Aset> getSemua() {
        return new ArrayList<>(cacheAset);
    }

    public Aset cariByKode(String kodeAset) {
        for (Aset a : cacheAset) {
            if (a.getKodeAset().equalsIgnoreCase(kodeAset)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Pencarian aset berdasarkan kata kunci bebas.
     */
    public List<Aset> cariAset(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getSemua();
        }
        String k = keyword.trim().toLowerCase();
        List<Aset> hasil = new ArrayList<>();
        for (Aset a : cacheAset) {
            if (a.getKodeAset().toLowerCase().contains(k)
                    || a.getNamaAset().toLowerCase().contains(k)
                    || a.getMerk().toLowerCase().contains(k)
                    || a.getKategori().toLowerCase().contains(k)
                    || a.getKodeRuangan().toLowerCase().contains(k)) {
                hasil.add(a);
            }
        }
        return hasil;
    }

    /**
     * Overloading metode cariAset berdasarkan filter kategori, kondisi, dan kata kunci.
     */
    public List<Aset> cariAset(String kategori, String kondisi, String keyword) {
        List<Aset> hasil = new ArrayList<>();
        for (Aset a : cariAset(keyword)) {
            boolean cocokKategori = "Semua".equalsIgnoreCase(kategori) || a.getKategori().equalsIgnoreCase(kategori);
            boolean cocokKondisi = "Semua".equalsIgnoreCase(kondisi) || a.getKondisi().equalsIgnoreCase(kondisi);
            if (cocokKategori && cocokKondisi) {
                hasil.add(a);
            }
        }
        return hasil;
    }

    public void tambah(Aset aset) throws ValidasiException, PenyimpananException {
        if (cariByKode(aset.getKodeAset()) != null) {
            throw new ValidasiException("Kode aset '" + aset.getKodeAset() + "' sudah terdaftar.");
        }
        cacheAset.add(aset);
        asetDAO.simpanSemua(cacheAset);
    }

    public void ubah(String kodeLama, Aset dataBaru) throws ValidasiException, PenyimpananException {
        int idx = cariIndeks(kodeLama);
        if (idx < 0) {
            throw new ValidasiException("Aset dengan kode '" + kodeLama + "' tidak ditemukan.");
        }
        if (!kodeLama.equalsIgnoreCase(dataBaru.getKodeAset()) && cariByKode(dataBaru.getKodeAset()) != null) {
            throw new ValidasiException("Kode aset '" + dataBaru.getKodeAset() + "' sudah digunakan aset lain.");
        }
        cacheAset.set(idx, dataBaru);
        asetDAO.simpanSemua(cacheAset);
    }

    public void hapus(String kodeAset) throws ValidasiException, PenyimpananException {
        int idx = cariIndeks(kodeAset);
        if (idx < 0) {
            throw new ValidasiException("Aset dengan kode '" + kodeAset + "' tidak ditemukan.");
        }
        cacheAset.remove(idx);
        asetDAO.simpanSemua(cacheAset);
    }

    public double hitungTotalNilaiPerolehan() {
        double total = 0;
        for (Aset a : cacheAset) {
            total += a.getHargaPerolehan();
        }
        return total;
    }

    public double hitungTotalPenyusutan(int tahunAcuan) {
        double total = 0;
        for (Aset a : cacheAset) {
            // Pemanggilan polimorfik melalui referensi kelas induk Aset
            total += a.hitungPenyusutan(tahunAcuan);
        }
        return total;
    }

    public double hitungTotalNilaiSekarang(int tahunAcuan) {
        double total = 0;
        for (Aset a : cacheAset) {
            total += a.hitungNilaiSekarang(tahunAcuan);
        }
        return total;
    }

    public int hitungPerluPerawatan() {
        int tahunSekarang = Year.now().getValue();
        int count = 0;
        for (Aset a : cacheAset) {
            if (a instanceof Maintainable m && m.perluPerawatan(tahunSekarang)) {
                count++;
            }
        }
        return count;
    }

    private int cariIndeks(String kodeAset) {
        for (int i = 0; i < cacheAset.size(); i++) {
            if (cacheAset.get(i).getKodeAset().equalsIgnoreCase(kodeAset)) {
                return i;
            }
        }
        return -1;
    }
}
