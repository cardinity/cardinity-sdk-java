package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Card;
import com.cardinity.model.Payment;
import com.cardinity.model.Recurring;

import java.math.BigDecimal;

public class PaymentValidator implements Validator<Payment> {

    private static final String MINIMUM_AMOUNT = "0.50";

    @Override
    public void validate(Payment payment) {

        if (payment == null)
            throw new ValidationException("Missing payment object.");

        // Mandatory fields
        if (!ValidationUtils.validateExactLength(payment.getCurrency(), 3))
            throw new ValidationException("Currency is mandatory. Should be three-letter ISO currency code.");

        if (!ValidationUtils.validateAmount(payment.getAmount(), new BigDecimal(MINIMUM_AMOUNT)))
            throw new ValidationException("Payment amount smaller than minimum (" + MINIMUM_AMOUNT + ").");

        if (!ValidationUtils.validateExactLength(payment.getCountry(), 2))
            throw new ValidationException("Country code is mandatory. Should be ISO 3166-1 alpha-2 country code.");

        if (payment.getPaymentMethod() == null)
            throw new ValidationException("Payment method is mandatory.");

        if (payment.getPaymentInstrument() == null)
            throw new ValidationException("Payment instrument is mandatory.");

        if (payment.getPaymentInstrument().getType() != payment.getPaymentMethod())
            throw new ValidationException("Payment instrument and payment type mismatch.");

        if (payment.getPaymentInstrument().getType() == Payment.Method.CARD)
            validateCard((Card) payment.getPaymentInstrument());
        else if (payment.getPaymentInstrument().getType() == Payment.Method.RECURRING)
            validateRecurring((Recurring) payment.getPaymentInstrument());
        else
            throw new ValidationException("Invalid payment instrument.");

        // Optional fields
        if (payment.getOrderId() != null && !ValidationUtils.validateIntervalLength(payment.getOrderId(), 2, 50))
            throw new ValidationException("Order ID must be between 2 and 50 characters long.");

        if (payment.getDescription() != null && !ValidationUtils.validateIntervalLength(payment.getDescription(), 0,
                255))
            throw new ValidationException("Description maximum length 255 characters.");
    }

    private void validateCard(Card card) {

        if (!ValidationUtils.validateInteger(card.getExpMonth(), 1, 12))
            throw new ValidationException("Invalid card exp month. Must be between 1 and 12.");

        if (!ValidationUtils.validateInteger(card.getExpYear(), 2000, 2100))
            throw new ValidationException("Invalid card exp year.");

        if (!ValidationUtils.validateInteger(card.getCvc(), 0, 9999))
            throw new ValidationException("Invalid card cvc code. Must be 3 or 4 numbers.");

        if (!ValidationUtils.validateIntervalLength(card.getHolder(), 1, 32))
            throw new ValidationException("Invalid card holder. Max length 32 characters.");

        if (!ValidationUtils.validateCardNumber(card.getPan()))
            throw new ValidationException("Invalid card pan.");
    }

    private void validateRecurring(Recurring recurring) {
        if (recurring.getPaymentId() == null)
            throw new ValidationException("Recurring payment id is mandatory.");
    }
}
