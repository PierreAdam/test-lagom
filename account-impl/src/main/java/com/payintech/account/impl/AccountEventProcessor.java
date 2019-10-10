package com.payintech.account.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AccountEventProcessor.
 *
 * @author Pierre Adam
 * @since 19.10.10
 */
public class AccountEventProcessor extends ReadSideProcessor<AccountEvent> {

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
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account (id, name) VALUES (?, ?)");
            preparedStatement.setString(1, accountCreated.id);
            preparedStatement.setString(2, accountCreated.name);
            preparedStatement.execute();
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareCreateTables(final Connection connection) {
        try {
            connection.createStatement().execute("CREATE TABLE account (id VARCHAR(255), name VARCHAR(255));");
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PSequence<AggregateEventTag<AccountEvent>> aggregateTags() {
        return TreePVector.singleton(AggregateEventTag.of(AccountEvent.class));
    }
}
