package com.alerting.domain;

import java.io.Serializable;

public class AlertQuery implements Serializable {

    private String queryString;
    private String param;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
