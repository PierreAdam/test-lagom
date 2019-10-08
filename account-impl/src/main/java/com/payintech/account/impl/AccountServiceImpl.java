package com.payintech.account.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.payintech.account.api.AccountService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * AccountServiceImpl.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public class AccountServiceImpl implements AccountService {

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
}
