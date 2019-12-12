package com.payintech.phone.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import com.payintech.phone.api.Phone;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

/**
 * PhoneState.
 *
 * @author Pierre Adam
 * @since 19.10.31
 */
@Immutable
@JsonDeserialize
public class PhoneState implements Jsonable {

    public final Optional<Phone> phone;

    @JsonCreator
    public PhoneState(final Optional<Phone> phone) {
        this.phone = Preconditions.checkNotNull(phone);
    }
}
