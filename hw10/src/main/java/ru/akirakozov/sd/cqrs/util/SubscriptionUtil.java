package ru.akirakozov.sd.cqrs.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SubscriptionUtil {
    private static final String PASSWORD_SALT = "GeeMuMGCNko91Pbu";

    public static String sha256(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPasswordHash(String password) {
        return sha256(PASSWORD_SALT + password);
    }
}
