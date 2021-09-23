package com.cardinity.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Payment {

    private UUID id;
    private BigDecimal amount;
    private String currency;
    private Date created;
    private Type type;
    private Boolean live;
    private String description;
    private String statementDescriptorSuffix;
    /**
     * @deprecated Unused and should be removed
     */
    @Deprecated
    private String authorizeData;
    private Boolean settle;
    private Status status;
    private String error;
    private String country;
    private String orderId;
    private Method paymentMethod;
    private PaymentInstrument paymentInstrument;
    private AuthorizationInformation authorizationInformation;
    private Threeds2Data threeds2Data;
    private Threeds2AuthorizationInformation threeds2AuthorizationInformation;

    public enum Status {

        @SerializedName("approved")
        APPROVED("approved"),
        @SerializedName("declined")
        DECLINED("declined"),
        @SerializedName("pending")
        PENDING("pending");

        private final String value;

        Status(final String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

    }

    public enum Method {
        @SerializedName("card")
        CARD("card"),
        @SerializedName("recurring")
        RECURRING("recurring");

        private final String value;

        Method(final String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

    }

    public enum Type {

        @SerializedName("purchase")
        PURCHASE("purchase"),
        @SerializedName("authorization")
        AUTHORIZATION("authorization");

        private final String value;

        Type(final String value) {
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
        this.amount = amount.setScale(2, BigDecimal.ROUND_DOWN);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getCreated() {
        return created;
    }

    public Type getType() {
        return type;
    }

    public Boolean getLive() {
        return live;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatementDescriptorSuffix() {
        return statementDescriptorSuffix;
    }

    public void setStatementDescriptorSuffix(String statementDescriptorSuffix) {
        this.statementDescriptorSuffix = statementDescriptorSuffix;
    }

    /**
     * @deprecated method is deprecated and shouldn't be used.
     * @param authorizeData
     */
    @Deprecated
    public void setAuthorizeData(String authorizeData) {
        this.authorizeData = authorizeData;
    }

    public void setSettle(Boolean settle) {
        this.settle = settle;
    }

    public Status getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public Method getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Method paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentInstrument getPaymentInstrument() {
        return paymentInstrument;
    }

    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    /**
     * @deprecated method is deprecated and shouldn't be used.
     * @return
     */
    @Deprecated
    public String getAuthorizeData() {
        return authorizeData;
    }

    public AuthorizationInformation getAuthorizationInformation() {
        return authorizationInformation;
    }

    public Threeds2Data getThreeds2Data() {
        return threeds2Data;
    }

    public void setThreeds2Data(Threeds2Data threeds2Data) {
        this.threeds2Data = threeds2Data;
    }

    public Threeds2AuthorizationInformation getThreeds2AuthorizationInformation() {
        return threeds2AuthorizationInformation;
    }

    public void setThreeds2AuthorizationInformation(Threeds2AuthorizationInformation threeds2AuthorizationInformation) {
        this.threeds2AuthorizationInformation = threeds2AuthorizationInformation;
    }

    public boolean isThreedsV1() {
        return authorizationInformation != null;
    }

    public boolean isThreedsV2() {
        return threeds2AuthorizationInformation != null;
    }
}
