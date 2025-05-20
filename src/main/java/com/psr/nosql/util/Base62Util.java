package com.psr.nosql.util;

public class Base62Util {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    public static String encode(long number) {
        if (number == 0) return "0";

        StringBuilder encoded = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % BASE);
            encoded.append(ALPHABET.charAt(remainder));
            number /= BASE;
        }

        return encoded.reverse().toString();
    }

    public static long decode(String encoded) {
        long result = 0;
        for (int i = 0; i < encoded.length(); i++) {
            int index = ALPHABET.indexOf(encoded.charAt(i));
            if (index == -1) {
                throw new IllegalArgumentException("Invalid Base62 character: " + encoded.charAt(i));
            }
            result = result * BASE + index;
        }
        return result;
    }
}
