package org.jboss.hsintegtest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;
import org.hibernate.search.annotations.Store;

/**
 *
 * https://github.com/Sanne/hibernate-search/blob/dc4692cd232a33d31fe963938efb51106eed7d52/hibernate-search-infinispan/src/test/java/org/hibernate/search/infinispan/SimpleEmail.java
 */
@Entity
@Indexed(index = "emails")
@ProvidedId
public class SimpleEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long id;

    @Field(analyze = Analyze.NO)
    @Column(name = "recipient")
    public String to = "";

    public String message = "";

    @Override
    public String toString() {
        return to + " - " + message;
    }

    @Id
    @GeneratedValue
    @Field(store = Store.YES)
    public Long getId() {
        return id;
    }

    @Field(store = Store.NO)
    @Column(name = "message")
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
