package com.payintech.phone.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.payintech.phone.api.Phone;

import java.util.Optional;

/**
 * PhoneEntity.
 *
 * @author Pierre Adam
 * @since 19.10.31
 */
public class PhoneEntity extends PersistentEntity<PhoneCommand, PhoneEvent, PhoneState> {

    @Override
    public Behavior initialBehavior(final Optional<PhoneState> snapshotState) {
        final BehaviorBuilder b = this.newBehaviorBuilder(snapshotState.orElse(new PhoneState(Optional.empty())));

        b.setCommandHandler(PhoneCommand.CreatePhone.class, this::createPhoneCommand);

        b.setReadOnlyCommandHandler(PhoneCommand.GetPhone.class, this::getPhoneROCommand);

        b.setEventHandler(PhoneEvent.PhoneCreated.class, this::accountCreatedEvent);

        return b.build();
    }

    private Persist<PhoneEvent> createPhoneCommand(final PhoneCommand.CreatePhone createPhone, final CommandContext<Phone> commandContext) {
        if (this.state().phone.isPresent()) {
            commandContext.invalidCommand("Phone " + this.entityId() + " already exists");
            return commandContext.done();
        }
        final Phone phone = createPhone.phone;
        final PhoneEvent.PhoneCreated event = new PhoneEvent.PhoneCreated(phone.uid, phone.accountUid, phone.phoneNumber);
        return commandContext.thenPersist(event, phoneCreated -> commandContext.reply(phone));
    }

    private void getPhoneROCommand(final PhoneCommand.GetPhone getPhone, final ReadOnlyCommandContext<PhoneCommand.GetPhoneReply> readOnlyCommandContext) {
        readOnlyCommandContext.reply(new PhoneCommand.GetPhoneReply(this.state().phone));
    }

    private PhoneState accountCreatedEvent(final PhoneEvent.PhoneCreated phoneCreated) {
        return new PhoneState(Optional.of(new Phone(phoneCreated.uid, phoneCreated.accountUid, phoneCreated.phoneNumber)));
    }
}
