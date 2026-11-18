package com.kelompok8.inventaris.aset;

/**
 * Interface untuk aset laboratorium yang memerlukan jadwal perawatan berkala.
 */
public interface Maintainable {
    boolean perluPerawatan(int tahunSekarang);

    String getJadwalPerawatan();
}
