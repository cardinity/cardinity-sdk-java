package com.cardinity.model;

public class Threeds2Data {
    private String notificationUrl;
    private BrowserInfo browserInfo;
    private Address billingAddress;
    private Address deliveryAddress;
    private CardholderInfo cardholderInfo;

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public BrowserInfo getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(BrowserInfo browserInfo) {
        this.browserInfo = browserInfo;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public CardholderInfo getCardholderInfo() {
        return cardholderInfo;
    }

    public void setCardholderInfo(CardholderInfo cardholderInfo) {
        this.cardholderInfo = cardholderInfo;
    }
}
