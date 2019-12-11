package com.payintech.account.impl.models;

import com.payintech.account.api.Account;
import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

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

    /**
     * The Id.
     */
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * The Uid.
     */
    @Column(name = "uid", nullable = false, unique = true)
    private UUID uid;

    /**
     * The Name.
     */
    @Column(name = "name", nullable = false, unique = false, length = 40)
    private String name;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public UUID getUid() {
        return this.uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(final UUID uid) {
        this.uid = uid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * As account account.
     *
     * @return the account
     */
    public Account asAccount() {
        return new Account(this.uid, this.name);
    }
}
