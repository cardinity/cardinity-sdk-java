package com.cardinity.validators;

import java.math.BigDecimal;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean validateExactLength(String text, int length) {
        return text != null && text.length() == length;
    }

    public static boolean validateIntervalLength(String text, int min, int max) {
        return text != null && text.length() >= min && text.length() <= max;
    }

    public static boolean validateAmount(BigDecimal amount, BigDecimal minimum) {
        return amount != null && amount.compareTo(minimum) >= 0;
    }

    public static boolean validateInteger(Integer number, int min, int max) {
        return number != null && number >= min && number <= max;
    }

    public static boolean validateCardNumber(String number) {
        return !isBlank(number) && isValidLuhnNumber(number) && isWholePositiveNumber(number);
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    private static boolean isValidLuhnNumber(String number) {
        boolean isOdd = true;
        int sum = 0;

        for (int index = number.length() - 1; index >= 0; index--) {
            char c = number.charAt(index);
            if (!Character.isDigit(c)) {
                return false;
            }
            int digitInteger = Integer.parseInt("" + c);
            isOdd = !isOdd;

            if (isOdd) {
                digitInteger *= 2;
            }

            if (digitInteger > 9) {
                digitInteger -= 9;
            }

            sum += digitInteger;
        }

        return sum % 10 == 0;
    }

    private static boolean isWholePositiveNumber(String value) {
        if (value == null) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
