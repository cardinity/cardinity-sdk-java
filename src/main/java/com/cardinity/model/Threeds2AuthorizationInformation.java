package com.cardinity.model;

import com.google.gson.annotations.SerializedName;

public class Threeds2AuthorizationInformation {
    private String acsUrl;
    @SerializedName("creq")
    private String cReq;

    public String getAcsUrl() {
        return acsUrl;
    }

    public void setAcsUrl(String acsUrl) {
        this.acsUrl = acsUrl;
    }

    public String getCReq() {
        return cReq;
    }

    public void setCReq(String cReq) {
        this.cReq = cReq;
    }
}
