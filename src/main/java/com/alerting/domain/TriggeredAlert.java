package com.alerting.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.alerting.domain.enumeration.TriggeredStatus;

/**
 * A TriggeredAlert.
 */
@Entity
@Table(name = "triggered_alert")
public class TriggeredAlert implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TriggeredStatus status;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @Column(name = "target_url")
    private String targetUrl;

    @OneToMany(mappedBy = "triggeredAlert")
    private Set<AlertHistory> alertHistories = new HashSet<>();

    @OneToMany(mappedBy = "triggeredAlert")
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("triggeredAlerts")
    private AlertDefinition alertDefinition;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TriggeredStatus getStatus() {
        return status;
    }

    public TriggeredAlert status(TriggeredStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TriggeredStatus status) {
        this.status = status;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public TriggeredAlert dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public TriggeredAlert dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public TriggeredAlert targetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public Set<AlertHistory> getAlertHistories() {
        return alertHistories;
    }

    public TriggeredAlert alertHistories(Set<AlertHistory> alertHistories) {
        this.alertHistories = alertHistories;
        return this;
    }

    public TriggeredAlert addAlertHistory(AlertHistory alertHistory) {
        this.alertHistories.add(alertHistory);
        alertHistory.setTriggeredAlert(this);
        return this;
    }

    public TriggeredAlert removeAlertHistory(AlertHistory alertHistory) {
        this.alertHistories.remove(alertHistory);
        alertHistory.setTriggeredAlert(null);
        return this;
    }

    public void setAlertHistories(Set<AlertHistory> alertHistories) {
        this.alertHistories = alertHistories;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public TriggeredAlert comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public TriggeredAlert addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTriggeredAlert(this);
        return this;
    }

    public TriggeredAlert removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setTriggeredAlert(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public AlertDefinition getAlertDefinition() {
        return alertDefinition;
    }

    public TriggeredAlert alertDefinition(AlertDefinition alertDefinition) {
        this.alertDefinition = alertDefinition;
        return this;
    }

    public void setAlertDefinition(AlertDefinition alertDefinition) {
        this.alertDefinition = alertDefinition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TriggeredAlert)) {
            return false;
        }
        return id != null && id.equals(((TriggeredAlert) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TriggeredAlert{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            ", targetUrl='" + getTargetUrl() + "'" +
            "}";
    }
}
