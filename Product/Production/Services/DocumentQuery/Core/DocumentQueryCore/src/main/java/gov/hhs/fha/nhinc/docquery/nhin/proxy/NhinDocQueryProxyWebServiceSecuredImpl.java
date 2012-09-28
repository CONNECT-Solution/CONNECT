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
package gov.hhs.fha.nhinc.docquery.nhin.proxy;

import javax.xml.ws.BindingProvider;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.description.NhinDocQueryServicePortDescriptor;
import gov.hhs.fha.nhinc.docrepository.DocumentProcessHelper;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is the component proxy for calling the NHIN doc query web service.
 * 
 * @author jhoppesc, Les Westberg
 */
public class NhinDocQueryProxyWebServiceSecuredImpl implements NhinDocQueryProxy {

    private Log log = null;

    public NhinDocQueryProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected DocumentProcessHelper getDocumentProcessHelper() {
        return new DocumentProcessHelper();
    }

    public ServicePortDescriptor<RespondingGatewayQueryPortType> getServicePortDescriptor(
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_g0:
            return new NhinDocQueryServicePortDescriptor();
        default:
            return new NhinDocQueryServicePortDescriptor();
        }
    }

    /**
     * Calls the respondingGatewayCrossGatewayQuery method of the web service.
     * 
     * @param request The information for the web service.
     * @return The response from the web service.
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion,
            NhinTargetSystemType target) throws Exception {
        AdhocQueryResponse response = new AdhocQueryResponse();

        try {
            String url = target.getUrl();
            if (NullChecker.isNullish(url)) {
                url = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(
                        target.getHomeCommunity().getHomeCommunityId(), NhincConstants.DOC_QUERY_SERVICE_NAME);
                log.debug("After target system URL look up. URL for service: " + NhincConstants.DOC_QUERY_SERVICE_NAME
                        + " is: " + url);
            }

            ServicePortDescriptor<RespondingGatewayQueryPortType> portDescriptor = getServicePortDescriptor(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);

            CONNECTClient<RespondingGatewayQueryPortType> client = CONNECTClientFactory.getInstance()
                    .getCONNECTClientSecured(portDescriptor, url, assertion);

            WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
            wsHelper.addTargetCommunity((BindingProvider) client.getPort(), target);
            wsHelper.addServiceName((BindingProvider) client.getPort(), NhincConstants.DOC_QUERY_SERVICE_NAME);

            response = (AdhocQueryResponse) client.invokePort(RespondingGatewayQueryPortType.class,
                    "respondingGatewayCrossGatewayQuery", request);
            
        } catch (Exception ex) {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            throw ex;
        }
        return response;
    }
}
