package com.payintech.account.impl.models;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * AccountModel.
 *
 * @author Pierre Adam
 * @since 19.10.14
 */
@Entity
@Table(name = "account")
public class AccountModel extends Model {

    /**
     * Helpers to request model.
     */
    public static final Finder<Long, AccountModel> find = new Finder<>(AccountModel.class);

    //    @Size(max = 125)
    @Column(name = "id", nullable = false, unique = false)
    private String id;

    //    @Size(max = 125)
    @Column(name = "name", nullable = false, unique = false)
    private String name;

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
