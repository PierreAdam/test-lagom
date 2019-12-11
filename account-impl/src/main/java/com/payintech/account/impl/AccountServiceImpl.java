package com.payintech.account.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.api.transport.RequestHeader;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.payintech.account.api.Account;
import com.payintech.account.api.AccountService;
import com.payintech.account.impl.models.AccountModel;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * AccountServiceImpl.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public class AccountServiceImpl implements AccountService {

    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The Persistent entities.
     */
    private final PersistentEntityRegistry persistentEntities;

    /**
     * Instantiates a new Account service.
     *
     * @param persistentEntities the persistent entities
     * @param readSide           the read side
     */
    @Inject
    public AccountServiceImpl(final PersistentEntityRegistry persistentEntities, final ReadSide readSide) {
        this.persistentEntities = persistentEntities;
        this.persistentEntities.register(AccountEntity.class);
        readSide.register(AccountEventProcessor.class);
    }

    /**
     * With auth header service call.
     *
     * @param <Request>   the type parameter
     * @param <Response>  the type parameter
     * @param serviceCall the service call
     * @return the header service call
     */
    public <Request, Response> HeaderServiceCall<Request, Response> withAuth(final BiFunction<RequestHeader, Request, CompletableFuture<Pair<ResponseHeader, Response>>> serviceCall) {
        return (requestHeader, request) -> {
            final Optional<String> authorization = requestHeader.getHeader("Authorization");
            if (!authorization.isPresent()) {
                throw new RuntimeException("YOU HAVE NO POWER HERE !");
            }


            return serviceCall.apply(requestHeader, request);
        };
    }

    @Override
    public HeaderServiceCall<NotUsed, List<Account>> listAccounts(final Optional<Integer> page) {
        final int perPage = 5;
        return (requestHeader, notUsed) -> CompletableFuture
                .supplyAsync(() -> AccountModel.find.query().setMaxRows(perPage).setFirstRow(page.orElseGet(() -> 0) * perPage).findPagedList())
                .thenApply(pagedList -> {
                    final PMap<String, PSequence<String>> headerMap = HashTreePMap
                            .<String, PSequence<String>>empty()
                            .plus("Total", TreePVector.singleton(String.format("%d", pagedList.getTotalCount())));
                    final ResponseHeader responseHeader = new ResponseHeader(Http.Status.OK, new MessageProtocol(), headerMap);
                    final List<Account> responseData = pagedList.getList().stream().map(AccountModel::asAccount).collect(Collectors.toList());
                    return Pair.create(responseHeader, responseData);
                });
    }

    @Override
    public ServiceCall<Account, Account> createAccount() {
        return account -> {
            account.uid = UUID.randomUUID();
            return this.accountEntityRef(account.uid)
                    .ask(new AccountCommand.CreateAccount(account));
        };
    }

    @Override
    public ServiceCall<NotUsed, Account> readAccount(final UUID uid) {
        return notUsed -> this.accountEntityRef(uid)
                .ask(new AccountCommand.GetAccount())
                .thenApply(reply -> {
                    if (reply.account.isPresent()) {
                        return reply.account.get();
                    } else {
                        throw new NotFound("Account " + uid + " not found");
                    }
                });
    }

    @Override
    public ServiceCall<Account, Account> updateAccount(final UUID uid) {
        return account -> {
            account.uid = uid;
            return this.accountEntityRef(uid)
                    .ask(new AccountCommand.UpdateAccount(account));
        };
    }

    @Override
    public ServiceCall<NotUsed, NotUsed> deleteAccount(final UUID uid) {
        return notUsed -> this.accountEntityRef(uid)
                .ask(new AccountCommand.DeleteAccount())
                .thenApply(done -> NotUsed.getInstance());
    }

    /**
     * Account entity ref persistent entity ref.
     *
     * @param uid the uid
     * @return the persistent entity ref
     */
    private PersistentEntityRef<AccountCommand> accountEntityRef(final UUID uid) {
        return this.persistentEntities.refFor(AccountEntity.class, uid.toString());
    }
}
