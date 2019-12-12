package com.payintech.phone.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;
import java.util.UUID;

/**
 * Account.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
@Immutable
@JsonDeserialize
public class Phone {

    public UUID uid;

    public UUID accountUid;

    public String phoneNumber;

    @JsonCreator
    public Phone(
            @JsonProperty(value = "uid", required = false) final UUID uid,
            @JsonProperty(value = "accountUid", required = true) final UUID accountUid,
            @JsonProperty(value = "phoneNumber", required = true) final String phoneNumber) {
        this.uid = uid;
        this.accountUid = accountUid;
        this.phoneNumber = phoneNumber;
    }
}
