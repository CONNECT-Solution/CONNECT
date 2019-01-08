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
package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy;

import gov.hhs.fha.nhinc.adapterpolicyengineorchestrator.AdapterPolicyEngineOrchestratorPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.proxy.service.AdapterPolicyEngineOrchServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the concrete implementation for the web service based call to the AdapterPolicyEngineOrchestrator.
 *
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl implements AdapterPolicyEngineOrchProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<AdapterPolicyEngineOrchestratorPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<AdapterPolicyEngineOrchestratorPortType> portDescriptor, String url,
            AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * Given a request to check the access policy, this service will interface with the Adapter PEP to determine if
     * access is to be granted or denied.
     *
     * @param checkPolicyRequest The request to check defined policy
     * @return The response which contains the access decision
     */
    @Override
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion) {
        LOG.debug("Begin AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl.checkPolicy");
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();
        String serviceName = NhincConstants.ADAPTER_POLICY_ENGINE_ORCHESTRATOR_SERVICE_NAME;

        try {
            LOG.debug("Before target system URL look up.");
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            LOG.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);

            if (NullChecker.isNotNullish(url)) {
                ServicePortDescriptor<AdapterPolicyEngineOrchestratorPortType> portDescriptor = new AdapterPolicyEngineOrchServicePortDescriptor();

                CONNECTClient<AdapterPolicyEngineOrchestratorPortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                oResponse = (CheckPolicyResponseType) client.invokePort(
                        AdapterPolicyEngineOrchestratorPortType.class, "checkPolicy", checkPolicyRequest);
            } else {
                LOG.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl.checkPolicy.  Error: "
                    + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        LOG.debug("End AdapterPolicyEngineOrchProxyWebServiceUnsecuredImpl.checkPolicy");
        return oResponse;
    }

}
