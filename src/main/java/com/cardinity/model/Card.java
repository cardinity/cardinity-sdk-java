package com.cardinity.model;

public class Card implements PaymentInstrument {

    private String cardBrand;
    private String pan;
    private Integer expYear;
    private Integer expMonth;
    private Integer cvc;
    private String holder;

    public String getCardBrand() {
        return cardBrand;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public void setExpYear(Integer expYear) {
        this.expYear = expYear;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Integer expMonth) {
        this.expMonth = expMonth;
    }

    public void setCvc(Integer cvc) {
        this.cvc = cvc;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public Integer getCvc() {
        return cvc;
    }

    @Override
    public Payment.Method getType() {
        return Payment.Method.CARD;
    }
}
