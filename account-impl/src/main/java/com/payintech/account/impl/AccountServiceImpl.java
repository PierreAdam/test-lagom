package com.payintech.account.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.payintech.account.api.Account;
import com.payintech.account.api.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * AccountServiceImpl.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public class AccountServiceImpl implements AccountService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PersistentEntityRegistry persistentEntities;

    @Inject
    public AccountServiceImpl(final PersistentEntityRegistry persistentEntities, final ReadSide readSide) {
        this.persistentEntities = persistentEntities;
        this.persistentEntities.register(AccountEntity.class);
        readSide.register(AccountEventProcessor.class);
    }

    @Override
    public ServiceCall<NotUsed, Account> getAccount(final String id) {
        return notUsed -> this.accountEntityRef(id)
                .ask(new AccountCommand.GetAccount())
                .thenApply(reply -> {
                    if (reply.account.isPresent()) {
                        return reply.account.get();
                    } else {
                        throw new NotFound("Account " + id + " not found");
                    }
                });
    }

    @Override
    public ServiceCall<Account, NotUsed> createAccount() {
        return account -> {
            return this.accountEntityRef(account.id)
                    .ask(new AccountCommand.CreateAccount(account))
                    .thenApply(done -> NotUsed.getInstance());
        };
    }

    private PersistentEntityRef<AccountCommand> accountEntityRef(final String id) {
        return this.persistentEntities.refFor(AccountEntity.class, id);
    }
}
