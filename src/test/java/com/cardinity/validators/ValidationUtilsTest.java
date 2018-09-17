package com.cardinity.validators;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ValidationUtilsTest {

    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor constructor = ValidationUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Object o = constructor.newInstance();
        assertNotNull(o);
    }

    @Test
    public void testValidateExactLength() throws Exception {
        assertTrue(ValidationUtils.validateExactLength("test", 4));
        assertFalse(ValidationUtils.validateExactLength("test", 3));
        assertFalse(ValidationUtils.validateExactLength(null, 4));
    }

    @Test
    public void testValidateIntervalLength() throws Exception {
        assertFalse(ValidationUtils.validateIntervalLength(null, 1, 10));
        assertFalse(ValidationUtils.validateIntervalLength("test", 1, 3));
        assertFalse(ValidationUtils.validateIntervalLength("test", 5, 10));
        assertTrue(ValidationUtils.validateIntervalLength("test", 1, 4));
    }

    @Test
    public void testValidateAmount() throws Exception {
        assertFalse(ValidationUtils.validateAmount(null, BigDecimal.ONE));
        assertFalse(ValidationUtils.validateAmount(BigDecimal.ZERO, BigDecimal.ONE));
        assertTrue(ValidationUtils.validateAmount(new BigDecimal(10), BigDecimal.ONE));
    }

    @Test
    public void testValidateInteger() throws Exception {
        assertFalse(ValidationUtils.validateInteger(null, 1, 10));
        assertFalse(ValidationUtils.validateInteger(1, 2, 10));
        assertFalse(ValidationUtils.validateInteger(11, 1, 10));
        assertTrue(ValidationUtils.validateInteger(5, 1, 10));
        assertTrue(ValidationUtils.validateInteger(5, 5, 10));
        assertTrue(ValidationUtils.validateInteger(10, 5, 10));
    }

    @Test
    public void testValidateCardNumber() throws Exception {
        assertFalse(ValidationUtils.validateCardNumber("1234123412341234"));
        assertFalse(ValidationUtils.validateCardNumber("-4111111111111111"));
        assertTrue(ValidationUtils.validateCardNumber("4111111111111111"));
    }
}
