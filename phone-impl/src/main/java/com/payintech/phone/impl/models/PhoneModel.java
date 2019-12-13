package com.payintech.phone.impl.models;

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
@Table(name = "phone")
public class PhoneModel extends Model {

    /**
     * Helpers to request model.
     */
    public static final Finder<Long, PhoneModel> find = new Finder<>(PhoneModel.class);

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

    @Column(name = "account_uid", nullable = false, unique = false)
    private UUID accountUid;

    @Column(name = "phone_number", nullable = false, unique = false, length = 20)
    private String phoneNumber;

    public Long getId() {
        return this.id;
    }

    public UUID getAccountUid() {
        return this.accountUid;
    }

    public void setAccountUid(final UUID accountUid) {
        this.accountUid = accountUid;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
