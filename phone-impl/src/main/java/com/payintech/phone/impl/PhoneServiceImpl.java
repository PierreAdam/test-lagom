package com.payintech.phone.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.BadRequest;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.payintech.account.api.AccountService;
import com.payintech.phone.api.Phone;
import com.payintech.phone.api.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

/**
 * PhoneServiceImpl.
 *
 * @author Pierre Adam
 * @since 19.12.12
 */
public class PhoneServiceImpl implements PhoneService {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The Persistent entities.
     */
    private final PersistentEntityRegistry persistentEntities;

    /**
     * The account service
     */
    private final AccountService accountService;

    /**
     * Instantiates a new Phone service.
     *
     * @param persistentEntities the persistent entities
     * @param readSide           the read side
     */
    @Inject
    public PhoneServiceImpl(final PersistentEntityRegistry persistentEntities, final ReadSide readSide, final AccountService accountService) {
        this.persistentEntities = persistentEntities;
        this.persistentEntities.register(PhoneEntity.class);
        this.accountService = accountService;
        readSide.register(PhoneEventProcessor.class);
    }

    @Override
    public ServiceCall<Phone, Phone> createPhone() {
        return phone -> {
            phone.uid = UUID.randomUUID();
            return this.accountService.readAccount(phone.accountUid)
                    .invoke()
                    .exceptionally(throwable -> {
                        this.logger.debug("Error  !!", throwable);
                        throw new BadRequest("The account does not exists !");
                    })
                    .thenCompose(account -> this.phoneEntityRef(phone.uid)
                            .ask(new PhoneCommand.CreatePhone(phone)));
        };
    }

    @Override
    public ServiceCall<NotUsed, Phone> readPhone(final UUID uid) {
        return notUsed -> this.phoneEntityRef(uid)
                .ask(new PhoneCommand.GetPhone())
                .thenApply(getPhoneReply -> {
                    if (getPhoneReply.phone.isPresent()) {
                        return getPhoneReply.phone.get();
                    } else {
                        throw new NotFound("Phone " + uid + " not found");
                    }
                });
    }

    @Override
    public ServiceCall<Phone, Phone> updatePhone(final UUID uid) {
        return phone -> {
            phone.uid = uid;
            return this.accountService
                    .readAccount(phone.accountUid)
                    .invoke()
                    .exceptionally(throwable -> {
                        this.logger.debug("Error !!", throwable);
                        throw new BadRequest("The account does not exists !");
                    })
                    .thenCompose(account -> this.phoneEntityRef(uid)
                            .ask(new PhoneCommand.UpdatePhone(phone)));
        };
    }

    @Override
    public ServiceCall<NotUsed, NotUsed> deletePhone(final UUID uid) {
        return phone -> this.phoneEntityRef(uid)
                .ask(new PhoneCommand.DeletePhone())
                .thenApply(done -> NotUsed.getInstance());
    }

    /**
     * Phone entity ref persistent entity ref.
     *
     * @param uid the uid
     * @return the persistent entity ref
     */
    private PersistentEntityRef<PhoneCommand> phoneEntityRef(final UUID uid) {
        return this.persistentEntities.refFor(PhoneEntity.class, uid.toString());
    }
}
