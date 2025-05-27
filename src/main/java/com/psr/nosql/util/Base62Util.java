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

    public static String encode(byte[] input) {
        StringBuilder result = new StringBuilder();
        java.math.BigInteger value = new java.math.BigInteger(1, input);

        while (value.compareTo(java.math.BigInteger.ZERO) > 0) {
            int remainder = value.mod(java.math.BigInteger.valueOf(62)).intValue();
            result.append(ALPHABET.charAt(remainder));
            value = value.divide(java.math.BigInteger.valueOf(62));
        }

        return result.reverse().toString();
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
