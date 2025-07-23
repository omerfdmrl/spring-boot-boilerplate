package dev.nyom.backend.utils;

import java.security.SecureRandom;

public class RandomUtils {
    private static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final SecureRandom random = new SecureRandom();

    public static String getSaltString() {
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < 18; i++) {
            int index = random.nextInt(SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
}