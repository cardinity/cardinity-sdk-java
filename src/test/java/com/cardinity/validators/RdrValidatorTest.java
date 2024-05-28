package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Rdr;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class RdrValidatorTest {

    public static final String LONG_STRING_EXAMPLE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis sem ac nisl " +
            "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
            "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
            "metus mollis.";

    Rdr rdr;
    Validator<Rdr> rdrValidator;

    @Before
    public void setUp() {
        rdr = new Rdr();
        rdr.setArn("123");
        rdr.setReasonCode("456");
        rdr.setReasonMessage("789");
        rdr.setPaymentMethod("card");
        rdr.setCaseId("asd");
        rdr.setDescription("fgh");

        rdrValidator = new RdrValidator();
    }

    @Test
    public void testValidateSuccess() {
        rdrValidator.validate(rdr);
    }

    @Test
    public void testValidateSuccessWithoutOptionalFields() {
        rdr.setCaseId(null);
        rdr.setDescription(null);
        rdrValidator.validate(rdr);
    }

    @Test(expected = ValidationException.class)
    public void testValidateNullObject() {
        rdrValidator.validate(null);
    }

    @Test(expected = ValidationException.class)
    public void testValidateNoReasonMessage() {
        rdr.setReasonMessage(null);
        rdrValidator.validate(rdr);
    }

    @Test(expected = ValidationException.class)
    public void testValidateNoReasonCode() {
        rdr.setReasonCode(null);
        rdrValidator.validate(rdr);
    }

    @Test(expected = ValidationException.class)
    public void testValidateNoArn() {
        rdr.setArn(null);
        rdrValidator.validate(rdr);
    }

    @Test(expected = ValidationException.class)
    public void testValidateNoPaymentMethod() {
        rdr.setPaymentMethod(null);
        rdrValidator.validate(rdr);
    }

    @Test(expected = ValidationException.class)
    public void testValidateDescriptionLength() {
        rdr.setDescription(LONG_STRING_EXAMPLE);
        rdrValidator.validate(rdr);
    }
}
