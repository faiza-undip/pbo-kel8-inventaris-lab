package com.kelompok8.inventaris.view;

import com.kelompok8.inventaris.aset.Aset;
import com.kelompok8.inventaris.aset.Maintainable;
import com.kelompok8.inventaris.controller.AsetController;
import com.kelompok8.inventaris.controller.PeminjamanController;
import com.kelompok8.inventaris.controller.RuanganController;
import com.kelompok8.inventaris.pengguna.Pengguna;
import com.kelompok8.inventaris.util.FormatUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;

/**
 * Panel ringkasan dashboard inventaris laboratorium.
 */
public class DashboardPanel extends JPanel {
    private final Pengguna pengguna;
    private final AsetController asetController;
    private final RuanganController ruanganController;
    private final PeminjamanController peminjamanController;

    private JLabel lblTotalAset;
    private JLabel lblTotalRuangan;
    private JLabel lblPinjamAktif;
    private JLabel lblPerluRawat;
    private JLabel lblNilaiPerolehan;
    private JLabel lblTotalPenyusutan;
    private JLabel lblNilaiSekarang;
    private DefaultTableModel modelPerawatan;

    public DashboardPanel(
            Pengguna pengguna,
            AsetController asetController,
            RuanganController ruanganController,
            PeminjamanController peminjamanController
    ) {
        this.pengguna = pengguna;
        this.asetController = asetController;
        this.ruanganController = ruanganController;
        this.peminjamanController = peminjamanController;
        initComponents();
        refreshData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(16, 16));
        setBackground(UiHelper.WARNA_LATAR);
        setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(Color.WHITE);
        banner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(14, 18, 14, 18)
        ));
        JLabel lblSalam = new JLabel("Selamat Datang, " + pengguna.getNamaLengkap() + " [" + pengguna.getRole() + "]");
        lblSalam.setFont(UiHelper.FONT_JUDUL);
        lblSalam.setForeground(UiHelper.WARNA_UTAMA);
        JLabel lblKet = new JLabel("Ringkasan kondisi aset, nilai penyusutan, dan status peminjaman laboratorium.");
        lblKet.setFont(UiHelper.FONT_BIASA);
        banner.add(lblSalam, BorderLayout.NORTH);
        banner.add(lblKet, BorderLayout.SOUTH);
        add(banner, BorderLayout.NORTH);

        JPanel tengah = new JPanel(new BorderLayout(14, 14));
        tengah.setOpaque(false);

        JPanel kartuGrid = new JPanel(new GridLayout(1, 4, 12, 12));
        kartuGrid.setOpaque(false);
        lblTotalAset = new JLabel("0");
        lblTotalRuangan = new JLabel("0");
        lblPinjamAktif = new JLabel("0");
        lblPerluRawat = new JLabel("0");
        kartuGrid.add(buatKartu("Total Aset", lblTotalAset, new Color(37, 99, 235)));
        kartuGrid.add(buatKartu("Total Ruangan", lblTotalRuangan, new Color(16, 185, 129)));
        kartuGrid.add(buatKartu("Sedang Dipinjam", lblPinjamAktif, new Color(245, 158, 11)));
        kartuGrid.add(buatKartu("Perlu Perawatan", lblPerluRawat, new Color(220, 38, 38)));

        JPanel nilaiPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        nilaiPanel.setOpaque(false);
        lblNilaiPerolehan = new JLabel("Rp0");
        lblTotalPenyusutan = new JLabel("Rp0");
        lblNilaiSekarang = new JLabel("Rp0");
        nilaiPanel.add(buatKartu("Total Nilai Perolehan", lblNilaiPerolehan, new Color(51, 65, 85)));
        nilaiPanel.add(buatKartu("Akumulasi Penyusutan", lblTotalPenyusutan, new Color(180, 83, 9)));
        nilaiPanel.add(buatKartu("Nilai Buku Saat Ini", lblNilaiSekarang, new Color(21, 128, 61)));

        JPanel ringkasanAtas = new JPanel(new GridLayout(2, 1, 12, 12));
        ringkasanAtas.setOpaque(false);
        ringkasanAtas.add(kartuGrid);
        ringkasanAtas.add(nilaiPanel);
        tengah.add(ringkasanAtas, BorderLayout.NORTH);

        modelPerawatan = new DefaultTableModel(
                new String[]{"Kode Aset", "Nama Aset", "Kategori", "Kondisi", "Ruangan", "Jadwal Perawatan"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabel = new JTable(modelPerawatan);
        tabel.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createTitledBorder("Daftar Aset yang Memerlukan Perawatan (Interface Maintainable)"));
        tengah.add(scroll, BorderLayout.CENTER);

        add(tengah, BorderLayout.CENTER);
    }

    private JPanel buatKartu(String judul, JLabel lblNilai, Color warnaAksen) {
        JPanel p = new JPanel(new BorderLayout(4, 6));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, warnaAksen),
                new EmptyBorder(12, 14, 12, 14)
        ));
        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblJudul.setForeground(UiHelper.WARNA_NETRAL);

        lblNilai.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNilai.setForeground(new Color(15, 23, 42));

        p.add(lblJudul, BorderLayout.NORTH);
        p.add(lblNilai, BorderLayout.CENTER);
        return p;
    }

    public void refreshData() {
        int tahunSekarang = Year.now().getValue();
        lblTotalAset.setText(String.valueOf(asetController.getSemua().size()));
        lblTotalRuangan.setText(String.valueOf(ruanganController.getSemua().size()));
        lblPinjamAktif.setText(String.valueOf(peminjamanController.hitungPeminjamanAktif()));
        lblPerluRawat.setText(String.valueOf(asetController.hitungPerluPerawatan()));

        lblNilaiPerolehan.setText(FormatUtil.formatRupiah(asetController.hitungTotalNilaiPerolehan()));
        lblTotalPenyusutan.setText(FormatUtil.formatRupiah(asetController.hitungTotalPenyusutan(tahunSekarang)));
        lblNilaiSekarang.setText(FormatUtil.formatRupiah(asetController.hitungTotalNilaiSekarang(tahunSekarang)));

        modelPerawatan.setRowCount(0);
        for (Aset a : asetController.getSemua()) {
            if (a instanceof Maintainable m && m.perluPerawatan(tahunSekarang)) {
                modelPerawatan.addRow(new Object[]{
                        a.getKodeAset(),
                        a.getNamaAset(),
                        a.getKategori(),
                        a.getKondisi(),
                        a.getKodeRuangan(),
                        m.getJadwalPerawatan()
                });
            }
        }
    }
}
