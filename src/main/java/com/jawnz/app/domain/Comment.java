package com.jawnz.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Comment.
 */
@Document(collection = "comment")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 250)
    @Pattern(regexp = "(^[a-zA-Z0-9 #!£$%?,.%&+\"'@]*$)")
    @Field("text")
    private String text;

    @Field("created")
    private ZonedDateTime created;

    @DBRef
    @Field("product")
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Product product;

    @DBRef
    @Field("parent")
    private Comment parent;

    @DBRef
    @Field("tag")
    private Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Comment text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public Comment created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Product getProduct() {
        return product;
    }

    public Comment product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Comment getParent() {
        return parent;
    }

    public Comment parent(Comment comment) {
        this.parent = comment;
        return this;
    }

    public void setParent(Comment comment) {
        this.parent = comment;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Comment tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Comment addTag(Tag tag) {
        this.tags.add(tag);
        tag.setComment(this);
        return this;
    }

    public Comment removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setComment(null);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", created='" + getCreated() + "'" +
            "}";
    }
}
