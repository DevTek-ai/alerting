package com.alerting.domain;

public class FirebaseDataDetail {

    private String sound;
    private String body;
    private Long alertHistoryId;
    private String title;
    private boolean content_available;
    private String priority;

    public FirebaseDataDetail() {

    }

    public FirebaseDataDetail(String sound, String body, Long alertHistoryId, String title, boolean content_available, String priority) {
        this.sound = sound;
        this.body = body;
        this.alertHistoryId = alertHistoryId;
        this.title = title;
        this.content_available = content_available;
        this.priority = priority;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getAlertHistoryId() {
        return alertHistoryId;
    }

    public void setAlertHistoryId(Long alertHistoryId) {
        this.alertHistoryId = alertHistoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isContent_available() {
        return content_available;
    }

    public void setContent_available(boolean content_available) {
        this.content_available = content_available;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
