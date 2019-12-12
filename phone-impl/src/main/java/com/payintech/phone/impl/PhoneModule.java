package com.payintech.phone.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.payintech.account.api.AccountService;
import com.payintech.phone.api.PhoneService;
import com.typesafe.config.Config;
import play.Environment;

import javax.inject.Inject;

/**
 * PhoneModule.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public class PhoneModule extends AbstractModule implements ServiceGuiceSupport {

    private final Environment environment;

    private final Config config;

    @Inject
    public PhoneModule(final Environment environment, final Config config) {
        this.environment = environment;
        this.config = config;
    }

    @Override
    protected void configure() {
        this.bindService(PhoneService.class, PhoneServiceImpl.class);
        this.bindClient(AccountService.class);
//        if (this.environment.isProd()) {
//            this.bind(ServiceLocator.class).to(ConfigurationServiceLocator.class);
//        }
    }
}
