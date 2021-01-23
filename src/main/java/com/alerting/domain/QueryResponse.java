package com.alerting.domain;

import java.util.List;

public class QueryResponse {
    private boolean status;
    private String errorMessage;
    private String data;
    private List<UserForAlert> userForAlerts;

    public List<UserForAlert> getUserForAlerts() {
        return userForAlerts;
    }

    public void setUserForAlerts(List<UserForAlert> userForAlerts) {
        this.userForAlerts = userForAlerts;
    }

// Getter Methods

    public boolean getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getData() {
        return data;
    }

    // Setter Methods

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setData(String data) {
        this.data = data;
    }
}
