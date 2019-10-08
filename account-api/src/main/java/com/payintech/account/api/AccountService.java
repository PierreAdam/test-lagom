package com.payintech.account.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

/**
 * AccountService.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public interface AccountService extends Service {

    ServiceCall<NotUsed, String> getAccount();

    ServiceCall<NotUsed, String> getAccountWithHeaders();

    @Override
    default Descriptor descriptor() {
        return Service.named("account")
                .withCalls(
                        Service.pathCall("/account", this::getAccount),
                        Service.pathCall("/account-with-headers", this::getAccountWithHeaders)
                )
                .withAutoAcl(true);
    }
}
