package com.kelompok8.inventaris.controller;

import com.kelompok8.inventaris.aset.Aset;
import com.kelompok8.inventaris.dao.PeminjamanDAO;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.peminjaman.Peminjaman;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller transaksi peminjaman aset laboratorium (pola MVC).
 */
public class PeminjamanController {
    private final PeminjamanDAO peminjamanDAO;
    private final AsetController asetController;
    private List<Peminjaman> cachePeminjaman;

    public PeminjamanController(AsetController asetController) throws PenyimpananException {
        this.peminjamanDAO = new PeminjamanDAO();
        this.asetController = asetController;
        muatUlang();
    }

    public void muatUlang() throws PenyimpananException {
        this.cachePeminjaman = new ArrayList<>(peminjamanDAO.muatSemua());
    }

    public List<Peminjaman> getSemua() {
        return new ArrayList<>(cachePeminjaman);
    }

    public List<Peminjaman> cari(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getSemua();
        }
        String k = keyword.trim().toLowerCase();
        List<Peminjaman> hasil = new ArrayList<>();
        for (Peminjaman p : cachePeminjaman) {
            if (p.getIdPeminjaman().toLowerCase().contains(k)
                    || p.getKodeAset().toLowerCase().contains(k)
                    || p.getNamaPeminjam().toLowerCase().contains(k)
                    || p.getStatus().toLowerCase().contains(k)
                    || p.getKeperluan().toLowerCase().contains(k)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public void tambah(Peminjaman peminjaman) throws ValidasiException, PenyimpananException {
        for (Peminjaman p : cachePeminjaman) {
            if (p.getIdPeminjaman().equalsIgnoreCase(peminjaman.getIdPeminjaman())) {
                throw new ValidasiException("ID Peminjaman '" + peminjaman.getIdPeminjaman() + "' sudah terdaftar.");
            }
        }
        validasiKetersediaanAset(peminjaman.getKodeAset(), peminjaman.getStatus(), null);
        cachePeminjaman.add(peminjaman);
        peminjamanDAO.simpanSemua(cachePeminjaman);
    }

    public void ubah(String idLama, Peminjaman dataBaru) throws ValidasiException, PenyimpananException {
        int idx = cariIndeks(idLama);
        if (idx < 0) {
            throw new ValidasiException("Transaksi peminjaman '" + idLama + "' tidak ditemukan.");
        }
        if (!idLama.equalsIgnoreCase(dataBaru.getIdPeminjaman())) {
            for (Peminjaman p : cachePeminjaman) {
                if (p.getIdPeminjaman().equalsIgnoreCase(dataBaru.getIdPeminjaman())) {
                    throw new ValidasiException("ID Peminjaman '" + dataBaru.getIdPeminjaman() + "' sudah digunakan.");
                }
            }
        }
        validasiKetersediaanAset(dataBaru.getKodeAset(), dataBaru.getStatus(), idLama);
        cachePeminjaman.set(idx, dataBaru);
        peminjamanDAO.simpanSemua(cachePeminjaman);
    }

    public void hapus(String idPeminjaman) throws ValidasiException, PenyimpananException {
        int idx = cariIndeks(idPeminjaman);
        if (idx < 0) {
            throw new ValidasiException("Transaksi peminjaman '" + idPeminjaman + "' tidak ditemukan.");
        }
        cachePeminjaman.remove(idx);
        peminjamanDAO.simpanSemua(cachePeminjaman);
    }

    public int hitungPeminjamanAktif() {
        int count = 0;
        for (Peminjaman p : cachePeminjaman) {
            if ("Dipinjam".equalsIgnoreCase(p.getStatus())) {
                count++;
            }
        }
        return count;
    }

    private void validasiKetersediaanAset(String kodeAset, String statusBaru, String idDikecualikan)
            throws ValidasiException {
        Aset aset = asetController.cariByKode(kodeAset);
        if (aset == null) {
            throw new ValidasiException("Aset dengan kode '" + kodeAset + "' tidak ditemukan.");
        }
        if ("Dipinjam".equalsIgnoreCase(statusBaru)) {
            if (!aset.isDapatDipinjam()) {
                throw new ValidasiException("Aset '" + aset.getNamaAset() + "' ditandai tidak dapat dipinjam.");
            }
            if ("Rusak Berat".equalsIgnoreCase(aset.getKondisi())) {
                throw new ValidasiException("Aset dalam kondisi Rusak Berat tidak dapat dipinjam.");
            }
            for (Peminjaman p : cachePeminjaman) {
                boolean bedaTransaksi = idDikecualikan == null || !p.getIdPeminjaman().equalsIgnoreCase(idDikecualikan);
                if (bedaTransaksi && p.getKodeAset().equalsIgnoreCase(kodeAset) && "Dipinjam".equalsIgnoreCase(p.getStatus())) {
                    throw new ValidasiException("Aset '" + kodeAset + "' sedang dipinjam oleh " + p.getNamaPeminjam() + ".");
                }
            }
        }
    }

    private int cariIndeks(String idPeminjaman) {
        for (int i = 0; i < cachePeminjaman.size(); i++) {
            if (cachePeminjaman.get(i).getIdPeminjaman().equalsIgnoreCase(idPeminjaman)) {
                return i;
            }
        }
        return -1;
    }
}
