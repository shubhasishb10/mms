package com.mms.thp.utility;

public class StringUtility {

    private StringUtility(){/* No constructor for utility class */}
    public static String normalizeString(String arg) {
        return arg.trim().replaceAll("\\s", "").toLowerCase();
    }
}
