package com.cardinity.model;

import java.math.BigDecimal;

public final class MoneyUtil {

    private MoneyUtil() {
    }

    public static BigDecimal formatAmount(BigDecimal amount) {

        if (amount == null)
            return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

        return amount.setScale(2, BigDecimal.ROUND_DOWN);
    }
}
