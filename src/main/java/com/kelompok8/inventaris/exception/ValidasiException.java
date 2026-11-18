package com.kelompok8.inventaris.exception;

/**
 * Custom exception untuk menangani kesalahan validasi input atau aturan bisnis.
 */
public class ValidasiException extends Exception {
    public ValidasiException(String message) {
        super(message);
    }

    public ValidasiException(String message, Throwable cause) {
        super(message, cause);
    }
}
