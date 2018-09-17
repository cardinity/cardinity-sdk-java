package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Settlement;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class SettlementValidatorTest {

    Settlement settlement;
    Validator<Settlement> settlementValidator;

    @Before
    public void setUp() throws Exception {
        settlement = new Settlement();
        settlement.setAmount(BigDecimal.ONE);

        settlementValidator = new SettlementValidator();
    }

    @Test
    public void testValidateSuccess() throws Exception {
        settlementValidator.validate(settlement);
    }

    @Test
    public void testValidateSuccessWithDescription() throws Exception {
        settlement.setDescription("TESTING");
        settlementValidator.validate(settlement);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongAmount() {
        settlement.setAmount(BigDecimal.ZERO);
        settlementValidator.validate(settlement);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongDescription() {
        settlement.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis sem ac " +
                "nisl " +
                "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
                "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
                "metus mollis.");
        settlementValidator.validate(settlement);
    }
}
