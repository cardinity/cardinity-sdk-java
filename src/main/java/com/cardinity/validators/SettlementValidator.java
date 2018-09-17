package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Settlement;

import java.math.BigDecimal;

public class SettlementValidator implements Validator<Settlement> {

    private static final String MINIMUM_AMOUNT = "0.01";

    @Override
    public void validate(Settlement settlement) {

        if (settlement == null)
            throw new ValidationException("Missing settlement object.");

        // Mandatory fields
        if (!ValidationUtils.validateAmount(settlement.getAmount(), new BigDecimal(MINIMUM_AMOUNT)))
            throw new ValidationException("Settlement amount smaller than minimum (" + MINIMUM_AMOUNT + ").");

        // Optional fields
        if (settlement.getDescription() != null && !ValidationUtils.validateIntervalLength(settlement.getDescription
                (), 0, 255))
            throw new ValidationException("Description maximum length 255 characters.");
    }

}
