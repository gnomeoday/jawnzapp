package com.jawnz.app.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.jawnz.app.domain.Thing1} entity.
 */
public class Thing1DTO implements Serializable {
    
    private String id;

    private String name;

    private Integer age;

    
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thing1DTO)) {
            return false;
        }

        return id != null && id.equals(((Thing1DTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Thing1DTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            "}";
    }
}
