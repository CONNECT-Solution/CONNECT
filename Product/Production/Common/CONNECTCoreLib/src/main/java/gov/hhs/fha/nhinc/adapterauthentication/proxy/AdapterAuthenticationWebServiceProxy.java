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
package gov.hhs.fha.nhinc.adapterauthentication.proxy;

import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * This is the concrete implementation for the Web based call to the
 * AdapterAuthentication.
 */
public class AdapterAuthenticationWebServiceProxy implements AdapterAuthenticationProxy {

    private static Log log = LogFactory.getLog(AdapterAuthenticationWebServiceProxy.class);
    private static String ADAPTER_AUTH_SERVICE_NAME = "adapterauthentication";
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterauthentication";
    private static final String SERVICE_LOCAL_PART = "AdapterAuthentication_Service";
    private static final String PORT_LOCAL_PART = "AdapterAuthentication_Port";
    private static final String WSDL_FILE = "AdapterAuthentication.wsdl";
    private static Service cachedService = null;
    private WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();

    /**
     * Given a request to authenticate a user, this service will determine if
     * this is an identifiable user within OpenSSO and if so will provide an
     * identifying token.
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is
     * implemented and if so the resulting token identifier
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest) {

        log.debug("Begin authenticateUser");
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();

        try {
            AdapterAuthenticationPortType authPort = getAdapterAuthenticationPort();
            authResp = authPort.authenticateUser(authenticateUserRequest);
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterAuthenticationWebServiceProxy.authenticateUser.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }

        log.debug("End authenticateUser");
        return authResp;
    }

    /**
     * Return a handle to the AdapterAuthentication web service.
     *
     * @return The handle to the Adapter Authentication port web service.
     */
    private AdapterAuthenticationPortType getAdapterAuthenticationPort()
            throws AdapterAuthenticationException {
        AdapterAuthenticationPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            String url = getUrl(ADAPTER_AUTH_SERVICE_NAME);

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterAuthenticationPortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, null, null);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }

        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = proxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    protected String getUrl(String serviceName) {
        String result = "";
        try {
            result = proxyHelper.getUrlLocalHomeCommunity(serviceName);
        } catch (Exception ex) {
            log.warn("Unable to retreive url for service: " + serviceName);
            log.warn("Error: " + ex.getMessage(), ex);
        }

        return result;
    }
}
