package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.PaymentLink;

import java.math.BigDecimal;

public class PaymentLinkValidator implements Validator<PaymentLink> {

    private static final String MINIMUM_AMOUNT = "0.50";

    @Override
    public void validate(PaymentLink paymentLink) {

        if (paymentLink == null)
            throw new ValidationException("Missing payment link object.");

        // Mandatory fields
        if (!ValidationUtils.validateExactLength(paymentLink.getCurrency(), 3))
            throw new ValidationException("Currency is mandatory. Should be three-letter ISO currency code.");

        if (!ValidationUtils.validateAmount(paymentLink.getAmount(), new BigDecimal(MINIMUM_AMOUNT)))
            throw new ValidationException("Payment amount smaller than minimum (" + MINIMUM_AMOUNT + ").");

        if (paymentLink.getExpirationDate()!= null && !ValidationUtils.isDateInFuture(paymentLink.getExpirationDate())) {
            throw new ValidationException("Payment link expiration date should be a future date");
        }
    }
}
