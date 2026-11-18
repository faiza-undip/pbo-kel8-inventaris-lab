package com.kelompok8.inventaris.view;

import com.kelompok8.inventaris.aset.Aset;
import com.kelompok8.inventaris.aset.Furnitur;
import com.kelompok8.inventaris.aset.Jaringan;
import com.kelompok8.inventaris.aset.Komputer;
import com.kelompok8.inventaris.aset.Maintainable;
import com.kelompok8.inventaris.controller.AsetController;
import com.kelompok8.inventaris.controller.RuanganController;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.ruangan.Ruangan;
import com.kelompok8.inventaris.util.FormatUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Year;
import java.util.List;

/**
 * Panel CRUD Manajemen Aset Laboratorium (Komputer, Jaringan, Furnitur).
 */
public class AsetPanel extends JPanel {
    private final AsetController asetController;
    private final RuanganController ruanganController;

    private JComboBox<String> cmbKategori;
    private JTextField txtKode;
    private JTextField txtNama;
    private JTextField txtMerk;
    private JSpinner spnTahun;
    private JTextField txtHarga;
    private JComboBox<String> cmbRuangan;
    private JRadioButton rbBaik;
    private JRadioButton rbRusakRingan;
    private JRadioButton rbRusakBerat;
    private JCheckBox chkDapatDipinjam;

    private JLabel lblKhusus1;
    private JLabel lblKhusus2;
    private JLabel lblKhusus3;
    private JTextField txtKhusus1;
    private JTextField txtKhusus2;
    private JTextField txtKhusus3;

    private JTextField txtCari;
    private JComboBox<String> cmbFilterKategori;
    private JComboBox<String> cmbFilterKondisi;
    private JTable tabel;
    private DefaultTableModel tableModel;
    private String kodeTerpilih;

    public AsetPanel(AsetController asetController, RuanganController ruanganController) {
        this.asetController = asetController;
        this.ruanganController = ruanganController;
        initComponents();
        refreshData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UiHelper.WARNA_LATAR);
        setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel atasPanel = new JPanel(new BorderLayout(8, 8));
        atasPanel.setOpaque(false);
        JLabel lblJudul = new JLabel("Manajemen Aset Laboratorium");
        lblJudul.setFont(UiHelper.FONT_JUDUL);
        lblJudul.setForeground(UiHelper.WARNA_UTAMA);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        filterPanel.setOpaque(false);
        cmbFilterKategori = new JComboBox<>(new String[]{"Semua", "Komputer", "Jaringan", "Furnitur"});
        cmbFilterKondisi = new JComboBox<>(new String[]{"Semua", "Baik", "Rusak Ringan", "Rusak Berat"});
        txtCari = new JTextField(14);
        JButton btnCari = UiHelper.buatTombol("Filter / Cari", UiHelper.WARNA_AKSENT);
        JButton btnReset = UiHelper.buatTombol("Reset", UiHelper.WARNA_NETRAL);

        btnCari.addActionListener(e -> terapkanFilter());
        btnReset.addActionListener(e -> {
            cmbFilterKategori.setSelectedIndex(0);
            cmbFilterKondisi.setSelectedIndex(0);
            txtCari.setText("");
            muatTabel(asetController.getSemua());
        });

        filterPanel.add(new JLabel("Kategori:"));
        filterPanel.add(cmbFilterKategori);
        filterPanel.add(new JLabel("Kondisi:"));
        filterPanel.add(cmbFilterKondisi);
        filterPanel.add(txtCari);
        filterPanel.add(btnCari);
        filterPanel.add(btnReset);

