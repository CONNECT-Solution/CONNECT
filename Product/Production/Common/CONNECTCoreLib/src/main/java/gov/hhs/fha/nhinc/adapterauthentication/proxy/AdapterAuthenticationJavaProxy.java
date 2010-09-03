/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapterauthentication.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the Java based call to the
 * AdapterAuthentication.
 */
public class AdapterAuthenticationJavaProxy implements AdapterAuthenticationProxy {

    private static Log log = LogFactory.getLog(AdapterAuthenticationJavaProxy.class);

    /**
     * Given a request to authenticate a user, this service will determine if
     * this is an identifiable user within OpenSSO and if so will provide an
     * identifying token.
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is
     * implemented and if so the resulting token identifier
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest) {

        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();

        AdapterAuthenticationImpl authImpl = new AdapterAuthenticationImpl();

        try
        {
            authResp = authImpl.authenticateUser(authenticateUserRequest);
        }
        catch (Exception ex)
        {
            String message = "Error occurred calling AdapterAuthenticationJavaProxy.authenticateUser.  Error: " +
                                   ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }

        return authResp;
    }
}
