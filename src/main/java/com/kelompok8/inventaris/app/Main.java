package com.kelompok8.inventaris.app;

import com.kelompok8.inventaris.view.LoginFrame;

import javax.swing.*;

/**
 * Titik masuk utama aplikasi GUI Inventaris Laboratorium Kelompok 8.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {
                // Gunakan LookAndFeel default jika Nimbus tidak tersedia
            }
            new LoginFrame().setVisible(true);
        });
    }
}
