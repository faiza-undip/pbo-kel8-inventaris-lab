package com.kelompok8.inventaris.view;

import com.kelompok8.inventaris.controller.RuanganController;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.ruangan.Ruangan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel CRUD manajemen ruangan laboratorium.
 */
public class RuanganPanel extends JPanel {
    private final RuanganController ruanganController;

    private JTextField txtKode;
    private JTextField txtNama;
    private JTextField txtGedung;
    private JSpinner spnKapasitas;
    private JTextField txtPenanggungJawab;
    private JTextField txtCari;
    private JTable tabel;
    private DefaultTableModel tableModel;
    private String kodeTerpilih;

    public RuanganPanel(RuanganController ruanganController) {
        this.ruanganController = ruanganController;
        initComponents();
        muatTabel(ruanganController.getSemua());
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UiHelper.WARNA_LATAR);
        setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel atasPanel = new JPanel(new BorderLayout(8, 8));
        atasPanel.setOpaque(false);
        JLabel lblJudul = new JLabel("Manajemen Ruangan Laboratorium");
        lblJudul.setFont(UiHelper.FONT_JUDUL);
        lblJudul.setForeground(UiHelper.WARNA_UTAMA);

        JPanel cariPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        cariPanel.setOpaque(false);
        txtCari = new JTextField(18);
        JButton btnCari = UiHelper.buatTombol("Cari", UiHelper.WARNA_AKSENT);
        JButton btnResetCari = UiHelper.buatTombol("Tampilkan Semua", UiHelper.WARNA_NETRAL);
        btnCari.addActionListener(e -> muatTabel(ruanganController.cari(txtCari.getText())));
        btnResetCari.addActionListener(e -> {
            txtCari.setText("");
            muatTabel(ruanganController.getSemua());
        });
        cariPanel.add(new JLabel("Kata kunci:"));
        cariPanel.add(txtCari);
        cariPanel.add(btnCari);
        cariPanel.add(btnResetCari);

        atasPanel.add(lblJudul, BorderLayout.WEST);
        atasPanel.add(cariPanel, BorderLayout.EAST);
        add(atasPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Data Ruangan"),
                new EmptyBorder(8, 10, 8, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtKode = new JTextField(14);
        txtNama = new JTextField(18);
        txtGedung = new JTextField(16);
        spnKapasitas = new JSpinner(new SpinnerNumberModel(30, 1, 500, 1));
        txtPenanggungJawab = new JTextField(18);

        tambahField(formPanel, gbc, 0, "Kode Ruangan:", txtKode);
        tambahField(formPanel, gbc, 1, "Nama Ruangan:", txtNama);
        tambahField(formPanel, gbc, 2, "Gedung / Lokasi:", txtGedung);
        tambahField(formPanel, gbc, 3, "Kapasitas (Orang):", spnKapasitas);
        tambahField(formPanel, gbc, 4, "Penanggung Jawab:", txtPenanggungJawab);

        JPanel tombolForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        tombolForm.setOpaque(false);
        JButton btnTambah = UiHelper.buatTombol("Tambah", UiHelper.WARNA_SUKSES);
        JButton btnUbah = UiHelper.buatTombol("Ubah", UiHelper.WARNA_AKSENT);
        JButton btnHapus = UiHelper.buatTombol("Hapus", UiHelper.WARNA_HAPUS);
        JButton btnBersih = UiHelper.buatTombol("Bersihkan", UiHelper.WARNA_NETRAL);

        btnTambah.addActionListener(e -> tambahRuangan());
        btnUbah.addActionListener(e -> ubahRuangan());
        btnHapus.addActionListener(e -> hapusRuangan());
        btnBersih.addActionListener(e -> bersihkanForm());

        tombolForm.add(btnTambah);
        tombolForm.add(btnUbah);
        tombolForm.add(btnHapus);
        tombolForm.add(btnBersih);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(tombolForm, gbc);

        tableModel = new DefaultTableModel(
                new String[]{"Kode Ruangan", "Nama Ruangan", "Gedung", "Kapasitas", "Penanggung Jawab"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel = new JTable(tableModel);
        tabel.setRowHeight(24);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiFormDariTabel();
            }
        });

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(tabel));
        split.setResizeWeight(0.36);
        add(split, BorderLayout.CENTER);
    }

    private void tambahField(JPanel panel, GridBagConstraints gbc, int baris, String label, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = baris;
        gbc.weightx = 0.35;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(comp, gbc);
    }

    private Ruangan bacaForm() throws ValidasiException {
        return new Ruangan(
                txtKode.getText(),
                txtNama.getText(),
                txtGedung.getText(),
                (Integer) spnKapasitas.getValue(),
                txtPenanggungJawab.getText()
        );
    }

    private void tambahRuangan() {
        try {
            ruanganController.tambah(bacaForm());
            muatTabel(ruanganController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Data ruangan berhasil ditambahkan.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void ubahRuangan() {
        if (kodeTerpilih == null) {
            UiHelper.tampilkanError(this, "Pilih ruangan pada tabel yang ingin diubah.");
            return;
        }
        try {
            ruanganController.ubah(kodeTerpilih, bacaForm());
            muatTabel(ruanganController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Data ruangan berhasil diperbarui.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void hapusRuangan() {
        if (kodeTerpilih == null) {
            UiHelper.tampilkanError(this, "Pilih ruangan pada tabel yang ingin dihapus.");
            return;
        }
        if (!UiHelper.konfirmasiHapus(this, "ruangan '" + kodeTerpilih + "'")) {
            return;
        }
        try {
            ruanganController.hapus(kodeTerpilih);
            muatTabel(ruanganController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Data ruangan berhasil dihapus.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void isiFormDariTabel() {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            return;
        }
        kodeTerpilih = String.valueOf(tableModel.getValueAt(row, 0));
        txtKode.setText(kodeTerpilih);
        txtNama.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtGedung.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        spnKapasitas.setValue(Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 3))));
        txtPenanggungJawab.setText(String.valueOf(tableModel.getValueAt(row, 4)));
    }

    private void bersihkanForm() {
        kodeTerpilih = null;
        txtKode.setText("");
        txtNama.setText("");
        txtGedung.setText("");
        spnKapasitas.setValue(30);
        txtPenanggungJawab.setText("");
        tabel.clearSelection();
    }

    public void muatTabel(List<Ruangan> daftar) {
        tableModel.setRowCount(0);
        for (Ruangan r : daftar) {
            tableModel.addRow(new Object[]{
                    r.getKodeRuangan(),
                    r.getNamaRuangan(),
                    r.getGedung(),
                    r.getKapasitas(),
                    r.getPenanggungJawab()
            });
        }
    }

    public void refreshData() {
        muatTabel(ruanganController.getSemua());
    }
}
