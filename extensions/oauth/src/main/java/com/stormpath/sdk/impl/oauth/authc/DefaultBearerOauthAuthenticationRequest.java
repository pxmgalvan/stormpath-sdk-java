/*
 * Copyright 2014 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stormpath.sdk.impl.oauth.authc;

import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.directory.AccountStore;
import com.stormpath.sdk.http.HttpRequest;
import com.stormpath.sdk.impl.oauth.http.OAuthHttpServletRequest;
import com.stormpath.sdk.lang.Assert;
import com.stormpath.sdk.oauth.authc.BearerLocation;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.TokenType;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 1.0.RC
 */
public class DefaultBearerOauthAuthenticationRequest extends OAuthAccessResourceRequest implements AuthenticationRequest {

    private static final String HTTP_REQUEST_NOT_SUPPORTED_MSG = "HttpRequest class [%s] is not supported. Supported classes: [%s, %s].";

    //This is used via reflection by: com.stormpath.sdk.impl.authc.ApiAuthenticationRequestFactory
    //Don't delete it.
    public DefaultBearerOauthAuthenticationRequest(Object httpRequest) throws OAuthSystemException, OAuthProblemException {
        this(getHttpServletRequest(httpRequest), new BearerLocation[]{BearerLocation.HEADER});
    }

    public DefaultBearerOauthAuthenticationRequest(HttpServletRequest httpServletRequest, BearerLocation[] bearerLocations) throws OAuthProblemException, OAuthSystemException {
        super(httpServletRequest, new TokenType[]{TokenType.BEARER}, convert(bearerLocations));
        Assert.notNull(httpServletRequest, "httpServletRequest cannot be null");
    }

    private static HttpServletRequest getHttpServletRequest(Object httpRequest) {
        HttpServletRequest httpServletRequest;
        Class httpRequestClass = httpRequest.getClass();
        if (HttpServletRequest.class.isAssignableFrom(httpRequestClass)) {
            httpServletRequest = (HttpServletRequest) httpRequest;
        } else if (HttpRequest.class.isAssignableFrom(httpRequestClass)) {
            httpServletRequest = new OAuthHttpServletRequest((HttpRequest) httpRequest);
        } else {
            throw new IllegalArgumentException(String.format(HTTP_REQUEST_NOT_SUPPORTED_MSG, httpRequest.getClass(), HttpRequest.class.getName(), HttpServletRequest.class.getName()));
        }
        return httpServletRequest;
    }

    private static ParameterStyle[] convert(BearerLocation[] bearerLocations) {
        Assert.notNull(bearerLocations);

        ParameterStyle[] parameterStyles = new ParameterStyle[bearerLocations.length];

        int index = 0;

        for (BearerLocation bearerLocation : bearerLocations) {
            switch (bearerLocation) {
                case HEADER:
                    parameterStyles[index] = ParameterStyle.HEADER;
                    break;

                case BODY:
                    parameterStyles[index] = ParameterStyle.BODY;
                    break;

                case QUERY_PARAM:
                    parameterStyles[index] = ParameterStyle.QUERY;
                    break;
                default:
                    throw new IllegalArgumentException("bearerLocations has an illegal argument.");
            }
            index++;
        }
        return parameterStyles;
    }

    @Override
    public Object getPrincipals() {
        throw new UnsupportedOperationException("getPrincipals() this operation is not supported ApiAuthenticationRequest.");
    }

    @Override
    public Object getCredentials() {
        throw new UnsupportedOperationException("getCredentials()this operation is not supported ApiAuthenticationRequest.");
    }

    @Override
    public String getHost() {
        throw new UnsupportedOperationException("getHost() this operation is not supported ApiAuthenticationRequest.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("clear() this operation is not supported ApiAuthenticationRequest.");
    }

    @Override
    public AccountStore getAccountStore() {
        throw new UnsupportedOperationException("getAccountStore()this operation is not supported OauthAuthenticationRequest.");
    }

}
