package com.payintech.account.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;
import java.util.UUID;

/**
 * AccountEvent.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
public interface AccountEvent extends Jsonable, AggregateEvent<AccountEvent> {

    @Override
    default public AggregateEventTag<AccountEvent> aggregateTag() {
        return AggregateEventTag.of(AccountEvent.class);
    }

    /**
     * The account created event.
     */
    @Immutable
    @JsonDeserialize
    public class AccountCreated implements AccountEvent {
        /**
         * The Uid.
         */
        public final UUID uid;

        /**
         * The Name.
         */
        public final String name;

        /**
         * Instantiates a new Account created.
         *
         * @param uid  the uid
         * @param name the name
         */
        @JsonCreator
        public AccountCreated(final UUID uid, final String name) {
            this.uid = uid;
            this.name = name;
        }
    }

    /**
     * The account updated event.
     */
    @Immutable
    @JsonDeserialize
    public class AccountUpdated extends AccountCreated {

        /**
         * Instantiates a new Account updated.
         *
         * @param uid  the uid
         * @param name the name
         */
        @JsonCreator
        public AccountUpdated(final UUID uid, final String name) {
            super(uid, name);
        }
    }

    /**
     * The account deleted event.
     */
    @Immutable
    @JsonDeserialize
    public class AccountDeleted implements AccountEvent {
        /**
         * The Uid.
         */
        public final UUID uid;

        /**
         * Instantiates a new Account deleted.
         *
         * @param uid the uid
         */
        @JsonCreator
        public AccountDeleted(final UUID uid) {
            this.uid = uid;
        }
    }
}
