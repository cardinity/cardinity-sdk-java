package com.cardinity;

import com.cardinity.exceptions.ValidationException;
import com.cardinity.model.*;
import com.cardinity.model.Void;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class CardinityIntegrationTest extends CardinityBaseTest {

    private static CardinityClient client;

    private static final String TEST_ORDER_ID = "cardinity-SDK-test";
    private static final String TEST_PAYMENT_DESCRIPTION = "cardinity-SDK-payment";
    private static final String TEST_REFUND_DESCRIPTION = "cardinity-SDK-refund";
    private static final String TEST_SETTLEMENT_DESCRIPTION = "cardinity-SDK-settlement";
    private static final String TEST_VOID_DESCRIPTION = "cardinity-SDK-void";

    @BeforeClass
    public static void setUpClass() throws Exception {
        final String consumerKey = System.getenv("CRD_KEY");
        final String consumerSecret = System.getenv("CRD_SECRET");
        if (consumerKey == null || consumerSecret == null) throw new Exception("Authorization keys missing");
        client = new CardinityClient(consumerKey, consumerSecret);
    }

    private static Payment createBaseCCPayment() {
        Payment payment = new Payment();
        payment.setCountry("LT");
        payment.setPaymentMethod(Payment.Method.CARD);
        Card card = new Card();
        card.setPan("4111111111111111");
        card.setCvc(1);
        card.setExpYear(2020);
        card.setExpMonth(1);
        card.setHolder("Cardinity Cardinity");
        payment.setPaymentInstrument(card);

        return payment;
    }

    @Test
    public void testCreateApprovedPayment() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setDescription(TEST_PAYMENT_DESCRIPTION);

        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());

        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
        assertEquals(TEST_PAYMENT_DESCRIPTION, resultPayment.getDescription());
    }

    @Test
    public void testCreateDeclinedPayment() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(160));
        payment.setCurrency("EUR");

        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());

        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.DECLINED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertNotNull(resultPayment.getError());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
    }

    @Test
    public void testApprovedThreeDSecurePayment() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setDescription("3d-pass");

        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());

        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.PENDING, resultPayment.getStatus());
        assertNotNull(resultPayment.getAuthorizationInformation());
        assertNotNull(resultPayment.getAuthorizationInformation().getUrl());
        assertNotNull(resultPayment.getAuthorizationInformation().getData());
        assertEquals("3d-pass", resultPayment.getAuthorizationInformation().getData());

        result = client.finalizePayment(resultPayment.getId(), "3d-pass");
        assertTrue(result.isValid());
        resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
    }

    @Test
    public void testDeclinedThreeDSecurePayment() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setDescription("3d-pass");

        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());

        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.PENDING, resultPayment.getStatus());
        assertNotNull(resultPayment.getAuthorizationInformation());
        assertNotNull(resultPayment.getAuthorizationInformation().getUrl());
        assertNotNull(resultPayment.getAuthorizationInformation().getData());
        assertEquals("3d-pass", resultPayment.getAuthorizationInformation().getData());

        result = client.finalizePayment(resultPayment.getId(), "3d-fail");
        assertTrue(result.isValid());
        resultPayment = result.getItem();
        assertEquals(Payment.Status.DECLINED, resultPayment.getStatus());
        assertNotNull(resultPayment.getError());
        assertTrue(resultPayment.getError().startsWith("3333"));
        assertNotNull(resultPayment.getId());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
    }

    @Test
    public void testCreateApprovedRecurringPayment() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");

        payment.setPaymentMethod(Payment.Method.RECURRING);
        Recurring recurringPayment = new Recurring();
        recurringPayment.setPaymentId(UUID.fromString("914f6d2a-f4a9-4cc5-992e-5842ebf3f257"));
        payment.setPaymentInstrument(recurringPayment);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        assertEquals(Payment.Status.APPROVED, result.getItem().getStatus());
    }

    @Test
    public void testCreateDeclinedRecurringPayment() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(160));
        payment.setCurrency("EUR");

        payment.setPaymentMethod(Payment.Method.RECURRING);
        Recurring recurringPayment = new Recurring();
        recurringPayment.setPaymentId(UUID.fromString("914f6d2a-f4a9-4cc5-992e-5842ebf3f257"));
        payment.setPaymentInstrument(recurringPayment);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        assertEquals(Payment.Status.DECLINED, result.getItem().getStatus());
    }

    @Test
    public void testGetExistingPayment() {

        UUID paymentId = UUID.fromString("914f6d2a-f4a9-4cc5-992e-5842ebf3f257");
        Result<Payment> result = client.getPayment(paymentId);
        assertTrue(result.isValid());

        Payment resultPayment = result.getItem();
        assertEquals(paymentId, resultPayment.getId());
    }

    @Test
    public void testGetNotExistingPayment() {

        UUID paymentId = UUID.fromString("914f6d2a-f4a9-4cc5-992e-5842ebf3f251");
        Result<Payment> result = client.getPayment(paymentId);
        assertFalse(result.isValid());

        CardinityError cardinityError = result.getCardinityError();
        assertEquals(Integer.valueOf(404), cardinityError.getStatus());
        assertNotNull(cardinityError.getDetail());
        assertNotNull(cardinityError.getTitle());
        assertNotNull(cardinityError.getType());
    }

    @Test
    public void testGetPaymentsListWithLimit() {
        Result<List<Payment>> result = client.getPayments(1);

        assertTrue(result.isValid());
        assertEquals(1, result.getItem().size());
    }

    @Test(expected = ValidationException.class)
    public void testGetPaymentsListWithInvalidLimit() {
        client.getPayments(-1);
    }

    @Test
    public void testGetPaymentsList() {
        Result<List<Payment>> result = client.getPayments();

        assertTrue(result.isValid());
        assertEquals(10, result.getItem().size());
    }

    @Test
    public void testCreateApprovedRefund() {
        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setOrderId(TEST_ORDER_ID);

        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());

        Refund refund = new Refund();
        refund.setAmount(payment.getAmount());
        refund.setDescription(TEST_REFUND_DESCRIPTION);
        Result<Refund> refundResult = client.createRefund(resultPayment.getId(), refund);
        assertTrue(refundResult.isValid());
        assertEquals(Refund.Status.APPROVED, refundResult.getItem().getStatus());
        assertEquals(TEST_REFUND_DESCRIPTION, refundResult.getItem().getDescription());
    }

    @Test
    public void testCreateDeclinedRefund() {
        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());

        Refund refund = new Refund(payment.getAmount(), "fail");
        Result<Refund> refundResult = client.createRefund(resultPayment.getId(), refund);
        assertTrue(refundResult.isValid());
        assertEquals(Refund.Status.DECLINED, refundResult.getItem().getStatus());
        assertNotNull(refundResult.getItem().getError());
    }

    @Test
    public void testGetRefund() {

        Result<Refund> refundsResult = client.getRefund(UUID.fromString("9bdea847-b6bc-491c-877a-4663ae49c0b9"), UUID
                .fromString("f862506b-9a5b-4127-9c1f-2e0635bfb805"));
        assertTrue(refundsResult.isValid());
        assertEquals(UUID.fromString("f862506b-9a5b-4127-9c1f-2e0635bfb805"), refundsResult.getItem().getId());
    }

    @Test
    public void testGetRefunds() {

        Result<List<Refund>> refundsResult = client.getRefunds(UUID.fromString("9bdea847-b6bc-491c-877a-4663ae49c0b9"));
        assertTrue(refundsResult.isValid());
        assertEquals(1, refundsResult.getItem().size());
    }

    @Test
    public void testCreateApprovedVoid() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());

        Void voidP = new Void();
        voidP.setDescription(TEST_VOID_DESCRIPTION);
        Result<Void> voidResult = client.createVoid(resultPayment.getId(), voidP);
        assertTrue(voidResult.isValid());
        assertEquals(Void.Status.APPROVED, voidResult.getItem().getStatus());
        assertEquals(TEST_VOID_DESCRIPTION, voidResult.getItem().getDescription());
    }

    @Test
    public void testCreateDeclinedVoid() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());

        Void voidP = new Void();
        voidP.setDescription("fail");
        Result<Void> voidResult = client.createVoid(resultPayment.getId(), voidP);
        assertTrue(voidResult.isValid());
        assertEquals(Void.Status.DECLINED, voidResult.getItem().getStatus());
        assertNotNull(voidResult.getItem().getError());
    }

    @Test
    public void testGetVoid() {

        Result<Void> voidResult = client.getVoid(UUID.fromString("e51690e7-4a35-4604-9fd6-6bc1fc772264"), UUID
                .fromString("d1cb56bb-5081-4c38-8f4d-7069e38989cc"));
        assertTrue(voidResult.isValid());
        assertEquals(UUID.fromString("d1cb56bb-5081-4c38-8f4d-7069e38989cc"), voidResult.getItem().getId());
    }

    @Test
    public void testGetVoids() {

        Result<List<Void>> voids = client.getVoids(UUID.fromString("e51690e7-4a35-4604-9fd6-6bc1fc772264"));
        assertTrue(voids.isValid());
        assertEquals(1, voids.getItem().size());
    }

    @Test
    public void testCreateApprovedSettlement() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());

        Settlement settlement = new Settlement();
        settlement.setAmount(new BigDecimal(9.99));
        settlement.setDescription(TEST_SETTLEMENT_DESCRIPTION);
        Result<Settlement> settlementResult = client.createSettlement(resultPayment.getId(), settlement);
        assertTrue(settlementResult.isValid());
        assertEquals(Settlement.Status.APPROVED, settlementResult.getItem().getStatus());
        assertEquals(TEST_SETTLEMENT_DESCRIPTION, settlementResult.getItem().getDescription());
    }

    @Test
    public void testCreateDeclinedSettlement() {

        Payment payment = createBaseCCPayment();
        payment.setAmount(new BigDecimal(10));
        payment.setCurrency("EUR");
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(Payment.Status.APPROVED, resultPayment.getStatus());

        Settlement settlement = new Settlement();
        settlement.setAmount(new BigDecimal(9.99));
        settlement.setDescription("fail");
        Result<Settlement> settlementResult = client.createSettlement(resultPayment.getId(), settlement);
        assertTrue(settlementResult.isValid());
        assertEquals(Settlement.Status.DECLINED, settlementResult.getItem().getStatus());
        assertNotNull(settlementResult.getItem().getError());
    }

    @Test
    public void testGetSettlement() {

        Result<Settlement> settlementResult = client.getSettlement(UUID.fromString
                ("8b492bfc-0e6a-45c7-a50e-073b511eca6c"), UUID.fromString("b98bb227-1074-465a-92a7-694aae992dcb"));
        assertTrue(settlementResult.isValid());
        assertEquals(UUID.fromString("b98bb227-1074-465a-92a7-694aae992dcb"), settlementResult.getItem().getId());
    }

    @Test
    public void testGetSettlements() {

        Result<List<Settlement>> settlements = client.getSettlements(UUID.fromString("8b492bfc-0e6a-45c7-a50e" +
                "-073b511eca6c"));
        assertTrue(settlements.isValid());
        assertEquals(1, settlements.getItem().size());
    }

}
