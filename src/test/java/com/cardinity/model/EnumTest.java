package com.cardinity.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnumTest {

    @Test
    public void testPaymentEnums() throws Exception {

        assertEquals("approved", Payment.Status.APPROVED.getValue());
        assertEquals("declined", Payment.Status.DECLINED.getValue());
        assertEquals("pending", Payment.Status.PENDING.getValue());

        assertEquals("recurring", Payment.Method.RECURRING.getValue());
        assertEquals("card", Payment.Method.CARD.getValue());

        assertEquals("authorization", Payment.Type.AUTHORIZATION.getValue());
        assertEquals("purchase", Payment.Type.PURCHASE.getValue());
    }

    @Test
    public void testRefundEnum() throws Exception {

        assertEquals("approved", Refund.Status.APPROVED.getValue());
        assertEquals("declined", Refund.Status.DECLINED.getValue());
    }

    @Test
    public void testSettlementEnum() throws Exception {

        assertEquals("approved", Settlement.Status.APPROVED.getValue());
        assertEquals("declined", Settlement.Status.DECLINED.getValue());
    }

    @Test
    public void testVoidEnum() throws Exception {

        assertEquals("approved", Void.Status.APPROVED.getValue());
        assertEquals("declined", Void.Status.DECLINED.getValue());
    }
}
