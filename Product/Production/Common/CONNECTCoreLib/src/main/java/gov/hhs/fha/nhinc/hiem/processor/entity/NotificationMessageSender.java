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
package gov.hhs.fha.nhinc.hiem.processor.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * Helper class used to send a notify message to a remote gateway.
 * 
 * @author Neil Webb
 */
public class NotificationMessageSender {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(NotificationMessageSender.class);

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitynotificationconsumer";
    private static final String SERVICE_LOCAL_PART = "EntityNotificationConsumerSecured";
    private static final String PORT_LOCAL_PART = "EntityNotificationConsumerSecuredPortSoap";
    private static final String WSDL_FILE = "EntityNotificationConsumerSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:Notify";

    /**
     * Send a notify message to a remote gateway.
     * 
     * @param notifyMessage Notify message to be sent
     * @param assertion Assertion to be used when sending the message.
     * @param endpointAddress URL to the remote gateway
     */
    public void sendNotify(NotificationMessageHolderType notifyMessage, AssertionType assertion, String endpointAddress) {
        if (assertion == null) {
            log.warn("NotificationMessageSender - The assertion was null for the entity notify message");
        } else {
            log.warn("NotificationMessageSender - The assertion was not null for the entity notify message");
        }

        try { // Call Web Service Operation
            org.oasis_open.docs.wsn.b_2.Notify notify = new org.oasis_open.docs.wsn.b_2.Notify();
            notify.getNotificationMessage().add(notifyMessage);

            org.oasis_open.docs.wsn.bw_2.NotificationConsumer port = getPort(endpointAddress, assertion);
            port.notify(notify);
        } catch (Exception ex) {
            log.error("Error sending Notify: " + ex.getMessage(), ex);
        }
    }

    protected org.oasis_open.docs.wsn.bw_2.NotificationConsumer getPort(String url, AssertionType assertIn) {
        org.oasis_open.docs.wsn.bw_2.NotificationConsumer oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        org.oasis_open.docs.wsn.bw_2.NotificationConsumer.class);

                // Initialize secured port
                getWebServiceProxyHelper().initializeSecurePort((BindingProvider) oPort, url,
                        NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED, WS_ADDRESSING_ACTION, assertIn);
            } else {
                log.error("Unable to obtain serivce - no port created.");
            }
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return oPort;
    }

    private WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }

    private Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                cachedService = getWebServiceProxyHelper().createService(wsdl, uri, service);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
