package com.kelompok8.inventaris.dao;

import com.kelompok8.inventaris.aset.Aset;
import com.kelompok8.inventaris.aset.Furnitur;
import com.kelompok8.inventaris.aset.Jaringan;
import com.kelompok8.inventaris.aset.Komputer;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.FormatUtil;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO untuk mengelola data aset laboratorium (Komputer, Jaringan, Furnitur) di data/aset.txt.
 */
public class AsetDAO extends BaseFileDAO<Aset> {
    public AsetDAO() {
        super("aset.txt");
    }

    @Override
    public List<Aset> muatSemua() throws PenyimpananException {
        if (!Files.exists(filePath)) {
            List<Aset> seed = buatDataAwal();
            simpanSemua(seed);
            return seed;
        }
        List<Aset> daftar = new ArrayList<>();
        for (String baris : bacaSemuaBaris()) {
            daftar.add(parseAset(baris));
        }
        return daftar;
    }

    private Aset parseAset(String baris) throws PenyimpananException {
        String[] p = baris.split("\\|", -1);
        if (p.length < 11) {
            throw new PenyimpananException("Kolom data aset tidak lengkap: " + baris);
        }
        try {
            String kategori = p[0];
            String kode = p[1];
            String nama = p[2];
            String merk = p[3];
            int tahun = Integer.parseInt(p[4]);
            double harga = Double.parseDouble(p[5]);
            String kondisi = p[6];
            String kodeRuangan = p[7];
            boolean dapatDipinjam = Boolean.parseBoolean(p[8]);

            return switch (kategori) {
                case "Komputer" -> new Komputer(
                        kode, nama, merk, tahun, harga, kondisi, kodeRuangan, dapatDipinjam,
                        p[9], Integer.parseInt(p[10]), Integer.parseInt(p[11])
                );
                case "Jaringan" -> new Jaringan(
                        kode, nama, merk, tahun, harga, kondisi, kodeRuangan, dapatDipinjam,
                        p[9], Integer.parseInt(p[10]), Integer.parseInt(p[11])
                );
                case "Furnitur" -> new Furnitur(
                        kode, nama, merk, tahun, harga, kondisi, kodeRuangan, dapatDipinjam,
                        p[9], p[10]
                );
                default -> throw new PenyimpananException("Kategori aset tidak dikenal: " + kategori);
            };
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | ValidasiException e) {
            throw new PenyimpananException("Gagal mem-parsing data aset: " + baris, e);
        }
    }

    @Override
    public void simpanSemua(List<Aset> daftar) throws PenyimpananException {
        List<String> baris = new ArrayList<>();
        for (Aset a : daftar) {
            StringBuilder sb = new StringBuilder();
            sb.append(FormatUtil.bersihkanDelimiter(a.getKategori())).append("|")
              .append(FormatUtil.bersihkanDelimiter(a.getKodeAset())).append("|")
              .append(FormatUtil.bersihkanDelimiter(a.getNamaAset())).append("|")
              .append(FormatUtil.bersihkanDelimiter(a.getMerk())).append("|")
              .append(a.getTahunPerolehan()).append("|")
              .append(a.getHargaPerolehan()).append("|")
              .append(FormatUtil.bersihkanDelimiter(a.getKondisi())).append("|")
              .append(FormatUtil.bersihkanDelimiter(a.getKodeRuangan())).append("|")
              .append(a.isDapatDipinjam()).append("|");

            if (a instanceof Komputer k) {
                sb.append(FormatUtil.bersihkanDelimiter(k.getProcessor())).append("|")
                  .append(k.getRamGb()).append("|")
                  .append(k.getStorageGb());
            } else if (a instanceof Jaringan j) {
                sb.append(FormatUtil.bersihkanDelimiter(j.getTipePerangkat())).append("|")
                  .append(j.getJumlahPort()).append("|")
                  .append(j.getKecepatanMbps());
            } else if (a instanceof Furnitur f) {
                sb.append(FormatUtil.bersihkanDelimiter(f.getBahan())).append("|")
                  .append(FormatUtil.bersihkanDelimiter(f.getDimensi())).append("|0");
            }
            baris.add(sb.toString());
        }
        tulisSemuaBaris(baris);
    }

    public Map<String, Aset> muatSebagaiMap() throws PenyimpananException {
        Map<String, Aset> map = new LinkedHashMap<>();
        for (Aset a : muatSemua()) {
            map.put(a.getKodeAset(), a);
        }
        return map;
    }

    private List<Aset> buatDataAwal() throws PenyimpananException {
        List<Aset> list = new ArrayList<>();
        try {
            list.add(new Komputer("AST-PC01", "Workstation Praktikum 01", "Lenovo ThinkCentre", 2024, 12500000, "Baik", "LAB-RPL", true, "Intel Core i7-13700", 16, 512));
            list.add(new Komputer("AST-PC02", "PC Grafika 01", "ASUS ProArt", 2023, 18000000, "Baik", "LAB-MM", true, "AMD Ryzen 7 7700X", 32, 1024));
            list.add(new Jaringan("AST-NET01", "Managed Switch Core", "Cisco Catalyst", 2023, 9500000, "Baik", "LAB-JAR", false, "Switch L3", 24, 1000));
            list.add(new Jaringan("AST-NET02", "Router Praktikum", "MikroTik RB4011", 2024, 3800000, "Rusak Ringan", "LAB-JAR", true, "Router", 10, 1000));
            list.add(new Furnitur("AST-FR01", "Meja Komputer Lab", "Informa Pro", 2022, 1850000, "Baik", "LAB-RPL", false, "Kayu MDF & Besi", "120x60x75 cm"));
            list.add(new Furnitur("AST-FR02", "Kursi Ergonomis Lab", "Ergotec", 2024, 1200000, "Baik", "LAB-MM", true, "Mesh & Rangka Baja", "60x60x110 cm"));
        } catch (ValidasiException e) {
            throw new PenyimpananException("Gagal membuat data awal aset.", e);
        }
        return list;
    }
}
