package com.kelompok8.inventaris.view;

import com.kelompok8.inventaris.controller.AsetController;
import com.kelompok8.inventaris.controller.PeminjamanController;
import com.kelompok8.inventaris.controller.RuanganController;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.pengguna.Pengguna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Jendela utama aplikasi Inventaris Laboratorium dengan menu bar dan navigasi CardLayout.
 */
public class MainFrame extends JFrame {
    private static final String CARD_DASHBOARD = "DASHBOARD";
    private static final String CARD_ASET = "ASET";
    private static final String CARD_RUANGAN = "RUANGAN";
    private static final String CARD_PEMINJAMAN = "PEMINJAMAN";

    private final Pengguna pengguna;
    private final RuanganController ruanganController;
    private final AsetController asetController;
    private final PeminjamanController peminjamanController;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private DashboardPanel dashboardPanel;
    private AsetPanel asetPanel;
    private RuanganPanel ruanganPanel;
    private PeminjamanPanel peminjamanPanel;

    public MainFrame(Pengguna pengguna) throws PenyimpananException {
        this.pengguna = pengguna;
        this.ruanganController = new RuanganController();
        this.asetController = new AsetController();
        this.peminjamanController = new PeminjamanController(asetController);
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistem Inventaris Laboratorium — Kelompok 8 PBO B");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 680));
        setLocationRelativeTo(null);

        setJMenuBar(buatMenuBar());

        JPanel root = new JPanel(new BorderLayout());
        root.add(buatSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        dashboardPanel = new DashboardPanel(pengguna, asetController, ruanganController, peminjamanController);
        asetPanel = new AsetPanel(asetController, ruanganController);
        ruanganPanel = new RuanganPanel(ruanganController);
        peminjamanPanel = new PeminjamanPanel(peminjamanController, asetController);

        contentPanel.add(dashboardPanel, CARD_DASHBOARD);
        contentPanel.add(asetPanel, CARD_ASET);
        contentPanel.add(ruanganPanel, CARD_RUANGAN);
        contentPanel.add(peminjamanPanel, CARD_PEMINJAMAN);

        root.add(contentPanel, BorderLayout.CENTER);
        setContentPane(root);
        tampilkanPanel(CARD_DASHBOARD);
    }

    private JMenuBar buatMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem miMuatUlang = new JMenuItem("Muat Ulang Data");
        JMenuItem miLogout = new JMenuItem("Logout");
        JMenuItem miKeluar = new JMenuItem("Keluar");
        miMuatUlang.addActionListener(e -> muatUlangSemua());
        miLogout.addActionListener(e -> logout());
        miKeluar.addActionListener(e -> System.exit(0));
        menuFile.add(miMuatUlang);
        menuFile.addSeparator();
        menuFile.add(miLogout);
        menuFile.add(miKeluar);

        JMenu menuNav = new JMenu("Navigasi");
        JMenuItem miDash = new JMenuItem("Dashboard");
        JMenuItem miAset = new JMenuItem("Manajemen Aset");
        JMenuItem miRuangan = new JMenuItem("Manajemen Ruangan");
        JMenuItem miPinjam = new JMenuItem("Peminjaman Aset");
        miDash.addActionListener(e -> tampilkanPanel(CARD_DASHBOARD));
        miAset.addActionListener(e -> tampilkanPanel(CARD_ASET));
        miRuangan.addActionListener(e -> tampilkanPanel(CARD_RUANGAN));
        miPinjam.addActionListener(e -> tampilkanPanel(CARD_PEMINJAMAN));
        menuNav.add(miDash);
        menuNav.add(miAset);
        menuNav.add(miRuangan);
        menuNav.add(miPinjam);

        JMenu menuBantuan = new JMenu("Bantuan");
        JMenuItem miTentang = new JMenuItem("Tentang Aplikasi");
        miTentang.addActionListener(e -> UiHelper.tampilkanInfo(
                this,
                "Aplikasi Inventaris Laboratorium (Java Swing MVC)\n"
                        + "Kelompok 8 PBO B:\n"
                        + "- Faiza Tanjia\n"
                        + "- Muhammad Baihaqi\n"
                        + "- Chicochaesa Elang Z"
        ));
        menuBantuan.add(miTentang);

        menuBar.add(menuFile);
        menuBar.add(menuNav);
        menuBar.add(menuBantuan);
        return menuBar;
    }

    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setBorder(new EmptyBorder(16, 12, 16, 12));

        JLabel lblBrand = new JLabel("<html><b>INVENTARIS LAB</b><br><small>Kelompok 8</small></html>");
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblBrand.setBorder(new EmptyBorder(0, 6, 18, 6));
        sidebar.add(lblBrand, BorderLayout.NORTH);

        JPanel navList = new JPanel(new GridLayout(6, 1, 0, 8));
        navList.setOpaque(false);
        JButton btnDash = buatTombolNav("Dashboard");
        JButton btnAset = buatTombolNav("Data Aset");
        JButton btnRuangan = buatTombolNav("Data Ruangan");
        JButton btnPinjam = buatTombolNav("Peminjaman");

        btnDash.addActionListener(e -> tampilkanPanel(CARD_DASHBOARD));
        btnAset.addActionListener(e -> tampilkanPanel(CARD_ASET));
        btnRuangan.addActionListener(e -> tampilkanPanel(CARD_RUANGAN));
        btnPinjam.addActionListener(e -> tampilkanPanel(CARD_PEMINJAMAN));

        navList.add(btnDash);
        navList.add(btnAset);
        navList.add(btnRuangan);
        navList.add(btnPinjam);
        sidebar.add(navList, BorderLayout.CENTER);

        JButton btnLogout = UiHelper.buatTombol("Logout (" + pengguna.getUsername() + ")", UiHelper.WARNA_HAPUS);
        btnLogout.addActionListener(e -> logout());
        sidebar.add(btnLogout, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton buatTombolNav(String teks) {
        JButton btn = new JButton(teks);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void tampilkanPanel(String card) {
        switch (card) {
            case CARD_DASHBOARD -> dashboardPanel.refreshData();
            case CARD_ASET -> asetPanel.refreshData();
            case CARD_RUANGAN -> ruanganPanel.refreshData();
            case CARD_PEMINJAMAN -> peminjamanPanel.refreshData();
            default -> {
            }
        }
        cardLayout.show(contentPanel, card);
    }

    private void muatUlangSemua() {
        try {
            ruanganController.muatUlang();
            asetController.muatUlang();
            peminjamanController.muatUlang();
            dashboardPanel.refreshData();
            asetPanel.refreshData();
            ruanganPanel.refreshData();
            peminjamanPanel.refreshData();
            UiHelper.tampilkanInfo(this, "Seluruh data berhasil dimuat ulang dari file.");
        } catch (PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
