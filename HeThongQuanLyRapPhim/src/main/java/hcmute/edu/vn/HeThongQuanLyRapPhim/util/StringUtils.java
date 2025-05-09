package hcmute.edu.vn.HeThongQuanLyRapPhim.util;

import java.text.Normalizer;

public class StringUtils {

    public static String normalizeString(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        // Normalize to decompose diacritics (e.g., "Nguyễn" -> "Nguyen")
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Remove diacritic marks
        normalized = normalized.replaceAll("\\p{M}", "");
        // Convert to lowercase and replace spaces with empty string
        return normalized.toLowerCase().replaceAll("\\s+", "");
    }
}
