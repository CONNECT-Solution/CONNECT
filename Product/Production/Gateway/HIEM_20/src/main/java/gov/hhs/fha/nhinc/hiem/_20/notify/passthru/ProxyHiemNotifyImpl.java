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
package gov.hhs.fha.nhinc.hiem._20.notify.passthru;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapHeaderHelper;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.notify.nhin.proxy.NhinHiemNotifyProxy;
import gov.hhs.fha.nhinc.notify.nhin.proxy.NhinHiemNotifyProxyObjectFactory;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 * 
 * @author jhoppesc
 */
public class ProxyHiemNotifyImpl {

    private static Log log = LogFactory.getLog(ProxyHiemNotifyImpl.class);

    public void notify(NotifyRequestType notifyRequest, WebServiceContext context) {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");
        Notify notify = notifyRequest.getNotify();

        SoapMessageElements soapHeaderElements = new SoapHeaderHelper().getSoapHeaderElements(context);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notify, soapHeaderElements, notifyRequest.getAssertion(), notifyRequest.getNhinTargetSystem());
        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    public void notify(NotifyRequestSecuredType notifyRequest, WebServiceContext context) {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");
        Notify notify = notifyRequest.getNotify();

        SoapMessageElements soapHeaderElements = new SoapHeaderHelper().getSoapHeaderElements(context);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notify, soapHeaderElements, SAML2AssertionExtractor.getInstance().extractSamlAssertion(context),
                notifyRequest.getNhinTargetSystem());
        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

}
