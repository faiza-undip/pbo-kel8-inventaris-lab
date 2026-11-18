package com.kelompok8.inventaris.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Helper bersama untuk konsistensi tampilan, dialog, dan komponen Swing.
 */
public final class UiHelper {
    public static final Color WARNA_UTAMA = new Color(30, 64, 175);
    public static final Color WARNA_AKSENT = new Color(37, 99, 235);
    public static final Color WARNA_HAPUS = new Color(220, 38, 38);
    public static final Color WARNA_SUKSES = new Color(22, 163, 74);
    public static final Color WARNA_NETRAL = new Color(100, 116, 139);
    public static final Color WARNA_LATAR = new Color(248, 250, 252);

    public static final Font FONT_JUDUL = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBJUDUL = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BIASA = new Font("Segoe UI", Font.PLAIN, 13);

    private UiHelper() {
    }

    public static JButton buatTombol(String teks, Color warnaLatar) {
        JButton btn = new JButton(teks);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(warnaLatar);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void tampilkanError(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    public static void tampilkanInfo(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean konfirmasiHapus(Component parent, String namaItem) {
        int pilihan = JOptionPane.showConfirmDialog(
                parent,
                "Apakah Anda yakin ingin menghapus " + namaItem + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return pilihan == JOptionPane.YES_OPTION;
    }
}
