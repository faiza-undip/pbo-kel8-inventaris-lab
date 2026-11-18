package com.kelompok8.inventaris.exception;

/**
 * Custom exception untuk menangani kesalahan pembacaan atau penulisan media penyimpanan.
 */
public class PenyimpananException extends Exception {
    public PenyimpananException(String message) {
        super(message);
    }

    public PenyimpananException(String message, Throwable cause) {
        super(message, cause);
    }
}
