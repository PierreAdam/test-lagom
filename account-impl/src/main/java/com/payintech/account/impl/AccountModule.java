package com.payintech.account.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.payintech.account.api.AccountService;

/**
 * AccountModule.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public class AccountModule extends AbstractModule implements ServiceGuiceSupport {

    @Override
    protected void configure() {
        this.bindService(AccountService.class, AccountServiceImpl.class);
    }
}
