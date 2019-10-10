package com.payintech.account.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;

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

    @Immutable
    @JsonDeserialize
    public class AccountCreated implements AccountEvent {
        public final String id;
        public final String name;

        @JsonCreator
        public AccountCreated(final String id, final String name) {
            this.id = id;
            this.name = name;
        }
    }
}
