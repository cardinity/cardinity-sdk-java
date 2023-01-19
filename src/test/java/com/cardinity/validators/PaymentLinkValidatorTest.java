package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.PaymentLink;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentLinkValidatorTest {

    private Validator<PaymentLink> paymentLinkValidator;
    private PaymentLink paymentLink;

    @Before
    public void setUp() {
        paymentLinkValidator = new PaymentLinkValidator();
        paymentLink = new PaymentLink();
        paymentLink.setAmount(new BigDecimal(1.00));
        paymentLink.setCurrency("EUR");
    }

    @Test
    public void testValidateSuccess() {
       paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithInvalidExpirationDate() {
        Date todayMinusOneDay = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
        paymentLink.setExpirationDate(todayMinusOneDay);
        paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithoutCurrency() {
        paymentLink.setCurrency(null);
        paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithInvalidCurrency() {
        paymentLink.setCurrency("US DOLLAR");
        paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithoutAmount() {
        paymentLink.setAmount(null);
        paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithInvalidAmount() {
        paymentLink.setAmount(BigDecimal.ZERO);
        paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithInvalidCountry() {
        paymentLink.setCountry("LITHUANIA");
        paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithInvalidDescription() {
        paymentLink.setDescription("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "123456789012345678901234567890123456789012345678901234567890");
        paymentLinkValidator.validate(paymentLink);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithInvalidOrderId() {
        paymentLink.setOrderId("123456789012345678901234567890123456789012345678901234567890");
        paymentLinkValidator.validate(paymentLink);
    }
}