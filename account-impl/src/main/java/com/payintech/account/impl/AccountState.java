package com.payintech.account.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.Jsonable;
import com.payintech.account.api.Account;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

/**
 * AccountState.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
@Immutable
@JsonDeserialize
public final class AccountState implements Jsonable {

    /**
     * The Account.
     */
    public final Optional<Account> account;

    /**
     * Instantiates a new Account state.
     *
     * @param account the account
     */
    @JsonCreator
    public AccountState(final Optional<Account> account) {
        this.account = account;
    }
}
