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

    @Override
    public RequestHeader transformServerRequest(final RequestHeader request) {
        this.logger.error("Transform Server Request");
        return request;
    }

    @Override
    public ResponseHeader transformServerResponse(final ResponseHeader response, final RequestHeader request) {
        this.logger.error("Transform Server Response");
        return response;
    }

    @Override
    public RequestHeader transformClientRequest(final RequestHeader request) {
        this.logger.error("Transform Client Request");
        return request;
    }

    @Override
    public ResponseHeader transformClientResponse(final ResponseHeader response, final RequestHeader request) {
        this.logger.error("Transform Client Response");
        return response;
    }
}
