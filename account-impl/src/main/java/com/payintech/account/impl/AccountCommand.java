package com.payintech.account.impl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import com.payintech.account.api.Account;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

/**
 * AccountCommand.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
public interface AccountCommand extends Jsonable {

    @Immutable
    @JsonDeserialize
    public final class CreateAccount implements AccountCommand, PersistentEntity.ReplyType<Done> {
        public final Account account;

        @JsonCreator
        public CreateAccount(final Account account) {
            this.account = account;
        }
    }

    @Immutable
    @JsonDeserialize
    public final class GetAccount implements AccountCommand, PersistentEntity.ReplyType<GetAccountReply> {
    }

    @Immutable
    @JsonDeserialize
    public final class GetAccountReply implements Jsonable {
        public final Optional<Account> account;

        @JsonCreator
        public GetAccountReply(final Optional<Account> account) {
            this.account = Preconditions.checkNotNull(account, "account");
        }
    }
}
