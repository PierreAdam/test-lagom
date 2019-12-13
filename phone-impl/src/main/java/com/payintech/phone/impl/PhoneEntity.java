package com.payintech.phone.impl;

import akka.Done;
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
        b.setCommandHandler(PhoneCommand.UpdatePhone.class, this::updatePhoneCommand);
        b.setCommandHandler(PhoneCommand.DeletePhone.class, this::deletePhoneCommand);

        b.setReadOnlyCommandHandler(PhoneCommand.GetPhone.class, this::getPhoneROCommand);

        b.setEventHandler(PhoneEvent.PhoneCreated.class, this::accountCreatedEvent);
        b.setEventHandler(PhoneEvent.PhoneUpdated.class, this::accountUpdatedEvent);
        b.setEventHandler(PhoneEvent.PhoneDeleted.class, this::accountDeletedEvent);

        return b.build();
    }

    private Persist<PhoneEvent> createPhoneCommand(final PhoneCommand.CreatePhone createPhone, final CommandContext<Phone> commandContext) {
        if (this.state().phone.isPresent()) {
            commandContext.invalidCommand("Phone " + this.entityId() + " already exists");
            return commandContext.done();
        }
        final Phone phone = createPhone.phone;
        final PhoneEvent event = new PhoneEvent.PhoneCreated(phone.uid, phone.accountUid, phone.phoneNumber);
        return commandContext.thenPersist(event, phoneCreated -> commandContext.reply(phone));
    }

    private Persist<PhoneEvent> updatePhoneCommand(final PhoneCommand.UpdatePhone updatePhone, final CommandContext<Phone> commandContext) {
        if (!this.state().phone.isPresent()) {
            commandContext.invalidCommand("Phone " + this.entityId() + " does not exists");
            return commandContext.done();
        }
        final Phone phone = updatePhone.phone;
        final PhoneEvent event = new PhoneEvent.PhoneUpdated(phone.uid, phone.accountUid, phone.phoneNumber);
        return commandContext.thenPersist(event, phoneEvent -> commandContext.reply(phone));
    }

    private Persist<PhoneEvent> deletePhoneCommand(final PhoneCommand.DeletePhone deletePhone, final CommandContext<Done> commandContext) {
        if (!this.state().phone.isPresent()) {
            commandContext.invalidCommand("Phone " + this.entityId() + " does not exists");
            return commandContext.done();
        }
        final PhoneEvent event = new PhoneEvent.PhoneDeleted(this.state().phone.get().uid);
        return commandContext.thenPersist(event, phoneEvent -> commandContext.reply(Done.getInstance()));
    }

    private void getPhoneROCommand(final PhoneCommand.GetPhone getPhone, final ReadOnlyCommandContext<PhoneCommand.GetPhoneReply> readOnlyCommandContext) {
        readOnlyCommandContext.reply(new PhoneCommand.GetPhoneReply(this.state().phone));
    }

    private PhoneState accountCreatedEvent(final PhoneEvent.PhoneCreated phoneCreated) {
        return new PhoneState(Optional.of(new Phone(phoneCreated.uid, phoneCreated.accountUid, phoneCreated.phoneNumber)));
    }

    private PhoneState accountUpdatedEvent(final PhoneEvent.PhoneUpdated phoneUpdated) {
        return new PhoneState(Optional.of(new Phone(phoneUpdated.uid, phoneUpdated.accountUid, phoneUpdated.phoneNumber)));
    }

    private PhoneState accountDeletedEvent(final PhoneEvent.PhoneDeleted phoneDeleted) {
        return new PhoneState(Optional.empty());
    }
}
