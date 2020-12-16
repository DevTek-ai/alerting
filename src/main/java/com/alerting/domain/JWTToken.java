package com.alerting.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class JWTToken implements Serializable {

    private String idToken;

    JWTToken(){

    }

    JWTToken(String idToken) {
        this.idToken = idToken;
    }

    @JsonProperty("id_token")
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
