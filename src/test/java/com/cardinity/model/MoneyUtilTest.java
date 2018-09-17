package com.cardinity.model;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MoneyUtilTest {

    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor constructor = MoneyUtil.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Object o = constructor.newInstance();
        assertNotNull(o);
    }

    @Test
    public void testFormatAmount() throws Exception {

        BigDecimal zero = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);
        assertTrue(zero.equals(MoneyUtil.formatAmount(null)));

        BigDecimal expected = new BigDecimal("1.99").setScale(2, BigDecimal.ROUND_DOWN);
        assertTrue(expected.equals(MoneyUtil.formatAmount(new BigDecimal(1.999))));
    }
}
