package com.gecm.lostfound.util;

import com.gecm.lostfound.model.ItemType;

public final class AppUtils {

    private AppUtils() {
    }

    public static String locationLabel(ItemType type) {
        return type == ItemType.lost ? "Lost at" : "Found at";
    }

    public static boolean isGmailAddress(String email) {
        return email != null && email.toLowerCase().endsWith("@gmail.com");
    }

    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}
