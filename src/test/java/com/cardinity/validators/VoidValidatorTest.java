package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Void;
import org.junit.Before;
import org.junit.Test;

public class VoidValidatorTest {

    Void voidP;
    Validator<Void> voidValidator;

    @Before
    public void setUp() throws Exception {
        voidP = new Void();
        voidValidator = new VoidValidator();
    }

    @Test
    public void testValidate() throws Exception {
        voidValidator.validate(voidP);
    }

    @Test
    public void testValidateWithDescription() throws Exception {
        voidP.setDescription("TESTING");
        voidValidator.validate(voidP);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongDescription() throws Exception {
        voidP.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis sem ac nisl " +
                "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
                "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
                "metus mollis.");
        voidValidator.validate(voidP);
    }
}
