package com.payintech.account.impl;

import akka.Done;
import com.google.common.collect.Lists;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.payintech.account.api.Account;
import org.pcollections.PSequence;

import java.util.Optional;

/**
 * AccountEntity.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
public class AccountEntity extends PersistentEntity<AccountCommand, AccountEvent, AccountState> {

    @Override
    public Behavior initialBehavior(final Optional<AccountState> snapshotState) {
        final BehaviorBuilder b = this.newBehaviorBuilder(snapshotState.orElse(new AccountState(Optional.empty())));

        b.setCommandHandler(AccountCommand.CreateAccount.class, this::createAccountCommand);
        b.setCommandHandler(AccountCommand.UpdateAccount.class, this::updateAccountCommand);
        b.setCommandHandler(AccountCommand.DeleteAccount.class, this::deleteAccountCommand);

        b.setReadOnlyCommandHandler(AccountCommand.GetAccount.class, this::getAccountROCommand);

        b.setEventHandler(AccountEvent.AccountCreated.class, this::accountCreatedEvent);
        b.setEventHandler(AccountEvent.AccountUpdated.class, this::accountUpdatedEvent);
        b.setEventHandler(AccountEvent.AccountDeleted.class, this::accountDeletedEvent);

        return b.build();
    }

    /**
     * Set the command handler for the command {@link com.payintech.account.impl.AccountCommand.CreateAccount}.
     * This method will check that the command is correct and if so, will create an {@link com.payintech.account.impl.AccountEvent.AccountCreated} event.
     *
     * @param createAccount  the create account command
     * @param commandContext the command context
     * @return persist persist
     */
    private Persist<AccountEvent> createAccountCommand(final AccountCommand.CreateAccount createAccount, final CommandContext<Account> commandContext) {
        if (this.state().account.isPresent()) {
            commandContext.invalidCommand("Account " + this.entityId() + " already exists");
            return commandContext.done();
        }
        final Account account = createAccount.account;
        final Optional<PSequence<String>> errors = account.getErrors();
        if (errors.isPresent()) {
            commandContext.invalidCommand("Invalid request");
            return commandContext.done();
        }
        final AccountEvent.AccountCreated event = new AccountEvent.AccountCreated(account.uid, account.name);
        return commandContext.thenPersistAll(Lists.newArrayList(event), () -> commandContext.reply(account));
    }

    /**
     * Set the command handler for the command {@link com.payintech.account.impl.AccountCommand.UpdateAccount}.
     * This method will check that the command is correct and if so, will create an {@link com.payintech.account.impl.AccountEvent.AccountUpdated} event.
     *
     * @param updateAccount  the update account
     * @param commandContext the command context
     * @return the persist
     */
    private Persist<AccountEvent> updateAccountCommand(final AccountCommand.UpdateAccount updateAccount, final CommandContext<Account> commandContext) {
        if (!this.state().account.isPresent()) {
            commandContext.invalidCommand("Account does not exist !");
            return commandContext.done();
        }
        final Account account = updateAccount.account;
        final Optional<PSequence<String>> errors = account.getErrors();
        if (errors.isPresent()) {
            commandContext.invalidCommand("Invalid request");
            return commandContext.done();
        }
        final AccountEvent.AccountUpdated event = new AccountEvent.AccountUpdated(account.uid, account.name);
        return commandContext.thenPersistAll(Lists.newArrayList(event), () -> commandContext.reply(account));
    }

    /**
     * Set the command handler for the command {@link com.payintech.account.impl.AccountCommand.DeleteAccount}.
     * This method will check that the command is correct and if so, will create an {@link com.payintech.account.impl.AccountEvent.AccountDeleted} event.
     *
     * @param deleteAccount  the delete account
     * @param commandContext the command context
     * @return the persist
     */
    private Persist<AccountEvent> deleteAccountCommand(final AccountCommand.DeleteAccount deleteAccount, final CommandContext<Done> commandContext) {
        if (!this.state().account.isPresent()) {
            commandContext.invalidCommand("Account does not exist !");
            return commandContext.done();
        }
        final AccountEvent.AccountDeleted event = new AccountEvent.AccountDeleted(this.state().account.get().uid);
        return commandContext.thenPersistAll(Lists.newArrayList(event), () -> commandContext.reply(Done.getInstance()));
    }

    /**
     * Set the command handler for the command {@link com.payintech.account.impl.AccountCommand.GetAccount}.
     * This method will only read the current state and return an answer in the for of a {@link com.payintech.account.impl.AccountCommand.GetAccountReply} object.
     *
     * @param getAccount             the get account
     * @param readOnlyCommandContext the read only command context
     */
    private void getAccountROCommand(final AccountCommand.GetAccount getAccount, final ReadOnlyCommandContext<AccountCommand.GetAccountReply> readOnlyCommandContext) {
        readOnlyCommandContext.reply(new AccountCommand.GetAccountReply(this.state().account));
    }

    /**
     * Set the event handler for the event {@link com.payintech.account.impl.AccountEvent.AccountCreated}.
     * This will set create the new state for the new account.
     *
     * @param accountCreated the account created
     * @return the account state
     */
    private AccountState accountCreatedEvent(final AccountEvent.AccountCreated accountCreated) {
        return new AccountState(Optional.of(new Account(accountCreated.uid, accountCreated.name)));
    }

    /**
     * Set the event handler for the event {@link com.payintech.account.impl.AccountEvent.AccountUpdated}.
     * This will set update the state for the account.
     *
     * @param accountUpdated the account updated
     * @return the account state
     */
    private AccountState accountUpdatedEvent(final AccountEvent.AccountUpdated accountUpdated) {
        return new AccountState(Optional.of(new Account(accountUpdated.uid, accountUpdated.name)));
    }

    /**
     * Set the event handler for the event {@link com.payintech.account.impl.AccountEvent.AccountDeleted}.
     * This will delete the state for the account.
     *
     * @param accountDeleted the account deleted
     * @return the account state
     */
    private AccountState accountDeletedEvent(final AccountEvent.AccountDeleted accountDeleted) {
        return new AccountState(Optional.empty());
    }
}
