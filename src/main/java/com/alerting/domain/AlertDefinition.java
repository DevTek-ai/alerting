package com.alerting.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.alerting.domain.enumeration.TriggerType;

import com.alerting.domain.enumeration.Category;

/**
 * A AlertDefinition.
 */
@Entity
@Table(name = "alert_definition")
public class AlertDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type")
    private TriggerType triggerType;

    @Column(name = "notify")
    private Boolean notify;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @Column(name = "query_statement")
    private String alertRuleQuery;

    @Column(name="type_selection")
    private String typeSelection;

    @Column(name="behaviour_selection")
    private String behaviourSelection;

    @Column(name="condition_selection")
    private String conditionSelection;

    @Column(name="custom_attribute_selection")
    private String customattributeSelection;

    @Column(name="from_date")
    private Instant fromDate;

    @Column(name="to_date")
    private Instant toDate;


    public String getCustomAttributeSelection() {
        return customattributeSelection;
    }

    public void setCustomAttributeSelection(String customAttributeSelection) {
        this.customattributeSelection = customAttributeSelection;
    }

    public Instant getFrom() {
        return fromDate;
    }

    public void setFrom(Instant from) {
        this.fromDate = from;
    }

    public Instant getTo() {
        return toDate;
    }

    public void setTo(Instant to) {
        this.toDate = to;
    }

    public String getBehaviourSelection() {
        return behaviourSelection;
    }

    public void setBehaviourSelection(String behaviourSelection) {
        this.behaviourSelection = behaviourSelection;
    }

    public String getConditionSelection() {
        return conditionSelection;
    }

    public void setConditionSelection(String conditionSelection) {
        this.conditionSelection = conditionSelection;
    }

    public String getTypeSelection() {
        return typeSelection;
    }

    public void setTypeSelection(String typeSelection) {
        this.typeSelection = typeSelection;
    }

    public String getAttributeSelection() {
        return attributeSelection;
    }

    public void setAttributeSelection(String attributeSelection) {
        this.attributeSelection = attributeSelection;
    }

    @Column(name="attribute_selection")
    private String attributeSelection;

    @OneToOne
    @JoinColumn(unique = true)
    private Schedular schedular;

    @OneToOne
    @JoinColumn(unique = true)
    private Statement statement;

    @OneToMany(mappedBy = "alertDefinition")
    private Set<TriggeredAlert> triggeredAlerts = new HashSet<>();

    @OneToMany(mappedBy = "alertDefinition")
    private Set<UserType> userTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public AlertDefinition title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public AlertDefinition message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public AlertDefinition triggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
        return this;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public Boolean isNotify() {
        return notify;
    }

    public AlertDefinition notify(Boolean notify) {
        this.notify = notify;
        return this;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public Category getCategory() {
        return category;
    }

    public AlertDefinition category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public AlertDefinition dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateUpdated() {
        return dateUpdated;
    }

    public AlertDefinition dateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    public void setDateUpdated(Instant dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Schedular getSchedular() {
        return schedular;
    }

    public AlertDefinition schedular(Schedular schedular) {
        this.schedular = schedular;
        return this;
    }

    public void setSchedular(Schedular schedular) {
        this.schedular = schedular;
    }

    public Statement getStatement() {
        return statement;
    }

    public AlertDefinition statement(Statement statement) {
        this.statement = statement;
        return this;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Set<TriggeredAlert> getTriggeredAlerts() {
        return triggeredAlerts;
    }

    public AlertDefinition triggeredAlerts(Set<TriggeredAlert> triggeredAlerts) {
        this.triggeredAlerts = triggeredAlerts;
        return this;
    }

    public AlertDefinition addTriggeredAlert(TriggeredAlert triggeredAlert) {
        this.triggeredAlerts.add(triggeredAlert);
        triggeredAlert.setAlertDefinition(this);
        return this;
    }

    public AlertDefinition removeTriggeredAlert(TriggeredAlert triggeredAlert) {
        this.triggeredAlerts.remove(triggeredAlert);
        triggeredAlert.setAlertDefinition(null);
        return this;
    }

    public void setTriggeredAlerts(Set<TriggeredAlert> triggeredAlerts) {
        this.triggeredAlerts = triggeredAlerts;
    }

    public Set<UserType> getUserTypes() {
        return userTypes;
    }

    public AlertDefinition userTypes(Set<UserType> userTypes) {
        this.userTypes = userTypes;
        return this;
    }

    public AlertDefinition addUserType(UserType userType) {
        this.userTypes.add(userType);
        userType.setAlertDefinition(this);
        return this;
    }

    public AlertDefinition removeUserType(UserType userType) {
        this.userTypes.remove(userType);
        userType.setAlertDefinition(null);
        return this;
    }

    public void setUserTypes(Set<UserType> userTypes) {
        this.userTypes = userTypes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlertDefinition)) {
            return false;
        }
        return id != null && id.equals(((AlertDefinition) o).id);
    }

    public String getAlertRuleQuery() {
        return alertRuleQuery;
    }

    public void setAlertRuleQuery(String alertRuleQuery) {
        this.alertRuleQuery = alertRuleQuery;
    }


    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AlertDefinition{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", message='" + getMessage() + "'" +
            ", triggerType='" + getTriggerType() + "'" +
            ", notify='" + isNotify() + "'" +
            ", category='" + getCategory() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateUpdated='" + getDateUpdated() + "'" +
            "}";
    }
}
