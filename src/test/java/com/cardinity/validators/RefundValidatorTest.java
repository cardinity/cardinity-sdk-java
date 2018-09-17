package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Refund;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class RefundValidatorTest {

    Refund refund;
    Validator<Refund> refundValidator;

    @Before
    public void setUp() throws Exception {
        refund = new Refund();
        refund.setAmount(BigDecimal.ONE);

        refundValidator = new RefundValidator();
    }

    @Test
    public void testValidateSuccess() throws Exception {
        refundValidator.validate(refund);
    }

    @Test
    public void testValidateSuccessWithDescription() throws Exception {
        refund.setDescription("TESTING");
        refundValidator.validate(refund);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongAmount() {
        refund.setAmount(BigDecimal.ZERO);
        refundValidator.validate(refund);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongDescription() {
        refund.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis sem ac nisl " +
                "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
                "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
                "metus mollis.");
        refundValidator.validate(refund);
    }
}
