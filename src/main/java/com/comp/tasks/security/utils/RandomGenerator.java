package com.comp.tasks.security.utils;

import java.security.SecureRandom;

public class RandomGenerator {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
    private static final String ALPHABET_NUMBERS = "0123456789";
    private static final String ALPHABET_PASS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates random string of given length from Base65 alphabet (numbers, lowercase letters, uppercase letters).
     *
     * @param count length
     * @return random string of given length
     */
    public static String generate(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public static String generateOnlyNumbers(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET_NUMBERS.charAt(RANDOM.nextInt(ALPHABET_NUMBERS.length())));
        }
        return sb.toString();
    }

    public static String generateAsUpperCase(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            sb.append(ALPHABET_PASS.charAt(RANDOM.nextInt(ALPHABET_PASS.length())));
        }
        return sb.toString();
    }
}
