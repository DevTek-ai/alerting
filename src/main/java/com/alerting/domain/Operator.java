package com.alerting.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Operator.
 */
@Entity
@Table(name = "operator")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @OneToOne
    @JoinColumn(unique = true)
    private Binary binary;

    @OneToOne
    @JoinColumn(unique = true)
    private Unary unary;

    @OneToOne(mappedBy = "operator")
    @JsonIgnore
    private Statement statement;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Operator type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Binary getBinary() {
        return binary;
    }

    public Operator binary(Binary binary) {
        this.binary = binary;
        return this;
    }

    public void setBinary(Binary binary) {
        this.binary = binary;
    }

    public Unary getUnary() {
        return unary;
    }

    public Operator unary(Unary unary) {
        this.unary = unary;
        return this;
    }

    public void setUnary(Unary unary) {
        this.unary = unary;
    }

    public Statement getStatement() {
        return statement;
    }

    public Operator statement(Statement statement) {
        this.statement = statement;
        return this;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Operator)) {
            return false;
        }
        return id != null && id.equals(((Operator) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Operator{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