        atasPanel.add(lblJudul, BorderLayout.WEST);
        atasPanel.add(filterPanel, BorderLayout.EAST);
        add(atasPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Data Aset"),
                new EmptyBorder(8, 10, 8, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbKategori = new JComboBox<>(new String[]{"Komputer", "Jaringan", "Furnitur"});
        cmbKategori.addActionListener(e -> perbaruiLabelKhusus());

        txtKode = new JTextField(15);
        txtNama = new JTextField(15);
        txtMerk = new JTextField(15);
        int tahunSekarang = Year.now().getValue();
        spnTahun = new JSpinner(new SpinnerNumberModel(tahunSekarang, 1990, tahunSekarang + 1, 1));
        spnTahun.setEditor(new JSpinner.NumberEditor(spnTahun, "#"));
        txtHarga = new JTextField(15);
        cmbRuangan = new JComboBox<>();

        rbBaik = new JRadioButton("Baik", true);
        rbRusakRingan = new JRadioButton("Rusak Ringan");
        rbRusakBerat = new JRadioButton("Rusak Berat");
        ButtonGroup bgKondisi = new ButtonGroup();
        bgKondisi.add(rbBaik);
        bgKondisi.add(rbRusakRingan);
        bgKondisi.add(rbRusakBerat);
        JPanel kondisiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        kondisiPanel.setOpaque(false);
        kondisiPanel.add(rbBaik);
        kondisiPanel.add(rbRusakRingan);
        kondisiPanel.add(rbRusakBerat);

        chkDapatDipinjam = new JCheckBox("Aset dapat dipinjam", true);
        chkDapatDipinjam.setOpaque(false);

        lblKhusus1 = new JLabel("Processor:");
        lblKhusus2 = new JLabel("RAM (GB):");
        lblKhusus3 = new JLabel("Storage (GB):");
        txtKhusus1 = new JTextField(15);
        txtKhusus2 = new JTextField(15);
        txtKhusus3 = new JTextField(15);

        int r = 0;
        tambahField(formPanel, gbc, r++, new JLabel("Kategori Aset:"), cmbKategori);
        tambahField(formPanel, gbc, r++, new JLabel("Kode Aset:"), txtKode);
        tambahField(formPanel, gbc, r++, new JLabel("Nama Aset:"), txtNama);
        tambahField(formPanel, gbc, r++, new JLabel("Merk:"), txtMerk);
        tambahField(formPanel, gbc, r++, new JLabel("Tahun Perolehan:"), spnTahun);
        tambahField(formPanel, gbc, r++, new JLabel("Harga (Rp):"), txtHarga);
        tambahField(formPanel, gbc, r++, new JLabel("Ruangan:"), cmbRuangan);
        tambahField(formPanel, gbc, r++, new JLabel("Kondisi:"), kondisiPanel);
        tambahField(formPanel, gbc, r++, new JLabel("Peminjaman:"), chkDapatDipinjam);
        tambahField(formPanel, gbc, r++, lblKhusus1, txtKhusus1);
        tambahField(formPanel, gbc, r++, lblKhusus2, txtKhusus2);
        tambahField(formPanel, gbc, r++, lblKhusus3, txtKhusus3);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 4));
        tombolPanel.setOpaque(false);
        JButton btnTambah = UiHelper.buatTombol("Tambah", UiHelper.WARNA_SUKSES);
        JButton btnUbah = UiHelper.buatTombol("Ubah", UiHelper.WARNA_AKSENT);
        JButton btnHapus = UiHelper.buatTombol("Hapus", UiHelper.WARNA_HAPUS);
        JButton btnDetail = UiHelper.buatTombol("Detail", UiHelper.WARNA_UTAMA);
        JButton btnBersih = UiHelper.buatTombol("Bersihkan", UiHelper.WARNA_NETRAL);

        btnTambah.addActionListener(e -> tambahAset());
        btnUbah.addActionListener(e -> ubahAset());
        btnHapus.addActionListener(e -> hapusAset());
        btnDetail.addActionListener(e -> lihatDetail());
        btnBersih.addActionListener(e -> bersihkanForm());

        tombolPanel.add(btnTambah);
        tombolPanel.add(btnUbah);
        tombolPanel.add(btnHapus);
        tombolPanel.add(btnDetail);
        tombolPanel.add(btnBersih);

        gbc.gridx = 0;
        gbc.gridy = r;
        gbc.gridwidth = 2;
        formPanel.add(tombolPanel, gbc);

