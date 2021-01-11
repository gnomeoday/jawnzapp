package com.jawnz.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Thing2.
 */
@Document(collection = "thing2")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "thing2")
public class Thing2 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @DBRef
    @Field("child")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Thing1 child;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Thing2 name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Thing1 getChild() {
        return child;
    }

    public Thing2 child(Thing1 thing1) {
        this.child = thing1;
        return this;
    }

    public void setChild(Thing1 thing1) {
        this.child = thing1;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thing2)) {
            return false;
        }
        return id != null && id.equals(((Thing2) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Thing2{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
