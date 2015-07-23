package com.cardinity.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

public class Void {

    private UUID id;
    private Date created;
    private Boolean live;
    private UUID parentId;
    private Status status;
    private String error;
    private String orderId;
    private String description;

    public Void(String description) {
        this.description = description;
    }

    public Void() {
    }

    public enum Status {

        @SerializedName("approved")
        APPROVED("approved"),
        @SerializedName("declined")
        DECLINED("declined");

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
}
