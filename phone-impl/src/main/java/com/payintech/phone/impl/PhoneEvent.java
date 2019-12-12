package com.payintech.phone.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;

import javax.annotation.concurrent.Immutable;
import java.util.UUID;

/**
 * PhoneEvent.
 *
 * @author Pierre Adam
 * @since 19.10.31
 */
public interface PhoneEvent extends Jsonable, AggregateEvent<PhoneEvent> {

    @Override
    default public AggregateEventTagger<PhoneEvent> aggregateTag() {
        return AggregateEventTag.of(PhoneEvent.class);
    }

    @Immutable
    @JsonDeserialize
    public class PhoneCreated implements PhoneEvent {
        public final UUID uid;
        public final UUID accountUid;
        public final String phoneNumber;

        @JsonCreator
        public PhoneCreated(final UUID uid, final UUID accountUid, final String phoneNumber) {
            this.uid = uid;
            this.accountUid = accountUid;
            this.phoneNumber = phoneNumber;
        }
    }
}
