package com.kelompok8.inventaris.dao;

import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.ruangan.Ruangan;
import com.kelompok8.inventaris.util.FormatUtil;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO untuk mengelola data ruangan laboratorium di file data/ruangan.txt.
 */
public class RuanganDAO extends BaseFileDAO<Ruangan> {
    public RuanganDAO() {
        super("ruangan.txt");
    }

    @Override
    public List<Ruangan> muatSemua() throws PenyimpananException {
        if (!Files.exists(filePath)) {
            List<Ruangan> seed = buatDataAwal();
            simpanSemua(seed);
            return seed;
        }
        List<Ruangan> daftar = new ArrayList<>();
        for (String baris : bacaSemuaBaris()) {
            String[] p = baris.split("\\|", -1);
            if (p.length >= 5) {
                try {
                    daftar.add(new Ruangan(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4]));
                } catch (NumberFormatException | ValidasiException e) {
                    throw new PenyimpananException("Format data ruangan tidak valid: " + baris, e);
                }
            }
        }
        return daftar;
    }

    @Override
    public void simpanSemua(List<Ruangan> daftar) throws PenyimpananException {
        List<String> baris = new ArrayList<>();
        for (Ruangan r : daftar) {
            baris.add(String.join("|",
                    FormatUtil.bersihkanDelimiter(r.getKodeRuangan()),
                    FormatUtil.bersihkanDelimiter(r.getNamaRuangan()),
                    FormatUtil.bersihkanDelimiter(r.getGedung()),
                    String.valueOf(r.getKapasitas()),
                    FormatUtil.bersihkanDelimiter(r.getPenanggungJawab())
            ));
        }
        tulisSemuaBaris(baris);
    }

    public Map<String, Ruangan> muatSebagaiMap() throws PenyimpananException {
        Map<String, Ruangan> map = new LinkedHashMap<>();
        for (Ruangan r : muatSemua()) {
            map.put(r.getKodeRuangan(), r);
        }
        return map;
    }

    private List<Ruangan> buatDataAwal() throws PenyimpananException {
        List<Ruangan> list = new ArrayList<>();
        try {
            list.add(new Ruangan("LAB-RPL", "Laboratorium Rekayasa Perangkat Lunak", "Gedung FIK Lt. 2", 40, "Faiza Tanjia"));
            list.add(new Ruangan("LAB-JAR", "Laboratorium Jaringan Komputer", "Gedung FIK Lt. 3", 32, "Muhammad Baihaqi"));
            list.add(new Ruangan("LAB-MM", "Laboratorium Multimedia & Grafika", "Gedung FIK Lt. 2", 30, "Chicochaesa Elang Z"));
        } catch (ValidasiException e) {
            throw new PenyimpananException("Gagal membuat data awal ruangan.", e);
        }
        return list;
    }
}
