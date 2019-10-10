package com.payintech.account.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.annotation.concurrent.Immutable;

/**
 * Account.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
@Immutable
@JsonDeserialize
public class Account {

    public final String id;

    public final String name;

    @JsonCreator
    public Account(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name) {
        this.id = id;
        this.name = name;
    }
}
