package com.alerting.domain;

import java.io.Serializable;

public class UserForAlert implements Serializable {
    private String login;
    private String firebaseToken;

    public UserForAlert() {
    }

    public UserForAlert(String login, String firebaseToken) {
        this.login = login;
        this.firebaseToken = firebaseToken;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
