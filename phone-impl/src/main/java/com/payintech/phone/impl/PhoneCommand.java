package com.payintech.phone.impl;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import com.payintech.phone.api.Phone;
import com.sun.istack.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

/**
 * PhoneCommand.
 *
 * @author Pierre Adam
 * @since 19.10.31
 */
public interface PhoneCommand extends Jsonable {

    @Immutable
    @JsonDeserialize
    public class CreatePhone implements PhoneCommand, PersistentEntity.ReplyType<Phone> {
        public final Phone phone;

        @JsonCreator
        public CreatePhone(@NotNull final Phone phone) {
            this.phone = Preconditions.checkNotNull(phone);
        }
    }

    @Immutable
    @JsonDeserialize
    public final class GetPhone implements PhoneCommand, PersistentEntity.ReplyType<GetPhoneReply> {
    }

    @Immutable
    @JsonDeserialize
    public final class GetPhoneReply implements Jsonable {
        public final Optional<Phone> phone;

        @JsonCreator
        public GetPhoneReply(final Optional<Phone> phone) {
            this.phone = Preconditions.checkNotNull(phone, "phone");
        }
    }

    @Immutable
    @JsonDeserialize
    public class UpdatePhone extends CreatePhone {
        @JsonCreator
        public UpdatePhone(@NotNull final Phone phone) {
            super(phone);
        }
    }

    /**
     * The Delete Phone Command.
     */
    @Immutable
    @JsonDeserialize
    public class DeletePhone implements PhoneCommand, PersistentEntity.ReplyType<Done> {
    }
}
