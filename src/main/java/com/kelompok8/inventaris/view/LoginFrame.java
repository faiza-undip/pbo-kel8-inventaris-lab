package com.kelompok8.inventaris.view;

import com.kelompok8.inventaris.controller.AuthController;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.pengguna.Pengguna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Tampilan halaman login aplikasi Inventaris Laboratorium.
 */
public class LoginFrame extends JFrame {
    private final AuthController authController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        this.authController = new AuthController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Inventaris Laboratorium (Kelompok 8)");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(440, 340));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(UiHelper.WARNA_LATAR);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = new JPanel(new GridLayout(2, 1, 2, 4));
        header.setOpaque(false);
        JLabel lblJudul = new JLabel("Sistem Inventaris Laboratorium", SwingConstants.CENTER);
        lblJudul.setFont(UiHelper.FONT_JUDUL);
        lblJudul.setForeground(UiHelper.WARNA_UTAMA);
        JLabel lblSub = new JLabel("Kelompok 8 — Silakan masuk untuk melanjutkan", SwingConstants.CENTER);
        lblSub.setFont(UiHelper.FONT_BIASA);
        header.add(lblJudul);
        header.add(lblSub);
        root.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        formPanel.add(new JLabel("Username"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        txtUsername = new JTextField(18);
        txtUsername.setText("admin");
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.25;
        formPanel.add(new JLabel("Password"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        txtPassword = new JPasswordField(18);
        txtPassword.setText("admin123");
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel lblHint = new JLabel("<html><i>Akun demo: admin / admin123 atau laboran / lab123</i></html>");
        lblHint.setForeground(UiHelper.WARNA_NETRAL);
        lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formPanel.add(lblHint, gbc);

        root.add(formPanel, BorderLayout.CENTER);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tombolPanel.setOpaque(false);
        JButton btnKeluar = UiHelper.buatTombol("Keluar", UiHelper.WARNA_NETRAL);
        JButton btnMasuk = UiHelper.buatTombol("Masuk", UiHelper.WARNA_UTAMA);

        btnKeluar.addActionListener(e -> System.exit(0));
        btnMasuk.addActionListener(e -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());

        tombolPanel.add(btnKeluar);
        tombolPanel.add(btnMasuk);
        root.add(tombolPanel, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
    }

    private void prosesLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        try {
            Pengguna pengguna = authController.login(username, password);
            MainFrame mainFrame = new MainFrame(pengguna);
            mainFrame.setVisible(true);
            dispose();
        } catch (ValidasiException | PenyimpananException ex) {
            UiHelper.tampilkanError(this, ex.getMessage());
        }
    }
}
