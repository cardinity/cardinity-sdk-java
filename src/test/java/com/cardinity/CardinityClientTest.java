package com.cardinity;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.Card;
import com.cardinity.model.Payment;
import com.cardinity.model.Refund;
import com.cardinity.model.Void;
import com.cardinity.model.Settlement;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

public class CardinityClientTest {

    static CardinityClient client;

    @BeforeClass
    public static void setUpClass() {
        client = new CardinityClient("consumerKey", "consumerSecret");
    }

    private static Payment createBaseCCPayment() {
        Payment payment = new Payment();
        payment.setCountry("LT");
        payment.setSettle(true);
        payment.setPaymentMethod(Payment.Method.CARD);
        Card card = new Card();
        card.setPan("4111111111111111");
        card.setCvc(123);
        card.setExpYear(2016);
        card.setExpMonth(1);
        card.setHolder("Cardinity Cardinity");
        payment.setPaymentInstrument(card);

        return payment;
    }

    /**
     * Payment validation tests
     */
    @Test(expected = ValidationException.class)
    public void testCreatePaymentValidationException1() {
        client.createPayment(null);
    }

    @Test(expected = ValidationException.class)
    public void testCreatePaymentValidationException2() {
        client.createPayment(new Payment());
    }

    @Test(expected = ValidationException.class)
    public void testFinalizePaymentValidationException1() {
        client.finalizePayment(null, "authorize_data_test");
    }

    @Test(expected = ValidationException.class)
    public void testFinalizePaymentValidationException2() {
        client.finalizePayment(UUID.randomUUID(), " ");
    }

    @Test(expected = ValidationException.class)
    public void testFinalizePaymentValidationException3() {
        client.finalizePayment(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testGetPaymentValidationException() {
        client.getPayment(null);
    }

    @Test(expected = ValidationException.class)
    public void testGetPaymentsValidationException1() {
        client.getPayments(101);
    }

    @Test(expected = ValidationException.class)
    public void testGetPaymentsValidationException2() {
        client.getPayments(-1);
    }

    /**
     * Settlement validation tests
     */
    @Test(expected = ValidationException.class)
    public void testCreateSettlementValidationException1() {
        client.createSettlement(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateSettlementValidationException2() {
        client.createSettlement(UUID.randomUUID(), null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateSettlementValidationException3() {
        Settlement settlement = new Settlement(BigDecimal.ZERO);
        client.createSettlement(UUID.randomUUID(), settlement);
    }

    @Test(expected = ValidationException.class)
    public void testCreateSettlementValidationException4() {
        Settlement settlement = new Settlement(BigDecimal.ONE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Nulla convallis sem ac nisl " +
                "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
                "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
                "metus mollis.");
        client.createSettlement(UUID.randomUUID(), settlement);
    }

    @Test(expected = ValidationException.class)
    public void testCreateSettlementValidationException5() {
        Settlement settlement = new Settlement();
        client.createSettlement(UUID.randomUUID(), settlement);
    }

    @Test(expected = ValidationException.class)
    public void testGetSettlementValidationException1() {
        client.getSettlement(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testGetSettlementValidationException2() {
        client.getSettlement(UUID.randomUUID(), null);
    }

    @Test(expected = ValidationException.class)
    public void testGetSettlementValidationException3() {
        client.getSettlement(null, UUID.randomUUID());
    }

    @Test(expected = ValidationException.class)
    public void testGetSettlementsValidationException() {
        client.getSettlements(null);
    }

    /**
     * Void validation tests
     */
    @Test(expected = ValidationException.class)
    public void testCreateVoidValidationException1() {
        client.createVoid(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateVoidValidationException2() {
        client.createVoid(UUID.randomUUID(), null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateVoidValidationException3() {
        Void voidP = new Void("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla " +
                "convallis sem ac nisl " +
                "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
                "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
                "metus mollis.");
        client.createVoid(UUID.randomUUID(), voidP);
    }

    @Test(expected = ValidationException.class)
    public void testGetVoidValidationException1() {
        client.getVoid(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testGetVoidValidationException2() {
        client.getVoid(UUID.randomUUID(), null);
    }

    @Test(expected = ValidationException.class)
    public void testGetVoidValidationException3() {
        client.getVoid(null, UUID.randomUUID());
    }

    @Test(expected = ValidationException.class)
    public void testGetVoidsValidationException() {
        client.getVoids(null);
    }

    /**
     * Refund validation tests
     */
    @Test(expected = ValidationException.class)
    public void testCreateRefundValidationException1() {
        client.createRefund(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateRefundValidationException2() {
        client.createRefund(UUID.randomUUID(), null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateRefundValidationException3() {
        Refund refund = new Refund(BigDecimal.ZERO);
        client.createRefund(UUID.randomUUID(), refund);
    }

    @Test(expected = ValidationException.class)
    public void testCreateRefundValidationException4() {
        Refund refund = new Refund(BigDecimal.ONE, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla convallis sem ac nisl " +
                "facilisis mattis non nec nibh. Praesent sit amet dolor libero. Fusce lacus orci, sollicitudin a " +
                "aliquam eu, porta quis velit. Aliquam et ex nisi. Phasellus dictum nisi at est faucibus, eu gravida " +
                "metus mollis.");
        client.createRefund(UUID.randomUUID(), refund);
    }

    @Test(expected = ValidationException.class)
    public void testCreateRefundValidationException5() {
        Refund refund = new Refund();
        client.createRefund(UUID.randomUUID(), refund);
    }

    @Test(expected = ValidationException.class)
    public void testGetRefundValidationException1() {
        client.getRefund(null, null);
    }

    @Test(expected = ValidationException.class)
    public void testGetRefundValidationException2() {
        client.getRefund(UUID.randomUUID(), null);
    }

    @Test(expected = ValidationException.class)
    public void testGetRefundValidationException3() {
        client.getRefund(null, UUID.randomUUID());
    }

    @Test(expected = ValidationException.class)
    public void testGetRefundsValidationException() {
        client.getRefunds(null);
    }

    /**
     * CardinityClient constructor validations test
     */
    @Test(expected = ValidationException.class)
    public void testCreateClientException1() {
        new CardinityClient(null, "test");
    }

    @Test(expected = ValidationException.class)
    public void testCreateClientException2() {
        new CardinityClient("", "test");
    }

    @Test(expected = ValidationException.class)
    public void testCreateClientException3() {
        new CardinityClient("test", null);
    }

    @Test(expected = ValidationException.class)
    public void testCreateClientException4() {
        new CardinityClient("test", "");
    }

    @Test(expected = ValidationException.class)
    public void testCreateClientException5() {
        new CardinityClient("", "");
    }

    @Test(expected = ValidationException.class)
    public void testCreateClientException6() {
        new CardinityClient(null, null);
    }

}
