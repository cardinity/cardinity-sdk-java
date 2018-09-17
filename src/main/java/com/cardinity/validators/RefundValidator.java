package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Refund;

import java.math.BigDecimal;

public class RefundValidator implements Validator<Refund> {

    private static final String MINIMUM_AMOUNT = "0.01";

    @Override
    public void validate(Refund refund) {

        if (refund == null)
            throw new ValidationException("Missing refund object.");

        // Mandatory fields
        if (!ValidationUtils.validateAmount(refund.getAmount(), new BigDecimal(MINIMUM_AMOUNT)))
            throw new ValidationException("Refund amount smaller than minimum (" + MINIMUM_AMOUNT + ").");

        // Optional fields
        if (refund.getDescription() != null && !ValidationUtils.validateIntervalLength(refund.getDescription(), 0, 255))
            throw new ValidationException("Description maximum length 255 characters.");
    }

}
