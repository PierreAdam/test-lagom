package com.payintech.account.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import com.payintech.account.impl.models.AccountModel;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Connection;

/**
 * AccountEventProcessor.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
public class AccountEventProcessor extends ReadSideProcessor<AccountEvent> {

    final Logger logger = LoggerFactory.getLogger(AccountEventProcessor.class);

    final JdbcSession session;

    final JdbcReadSide readSide;

    @Inject
    public AccountEventProcessor(final JdbcSession session, final JdbcReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    @Override
    public ReadSideHandler<AccountEvent> buildHandler() {
        return this.readSide.<AccountEvent>builder("account_offset")
                .setGlobalPrepare(this::prepareCreateTables)
                .setEventHandler(AccountEvent.AccountCreated.class, this::processAccountCreated)
                .build();
    }

    private void processAccountCreated(final Connection connection, final AccountEvent.AccountCreated accountCreated, final Offset offset) {
        this.logger.error("Create account");
        final AccountModel accountModel = new AccountModel();
        accountModel.setId(accountCreated.id);
        accountModel.setName(accountCreated.name);
        accountModel.save();
    }

    private void prepareCreateTables(final Connection connection) {
        this.logger.error("CREATE TABLES");
    }

    @Override
    public PSequence<AggregateEventTag<AccountEvent>> aggregateTags() {
        return TreePVector.singleton(AggregateEventTag.of(AccountEvent.class));
    }
}
