package com.cardinity;

import com.cardinity.model.Void;
import com.cardinity.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.cardinity.model.Payment.Status.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class CardinityIntegrationTest extends CardinityBaseTest {

    private static CardinityClient client;
    private static CardinityClient passthroughClient;

    private static final String TEST_ORDER_ID = "cardinity-SDK-test";
    private static final String TEST_PAYMENT_DESCRIPTION = "cardinity-SDK-payment";
    private static final String TEST_REFUND_DESCRIPTION = "cardinity-SDK-refund";
    private static final String TEST_SETTLEMENT_DESCRIPTION = "cardinity-SDK-settlement";
    private static final String TEST_VOID_DESCRIPTION = "cardinity-SDK-void";
    private static final String TEST_PAYMENT_LINK_DESCRIPTION = "cardinity-SDK-paymentLink";

    @BeforeClass
    public static void setUpClass() throws Exception {
        final String consumerKey = System.getenv("CRD_KEY");
        final String consumerSecret = System.getenv("CRD_SECRET");
        if (consumerKey == null || consumerSecret == null) throw new Exception("Authorization keys missing");
        client = new CardinityClient(consumerKey, consumerSecret);

        final String passthroughConsumerKey = System.getenv("CRD_PASSTHROUGH_KEY");
        final String passthroughConsumerSecret = System.getenv("CRD_PASSTHROUGH_SECRET");
        if (passthroughConsumerKey == null || passthroughConsumerSecret == null) throw new Exception("Authorization keys missing");
        passthroughClient = new CardinityClient(passthroughConsumerKey, passthroughConsumerSecret);
    }

    private static Payment getBaseCCPayment() {
        Payment payment = new Payment();
        payment.setCountry("LT");
        payment.setAmount(new BigDecimal(10).setScale(2, RoundingMode.HALF_EVEN));
        payment.setCurrency("EUR");
        payment.setDescription(TEST_PAYMENT_DESCRIPTION);
        payment.setPaymentMethod(Payment.Method.CARD);
        Card card = new Card();
        card.setPan("4111111111111111");
        card.setCvc(123);
        card.setExpYear(Calendar.getInstance().get(Calendar.YEAR) + 1);
        card.setExpMonth(1);
        card.setHolder("Cardinity Cardinity");
        payment.setPaymentInstrument(card);
        return payment;
    }

    private static Payment getBaseCCPayment(String currency, BigDecimal amount) {
        Payment payment = getBaseCCPayment();
        payment.setAmount(amount);
        payment.setCurrency(currency);
        return payment;
    }

    private static Threeds2Data getThreeds2Data() {
        Threeds2Data threeds2Data = new Threeds2Data();
        threeds2Data.setNotificationUrl("http://notification.url");

        BrowserInfo browserInfo = new BrowserInfo();
        browserInfo.setAcceptHeader("text/html");
        browserInfo.setBrowserLanguage("en-US");
        browserInfo.setScreenWidth(1920);
        browserInfo.setScreenHeight(1040);
        browserInfo.setChallengeWindowSize(BrowserInfo.ChallengeWindowSize.SIZE_600X400);
        browserInfo.setUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:21.0) Gecko/20100101 Firefox/21.0");
        browserInfo.setColorDepth(24);
        browserInfo.setTimeZone(-60);
        browserInfo.setIpAddress("216.58.207.35");
        threeds2Data.setBrowserInfo(browserInfo);

        Address billingAddress = new Address();
        billingAddress.setAddressLine1("8239 Louie Street");
        billingAddress.setAddressLine2("line2");
        billingAddress.setAddressLine3("line3");
        billingAddress.setCity("Coltenburgh");
        billingAddress.setCountry("US");
        billingAddress.setPostalCode("84603");
        billingAddress.setState("DC");
        threeds2Data.setBillingAddress(billingAddress);

        Address deliveryAddress = new Address();
        deliveryAddress.setAddressLine1("Schallerallee 33");
        deliveryAddress.setCity("Ravensburg");
        deliveryAddress.setCountry("DE");
        deliveryAddress.setPostalCode("82940");
        threeds2Data.setDeliveryAddress(deliveryAddress);

        CardholderInfo cardholderInfo = new CardholderInfo();
        cardholderInfo.setEmailAddress("card.holder@example.com");
        cardholderInfo.setMobilePhoneNumber("+353209120599");
        cardholderInfo.setHomePhoneNumber("+353209174412");
        cardholderInfo.setWorkPhoneNumber("+353209134251");
        threeds2Data.setCardholderInfo(cardholderInfo);

        return threeds2Data;
    }

    @Test
    public void testCreateApprovedPayment() {
        Payment resultPayment = createApprovedPayment();
        assertEquals(APPROVED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
        assertEquals(TEST_PAYMENT_DESCRIPTION, resultPayment.getDescription());
        assertFalse(resultPayment.isThreedsV2());
        assertFalse(resultPayment.isThreedsV1());
    }

    @Test
    public void testCreateDeclinedPayment() {
        Payment payment = getBaseCCPayment();
        payment.setAmount(new BigDecimal(160).setScale(2, RoundingMode.HALF_EVEN));
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());

        Payment resultPayment = result.getItem();
        assertEquals(DECLINED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertNotNull(resultPayment.getError());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
        assertFalse(resultPayment.isThreedsV2());
        assertFalse(resultPayment.isThreedsV1());
    }

    @Test
    public void testCreateApprovedPaymentWithDescriptorSuffix() {
        Payment payment = getBaseCCPayment();
        payment.setStatementDescriptorSuffix("DS");
        Result<Payment> initialResult = client.createPayment(payment);
        assertTrue(initialResult.isValid());
        Payment resultPayment = initialResult.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
        assertEquals(TEST_PAYMENT_DESCRIPTION, resultPayment.getDescription());
        assertFalse(resultPayment.isThreedsV2());
        assertFalse(resultPayment.isThreedsV1());
    }

    @Test
    public void testApprovedThreeDSecurePaymentV2() {
        Payment payment = getBaseCCPayment();
        payment.setDescription("3ds2-pass");
        payment.setThreeds2Data(getThreeds2Data());
        Payment resultPayment = createPendingPayment(payment, true);

        Result<Payment> finalizeResult = client.finalizePaymentV2(resultPayment.getId(), "3ds2-pass");
        assertTrue(finalizeResult.isValid());
        Payment finalizedPayment = finalizeResult.getItem();
        assertEquals(APPROVED, finalizedPayment.getStatus());
        assertEquals(resultPayment.getId(), finalizedPayment.getId());
    }

    @Test
    public void testCreateApprovedRecurringPayment() {
        Payment initialResultPayment = createApprovedPayment();
        Payment payment = getBaseCCPayment();
        payment.setAmount(new BigDecimal(20).setScale(2, RoundingMode.HALF_EVEN));
        payment.setPaymentMethod(Payment.Method.RECURRING);
        Recurring recurringPayment = new Recurring();
        recurringPayment.setPaymentId(initialResultPayment.getId());
        payment.setPaymentInstrument(recurringPayment);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        assertEquals(APPROVED, result.getItem().getStatus());
    }

    @Test
    public void testCreateDeclinedRecurringPayment() {
        Payment initialResultPayment = createApprovedPayment();
        Payment payment = getBaseCCPayment();
        payment.setAmount(new BigDecimal(160).setScale(2, RoundingMode.HALF_EVEN));
        payment.setPaymentMethod(Payment.Method.RECURRING);
        Recurring recurringPayment = new Recurring();
        recurringPayment.setPaymentId(initialResultPayment.getId());
        payment.setPaymentInstrument(recurringPayment);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        assertEquals(DECLINED, result.getItem().getStatus());
    }

    @Test
    public void testGetExistingPayment() {
        Payment initialResultPayment = createApprovedPayment();
        Result<Payment> result = client.getPayment(initialResultPayment.getId());
        assertTrue(result.isValid());

        Payment resultPayment = result.getItem();
        assertEquals(initialResultPayment.getId(), resultPayment.getId());
        assertEquals(initialResultPayment.getStatus(), resultPayment.getStatus());
    }

    @Test
    public void testGetNotExistingPayment() {
        UUID paymentId = UUID.fromString("00000000-0000-0000-0000-000000000000");
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
        Result<List<Payment>> result = client.getPayments(2);
        assertTrue(result.isValid());
        assertEquals(2, result.getItem().size());
    }

    @Test
    public void testGetPaymentsList() {
        Result<List<Payment>> result = client.getPayments();
        assertTrue(result.isValid());
        assertEquals(10, result.getItem().size());
    }

    @Test
    public void testCreateApprovedRefund() {
        Payment payment = getBaseCCPayment();
        payment.setOrderId(TEST_ORDER_ID);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());
        Refund refund = new Refund();
        refund.setAmount(resultPayment.getAmount());
        refund.setDescription(TEST_REFUND_DESCRIPTION);
        Result<Refund> refundResult = client.createRefund(resultPayment.getId(), refund);
        assertTrue(refundResult.isValid());
        assertEquals(Refund.Status.APPROVED, refundResult.getItem().getStatus());
        assertEquals(TEST_REFUND_DESCRIPTION, refundResult.getItem().getDescription());
    }

    @Test
    public void testCreateDeclinedRefund() {
        Payment resultPayment = createApprovedPayment();
        Refund refund = new Refund(resultPayment.getAmount(), "fail");
        Result<Refund> refundResult = client.createRefund(resultPayment.getId(), refund);
        assertTrue(refundResult.isValid());
        assertEquals(Refund.Status.DECLINED, refundResult.getItem().getStatus());
        assertNotNull(refundResult.getItem().getError());
    }

    @Test
    public void testGetRefunds() {
        Payment resultPayment = createApprovedPayment();
        Refund refund = new Refund();
        refund.setAmount(resultPayment.getAmount());
        Result<Refund> initialRefundResult = client.createRefund(resultPayment.getId(), refund);
        assertTrue(initialRefundResult.isValid());
        assertEquals(Refund.Status.APPROVED, initialRefundResult.getItem().getStatus());
        Refund resultRefund = initialRefundResult.getItem();

        Result<Refund> refundResult = client.getRefund(resultPayment.getId(), resultRefund.getId());
        assertTrue(refundResult.isValid());
        assertEquals(resultRefund.getId(), refundResult.getItem().getId());
        assertEquals(resultRefund.getStatus(), refundResult.getItem().getStatus());

        Result<List<Refund>> refundsResult = client.getRefunds(resultPayment.getId());
        assertTrue(refundsResult.isValid());
        assertEquals(1, refundsResult.getItem().size());
    }

    @Test
    public void testCreateApprovedVoid() {
        Payment payment = getBaseCCPayment();
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());

        Void voidP = new Void();
        voidP.setDescription(TEST_VOID_DESCRIPTION);
        Result<Void> voidResult = client.createVoid(resultPayment.getId(), voidP);
        assertTrue(voidResult.isValid());
        assertEquals(Void.Status.APPROVED, voidResult.getItem().getStatus());
        assertEquals(TEST_VOID_DESCRIPTION, voidResult.getItem().getDescription());
    }

    @Test
    public void testCreateDeclinedVoid() {
        Payment payment = getBaseCCPayment();
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());

        Void voidP = new Void();
        voidP.setDescription("fail");
        Result<Void> voidResult = client.createVoid(resultPayment.getId(), voidP);
        assertTrue(voidResult.isValid());
        assertEquals(Void.Status.DECLINED, voidResult.getItem().getStatus());
        assertNotNull(voidResult.getItem().getError());
    }

    @Test
    public void testGetVoids() {
        Payment payment = getBaseCCPayment();
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());

        Void initialVoid = new Void();
        Result<Void> initialVoidResult = client.createVoid(resultPayment.getId(), initialVoid);
        assertTrue(initialVoidResult.isValid());
        Void initialResultVoid = initialVoidResult.getItem();
        assertEquals(Void.Status.APPROVED, initialResultVoid.getStatus());

        Result<Void> voidResult = client.getVoid(resultPayment.getId(), initialResultVoid.getId());
        assertTrue(voidResult.isValid());
        assertEquals(initialResultVoid.getId(), voidResult.getItem().getId());
        assertEquals(initialResultVoid.getStatus(), voidResult.getItem().getStatus());

        Result<List<Void>> voidsResult = client.getVoids(resultPayment.getId());
        assertTrue(voidsResult.isValid());
        assertEquals(1, voidsResult.getItem().size());
    }

    @Test
    public void testCreateApprovedSettlement() {
        Payment payment = getBaseCCPayment();
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());

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
        Payment payment = getBaseCCPayment();
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());

        Settlement settlement = new Settlement();
        settlement.setAmount(new BigDecimal(9.99));
        settlement.setDescription("fail");
        Result<Settlement> settlementResult = client.createSettlement(resultPayment.getId(), settlement);
        assertTrue(settlementResult.isValid());
        assertEquals(Settlement.Status.DECLINED, settlementResult.getItem().getStatus());
        assertNotNull(settlementResult.getItem().getError());
    }

    @Test
    public void testGetSettlements() {
        Payment payment = getBaseCCPayment();
        payment.setSettle(false);
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());

        Settlement initialSettlement = new Settlement();
        initialSettlement.setAmount(new BigDecimal(9.99));
        Result<Settlement> initialSettlementResult = client.createSettlement(resultPayment.getId(), initialSettlement);
        assertTrue(initialSettlementResult.isValid());
        Settlement initialResultSettlement = initialSettlementResult.getItem();
        assertEquals(Settlement.Status.APPROVED, initialResultSettlement.getStatus());

        Result<Settlement> settlementResult = client.getSettlement(resultPayment.getId(), initialResultSettlement.getId());
        assertTrue(settlementResult.isValid());
        assertEquals(initialResultSettlement.getId(), settlementResult.getItem().getId());
        assertEquals(initialResultSettlement.getStatus(), settlementResult.getItem().getStatus());

        Result<List<Settlement>> settlements = client.getSettlements(resultPayment.getId());
        assertTrue(settlements.isValid());
        assertEquals(1, settlements.getItem().size());
    }

    @Test
    public void testCreatePaymentLink() {
        Date todayPlus7Days = new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 7));

        PaymentLink paymentLink = new PaymentLink();
        paymentLink.setAmount(new BigDecimal("1.00"));
        paymentLink.setCurrency("EUR");
        paymentLink.setCountry("LT");
        paymentLink.setDescription(TEST_PAYMENT_LINK_DESCRIPTION);
        paymentLink.setExpirationDate(todayPlus7Days);
        paymentLink.setMultipleUse(true);

        Result<PaymentLink> result = client.createPaymentLink(paymentLink);
        assertTrue(result.isValid());
        assertNotNull(result.getItem().getUrl());
        assertEquals(todayPlus7Days, result.getItem().getExpirationDate());
    }

    @Test
    public void testCreateAndUpdatePaymentLink() {
        PaymentLink paymentLink = new PaymentLink();

        paymentLink.setAmount(new BigDecimal("1.00"));
        paymentLink.setCurrency("EUR");
        paymentLink.setDescription("Payment Link Description");

        Result<PaymentLink> createResult = client.createPaymentLink(paymentLink);
        assertTrue(createResult.isValid());

        PaymentLinkUpdate paymentLinkUpdate = new PaymentLinkUpdate();
        Date todayPlus10Days = new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * 10));
        paymentLinkUpdate.setExpirationDate(todayPlus10Days);
        paymentLinkUpdate.setEnabled(false);

        Result<PaymentLink> updateResult = client.updatePaymentLink(createResult.getItem().getId(), paymentLinkUpdate);
        assertTrue(updateResult.isValid());
        assertEquals(todayPlus10Days, updateResult.getItem().getExpirationDate());
        assertFalse(updateResult.getItem().getEnabled());
    }

    @Test
    public void testCreateAndDeletePaymentLink() {
        PaymentLink paymentLink = new PaymentLink();

        paymentLink.setAmount(new BigDecimal("1.00"));
        paymentLink.setCurrency("EUR");
        paymentLink.setDescription("Payment Link Description");

        Result<PaymentLink> createResult = client.createPaymentLink(paymentLink);
        assertTrue(createResult.isValid());

        Result<PaymentLink> deleteResult = client.deletePaymentLink(createResult.getItem().getId());
        assertTrue(deleteResult.isValid());
        assertEquals(createResult.getItem().getId(), deleteResult.getItem().getId());
        assertTrue(deleteResult.getItem().getDeleted());
    }

    @Test
    public void testGetPaymentLink() {
        PaymentLink paymentLink = new PaymentLink();
        paymentLink.setAmount(new BigDecimal("1.00"));
        paymentLink.setCurrency("EUR");
        paymentLink.setDescription("Payment Link Description");
        Result<PaymentLink> createResult = client.createPaymentLink(paymentLink);
        assertTrue(createResult.isValid());

        Result<PaymentLink> getResult = client.getPaymentLink(createResult.getItem().getId());
        assertEquals(getResult.getItem().getId(), createResult.getItem().getId());
    }

    @Test
    public void testGetAllChargebacks() {
        Result<List<Chargeback>> allChargebacks = client.getAllChargebacks();
        assertTrue(allChargebacks.isValid());
        int size = allChargebacks.getItem().size();
        assertTrue(size >= 1);
        assertTrue(size <= 10);
    }

    @Test
    public void testGetAllChargebacksWithLimit() {
        int limit = 3;
        Result<List<Chargeback>> allChargebacks = client.getAllChargebacks(limit);
        assertTrue(allChargebacks.isValid());
        int size = allChargebacks.getItem().size();
        assertTrue(size >= 1);
        assertTrue(size <= limit);
    }

    @Test
    public void testCreateApprovedPaymentNoDecimal() {
        Payment resultPayment = createApprovedPaymentJPY();
        assertEquals(APPROVED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
        assertEquals(TEST_PAYMENT_DESCRIPTION, resultPayment.getDescription());
        assertFalse(resultPayment.isThreedsV2());
        assertFalse(resultPayment.isThreedsV1());
    }

    @Test
    public void testCreateApprovedPaymentMoreDecimal() {
        Payment resultPayment = createApprovedPaymentKWD();
        assertEquals(APPROVED, resultPayment.getStatus());
        assertNotNull(resultPayment.getId());
        assertThat(resultPayment.getPaymentInstrument(), instanceOf(Card.class));
        assertFalse(resultPayment.getLive());
        assertEquals(TEST_PAYMENT_DESCRIPTION, resultPayment.getDescription());
        assertFalse(resultPayment.isThreedsV2());
        assertFalse(resultPayment.isThreedsV1());
    }

    private Payment createApprovedPayment() {
        Payment payment = getBaseCCPayment();
        Result<Payment> initialResult = client.createPayment(payment);
        assertTrue(initialResult.isValid());
        Payment resultPayment = initialResult.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());
        return resultPayment;
    }

    private Payment createApprovedPaymentJPY() {
        Payment payment = getBaseCCPayment("JPY", BigDecimal.valueOf(10).setScale(0, RoundingMode.HALF_EVEN));
        Result<Payment> initialResult = passthroughClient.createPayment(payment);
        assertTrue(initialResult.isValid());
        Payment resultPayment = initialResult.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());
        return resultPayment;
    }

    private Payment createApprovedPaymentKWD() {
        Payment payment = getBaseCCPayment("KWD", BigDecimal.valueOf(10.000).setScale(3, RoundingMode.HALF_EVEN));
        Result<Payment> initialResult = passthroughClient.createPayment(payment);
        assertTrue(initialResult.isValid());
        Payment resultPayment = initialResult.getItem();
        assertEquals(APPROVED, resultPayment.getStatus());
        return resultPayment;
    }

    private Payment createPendingPayment(Payment payment, boolean v2) {
        Result<Payment> result = client.createPayment(payment);
        assertTrue(result.isValid());
        Payment resultPayment = result.getItem();
        assertEquals(PENDING, resultPayment.getStatus());
        assertEquals(v2, resultPayment.isThreedsV2());
        assertEquals(!v2, resultPayment.isThreedsV1());
        if (v2) {
            assertNotNull(resultPayment.getThreeds2AuthorizationInformation());
            assertNotNull(resultPayment.getThreeds2AuthorizationInformation().getAcsUrl());
            assertNotNull(resultPayment.getThreeds2AuthorizationInformation().getCReq());
            assertNull(resultPayment.getAuthorizationInformation());
        } else {
            assertNotNull(resultPayment.getAuthorizationInformation());
            assertNotNull(resultPayment.getAuthorizationInformation().getUrl());
            assertNotNull(resultPayment.getAuthorizationInformation().getData());
            assertNull(resultPayment.getThreeds2AuthorizationInformation());
        }
        return resultPayment;
    }
}
