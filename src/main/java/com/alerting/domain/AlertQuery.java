package com.alerting.domain;


import java.util.List;
import java.io.Serializable;
import java.time.Instant;

public class AlertQuery implements Serializable {

    private String queryString;
    private List<String> users;
    private boolean runQuery;
    private String param;
    private Instant from;
    private Instant to;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }


    public boolean isRunQuery() {
        return runQuery;
    }

    public void setRunQuery(boolean runQuery) {
        this.runQuery = runQuery;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }


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
