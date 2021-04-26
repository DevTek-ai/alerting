package com.alerting.domain;

public class Lookup {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Lookup(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    private String value;
}
