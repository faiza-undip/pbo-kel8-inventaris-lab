package com.kelompok8.inventaris.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilitas pemformatan angka mata uang dan sanitasi teks penyimpanan file.
 */
public final class FormatUtil {
    private static final Locale LOCALE_ID = Locale.forLanguageTag("id-ID");

    private FormatUtil() {
    }

    public static String formatRupiah(double nilai) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(LOCALE_ID);
        nf.setMaximumFractionDigits(0);
        return nf.format(nilai);
    }

    public static String bersihkanDelimiter(String teks) {
        if (teks == null) {
            return "";
        }
        return teks.replace("|", "/").replace("\r", " ").replace("\n", " ").trim();
    }
}
