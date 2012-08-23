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
package gov.hhs.fha.nhinc.docquery.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.passthru.proxy.description.PassthruDocQueryServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author JHOPPESC
 */
public class PassthruDocQueryProxyWebServiceUnsecuredImpl implements PassthruDocQueryProxy {
    private Log log = null;

    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocQueryProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public ServicePortDescriptor<NhincProxyDocQueryPortType> getServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_a0:
            return new PassthruDocQueryServicePortDescriptor();
        default:
            return new PassthruDocQueryServicePortDescriptor();
        }
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, AssertionType assertion,
            NhinTargetSystemType target) throws Exception {
        log.debug("Begin respondingGatewayCrossGatewayQuery");
        AdhocQueryResponse response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.NHINC_PROXY_DOC_QUERY_SERVICE_NAME);

            if (body == null) {
                log.error("Message was null");
            } else if (assertion == null) {
                log.error("AssertionType was null");
            } else if (target == null) {
                log.error("target was null");
            } else {
                RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
                request.setAdhocQueryRequest(body);
                request.setAssertion(assertion);
                request.setNhinTargetSystem(target);
                
                ServicePortDescriptor<NhincProxyDocQueryPortType> portDescriptor = getServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                CONNECTClient<NhincProxyDocQueryPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                response = (AdhocQueryResponse) client.invokePort(NhincProxyDocQueryPortType.class,
                        "respondingGatewayCrossGatewayQuery", request);
            }
        } catch (Exception ex) {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            throw ex;

        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}
