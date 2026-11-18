package com.kelompok8.inventaris.controller;

import com.kelompok8.inventaris.dao.PenggunaDAO;
import com.kelompok8.inventaris.exception.PenyimpananException;
import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.pengguna.Pengguna;
import com.kelompok8.inventaris.util.Validator;

import java.util.Map;

/**
 * Controller autentikasi pengguna (pola MVC).
 */
public class AuthController {
    private final PenggunaDAO penggunaDAO;

    public AuthController() {
        this.penggunaDAO = new PenggunaDAO();
    }

    public Pengguna login(String username, String password) throws ValidasiException, PenyimpananException {
        String user = Validator.wajibIsi(username, "Username");
        String pass = Validator.wajibIsi(password, "Password");

        Map<String, Pengguna> daftarPengguna = penggunaDAO.muatSebagaiMap();
        Pengguna ditemukan = daftarPengguna.get(user);
        if (ditemukan == null || !ditemukan.cocokPassword(pass)) {
            throw new ValidasiException("Username atau password salah.");
        }
        return ditemukan;
    }
}
