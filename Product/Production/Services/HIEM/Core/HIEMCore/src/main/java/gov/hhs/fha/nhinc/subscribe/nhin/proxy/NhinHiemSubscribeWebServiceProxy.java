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
package gov.hhs.fha.nhinc.subscribe.nhin.proxy;

import javax.xml.ws.BindingProvider;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.subscribe.nhin.proxy.service.NhinHiemSubscribeServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.NotificationProducer;

/**
 * 
 * @author Jon Hoppesch
 */
public class NhinHiemSubscribeWebServiceProxy implements NhinHiemSubscribeProxy {

    private static Log log = LogFactory.getLog(NhinHiemSubscribeWebServiceProxy.class);

    protected CONNECTClient<NotificationProducer> getCONNECTClientSecured(
            ServicePortDescriptor<NotificationProducer> portDescriptor, String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

    @Override
    public SubscribeResponse subscribe(Subscribe subscribe, AssertionType assertion, NhinTargetSystemType target)
            throws Exception {
        SubscribeResponse response = null;

        try {
            String url = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTarget(target,
                    NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME);

            if (NullChecker.isNullish(url)) {
                log.error("The URL for service: " + NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME + " is null");
            } else if (target == null) {
                log.error("Target system passed into the proxy is null");
            } else {
                ServicePortDescriptor<NotificationProducer> portDescriptor = new NhinHiemSubscribeServicePortDescriptor();

                CONNECTClient<NotificationProducer> client = getCONNECTClientSecured(portDescriptor, url, assertion);

                WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
                wsHelper.addTargetCommunity((BindingProvider) client.getPort(), target);
                wsHelper.addTargetApiLevel((BindingProvider) client.getPort(), GATEWAY_API_LEVEL.LEVEL_g0);
                wsHelper.addServiceName((BindingProvider) client.getPort(), 
                        NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME);
                
                response = (SubscribeResponse) client.invokePort(NotificationProducer.class, "subscribe", subscribe);
            }

        } catch (Exception ex) {
            log.error("Error sending subscribe to remote gateway.", ex);
        }

        return response;
    }

}
