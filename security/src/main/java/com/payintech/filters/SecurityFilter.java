package com.payintech.filters;

import com.lightbend.lagom.javadsl.api.transport.HeaderFilter;
import com.lightbend.lagom.javadsl.api.transport.RequestHeader;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SecurityFilter.
 *
 * @author Pierre Adam
 * @since 19.12.11
 */
public class SecurityFilter implements HeaderFilter {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String serviceName;

    public SecurityFilter(final String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public RequestHeader transformClientRequest(final RequestHeader request) {
        this.logger.trace("[{}] Transform Client Request", this.serviceName);
        return request;
    }

    @Override
    public RequestHeader transformServerRequest(final RequestHeader request) {
        this.logger.trace("[{}] Transform Server Request", this.serviceName);
        return request;
    }

    @Override
    public ResponseHeader transformServerResponse(final ResponseHeader response, final RequestHeader request) {
        this.logger.trace("[{}] Transform Server Response", this.serviceName);
        return response;
    }

    @Override
    public ResponseHeader transformClientResponse(final ResponseHeader response, final RequestHeader request) {
        this.logger.trace("[{}] Transform Client Response", this.serviceName);
        return response;
    }
}
