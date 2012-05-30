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
package gov.hhs.fha.nhinc.hiem._20.entity.proxy;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxyObjectFactory;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author jhoppesc
 */
public class ProxyHiemNotifyImpl {

    private static Log log = LogFactory.getLog(ProxyHiemNotifyImpl.class);

    public void notify(NotifyRequestType notifyRequest, WebServiceContext context) {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");

        // Log the start of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                NhincConstants.HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        Element notifyElement = new SoapUtil().extractFirstElement(context, "notifySoapMessage", "Notify");

        log.debug("extracting soap header elements");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper
                .createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted soap header elements");

        // Audit the HIEM Notify Request Message sent on the Nhin Interface
        audit(notifyRequest);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notifyElement, referenceParametersElements, notifyRequest.getAssertion(),
                notifyRequest.getNhinTargetSystem());

        // Log the end of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                NhincConstants.HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    public void notify(NotifyRequestSecuredType notifyRequest, WebServiceContext context) {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");
        // Log the start of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                NhincConstants.HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        Element notifyElement = new SoapUtil().extractFirstElement(context, "notifySoapMessage", "Notify");

        log.debug("NOTIFY MESSAGE RECEIVED FROM SECURED INTERFACE: "
                + XmlUtility.serializeElementIgnoreFaults(notifyElement));

        log.debug("extracting soap header elements");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        // ReferenceParametersElements referenceParametersElements =
        // referenceParametersHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper
                .createReferenceParameterElements(context, "notifySoapMessage");
        log.debug("extracted soap header elements");

        // Audit the HIEM Notify Request Message sent on the Nhin Interface
        audit(notifyRequest);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notifyElement, referenceParametersElements, SamlTokenExtractor.GetAssertion(context),
                notifyRequest.getNhinTargetSystem());

        // Log the end of the nhin performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                NhincConstants.HIEM_NOTIFY_PROXY_SERVICE_NAME_SECURED, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    private AcknowledgementType audit(NotifyRequestType notifyRequest) {
        AcknowledgementType ack = null;

        return ack;
    }

    private AcknowledgementType audit(NotifyRequestSecuredType notifyRequest) {
        AcknowledgementType ack = null;

        return ack;
    }
}
