package com.cardinity.model;

import com.cardinity.CardinityBaseTest;
import com.cardinity.exceptions.CardinityException;
import com.cardinity.rest.RestResource;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.UUID;

import static org.junit.Assert.*;

public class DeserializationTest extends CardinityBaseTest {

    @Test
    public void testDeserializeApprovedPayment() throws IOException, ParseException {
        String json = resource("approved_payment.json");
        Payment payment = RestResource.GSON.fromJson(json, Payment.class);

        assertEquals(UUID.fromString("90095d47-11bb-468b-8764-fd4fbb49a9f9"), payment.getId());
        assertEquals("EUR", payment.getCurrency());
        assertEquals(new BigDecimal("15.00"), payment.getAmount());
        assertEquals(formatterWithMillis.parse("2014-12-19T11:52:53.3Z"), payment.getCreated());
        assertFalse(payment.getLive());
        assertEquals(Payment.Type.AUTHORIZATION, payment.getType());
        assertEquals(Payment.Status.APPROVED, payment.getStatus());
        assertEquals("123456", payment.getOrderId());
        assertEquals("some description", payment.getDescription());
        assertEquals("LT", payment.getCountry());
        assertEquals(Payment.Method.CARD, payment.getPaymentMethod());
        assertNotNull(payment.getPaymentInstrument());
        assertEquals(Payment.Method.CARD, payment.getPaymentInstrument().getType());

        Card card = (Card) payment.getPaymentInstrument();
        assertEquals("1111", card.getPan());
        assertEquals("Mike Dough", card.getHolder());
        assertEquals("Visa", card.getCardBrand());
        assertEquals(Integer.valueOf(2016), card.getExpYear());
        assertEquals(Integer.valueOf(12), card.getExpMonth());
    }

    @Test
    public void testDeserializeDeclinedPayment() throws IOException, ParseException {
        String json = resource("declined_payment.json");
        Payment payment = RestResource.GSON.fromJson(json, Payment.class);

        assertEquals(UUID.fromString("3c4e8dcf-4e3e-4df0-9acb-622868c6c67b"), payment.getId());
        assertEquals("EUR", payment.getCurrency());
        assertEquals(new BigDecimal("150.00"), payment.getAmount());
        assertEquals(formatterWithMillis.parse("2015-01-07T12:23:11.12Z"), payment.getCreated());
        assertFalse(payment.getLive());
        assertEquals(Payment.Type.AUTHORIZATION, payment.getType());
        assertEquals(Payment.Status.DECLINED, payment.getStatus());
        assertEquals("3000: Do not Honor", payment.getError());
        assertEquals("12345678", payment.getOrderId());
        assertEquals("some description", payment.getDescription());
        assertEquals("LT", payment.getCountry());
        assertEquals(Payment.Method.CARD, payment.getPaymentMethod());
        assertNotNull(payment.getPaymentInstrument());
        assertEquals(Payment.Method.CARD, payment.getPaymentInstrument().getType());

        Card card = (Card) payment.getPaymentInstrument();
        assertEquals("0067", card.getPan());
        assertEquals("Mike Dough", card.getHolder());
        assertEquals("Visa", card.getCardBrand());
        assertEquals(Integer.valueOf(2016), card.getExpYear());
        assertEquals(Integer.valueOf(11), card.getExpMonth());
    }

    @Test
    public void testDeserializePendingPayment() throws IOException, ParseException {
        String json = resource("pending_payment.json");
        Payment payment = RestResource.GSON.fromJson(json, Payment.class);

        assertEquals(UUID.fromString("8e037fbb-fe5b-4781-b109-b3e93d5f2c0d"), payment.getId());
        assertEquals("EUR", payment.getCurrency());
        assertEquals(new BigDecimal("20.00"), payment.getAmount());
        assertEquals(formatterWithMillis.parse("2014-12-17T09:43:31.123Z"), payment.getCreated());
        assertFalse(payment.getLive());
        assertEquals(Payment.Type.AUTHORIZATION, payment.getType());
        assertEquals(Payment.Status.PENDING, payment.getStatus());
        assertEquals("1234567", payment.getOrderId());
        assertEquals("some description", payment.getDescription());
        assertEquals("LT", payment.getCountry());
        assertEquals(Payment.Method.CARD, payment.getPaymentMethod());
        assertNotNull(payment.getPaymentInstrument());
        assertEquals(Payment.Method.CARD, payment.getPaymentInstrument().getType());

        Card card = (Card) payment.getPaymentInstrument();
        assertEquals("4447", card.getPan());
        assertEquals("John Smith", card.getHolder());
        assertEquals("Visa", card.getCardBrand());
        assertEquals(Integer.valueOf(2017), card.getExpYear());
        assertEquals(Integer.valueOf(5), card.getExpMonth());

        assertNotNull(payment.getAuthorizationInformation());
        assertEquals("https://authorization.url/auth", payment.getAuthorizationInformation().getUrl());
        assertEquals("eJxdUl1vwjagsagsagsagasgasgsagasgsa", payment.getAuthorizationInformation().getData());
    }

    @Test(expected = CardinityException.class)
    public void testDeserializeInvalidMethodPayment() throws IOException {
        String json = resource("invalid_method_payment.json");
        RestResource.GSON.fromJson(json, Payment.class);
    }

    @Test
    public void testDeserializeApprovedRefund() throws IOException, ParseException {
        String json = resource("approved_refund.json");
        Refund refund = RestResource.GSON.fromJson(json, Refund.class);

        assertEquals(Refund.Status.APPROVED, refund.getStatus());
        assertEquals(UUID.fromString("25e6f869-6675-4488-bd47-ccd298f74b3f"), refund.getId());
        assertEquals("EUR", refund.getCurrency());
        assertEquals(new BigDecimal("0.50"), refund.getAmount());
        assertEquals(formatter.parse("2015-01-16T09:05:27Z"), refund.getCreated());
        assertFalse(refund.getLive());
        assertEquals(UUID.fromString("f02862aa-5dcc-4637-9ddf-b9140c2b4b06"), refund.getParentId());
    }

