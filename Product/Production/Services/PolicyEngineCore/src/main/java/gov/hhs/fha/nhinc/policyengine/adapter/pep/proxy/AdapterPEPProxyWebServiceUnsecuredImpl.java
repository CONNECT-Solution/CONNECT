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
package gov.hhs.fha.nhinc.policyengine.adapter.pep.proxy;

import gov.hhs.fha.nhinc.adapterpep.AdapterPEPPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.pep.proxy.service.AdapterPEPServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the concrete implementation for the Web based call to the AdapterPEP.
 */
public class AdapterPEPProxyWebServiceUnsecuredImpl implements AdapterPEPProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPEPProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPEPProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<AdapterPEPPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<AdapterPEPPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * Given a request to check the access policy, this service will interface with the PDP to determine if access is to
     * be granted or denied.
     *
     * @param request The xacml request to check defined policy
     * @return The xacml response which contains the access decision
     */
    @Override
    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType request, AssertionType assertion) {
        LOG.trace("Begin AdapterPEPProxyWebServiceUnsecuredImpl.checkPolicy");
        CheckPolicyResponseType checkPolicyResponse = new CheckPolicyResponseType();
        String serviceName = NhincConstants.ADAPTER_PEP_SERVICE_NAME;

        try {
            LOG.debug("Before target system URL look up.");
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            LOG.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);

            if (NullChecker.isNotNullish(url)) {

                ServicePortDescriptor<AdapterPEPPortType> portDescriptor = new AdapterPEPServicePortDescriptor();

                CONNECTClient<AdapterPEPPortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                checkPolicyResponse = (CheckPolicyResponseType) client.invokePort(AdapterPEPPortType.class,
                        "checkPolicy", request);
            } else {
                LOG.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterPEPProxyWebServiceUnsecuredImpl.checkPolicy.  Error: "
                    + ex.getMessage();
            LOG.error(message, ex);
            throw new RuntimeException(message, ex);
        }

        LOG.trace("End AdapterPEPProxyWebServiceUnsecuredImpl.checkPolicy");
        return checkPolicyResponse;
    }

}
