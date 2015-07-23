package com.cardinity.model;

import java.util.Set;

public class CardinityError {

    private String type;
    private String title;
    private Integer status;
    private String detail;
    private Set<ErrorItem> errors;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public Set<ErrorItem> getErrorItems() {
        return errors;
    }

}
