/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyWebServiceHelper;
import gov.hhs.fha.nhinc.patientdiscovery.entity.proxy.service.EntityPatientDiscoverySecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.patientdiscovery.entity.proxy.service.EntityPatientDiscoveryServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public abstract class EntityPatientDiscoveryProxyWebServicAbstract {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterPatientDiscoveryProxyWebServiceHelper.class);
    private String endpointURL = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * @return the oProxyHelper
     */
    public WebServiceProxyHelper getoProxyHelper() {
        return oProxyHelper;
    }

    /**
     * @param oProxyHelper the oProxyHelper to set
     */
    public void setoProxyHelper(WebServiceProxyHelper oProxyHelper) {
        this.oProxyHelper = oProxyHelper;
    }

    /**
     * @return the connectionManager
     */
    public String getendpointURL() {
        return endpointURL;
    }

    /**
     * @param endpointURL the endpointURL to set
     */
    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    String getEndpointURLForService(String serviceName) {
        String endpointURL = null;
        try {
            endpointURL = getEndpointURLFromExchange(serviceName);
            LOG.debug("Retrieved endpoint URL: {} for service : {} ", endpointURL, serviceName);
        } catch (ExchangeManagerException ex) {
            LOG.error("Error getting url for : {}  from the connection manager. Error: {}", serviceName,
                ex.getMessage(), ex);
        }

        return endpointURL;
    }

    String getEndpointURLFromExchange(String serviceName) throws ExchangeManagerException {
        try {
            endpointURL = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
        } catch (Exception ex) {
            LOG.error("Error getting url for : {}  from the connection manager. Error: {}", serviceName,
                ex.getMessage(), ex);
        }
        setEndpointURL(endpointURL);
        return endpointURL;
    }

    String setInvokeConnectionManager(String serviceName) throws ExchangeManagerException {
        return ExchangeManager.getInstance().getEndpointURL(serviceName);
    }

    RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(Class portType, String endpointURL,
        PRPAIN201305UV02 pdRequest, AssertionType assertion, NhinTargetCommunitiesType targetCommunities) {
        LOG.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        try {
            if (pdRequest == null) {
                LOG.error("PRPAIN201305UV02 was null");
            } else if (assertion == null) {
                LOG.error("AssertionType was null");
            } else if (targetCommunities == null) {
                LOG.error("NhinTargetCommunitiesType was null");
            } else {
                RespondingGatewayPRPAIN201305UV02RequestType request
                    = new RespondingGatewayPRPAIN201305UV02RequestType();
                request.setPRPAIN201305UV02(pdRequest);
                request.setAssertion(assertion);
                request.setNhinTargetCommunities(targetCommunities);
                if (portType == EntityPatientDiscoveryPortType.class) {
                    response = (RespondingGatewayPRPAIN201306UV02ResponseType) getDiscoveryPortClient(endpointURL,
                        assertion).invokePort(EntityPatientDiscoveryPortType.class, "respondingGatewayPRPAIN201305UV02",
                            request);
                } else if (portType == EntityPatientDiscoverySecuredPortType.class) {
                    response = (RespondingGatewayPRPAIN201306UV02ResponseType) getSecuredPortClient(endpointURL,
                        assertion).invokePort(EntityPatientDiscoverySecuredPortType.class,
                            "respondingGatewayPRPAIN201305UV02", request);
                }
            }
        } catch (Exception ex) {
            LOG.error("Error calling respondingGatewayPRPAIN201305UV02: {} ", ex.getMessage(), ex);
        }

        LOG.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }

    /**
     * @param url
     * @param assertion
     * @return
     */
    CONNECTClient<EntityPatientDiscoveryPortType> getDiscoveryPortClient(final String url,
        final AssertionType assertion) {
        ServicePortDescriptor<EntityPatientDiscoveryPortType> portDescriptor
            = new EntityPatientDiscoveryServicePortDescriptor();
        return CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * @param url
     * @param assertion
     * @return
     */
    CONNECTClient<EntityPatientDiscoverySecuredPortType> getSecuredPortClient(final String url,
        final AssertionType assertion) {
        ServicePortDescriptor<EntityPatientDiscoverySecuredPortType> portDescriptor
            = new EntityPatientDiscoverySecuredServicePortDescriptor();
        return CONNECTClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

}
