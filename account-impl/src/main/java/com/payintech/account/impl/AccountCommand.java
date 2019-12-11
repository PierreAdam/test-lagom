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

    /**
     * The Create Account Command.
     */
    @Immutable
    @JsonDeserialize
    public final class CreateAccount implements AccountCommand, PersistentEntity.ReplyType<Account> {
        /**
         * The Account to create.
         */
        public final Account account;

        /**
         * Instantiates a new Create account.
         *
         * @param account the account
         */
        @JsonCreator
        public CreateAccount(final Account account) {
            this.account = account;
        }
    }

    /**
     * The Update Account Command.
     */
    @Immutable
    @JsonDeserialize
    public final class UpdateAccount implements AccountCommand, PersistentEntity.ReplyType<Account> {
        /**
         * The Account to update.
         */
        public final Account account;

        /**
         * Instantiates a new Update account.
         *
         * @param account the account
         */
        @JsonCreator
        public UpdateAccount(final Account account) {
            this.account = account;
        }
    }

    /**
     * The Get Account Command.
     */
    @Immutable
    @JsonDeserialize
    public final class GetAccount implements AccountCommand, PersistentEntity.ReplyType<GetAccountReply> {
    }

    /**
     * The return class of the Get Account Command {@link GetAccount}
     */
    @Immutable
    @JsonDeserialize
    public final class GetAccountReply implements Jsonable {
        /**
         * The requested Account.
         */
        public final Optional<Account> account;

        /**
         * Instantiates a new Get account reply.
         *
         * @param account the account
         */
        @JsonCreator
        public GetAccountReply(final Optional<Account> account) {
            this.account = Preconditions.checkNotNull(account, "account");
        }
    }

    /**
     * The Delete Account Command.
     */
    @Immutable
    @JsonDeserialize
    public class DeleteAccount implements AccountCommand, PersistentEntity.ReplyType<Done> {
    }
}
