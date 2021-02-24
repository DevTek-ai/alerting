package com.alerting.domain;

public class GraphCategory {
    private Long critical;
    private Long warning;
    private Long info;

    public Long getCritical() {
        return critical;
    }

    public void setCritical(Long critical) {
        this.critical = critical;
    }

    public Long getWarning() {
        return warning;
    }

    public void setWarning(Long warning) {
        this.warning = warning;
    }

    public Long getInfo() {
        return info;
    }

    public void setInfo(Long info) {
        this.info = info;
    }
}
