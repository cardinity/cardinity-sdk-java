package com.cardinity.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class PaymentLink {
    private UUID id;
    private String url;
    private BigDecimal amount;
    private String currency;
    private String country;
    private String description;
    private String orderId;
    private Date expirationDate;
    private Boolean multipleUse;
    private Boolean enabled;

    public UUID getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getMultipleUse() {
        return multipleUse;
    }

    public void setMultipleUse(Boolean multipleUse) {
        this.multipleUse = multipleUse;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
