package com.alerting.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A AlertHistory.
 */
@Entity
@Table(name = "alert_history")
public class AlertHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "message")
    private String message;

    @Column(name = "email_status")
    private Boolean emailStatus;

    @Column(name = "web_socket_status")
    private Boolean webSocketStatus;

    @Column(name = "sms_status")
    private Boolean smsStatus;

    @Column(name = "web_sockect_read")
    private Boolean webSockectRead;

    @Column(name = "retry_attempts")
    private Integer retryAttempts;

    @Column(name = "error_log")
    private String errorLog;

    @Column(name = "receipient_email")
    private String receipientEmail;

    @Column(name = "user_id")
    private Long userId;

    @Column(name="category")
    private Integer category;

    @Column(name="created_date")
    private Instant dateCreated;

    @Column(name="modified_date")
    private Instant dateModified;

    @Column(name="triggered_id")
    private Long triggeredId;

    @Column(name="triggered_type")
    private String triggeredType;

    @Column(name="user_login")
    private String login;

    @Column(name="attribute")
    private String attribute;
    @Column(name="behaviour")
    private String behaviour;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getTriggeredId() {
        return triggeredId;
    }

    public void setTriggeredId(Long triggeredId) {
        this.triggeredId = triggeredId;
    }

    public String getTriggeredType() {
        return triggeredType;
    }

    public void setTriggeredType(String triggeredType) {
        this.triggeredType = triggeredType;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    @ManyToOne
    @JsonIgnoreProperties("alertHistories")
    private TriggeredAlert triggeredAlert;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public AlertHistory subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public AlertHistory message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isEmailStatus() {
        return emailStatus;
    }

    public AlertHistory emailStatus(Boolean emailStatus) {
        this.emailStatus = emailStatus;
        return this;
    }

    public void setEmailStatus(Boolean emailStatus) {
        this.emailStatus = emailStatus;
    }

    public Boolean isWebSocketStatus() {
        return webSocketStatus;
    }

    public AlertHistory webSocketStatus(Boolean webSocketStatus) {
        this.webSocketStatus = webSocketStatus;
        return this;
    }

    public void setWebSocketStatus(Boolean webSocketStatus) {
        this.webSocketStatus = webSocketStatus;
    }

    public Boolean isSmsStatus() {
        return smsStatus;
    }

    public AlertHistory smsStatus(Boolean smsStatus) {
        this.smsStatus = smsStatus;
        return this;
    }

    public void setSmsStatus(Boolean smsStatus) {
        this.smsStatus = smsStatus;
    }

    public Boolean isWebSockectRead() {
        return webSockectRead;
    }

    public AlertHistory webSockectRead(Boolean webSockectRead) {
        this.webSockectRead = webSockectRead;
        return this;
    }

    public void setWebSockectRead(Boolean webSockectRead) {
        this.webSockectRead = webSockectRead;
    }

    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    public AlertHistory retryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
        return this;
    }

    public void setRetryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public AlertHistory errorLog(String errorLog) {
        this.errorLog = errorLog;
        return this;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getReceipientEmail() {
        return receipientEmail;
    }

    public AlertHistory receipientEmail(String receipientEmail) {
        this.receipientEmail = receipientEmail;
        return this;
    }

    public void setReceipientEmail(String receipientEmail) {
        this.receipientEmail = receipientEmail;
    }

    public Long getUserId() {
        return userId;
    }

    public AlertHistory userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TriggeredAlert getTriggeredAlert() {
        return triggeredAlert;
    }

    public AlertHistory triggeredAlert(TriggeredAlert triggeredAlert) {
        this.triggeredAlert = triggeredAlert;
        return this;
    }

    public void setTriggeredAlert(TriggeredAlert triggeredAlert) {
        this.triggeredAlert = triggeredAlert;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertHistory)) {
            return false;
        }
        return id != null && id.equals(((AlertHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AlertHistory{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", message='" + getMessage() + "'" +
            ", emailStatus='" + isEmailStatus() + "'" +
            ", webSocketStatus='" + isWebSocketStatus() + "'" +
            ", smsStatus='" + isSmsStatus() + "'" +
            ", webSockectRead='" + isWebSockectRead() + "'" +
            ", retryAttempts=" + getRetryAttempts() +
            ", errorLog='" + getErrorLog() + "'" +
            ", receipientEmail='" + getReceipientEmail() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
