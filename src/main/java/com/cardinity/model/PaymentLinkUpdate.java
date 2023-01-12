package com.cardinity.model;

import java.util.Date;

public class PaymentLinkUpdate {
    private Date expirationDate;
    private Boolean enabled;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
