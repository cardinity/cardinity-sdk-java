package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Rdr;

public class RdrValidator implements Validator<Rdr> {

    @Override
    public void validate(Rdr rdr) {

        if (rdr == null)
            throw new ValidationException("Missing rdr object.");

        // Mandatory fields
        if (rdr.getPaymentMethod() == null)
            throw new ValidationException("Payment method is mandatory.");

        if (!ValidationUtils.validateIntervalLength(rdr.getReasonCode(), 1, 10))
            throw new ValidationException("Reason code is mandatory. Reason code maximum length 10 characters.");

        if (!ValidationUtils.validateIntervalLength(rdr.getReasonMessage(), 1, 255))
            throw new ValidationException("Reason message is mandatory. Reason message maximum length 255 characters.");

        if (!ValidationUtils.validateIntervalLength(rdr.getArn(), 1, 100))
            throw new ValidationException("Arn is mandatory. Arn maximum length 100 characters.");

        // Optional fields
        if (rdr.getCaseId() != null && !ValidationUtils.validateIntervalLength(rdr.getCaseId(), 0, 50)) {
            throw new ValidationException("Case ID maximum length 50 characters.");
        }

        if (rdr.getDescription() != null && !ValidationUtils.validateIntervalLength(rdr.getDescription(), 0, 255)) {
            throw new ValidationException("Description maximum length 255 characters.");
        }
    }

}
