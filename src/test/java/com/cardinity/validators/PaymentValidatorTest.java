package com.cardinity.validators;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Card;
import com.cardinity.model.Payment;
import com.cardinity.model.Recurring;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentValidatorTest {

    Payment payment;
    Validator<Payment> paymentValidator;

    @Before
    public void setUp() {
        payment = new Payment();
        payment.setAmount(BigDecimal.ONE);
        payment.setCountry("LT");
        payment.setCurrency("EUR");
        payment.setSettle(true);
        payment.setPaymentMethod(Payment.Method.CARD);
        Card card = new Card();
        card.setCvc(123);
        card.setExpYear(2016);
        card.setExpMonth(11);
        card.setHolder("John Doe");
        card.setPan("4111111111111111");
        payment.setPaymentInstrument(card);

        paymentValidator = new PaymentValidator();
    }

    @Test
    public void testSuccessValidateCardPayment() {
        paymentValidator.validate(payment);
    }

    @Test
    public void testSuccessValidateRecurringPayment() {
        Recurring recurring = new Recurring();
        recurring.setPaymentId(UUID.randomUUID());
        payment.setPaymentInstrument(recurring);
        payment.setPaymentMethod(Payment.Method.RECURRING);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongCurrency() throws Exception {
        payment.setCurrency("EURA");
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongAmount() throws Exception {
        payment.setAmount(BigDecimal.ZERO);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongCountry() throws Exception {
        payment.setCountry("LTU");
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateMissingPaymentMethod() throws Exception {
        payment.setPaymentMethod(null);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateMissingPaymentInstrument() throws Exception {
        payment.setPaymentInstrument(null);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateInstrumentAndMethodMismatch() throws Exception {
        payment.setPaymentMethod(Payment.Method.RECURRING);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongOrderId() throws Exception {
        payment.setOrderId("x");
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongDescription() throws Exception {
        payment.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis sem ac nisl " +
                "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
                "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
                "metus mollis.");
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongCardPAN() throws Exception {
        Card card = (Card) payment.getPaymentInstrument();
        card.setPan("123412341231234");
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongCardCvc() throws Exception {
        Card card = (Card) payment.getPaymentInstrument();
        card.setCvc(10000);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongCardExpMonth() throws Exception {
        Card card = (Card) payment.getPaymentInstrument();
        card.setExpMonth(13);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongCardExpYear() throws Exception {
        Card card = (Card) payment.getPaymentInstrument();
        card.setExpYear(1999);
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateWrongCardHolder() throws Exception {
        Card card = (Card) payment.getPaymentInstrument();
        card.setHolder("John Doe John Doe John Doe John Doe John Doe John Doe John Doe");
        paymentValidator.validate(payment);
    }

    @Test(expected = ValidationException.class)
    public void testValidateMissingRecurringId() throws Exception {
        Recurring recurring = new Recurring();
        payment.setPaymentMethod(Payment.Method.RECURRING);
        payment.setPaymentInstrument(recurring);
        paymentValidator.validate(payment);
    }

}
