package com.alerting.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Statement.
 */
@Entity
@Table(name = "statement")
public class Statement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Operand operand;

    @OneToOne
    @JoinColumn(unique = true)
    private Operator operator;

    @OneToOne
    @JoinColumn(unique = true)
    private Binary binary;

    @OneToOne(mappedBy = "statement")
    @JsonIgnore
    private AlertDefinition alertDefinition;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operand getOperand() {
        return operand;
    }

    public Statement operand(Operand operand) {
        this.operand = operand;
        return this;
    }

    public void setOperand(Operand operand) {
        this.operand = operand;
    }

    public Operator getOperator() {
        return operator;
    }

    public Statement operator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Binary getBinary() {
        return binary;
    }

    public Statement binary(Binary binary) {
        this.binary = binary;
        return this;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }

    public AlertDefinition getAlertDefinition() {
        return alertDefinition;
    }

    public Statement alertDefinition(AlertDefinition alertDefinition) {
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
        if (!(o instanceof Statement)) {
            return false;
        }
        return id != null && id.equals(((Statement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Statement{" +
            "id=" + getId() +
            "}";
    }
}
