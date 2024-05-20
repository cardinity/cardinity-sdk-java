package com.cardinity.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;

public class Rdr {

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
    private String paymentMethod;
    private String reasonCode;
    private String reasonMessage;
    private String caseId;
    private String arn;

    public enum Status {
        @SerializedName("approved")
        APPROVED("approved"),
        @SerializedName("declined")
        DECLINED("declined"),
        @SerializedName("initiated")
        INITIATED("initiated");

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

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.DOWN);
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonMessage() {
        return reasonMessage;
    }

    public void setReasonMessage(String reasonMessage) {
        this.reasonMessage = reasonMessage;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getArn() {
        return arn;
    }

    public void setArn(String arn) {
        this.arn = arn;
    }
}
