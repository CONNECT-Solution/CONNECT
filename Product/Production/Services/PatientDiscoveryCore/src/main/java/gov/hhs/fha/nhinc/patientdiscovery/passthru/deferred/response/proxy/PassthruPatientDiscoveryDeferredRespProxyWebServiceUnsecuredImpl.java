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
package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncresp.NhincProxyPatientDiscoveryAsyncRespPortType;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy.service.NhincProxyPatientDiscoveryAsyncRespServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;

/**
 * 
 * @author Neil Webb
 */
public class PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl implements
        PassthruPatientDiscoveryDeferredRespProxy {
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion,
            NhinTargetSystemType targetSystem) {
        log.debug("Begin PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl.proxyProcessPatientDiscoveryAsyncResp(...)");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        String serviceName = NhincConstants.PATIENT_DISCOVERY_PASSTHRU_ASYNC_RESP_SERVICE_NAME;

        try {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if (log.isDebugEnabled()) {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url)) {
                ProxyPRPAIN201306UVProxyRequestType wsRequest = new ProxyPRPAIN201306UVProxyRequestType();
                wsRequest.setPRPAIN201306UV02(request);
                wsRequest.setAssertion(assertion);
                wsRequest.setNhinTargetSystem(targetSystem);
                ServicePortDescriptor<NhincProxyPatientDiscoveryAsyncRespPortType> portDescriptor = new NhincProxyPatientDiscoveryAsyncRespServicePortDescriptor();
                CONNECTClient<NhincProxyPatientDiscoveryAsyncRespPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);
                response = (MCCIIN000002UV01) client.invokePort(NhincProxyPatientDiscoveryAsyncRespPortType.class,
                        "proxyProcessPatientDiscoveryAsyncResp", wsRequest);
            } else {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error: Failed to retrieve url for service: " + serviceName + " for local home community");
            log.error(ex.getMessage(), ex);
        }

        log.debug("End PassthruPatientDiscoveryDeferredRespProxyWebServiceUnsecuredImpl.proxyProcessPatientDiscoveryAsyncResp(...)");
        return response;
    }

}
