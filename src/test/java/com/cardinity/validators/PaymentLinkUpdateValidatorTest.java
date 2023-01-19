package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.PaymentLinkUpdate;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class PaymentLinkUpdateValidatorTest {

    private Validator<PaymentLinkUpdate> paymentLinkUpdateValidator;
    private PaymentLinkUpdate paymentLinkUpdate;

    @Before
    public void setUp() {
        paymentLinkUpdateValidator = new PaymentLinkUpdateValidator();
        paymentLinkUpdate = new PaymentLinkUpdate();
    }

    @Test
    public void testValidateSuccess() {
        paymentLinkUpdate.setEnabled(false);
        paymentLinkUpdateValidator.validate(paymentLinkUpdate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithInvalidExpirationDate() {
        Date todayMinusOneDay = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
        paymentLinkUpdate.setExpirationDate(todayMinusOneDay);
        paymentLinkUpdateValidator.validate(paymentLinkUpdate);
    }

    @Test(expected = ValidationException.class)
    public void testValidateFailWithAllParametersNull() {
        paymentLinkUpdateValidator.validate(paymentLinkUpdate);
    }

}