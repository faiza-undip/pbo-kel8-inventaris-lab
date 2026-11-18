package com.kelompok8.inventaris.dao;

import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.pengguna.Pengguna;
import com.kelompok8.inventaris.util.FormatUtil;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO untuk mengelola data pengguna (login & role) di file data/pengguna.txt.
 */
public class PenggunaDAO extends BaseFileDAO<Pengguna> {
    public PenggunaDAO() {
        super("pengguna.txt");
    }

    @Override
    public List<Pengguna> muatSemua() throws PenyimpananException {
        if (!Files.exists(filePath)) {
            List<Pengguna> seed = buatDataAwal();
            simpanSemua(seed);
            return seed;
        }
        List<Pengguna> daftar = new ArrayList<>();
        for (String baris : bacaSemuaBaris()) {
            String[] p = baris.split("\\|", -1);
            if (p.length >= 4) {
                try {
                    daftar.add(new Pengguna(p[0], p[1], p[2], p[3]));
                } catch (ValidasiException e) {
                    throw new PenyimpananException("Format data pengguna tidak valid: " + baris, e);
                }
            }
        }
        return daftar;
    }

    @Override
    public void simpanSemua(List<Pengguna> daftar) throws PenyimpananException {
        List<String> baris = new ArrayList<>();
        for (Pengguna u : daftar) {
            baris.add(String.join("|",
                    FormatUtil.bersihkanDelimiter(u.getUsername()),
                    FormatUtil.bersihkanDelimiter(u.getPassword()),
                    FormatUtil.bersihkanDelimiter(u.getNamaLengkap()),
                    FormatUtil.bersihkanDelimiter(u.getRole())
            ));
        }
        tulisSemuaBaris(baris);
    }

    public Map<String, Pengguna> muatSebagaiMap() throws PenyimpananException {
        Map<String, Pengguna> map = new LinkedHashMap<>();
        for (Pengguna p : muatSemua()) {
            map.put(p.getUsername(), p);
        }
        return map;
    }

    private List<Pengguna> buatDataAwal() throws PenyimpananException {
        List<Pengguna> list = new ArrayList<>();
        try {
            list.add(new Pengguna("admin", "admin123", "Faiza Tanjia (Koordinator Lab)", "Admin"));
            list.add(new Pengguna("laboran", "lab123", "Muhammad Baihaqi (Laboran)", "Laboran"));
            list.add(new Pengguna("asisten", "asisten123", "Chicochaesa Elang Z (Asisten Lab)", "Laboran"));
        } catch (ValidasiException e) {
            throw new PenyimpananException("Gagal membuat data awal pengguna.", e);
        }
        return list;
    }
}
