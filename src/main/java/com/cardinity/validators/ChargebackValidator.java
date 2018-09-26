package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Chargeback;

public class ChargebackValidator implements Validator<Chargeback> {

    @Override
    public void validate(Chargeback chargeback) {

        if (chargeback == null)
            throw new ValidationException("Missing chargeback object.");

        // Mandatory fields
        if (!ValidationUtils.validateIntervalLength(chargeback.getDescription(), 1, 255))
            throw new ValidationException("Description is mandatory. Description maximum length 255 characters.");

        if (chargeback.getPaymentMethod() == null)
            throw new ValidationException("Payment method is mandatory.");

        if (!ValidationUtils.validateIntervalLength(chargeback.getReasonCode(), 1, 10))
            throw new ValidationException("Reason code is mandatory. Reason code maximum length 10 characters.");

        if (!ValidationUtils.validateIntervalLength(chargeback.getReasonMessage(), 1, 255))
            throw new ValidationException("Reason message is mandatory. Reason message maximum length 255 characters.");

        if (!ValidationUtils.validateIntervalLength(chargeback.getArn(), 1, 100))
            throw new ValidationException("Arn is mandatory. Arn maximum length 100 characters.");

        // Optional fields
        if (chargeback.getCaseId() != null && !ValidationUtils.validateIntervalLength(chargeback.getCaseId(), 0, 50))
            throw new ValidationException("Case ID maximum length 50 characters.");
    }

}
