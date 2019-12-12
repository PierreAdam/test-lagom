package com.payintech.phone.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import java.util.UUID;

/**
 * AccountService.
 *
 * @author Pierre Adam
 * @since 19.10.08
 */
public interface PhoneService extends Service {

    ServiceCall<Phone, Phone> createPhone();

    ServiceCall<NotUsed, Phone> readPhone(UUID uid);

    ServiceCall<Phone, Phone> updatePhone(UUID uid);

    ServiceCall<NotUsed, NotUsed> deletePhone(UUID uid);

    @Override
    default Descriptor descriptor() {
        return Service.named("phone")
                .withCalls(
                        Service.restCall(Method.POST, "/phone", this::createPhone),
                        Service.restCall(Method.GET, "/phone/:uid", this::readPhone),
                        Service.restCall(Method.PUT, "/phone/:uid", this::updatePhone),
                        Service.restCall(Method.DELETE, "/phone/:uid", this::deletePhone)
                )
                .withAutoAcl(true);
    }
}
