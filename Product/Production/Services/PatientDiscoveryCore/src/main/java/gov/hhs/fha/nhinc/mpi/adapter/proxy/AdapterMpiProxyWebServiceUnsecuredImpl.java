/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.mpi.adapter.proxy;

import gov.hhs.fha.nhinc.adaptermpi.AdapterMpiPortType;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.mpi.adapter.proxy.service.AdapterMpiUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02AdapterEventDescBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the proxy class for the unsecured AdapterComponentProxy interface.
 */
public class AdapterMpiProxyWebServiceUnsecuredImpl implements AdapterMpiProxy {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterMpiProxyWebServiceUnsecuredImpl.class);
    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Method to get an Unsecured CONNECT Client.
     *
     * @param portDescriptor the portDescriptor
     * @param url the intended URL
     * @param assertion the assertion
     * @return a CONNECTClient of type AdapterMpiPortType
     */
    protected CONNECTClient<AdapterMpiPortType> getCONNECTClientUnsecured(
        ServicePortDescriptor<AdapterMpiPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * Find the matching candidates from the MPI.
     *
     * @param request The information to use for matching.
     * @param assertion The assertion data.
     * @return The matches that are found.
     */
    @Override
    @AdapterDelegationEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
        afterReturningBuilder = PRPAIN201305UV02AdapterEventDescBuilder.class,
        serviceType = "Patient Discovery MPI", version = "1.0")
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 request, AssertionType assertion) {
        return findCandidatesMpiUnsecured(request, assertion);
    }

    /**
     * The sole purpose of this method is to pass NhinTargetSystemType for Adapter Delegation Events. We need
     * NhinTargetSystemType to log responding_hcids for BEGIN and END ADAPTER DELEGATION EVENTS.
     *
     * @param request
     * @param assertion The assertion data.
     * @param target
     * @return The matches that are found.
     */
    @Override
    @AdapterDelegationEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
        afterReturningBuilder = PRPAIN201305UV02AdapterEventDescBuilder.class,
        serviceType = "Patient Discovery MPI", version = "1.0")
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 request, AssertionType assertion,
        NhinTargetSystemType target) {
        return findCandidatesMpiUnsecured(request, assertion);
    }

    private PRPAIN201306UV02 findCandidatesMpiUnsecured(PRPAIN201305UV02 request, AssertionType assertion) {
        String url;
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        String sServiceName = NhincConstants.ADAPTER_MPI_SERVICE_NAME;

        try {
            if (request != null) {
                url = oProxyHelper.getAdapterEndPointFromConnectionManager(sServiceName);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayPRPAIN201305UV02RequestType wsRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                    wsRequest.setPRPAIN201305UV02(request);
                    wsRequest.setAssertion(assertion);

                    ServicePortDescriptor<AdapterMpiPortType> portDescriptor = new AdapterMpiUnsecuredServicePortDescriptor();
                    CONNECTClient<AdapterMpiPortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                    response = (PRPAIN201306UV02) client.invokePort(AdapterMpiPortType.class, "findCandidates",
                        wsRequest);
                } else {
                    LOG.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                LOG.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            LOG.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  "
                + "Exception: " + e.getMessage(), e);
        }
        return response;
    }
}
