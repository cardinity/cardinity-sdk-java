package com.cardinity.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Refund {

    private UUID id;
    private BigDecimal amount;
    private String currency;
    private Date created;
    private Boolean live;
    private UUID parentId;
    private Status status;
    private String error;
    private String orderId;
    private String description;

    public Refund(BigDecimal amount, String description) {
        this.amount = MoneyUtil.formatAmount(amount);
        this.description = description;
    }

    public Refund(BigDecimal amount) {
        this.amount = MoneyUtil.formatAmount(amount);
    }

    public Refund() {
    }

    public enum Status {

        @SerializedName("approved")
        APPROVED("approved"),
        @SerializedName("declined")
        DECLINED("declined"),
        @SerializedName("processing")
        PROCESSING("processing");

        private final String value;

        Status(final String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getCreated() {
        return created;
    }

    public Boolean getLive() {
        return live;
    }

    public UUID getParentId() {
        return parentId;
    }

    public Status getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = MoneyUtil.formatAmount(amount);
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
