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

    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(AccountEventProcessor.class);

    /**
     * The Session.
     */
    private final JdbcSession session;

    /**
     * The Read side.
     */
    private final JdbcReadSide readSide;

    /**
     * Instantiates a new Account event processor.
     *
     * @param session  the session
     * @param readSide the read side
     */
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
                .setEventHandler(AccountEvent.AccountUpdated.class, this::processAccountUpdated)
                .setEventHandler(AccountEvent.AccountDeleted.class, this::processAccountDeleted)
                .build();
    }

    /**
     * Handle the account creation on the relational database.
     *
     * @param connection     the connection
     * @param accountCreated the account created
     * @param offset         the offset
     */
    private void processAccountCreated(final Connection connection, final AccountEvent.AccountCreated accountCreated, final Offset offset) {
        final AccountModel accountModel = new AccountModel();
        accountModel.setUid(accountCreated.uid);
        accountModel.setName(accountCreated.name);
        accountModel.save();
    }

    /**
     * Handle the account deletion on the relational database.
     *
     * @param connection     the connection
     * @param accountDeleted the account deleted
     * @param offset         the offset
     */
    private void processAccountDeleted(final Connection connection, final AccountEvent.AccountDeleted accountDeleted, final Offset offset) {
        final AccountModel account = AccountModel.find.query().where().eq("uid", accountDeleted.uid).findOne();
        if (account == null) {
            this.logger.error("An error occurred while processing AccountDeleted Event. The account[{}] does not exist in database.", accountDeleted.uid);
            return;
        }
        account.delete();
    }

    /**
     * Handle the account update on the relational database.
     *
     * @param connection     the connection
     * @param accountUpdated the account updated
     * @param offset         the offset
     */
    private void processAccountUpdated(final Connection connection, final AccountEvent.AccountUpdated accountUpdated, final Offset offset) {
        final AccountModel account = AccountModel.find.query().where().eq("uid", accountUpdated.uid).findOne();
        if (account == null) {
            this.logger.error("An error occurred while processing AccountUpdated Event. The account[{}] does not exist in database.", accountUpdated.uid);
            return;
        }
        account.setName(accountUpdated.name);
        account.save();
    }

    /**
     * Prepare the database.
     *
     * @param connection the connection
     */
    private void prepareCreateTables(final Connection connection) {
        // Does nothing cause Ebean does everything by himself. But this can be used to initialize the database if needed.
    }

    @Override
    public PSequence<AggregateEventTag<AccountEvent>> aggregateTags() {
        return TreePVector.singleton(AggregateEventTag.of(AccountEvent.class));
    }
}