        tableModel = new DefaultTableModel(
                new String[]{
                        "Kode", "Nama Aset", "Kategori", "Merk", "Tahun",
                        "Harga Beli", "Penyusutan", "Nilai Sekarang", "Kondisi", "Ruangan", "Spesifikasi"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel = new JTable(tableModel);
        tabel.setRowHeight(24);
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiFormDariTabel();
            }
        });

        JScrollPane scrollForm = new JScrollPane(formPanel);
        JScrollPane scrollTabel = new JScrollPane(tabel);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollForm, scrollTabel);
        split.setResizeWeight(0.38);
        add(split, BorderLayout.CENTER);
        perbaruiLabelKhusus();
    }

    private void tambahField(JPanel panel, GridBagConstraints gbc, int baris, JLabel label, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = baris;
        gbc.weightx = 0.32;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.68;
        panel.add(comp, gbc);
    }

    private void perbaruiLabelKhusus() {
        String kategori = String.valueOf(cmbKategori.getSelectedItem());
        if ("Komputer".equals(kategori)) {
            lblKhusus1.setText("Processor:");
            lblKhusus2.setText("RAM (GB):");
            lblKhusus3.setText("Storage (GB):");
            lblKhusus3.setVisible(true);
            txtKhusus3.setVisible(true);
        } else if ("Jaringan".equals(kategori)) {
            lblKhusus1.setText("Tipe Perangkat:");
            lblKhusus2.setText("Jumlah Port:");
            lblKhusus3.setText("Kecepatan (Mbps):");
            lblKhusus3.setVisible(true);
            txtKhusus3.setVisible(true);
        } else {
            lblKhusus1.setText("Bahan Furnitur:");
            lblKhusus2.setText("Dimensi:");
            lblKhusus3.setVisible(false);
            txtKhusus3.setVisible(false);
        }
    }

    private String getKondisiTerpilih() {
        if (rbRusakRingan.isSelected()) {
            return "Rusak Ringan";
        }
        if (rbRusakBerat.isSelected()) {
            return "Rusak Berat";
        }
        return "Baik";
    }

    private void setKondisiTerpilih(String kondisi) {
        if ("Rusak Ringan".equalsIgnoreCase(kondisi)) {
            rbRusakRingan.setSelected(true);
        } else if ("Rusak Berat".equalsIgnoreCase(kondisi)) {
            rbRusakBerat.setSelected(true);
        } else {
            rbBaik.setSelected(true);
        }
    }

    private Aset bacaForm() throws ValidasiException {
        String kategori = String.valueOf(cmbKategori.getSelectedItem());
        String kode = txtKode.getText();
        String nama = txtNama.getText();
        String merk = txtMerk.getText();
        int tahun = (Integer) spnTahun.getValue();
        double harga;
        try {
            harga = Double.parseDouble(txtHarga.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidasiException("Harga perolehan harus berupa angka yang valid.");
        }
        String kondisi = getKondisiTerpilih();
        String kodeRuangan = cmbRuangan.getSelectedItem() == null ? "" : String.valueOf(cmbRuangan.getSelectedItem());
        boolean bolehPinjam = chkDapatDipinjam.isSelected();

        return switch (kategori) {
            case "Komputer" -> new Komputer(
                    kode, nama, merk, tahun, harga, kondisi, kodeRuangan, bolehPinjam,
                    txtKhusus1.getText(),
                    parseBilanganBulat(txtKhusus2.getText(), "RAM (GB)"),
                    parseBilanganBulat(txtKhusus3.getText(), "Storage (GB)")
            );
            case "Jaringan" -> new Jaringan(
                    kode, nama, merk, tahun, harga, kondisi, kodeRuangan, bolehPinjam,
                    txtKhusus1.getText(),
                    parseBilanganBulat(txtKhusus2.getText(), "Jumlah Port"),
                    parseBilanganBulat(txtKhusus3.getText(), "Kecepatan (Mbps)")
            );
            default -> new Furnitur(
                    kode, nama, merk, tahun, harga, kondisi, kodeRuangan, bolehPinjam,
                    txtKhusus1.getText(), txtKhusus2.getText()
            );
        };
    }

    private int parseBilanganBulat(String teks, String namaField) throws ValidasiException {
        try {
            return Integer.parseInt(teks.trim());
        } catch (NumberFormatException e) {
            throw new ValidasiException(namaField + " harus berupa bilangan bulat.");
        }
    }

    private void tambahAset() {
        try {
            asetController.tambah(bacaForm());
            muatTabel(asetController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Data aset berhasil ditambahkan.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void ubahAset() {
        if (kodeTerpilih == null) {
            UiHelper.tampilkanError(this, "Pilih aset pada tabel yang ingin diubah.");
            return;
        }
        try {
            asetController.ubah(kodeTerpilih, bacaForm());
            muatTabel(asetController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Data aset berhasil diperbarui.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void hapusAset() {
        if (kodeTerpilih == null) {
            UiHelper.tampilkanError(this, "Pilih aset pada tabel yang ingin dihapus.");
            return;
        }
        if (!UiHelper.konfirmasiHapus(this, "aset '" + kodeTerpilih + "'")) {
            return;
        }
        try {
            asetController.hapus(kodeTerpilih);
            muatTabel(asetController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Data aset berhasil dihapus.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void lihatDetail() {
        if (kodeTerpilih == null) {
            UiHelper.tampilkanError(this, "Pilih aset pada tabel untuk melihat detail.");
            return;
        }
        Aset a = asetController.cariByKode(kodeTerpilih);
        if (a == null) {
            return;
        }
        int tahunSekarang = Year.now().getValue();
        StringBuilder sb = new StringBuilder();
        sb.append("Kode Aset       : ").append(a.getKodeAset()).append("\n")
          .append("Nama Aset       : ").append(a.getNamaAset()).append("\n")
          .append("Kategori        : ").append(a.getKategori()).append("\n")
          .append("Merk            : ").append(a.getMerk()).append("\n")
          .append("Tahun Perolehan : ").append(a.getTahunPerolehan()).append("\n")
          .append("Harga Perolehan : ").append(FormatUtil.formatRupiah(a.getHargaPerolehan())).append("\n")
          .append("Penyusutan (").append(tahunSekarang).append("): ").append(FormatUtil.formatRupiah(a.hitungPenyusutan())).append("\n")
          .append("Nilai Sekarang  : ").append(FormatUtil.formatRupiah(a.hitungNilaiSekarang())).append("\n")
          .append("Kondisi         : ").append(a.getKondisi()).append("\n")
          .append("Ruangan         : ").append(a.getKodeRuangan()).append("\n")
          .append("Dapat Dipinjam  : ").append(a.isDapatDipinjam() ? "Ya" : "Tidak").append("\n")
          .append("Spesifikasi     : ").append(a.getSpesifikasiRingkas()).append("\n");
        if (a instanceof Maintainable m) {
            sb.append("Perlu Perawatan : ").append(m.perluPerawatan(tahunSekarang) ? "Ya" : "Belum").append("\n")
              .append("Jadwal Perawatan: ").append(m.getJadwalPerawatan()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Detail Aset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void isiFormDariTabel() {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            return;
        }
        String kode = String.valueOf(tableModel.getValueAt(row, 0));
        Aset a = asetController.cariByKode(kode);
        if (a == null) {
            return;
        }
        kodeTerpilih = a.getKodeAset();
        cmbKategori.setSelectedItem(a.getKategori());
        txtKode.setText(a.getKodeAset());
        txtNama.setText(a.getNamaAset());
        txtMerk.setText(a.getMerk());
        spnTahun.setValue(a.getTahunPerolehan());
        txtHarga.setText(String.format("%.0f", a.getHargaPerolehan()));
        cmbRuangan.setSelectedItem(a.getKodeRuangan());
        setKondisiTerpilih(a.getKondisi());
        chkDapatDipinjam.setSelected(a.isDapatDipinjam());

        if (a instanceof Komputer k) {
            txtKhusus1.setText(k.getProcessor());
            txtKhusus2.setText(String.valueOf(k.getRamGb()));
            txtKhusus3.setText(String.valueOf(k.getStorageGb()));
        } else if (a instanceof Jaringan j) {
            txtKhusus1.setText(j.getTipePerangkat());
            txtKhusus2.setText(String.valueOf(j.getJumlahPort()));
            txtKhusus3.setText(String.valueOf(j.getKecepatanMbps()));
        } else if (a instanceof Furnitur f) {
            txtKhusus1.setText(f.getBahan());
            txtKhusus2.setText(f.getDimensi());
            txtKhusus3.setText("");
        }
    }

    private void bersihkanForm() {
        kodeTerpilih = null;
        txtKode.setText("");
        txtNama.setText("");
        txtMerk.setText("");
        spnTahun.setValue(Year.now().getValue());
        txtHarga.setText("");
        rbBaik.setSelected(true);
        chkDapatDipinjam.setSelected(true);
        txtKhusus1.setText("");
        txtKhusus2.setText("");
        txtKhusus3.setText("");
        tabel.clearSelection();
    }

    private void terapkanFilter() {
        String kategori = String.valueOf(cmbFilterKategori.getSelectedItem());
        String kondisi = String.valueOf(cmbFilterKondisi.getSelectedItem());
        String keyword = txtCari.getText();
        muatTabel(asetController.cariAset(kategori, kondisi, keyword));
    }

    public void muatTabel(List<Aset> daftar) {
        tableModel.setRowCount(0);
        for (Aset a : daftar) {
            tableModel.addRow(new Object[]{
                    a.getKodeAset(),
                    a.getNamaAset(),
                    a.getKategori(),
                    a.getMerk(),
                    a.getTahunPerolehan(),
                    FormatUtil.formatRupiah(a.getHargaPerolehan()),
                    FormatUtil.formatRupiah(a.hitungPenyusutan()),
                    FormatUtil.formatRupiah(a.hitungNilaiSekarang()),
                    a.getKondisi(),
                    a.getKodeRuangan(),
                    a.getSpesifikasiRingkas()
            });
        }
    }

    public void refreshData() {
        cmbRuangan.removeAllItems();
        for (Ruangan r : ruanganController.getSemua()) {
            cmbRuangan.addItem(r.getKodeRuangan());
        }
        terapkanFilter();
    }
}
