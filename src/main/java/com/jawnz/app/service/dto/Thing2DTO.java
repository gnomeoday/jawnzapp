package com.jawnz.app.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.jawnz.app.domain.Thing2} entity.
 */
public class Thing2DTO implements Serializable {
    
    private String id;

    private String name;

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thing2DTO)) {
            return false;
        }

        return id != null && id.equals(((Thing2DTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Thing2DTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
