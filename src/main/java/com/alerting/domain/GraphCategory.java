package com.alerting.domain;

public class GraphCategory {
    private Long error = 0L;
    private Long warning = 0L;
    private Long info = 0L;

    public Long getError() {
        return error;
    }

    public void setError(Long error) {
        this.error = error;
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
