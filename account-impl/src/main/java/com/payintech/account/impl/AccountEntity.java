package com.payintech.account.impl;

import akka.Done;
import com.google.common.collect.Lists;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.payintech.account.api.Account;

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

        b.setCommandHandler(AccountCommand.CreateAccount.class, (createAccount, commandContext) -> {
            if (this.state().account.isPresent()) {
                commandContext.invalidCommand("Account " + this.entityId() + " already exists");
                return commandContext.done();
            }
            final Account account = createAccount.account;
            final AccountEvent.AccountCreated event = new AccountEvent.AccountCreated(account.id, account.name);
            return commandContext.thenPersistAll(Lists.newArrayList(event), () -> commandContext.reply(Done.getInstance()));
        });

        b.setEventHandler(AccountEvent.AccountCreated.class, accountCreated ->
                new AccountState(Optional.of(new Account(accountCreated.id, accountCreated.name)))
        );

        b.setReadOnlyCommandHandler(AccountCommand.GetAccount.class, (getAccount, readOnlyCommandContext) ->
                readOnlyCommandContext.reply(new AccountCommand.GetAccountReply(this.state().account))
        );

        return b.build();
    }
}
