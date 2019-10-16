package com.payintech.account.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.ServiceLocator;
import com.lightbend.lagom.javadsl.client.ConfigurationServiceLocator;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.payintech.account.api.AccountService;
import com.typesafe.config.Config;
import play.Environment;

import javax.inject.Inject;

/**
 * AccountModule.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public class AccountModule extends AbstractModule implements ServiceGuiceSupport {

    private final Environment environment;

    private final Config config;

    @Inject
    public AccountModule(final Environment environment, final Config config) {
        this.environment = environment;
        this.config = config;
    }

    @Override
    protected void configure() {
        this.bindService(AccountService.class, AccountServiceImpl.class);
        if (this.environment.isProd()) {
            this.bind(ServiceLocator.class).to(ConfigurationServiceLocator.class);
        }
    }
}
