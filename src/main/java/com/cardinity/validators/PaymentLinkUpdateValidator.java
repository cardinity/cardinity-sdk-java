package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.PaymentLinkUpdate;

public class PaymentLinkUpdateValidator implements Validator<PaymentLinkUpdate> {

    @Override
    public void validate(PaymentLinkUpdate paymentLinkUpdate) {

        if (paymentLinkUpdate == null)
            throw new ValidationException("Missing payment link object.");

        if(paymentLinkUpdate.getEnabled() == null && paymentLinkUpdate.getExpirationDate() == null)
            throw new ValidationException("At least one parameter should be non-null");

        if (paymentLinkUpdate.getExpirationDate()!= null && !ValidationUtils.isDateInFuture(paymentLinkUpdate.getExpirationDate()))
            throw new ValidationException("Payment link expiration date should be a future date");
    }
}
