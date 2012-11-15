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
package gov.hhs.fha.nhinc.hiem._20.subscribe.passthru;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.subscribe.nhin.proxy.NhinHiemSubscribeProxy;
import gov.hhs.fha.nhinc.subscribe.nhin.proxy.NhinHiemSubscribeProxyObjectFactory;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 * @author Jon Hoppesch
 */
public class ProxyHiemSubscribeImpl {

    private static Log log = LogFactory.getLog(ProxyHiemSubscribeImpl.class);

    public SubscribeResponse subscribe(SubscribeRequestType subscribeRequest, WebServiceContext context)
            throws Exception {
        log.debug("Entering ProxyHiemSubscribeImpl.subscribe...");
        SubscribeResponse resp = null;

        NhinHiemSubscribeProxyObjectFactory hiemSubscribeFactory = new NhinHiemSubscribeProxyObjectFactory();
        NhinHiemSubscribeProxy proxy = hiemSubscribeFactory.getNhinHiemSubscribeProxy();

        resp = proxy.subscribe(subscribeRequest.getSubscribe(), subscribeRequest.getAssertion(),
                subscribeRequest.getNhinTargetSystem());

       log.debug("Exiting ProxyHiemSubscribeImpl.subscribe...");
        return resp;
    }

    public SubscribeResponse subscribe(SubscribeRequestSecuredType subscribeRequest, WebServiceContext context)
            throws Exception {
        log.debug("Entering Secured ProxyHiemSubscribeImpl.subscribe...");
        SubscribeResponse resp = null;

        NhinHiemSubscribeProxyObjectFactory hiemSubscribeFactory = new NhinHiemSubscribeProxyObjectFactory();
        NhinHiemSubscribeProxy proxy = hiemSubscribeFactory.getNhinHiemSubscribeProxy();

        resp = proxy.subscribe(subscribeRequest.getSubscribe(), SAML2AssertionExtractor.getInstance().extractSamlAssertion(context),
                subscribeRequest.getNhinTargetSystem());

        log.debug("Exiting Secured ProxyHiemSubscribeImpl.subscribe...");
        return resp;
    }

}