    @Test
    public void testDeserializeDeclinedRefund() throws IOException, ParseException {
        String json = resource("declined_refund.json");
        Refund refund = RestResource.GSON.fromJson(json, Refund.class);

        assertEquals(Refund.Status.DECLINED, refund.getStatus());
        assertNotNull(refund.getError());
        assertEquals("123456", refund.getOrderId());
        assertEquals(UUID.fromString("25e6f869-6675-4488-bd47-ccd298f74b3f"), refund.getId());
        assertEquals("EUR", refund.getCurrency());
        assertEquals(new BigDecimal("0.50"), refund.getAmount());
        assertEquals(formatter.parse("2015-01-16T09:05:27Z"), refund.getCreated());
        assertFalse(refund.getLive());
        assertEquals(UUID.fromString("f02862aa-5dcc-4637-9ddf-b9140c2b4b06"), refund.getParentId());
    }

    @Test
    public void testDeserializeApprovedSettlement() throws IOException, ParseException {
        String json = resource("approved_settlement.json");
        Settlement settlement = RestResource.GSON.fromJson(json, Settlement.class);

        assertEquals(Settlement.Status.APPROVED, settlement.getStatus());
        assertEquals(UUID.fromString("25e6f869-6675-4488-bd47-ccd298f74b3f"), settlement.getId());
        assertEquals("EUR", settlement.getCurrency());
        assertEquals(new BigDecimal("0.50"), settlement.getAmount());
        assertEquals(formatter.parse("2015-01-16T09:05:27Z"), settlement.getCreated());
        assertTrue(settlement.getLive());
        assertEquals(UUID.fromString("f02862aa-5dcc-4637-9ddf-b9140c2b4b06"), settlement.getParentId());
    }

    @Test
    public void testDeserializeDeclinedSettlement() throws IOException, ParseException {
        String json = resource("declined_settlement.json");
        Settlement settlement = RestResource.GSON.fromJson(json, Settlement.class);

        assertEquals(Settlement.Status.DECLINED, settlement.getStatus());
        assertNotNull(settlement.getError());
        assertEquals("123456", settlement.getOrderId());
        assertEquals(UUID.fromString("25e6f869-6675-4488-bd47-ccd298f74b3f"), settlement.getId());
        assertEquals("EUR", settlement.getCurrency());
        assertEquals(new BigDecimal("0.50"), settlement.getAmount());
        assertEquals(formatter.parse("2015-01-16T09:05:27Z"), settlement.getCreated());
        assertTrue(settlement.getLive());
        assertEquals(UUID.fromString("f02862aa-5dcc-4637-9ddf-b9140c2b4b06"), settlement.getParentId());
    }

    @Test
    public void testDeserializeApprovedVoid() throws IOException, ParseException {
        String json = resource("approved_void.json");
        Void voided = RestResource.GSON.fromJson(json, Void.class);

        assertEquals(Void.Status.APPROVED, voided.getStatus());
        assertEquals(UUID.fromString("25e6f869-6675-4488-bd47-ccd298f74b3f"), voided.getId());
        assertEquals(formatter.parse("2015-01-16T09:05:27Z"), voided.getCreated());
        assertFalse(voided.getLive());
        assertEquals(UUID.fromString("90ebe1a8-ebaf-48c5-b654-19c8e6ae5368"), voided.getParentId());
        assertEquals("some description", voided.getDescription());
    }

    @Test
    public void testDeserializeDeclinedVoid() throws IOException, ParseException {
        String json = resource("declined_void.json");
        Void voided = RestResource.GSON.fromJson(json, Void.class);

        assertEquals(Void.Status.DECLINED, voided.getStatus());
        assertNotNull(voided.getError());
        assertEquals("123456", voided.getOrderId());
        assertEquals(UUID.fromString("25e6f869-6675-4488-bd47-ccd298f74b3f"), voided.getId());
        assertEquals(formatter.parse("2015-01-16T09:05:27Z"), voided.getCreated());
        assertFalse(voided.getLive());
        assertEquals(UUID.fromString("90ebe1a8-ebaf-48c5-b654-19c8e6ae5368"), voided.getParentId());
        assertEquals("some description", voided.getDescription());
    }

    @Test
    public void testDeserializeRestException() throws IOException {

        String json = resource("validation_error.json");
        CardinityError cardinityError = RestResource.GSON.fromJson(json, CardinityError.class);
        assertEquals("https://developers.cardinity.com/api/v1/#400", cardinityError.getType());
        assertEquals("Validation Failed", cardinityError.getTitle());
        assertEquals(Integer.valueOf(400), cardinityError.getStatus());
        assertEquals("The content you've send contains 2 validation errors.", cardinityError.getDetail());
        assertNotNull(cardinityError.getErrorItems());
        assertEquals(2, cardinityError.getErrorItems().size());
    }

    @Test
    public void testDeserializeError() throws IOException {

        String json = resource("validation_error.json");
        CardinityError cardinityError = RestResource.GSON.fromJson(json, CardinityError.class);
        assertNotNull(cardinityError.getErrorItems());
        assertEquals(2, cardinityError.getErrorItems().size());
        ErrorItem item = cardinityError.getErrorItems().iterator().next();
        assertNotNull(item.getMessage());
        assertNotNull(item.getField());
        assertNotNull(item.getRejected());
    }

}
