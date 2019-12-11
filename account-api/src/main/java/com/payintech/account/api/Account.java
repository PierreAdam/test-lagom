package com.payintech.account.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;
import java.util.UUID;

/**
 * Account.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
@Immutable
@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    public UUID uid;

    public String name;

    @JsonCreator
    public Account(
            @JsonProperty(value = "uid", required = false) final UUID uid,
            @JsonProperty(value = "name", required = true) final String name) {
        this.uid = uid;
        this.name = name;
    }

    @JsonIgnore
    public Optional<PSequence<String>> getErrors() {
        PSequence<String> errors = TreePVector.empty();
        this.name = this.name.trim();

        if (this.name.isEmpty()) {
            errors = errors.plus("Name is empty.");
        }

        return errors.size() == 0 ? Optional.empty() : Optional.of(errors);
    }
}
