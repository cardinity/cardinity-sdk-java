package com.cardinity.model;

import com.cardinity.CardinityBaseTest;
import com.cardinity.rest.RestResource;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SerializationTest extends CardinityBaseTest {
    @Test
    public void testSerializePaymentWithThreeds2Data() throws IOException {
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(20));
        payment.setCurrency("EUR");
        payment.setCountry("LT");
        payment.setSettle(true);
        payment.setOrderId("ord123456");
        payment.setDescription("some description");
        payment.setPaymentMethod(Payment.Method.CARD);

        Card card = new Card();
        card.setPan("4111111111111111");
        card.setCvc(123);
        card.setExpYear(2021);
        card.setExpMonth(1);
        card.setHolder("John Doe");
        payment.setPaymentInstrument(card);

        Threeds2Data threeds2Data = new Threeds2Data();
        threeds2Data.setNotificationUrl("http://notification.url");
        payment.setThreeds2Data(threeds2Data);

        BrowserInfo browserInfo = new BrowserInfo();
        browserInfo.setAcceptHeader("text/html");
        browserInfo.setBrowserLanguage("en-US");
        browserInfo.setScreenWidth(1920);
        browserInfo.setScreenHeight(1040);
        browserInfo.setChallengeWindowSize(BrowserInfo.ChallengeWindowSize.SIZE_500X600);
        browserInfo.setUserAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:21.0) Gecko/20100101 Firefox/21.0");
        browserInfo.setColorDepth(24);
        browserInfo.setTimeZone(-60);
        browserInfo.setIpAddress("216.58.207.35");
        browserInfo.setJavaEnabled(true);
        browserInfo.setJavascriptEnabled(false);
        threeds2Data.setBrowserInfo(browserInfo);

        Address billingAddress = new Address();
        billingAddress.setAddressLine1("8239 Louie Street");
        billingAddress.setAddressLine2("line2");
        billingAddress.setAddressLine3("line3");
        billingAddress.setCity("Coltenburgh");
        billingAddress.setCountry("USA");
        billingAddress.setPostalCode("84603");
        billingAddress.setState("DC");
        threeds2Data.setBillingAddress(billingAddress);

        Address deliveryAddress = new Address();
        deliveryAddress.setAddressLine1("Schallerallee 33");
        deliveryAddress.setCity("Ravensburg");
        deliveryAddress.setCountry("DEU");
        deliveryAddress.setPostalCode("82940");
        threeds2Data.setDeliveryAddress(deliveryAddress);

        CardholderInfo cardholderInfo = new CardholderInfo();
        cardholderInfo.setEmailAddress("card.holder@example.com");
        cardholderInfo.setMobilePhoneNumber("+353209120599");
        cardholderInfo.setHomePhoneNumber("+353209174412");
        cardholderInfo.setWorkPhoneNumber("+353209134251");
        threeds2Data.setCardholderInfo(cardholderInfo);

        assertEquals(resource("payment_with_threeds2_data.json"), RestResource.GSON.toJson(payment));
    }
}
