package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Void;

public class VoidValidator implements Validator<Void> {

    @Override
    public void validate(Void voidP) {

        if (voidP == null)
            throw new ValidationException("Missing void object.");

        // Optional fields
        if (voidP.getDescription() != null && !ValidationUtils.validateIntervalLength(voidP.getDescription(), 0, 255))
            throw new ValidationException("Description maximum length 255 characters.");
    }

}
