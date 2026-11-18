package com.kelompok8.inventaris.pengguna;

import com.kelompok8.inventaris.exception.ValidasiException;
import com.kelompok8.inventaris.util.Validator;

/**
 * Model pengguna aplikasi inventaris laboratorium.
 */
public class Pengguna {
    private String username;
    private String password;
    private String namaLengkap;
    private String role; // Admin, Laboran

    public Pengguna(String username, String password, String namaLengkap, String role) throws ValidasiException {
        setUsername(username);
        setPassword(password);
        setNamaLengkap(namaLengkap);
        setRole(role);
    }

    public boolean cocokPassword(String inputPassword) {
        return password != null && password.equals(inputPassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws ValidasiException {
        this.username = Validator.wajibIsi(username, "Username");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws ValidasiException {
        this.password = Validator.wajibIsi(password, "Password");
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) throws ValidasiException {
        this.namaLengkap = Validator.wajibIsi(namaLengkap, "Nama Lengkap");
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) throws ValidasiException {
        this.role = Validator.wajibIsi(role, "Role");
    }
}
