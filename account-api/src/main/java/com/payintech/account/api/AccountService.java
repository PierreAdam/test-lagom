package com.payintech.account.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import com.payintech.filters.SecurityFilter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * AccountService.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public interface AccountService extends Service {

    ServiceCall<NotUsed, List<Account>> listAccounts(Optional<Integer> page);

    ServiceCall<Account, Account> createAccount();

    ServiceCall<NotUsed, Account> readAccount(UUID uid);

    ServiceCall<Account, Account> updateAccount(UUID uid);

    ServiceCall<NotUsed, NotUsed> deleteAccount(UUID uid);

    @Override
    default Descriptor descriptor() {
        return Service.named("account")
                .withCalls(
                        Service.restCall(Method.GET, "/accounts?page", this::listAccounts),
                        Service.restCall(Method.POST, "/account", this::createAccount),
                        Service.restCall(Method.GET, "/account/:uid", this::readAccount),
                        Service.restCall(Method.PUT, "/account/:uid", this::updateAccount),
                        Service.restCall(Method.DELETE, "/account/:uid", this::deleteAccount)
                )
                .withHeaderFilter(new SecurityFilter("Account"))
                .withAutoAcl(true);
    }
}
