/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.adapterauthentication;

import javax.xml.ws.BindingType;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;

import org.apache.log4j.Logger;

/**
 * 
 * @author Sai Valluripalli
 */
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterAuthenticationService implements gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationPortType{

    private static final Logger LOG = Logger.getLogger(AdapterAuthenticationService.class);

    /**
     * Given a request to authenticate a user, this service will determine if an authentication service is configured
     * for use (such as OpenSSO) and if so will provide an identifying token.
     * 
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is implemented and if so the resulting token
     *         identifier
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest) {
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();

        AdapterAuthenticationImpl adapterAuthImpl = new AdapterAuthenticationImpl();

        try {
            authResp = adapterAuthImpl.authenticateUser(authenticateUserRequest);
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterAuthenticationImpl.authenticateUser.  Error: "
                    + ex.getMessage();
            LOG.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return authResp;
    }
}
