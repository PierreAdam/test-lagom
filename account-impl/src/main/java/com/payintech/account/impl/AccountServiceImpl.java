package com.payintech.account.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import com.payintech.account.api.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * AccountServiceImpl.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public class AccountServiceImpl implements AccountService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    public AccountServiceImpl() {
    }

    @Override
    public ServiceCall<NotUsed, String> getAccount() {
        return notUsed -> {
            final String name = "Pierre";
            return CompletableFuture
                    .completedFuture(name);
        };
    }

    @Override
    public ServerServiceCall<NotUsed, String> getAccountWithHeaders() {
        return HeaderServiceCall.of(
                (requestHeader, notUsed) -> {
                    this.logger.error("Header : {}", requestHeader.headers());
                    final ResponseHeader responseHeader = ResponseHeader.OK.withHeader("Server", "1337");
                    return CompletableFuture.completedFuture(Pair.create(responseHeader, "Pierre"));
                });
    }
}
