package com.payintech.phone.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.Offset;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;
import com.payintech.phone.impl.models.PhoneModel;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Connection;

/**
 * PhoneEventProcessor.
 *
 * @author Liviu Varga
 * @since 19.10.13
 */
public class PhoneEventProcessor extends ReadSideProcessor<PhoneEvent> {

    /**
     * The Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(PhoneEventProcessor.class);

    /**
     * The Session.
     */
    private final JdbcSession session;

    /**
     * The Read side.
     */
    private final JdbcReadSide readSide;

    /**
     * Instantiates a new Phone event processor.
     *
     * @param session  the session
     * @param readSide the read side
     */
    @Inject
    public PhoneEventProcessor(final JdbcSession session, final JdbcReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    @Override
    public ReadSideHandler<PhoneEvent> buildHandler() {
        return this.readSide.<PhoneEvent>builder("phone_offset")
                .setGlobalPrepare(this::prepareCreateTables)
                .setEventHandler(PhoneEvent.PhoneCreated.class, this::processPhoneCreated)
                .setEventHandler(PhoneEvent.PhoneUpdated.class, this::processPhoneUpdated)
                .setEventHandler(PhoneEvent.PhoneDeleted.class, this::processPhoneDeleted)
                .build();
    }

    /**
     * Handle the phone creation on the relational database.
     *
     * @param connection     the connection
     * @param phoneCreated   the phone created
     * @param offset         the offset
     */
    private void processPhoneCreated(final Connection connection, final PhoneEvent.PhoneCreated phoneCreated, final Offset offset) {
        final PhoneModel phoneModel = new PhoneModel();
        phoneModel.setUid(phoneCreated.uid);
        phoneModel.setAccountUid(phoneCreated.accountUid);
        phoneModel.setPhoneNumber(phoneCreated.phoneNumber);
        phoneModel.save();
    }

    /**
     * Handle the phone deletion on the relational database.
     *
     * @param connection     the connection
     * @param phoneDeleted   the phone deleted
     * @param offset         the offset
     */
    private void processPhoneDeleted(final Connection connection, final PhoneEvent.PhoneDeleted phoneDeleted, final Offset offset) {
        final PhoneModel phone = PhoneModel.find.query().where().eq("uid", phoneDeleted.uid).findOne();
        if (phone == null) {
            this.logger.error("An error occurred while processing PhoneDeleted Event. The phone[{}] does not exist in database.", phoneDeleted.uid);
            return;
        }
        phone.delete();
    }

    /**
     * Handle the phone update on the relational database.
     *
     * @param connection     the connection
     * @param phoneUpdated   the phone updated
     * @param offset         the offset
     */
    private void processPhoneUpdated(final Connection connection, final PhoneEvent.PhoneUpdated phoneUpdated, final Offset offset) {
        final PhoneModel phone = PhoneModel.find.query().where().eq("uid", phoneUpdated.uid).findOne();
        if (phone == null) {
            this.logger.error("An error occurred while processing PhoneUpdated Event. The phone[{}] does not exist in database.", phoneUpdated.uid);
            return;
        }
        phone.setPhoneNumber(phoneUpdated.phoneNumber);
        phone.save();
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
    public PSequence<AggregateEventTag<PhoneEvent>> aggregateTags() {
        return TreePVector.singleton(AggregateEventTag.of(PhoneEvent.class));
    }
}
