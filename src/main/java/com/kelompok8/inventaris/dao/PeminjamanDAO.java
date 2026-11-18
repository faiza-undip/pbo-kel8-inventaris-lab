package com.kelompok8.inventaris.dao;

import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.peminjaman.Peminjaman;
import com.kelompok8.inventaris.util.FormatUtil;

import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk mengelola transaksi peminjaman aset di file data/peminjaman.txt.
 */
public class PeminjamanDAO extends BaseFileDAO<Peminjaman> {
    public PeminjamanDAO() {
        super("peminjaman.txt");
    }

    @Override
    public List<Peminjaman> muatSemua() throws PenyimpananException {
        if (!Files.exists(filePath)) {
            List<Peminjaman> seed = buatDataAwal();
            simpanSemua(seed);
            return seed;
        }
        List<Peminjaman> daftar = new ArrayList<>();
        for (String baris : bacaSemuaBaris()) {
            String[] p = baris.split("\\|", -1);
            if (p.length >= 7) {
                try {
                    daftar.add(new Peminjaman(
                            p[0],
                            p[1],
                            p[2],
                            LocalDate.parse(p[3]),
                            LocalDate.parse(p[4]),
                            p[5],
                            p[6]
                    ));
                } catch (DateTimeParseException | ValidasiException e) {
                    throw new PenyimpananException("Format data peminjaman tidak valid: " + baris, e);
                }
            }
        }
        return daftar;
    }

    @Override
    public void simpanSemua(List<Peminjaman> daftar) throws PenyimpananException {
        List<String> baris = new ArrayList<>();
        for (Peminjaman pm : daftar) {
            baris.add(String.join("|",
                    FormatUtil.bersihkanDelimiter(pm.getIdPeminjaman()),
                    FormatUtil.bersihkanDelimiter(pm.getKodeAset()),
                    FormatUtil.bersihkanDelimiter(pm.getNamaPeminjam()),
                    pm.getTanggalPinjam().toString(),
                    pm.getTanggalKembali().toString(),
                    FormatUtil.bersihkanDelimiter(pm.getStatus()),
                    FormatUtil.bersihkanDelimiter(pm.getKeperluan())
            ));
        }
        tulisSemuaBaris(baris);
    }

    private List<Peminjaman> buatDataAwal() throws PenyimpananException {
        List<Peminjaman> list = new ArrayList<>();
        try {
            LocalDate hariIni = LocalDate.now();
            list.add(new Peminjaman("PJM-001", "AST-PC01", "Raka Pratama", hariIni.minusDays(2), hariIni.plusDays(3), "Dipinjam", "Praktikum Basis Data"));
            list.add(new Peminjaman("PJM-002", "AST-NET02", "Sinta Dewi", hariIni.minusDays(7), hariIni.minusDays(2), "Dikembalikan", "Simulasi Routing BGP"));
        } catch (ValidasiException e) {
            throw new PenyimpananException("Gagal membuat data awal peminjaman.", e);
        }
        return list;
    }
}
