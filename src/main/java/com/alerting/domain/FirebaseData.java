package com.alerting.domain;

public class FirebaseData {
    private String to;
    private FirebaseDataDetail data;

    public FirebaseData() {
    }
    public FirebaseData(String to, FirebaseDataDetail data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public FirebaseDataDetail getData() {
        return data;
    }

    public void setData(FirebaseDataDetail data) {
        this.data = data;
    }
}
