package com.kelompok8.inventaris.view;

import com.kelompok8.inventaris.aset.Aset;
import com.kelompok8.inventaris.controller.AsetController;
import com.kelompok8.inventaris.controller.PeminjamanController;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.peminjaman.Peminjaman;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Panel CRUD transaksi peminjaman aset laboratorium.
 */
public class PeminjamanPanel extends JPanel {
    private final PeminjamanController peminjamanController;
    private final AsetController asetController;

    private JTextField txtId;
    private JComboBox<String> cmbAset;
    private JTextField txtPeminjam;
    private JSpinner spnTglPinjam;
    private JSpinner spnTglKembali;
    private JRadioButton rbDipinjam;
    private JRadioButton rbDikembalikan;
    private JTextField txtKeperluan;

    private JTextField txtCari;
    private JTable tabel;
    private DefaultTableModel tableModel;
    private String idTerpilih;

    public PeminjamanPanel(PeminjamanController peminjamanController, AsetController asetController) {
        this.peminjamanController = peminjamanController;
        this.asetController = asetController;
        initComponents();
        refreshData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setBackground(UiHelper.WARNA_LATAR);
        setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel atasPanel = new JPanel(new BorderLayout(8, 8));
        atasPanel.setOpaque(false);
        JLabel lblJudul = new JLabel("Transaksi Peminjaman Aset");
        lblJudul.setFont(UiHelper.FONT_JUDUL);
        lblJudul.setForeground(UiHelper.WARNA_UTAMA);

        JPanel cariPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        cariPanel.setOpaque(false);
        txtCari = new JTextField(18);
        JButton btnCari = UiHelper.buatTombol("Cari", UiHelper.WARNA_AKSENT);
        JButton btnReset = UiHelper.buatTombol("Tampilkan Semua", UiHelper.WARNA_NETRAL);
        btnCari.addActionListener(e -> muatTabel(peminjamanController.cari(txtCari.getText())));
        btnReset.addActionListener(e -> {
            txtCari.setText("");
            muatTabel(peminjamanController.getSemua());
        });
        cariPanel.add(new JLabel("Kata kunci:"));
        cariPanel.add(txtCari);
        cariPanel.add(btnCari);
        cariPanel.add(btnReset);

        atasPanel.add(lblJudul, BorderLayout.WEST);
        atasPanel.add(cariPanel, BorderLayout.EAST);
        add(atasPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Form Peminjaman"),
                new EmptyBorder(8, 10, 8, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(15);
        cmbAset = new JComboBox<>();
        txtPeminjam = new JTextField(15);
        spnTglPinjam = buatDateSpinner(new Date());
        spnTglKembali = buatDateSpinner(Date.from(LocalDate.now().plusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        rbDipinjam = new JRadioButton("Dipinjam", true);
        rbDikembalikan = new JRadioButton("Dikembalikan");
        ButtonGroup bgStatus = new ButtonGroup();
        bgStatus.add(rbDipinjam);
        bgStatus.add(rbDikembalikan);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        statusPanel.setOpaque(false);
        statusPanel.add(rbDipinjam);
        statusPanel.add(rbDikembalikan);

        txtKeperluan = new JTextField(15);

        int r = 0;
        tambahField(formPanel, gbc, r++, "ID Peminjaman:", txtId);
        tambahField(formPanel, gbc, r++, "Kode Aset:", cmbAset);
        tambahField(formPanel, gbc, r++, "Nama Peminjam:", txtPeminjam);
        tambahField(formPanel, gbc, r++, "Tanggal Pinjam:", spnTglPinjam);
        tambahField(formPanel, gbc, r++, "Tanggal Kembali:", spnTglKembali);
        tambahField(formPanel, gbc, r++, "Status:", statusPanel);
        tambahField(formPanel, gbc, r++, "Keperluan:", txtKeperluan);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        tombolPanel.setOpaque(false);
        JButton btnTambah = UiHelper.buatTombol("Pinjam / Tambah", UiHelper.WARNA_SUKSES);
        JButton btnUbah = UiHelper.buatTombol("Ubah / Kembalikan", UiHelper.WARNA_AKSENT);
        JButton btnHapus = UiHelper.buatTombol("Hapus", UiHelper.WARNA_HAPUS);
        JButton btnBersih = UiHelper.buatTombol("Bersihkan", UiHelper.WARNA_NETRAL);

        btnTambah.addActionListener(e -> tambahPeminjaman());
        btnUbah.addActionListener(e -> ubahPeminjaman());
        btnHapus.addActionListener(e -> hapusPeminjaman());
        btnBersih.addActionListener(e -> bersihkanForm());

        tombolPanel.add(btnTambah);
        tombolPanel.add(btnUbah);
        tombolPanel.add(btnHapus);
        tombolPanel.add(btnBersih);

        gbc.gridx = 0;
        gbc.gridy = r;
        gbc.gridwidth = 2;
        formPanel.add(tombolPanel, gbc);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Kode Aset", "Nama Peminjam", "Tgl Pinjam", "Tgl Kembali", "Status", "Keperluan"}, 0
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
        split.setResizeWeight(0.38);
        add(split, BorderLayout.CENTER);
    }

    private JSpinner buatDateSpinner(Date initialDate) {
        JSpinner spinner = new JSpinner(new SpinnerDateModel(initialDate, null, null, java.util.Calendar.DAY_OF_MONTH));
        spinner.setEditor(new JSpinner.DateEditor(spinner, "yyyy-MM-dd"));
        return spinner;
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

    private LocalDate toLocalDate(JSpinner spinner) {
        Date date = (Date) spinner.getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Peminjaman bacaForm() throws ValidasiException {
        String kodeAset = cmbAset.getSelectedItem() == null ? "" : String.valueOf(cmbAset.getSelectedItem());
        String status = rbDikembalikan.isSelected() ? "Dikembalikan" : "Dipinjam";
        return new Peminjaman(
                txtId.getText(),
                kodeAset,
                txtPeminjam.getText(),
                toLocalDate(spnTglPinjam),
                toLocalDate(spnTglKembali),
                status,
                txtKeperluan.getText()
        );
    }

    private void tambahPeminjaman() {
        try {
            peminjamanController.tambah(bacaForm());
            muatTabel(peminjamanController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Transaksi peminjaman berhasil disimpan.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void ubahPeminjaman() {
        if (idTerpilih == null) {
            UiHelper.tampilkanError(this, "Pilih transaksi pada tabel yang ingin diubah.");
            return;
        }
        try {
            peminjamanController.ubah(idTerpilih, bacaForm());
            muatTabel(peminjamanController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Transaksi peminjaman berhasil diperbarui.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void hapusPeminjaman() {
        if (idTerpilih == null) {
            UiHelper.tampilkanError(this, "Pilih transaksi pada tabel yang ingin dihapus.");
            return;
        }
        if (!UiHelper.konfirmasiHapus(this, "transaksi '" + idTerpilih + "'")) {
            return;
        }
        try {
            peminjamanController.hapus(idTerpilih);
            muatTabel(peminjamanController.getSemua());
            bersihkanForm();
            UiHelper.tampilkanInfo(this, "Transaksi peminjaman berhasil dihapus.");
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void isiFormDariTabel() {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            return;
        }
        idTerpilih = String.valueOf(tableModel.getValueAt(row, 0));
        txtId.setText(idTerpilih);
        cmbAset.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 1)));
        txtPeminjam.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        spnTglPinjam.setValue(toDate(LocalDate.parse(String.valueOf(tableModel.getValueAt(row, 3)))));
        spnTglKembali.setValue(toDate(LocalDate.parse(String.valueOf(tableModel.getValueAt(row, 4)))));
        String status = String.valueOf(tableModel.getValueAt(row, 5));
        rbDikembalikan.setSelected("Dikembalikan".equalsIgnoreCase(status));
        rbDipinjam.setSelected(!"Dikembalikan".equalsIgnoreCase(status));
        txtKeperluan.setText(String.valueOf(tableModel.getValueAt(row, 6)));
    }

    private void bersihkanForm() {
        idTerpilih = null;
        txtId.setText("");
        txtPeminjam.setText("");
        spnTglPinjam.setValue(new Date());
        spnTglKembali.setValue(toDate(LocalDate.now().plusDays(3)));
        rbDipinjam.setSelected(true);
        txtKeperluan.setText("");
        tabel.clearSelection();
    }

    public void muatTabel(List<Peminjaman> daftar) {
        tableModel.setRowCount(0);
        for (Peminjaman p : daftar) {
            tableModel.addRow(new Object[]{
                    p.getIdPeminjaman(),
                    p.getKodeAset(),
                    p.getNamaPeminjam(),
                    p.getTanggalPinjam(),
                    p.getTanggalKembali(),
                    p.getStatus(),
                    p.getKeperluan()
            });
        }
    }

    public void refreshData() {
        cmbAset.removeAllItems();
        for (Aset a : asetController.getSemua()) {
            cmbAset.addItem(a.getKodeAset());
        }
        muatTabel(peminjamanController.getSemua());
    }
}
