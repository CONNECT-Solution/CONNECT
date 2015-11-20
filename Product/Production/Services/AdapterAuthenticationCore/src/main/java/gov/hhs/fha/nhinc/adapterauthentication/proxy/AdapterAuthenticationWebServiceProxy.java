/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.adapterauthentication.proxy;

import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationPortType;
import gov.hhs.fha.nhinc.adapterauthentication.proxy.service.AdapterAuthenticationServicePortDescriptor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the concrete implementation for the Web based call to the AdapterAuthentication.
 */
public class AdapterAuthenticationWebServiceProxy implements AdapterAuthenticationProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterAuthenticationWebServiceProxy.class);
    private static String ADAPTER_AUTH_SERVICE_NAME = "adapterauthentication";
    private WebServiceProxyHelper proxyHelper = null;

    public AdapterAuthenticationWebServiceProxy() {
        proxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<AdapterAuthenticationPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<AdapterAuthenticationPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * Given a request to authenticate a user, this service will determine if this is an identifiable user within
     * OpenSSO and if so will provide an identifying token.
     *
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is implemented and if so the resulting token
     *         identifier
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest) {

        LOG.debug("Begin authenticateUser");
        AuthenticateUserResponseType authResp = null;

        try {
            String url = proxyHelper.getAdapterEndPointFromConnectionManager(ADAPTER_AUTH_SERVICE_NAME);

            if (NullChecker.isNotNullish(url)) {

                if (authenticateUserRequest == null) {
                    LOG.error("Request was null");
                } else {
                    ServicePortDescriptor<AdapterAuthenticationPortType> portDescriptor = new AdapterAuthenticationServicePortDescriptor();
                    CONNECTClient<AdapterAuthenticationPortType> client = getCONNECTClientUnsecured(portDescriptor,
                            url, null);

                    authResp = (AuthenticateUserResponseType) client.invokePort(AdapterAuthenticationPortType.class,
                            "authenticateUser", authenticateUserRequest);
                }
            } else {
                LOG.error("Failed to call the web service (" + ADAPTER_AUTH_SERVICE_NAME + ").  The URL is null.");
            }
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterAuthenticationWebServiceProxy.authenticateUser.  Error: "
                    + ex.getMessage();
            LOG.error(message, ex);
            throw new RuntimeException(message, ex);
        }

        LOG.debug("End authenticateUser");
        return authResp;
    }
}
