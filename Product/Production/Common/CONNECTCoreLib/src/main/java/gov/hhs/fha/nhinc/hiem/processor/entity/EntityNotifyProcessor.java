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

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.NotifyBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

/**
 * 
 * 
 * @author Neil Webb
 */
public class EntityNotifyProcessor {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(EntityNotifyProcessor.class);

    public void processNotify(Notify notify, AssertionType assertion, String rawNotifyXml) {
        log.debug("Received Notify: " + rawNotifyXml);
        if (assertion == null) {
            log.warn("EntityNotifyProcessor.processNotify - The assertion was null for the entity notify message");
        } else {
            log.warn("EntityNotifyProcessor.processNotify - The assertion was not null for the entity notify message");
        }
        auditInputMessage(notify, assertion,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        NodeList notificationMessageNodes = getNotificationMessageNodes(rawNotifyXml);
        if (notificationMessageNodes != null) {
            for (int i = 0; i < notificationMessageNodes.getLength(); i++) {
                try {
                    Node notificationMessageNode = notificationMessageNodes.item(i);
                    processSingleNotify(notificationMessageNode, assertion, rawNotifyXml);
                } catch (XPathExpressionException ex) {
                    log.error("failed to process notify", ex);
                }
            }
        }
    }

    private void processSingleNotify(Node notificationMessageNode, AssertionType assertion, String rawNotifyXml)
            throws XPathExpressionException {
        if (assertion == null) {
            log.warn("EntityNotifyProcessor.processSingleNotify - The assertion was null for the entity notify message");
        } else {
            log.info("EntityNotifyProcessor.processSingleNotify - The assertion was not null for the entity notify message");
        }
        // Update notify
        // Policy check
        // Audit
        if (notificationMessageNode != null) {
            String nodeName = notificationMessageNode.getLocalName();
            log.debug("Node name: " + nodeName);
            if (notificationMessageNode instanceof Element) {
                Element notificationMessageElement = (Element) notificationMessageNode;
                // NotificationMessageHolderType notifyMessage =
                // buildNotificationMessageHolder(notificationMessageElement);
                HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
                try {
                    // TODO: Switch producer to "adapter" when NHIN Subscribe supports forwarding subscription to an
                    // adapter
                    List<HiemSubscriptionItem> subscriptions = service.RetrieveByNotificationMessage(
                            notificationMessageElement, "gateway");
                    if (subscriptions != null) {
                        log.info("found " + subscriptions.size() + " matching subscriptions");

                        for (HiemSubscriptionItem subscription : subscriptions) {
                            log.info("processing subscription.  SubscriptionReference=["
                                    + subscription.getSubscriptionReferenceXML() + "]");
                            if (subscription.getParentSubscriptionReferenceXML() != null) {
                                log.info("has parent - retrieving [" + subscription.getParentSubscriptionReferenceXML()
                                        + "]");
                                subscription = service.retrieveByLocalSubscriptionReference(subscription
                                        .getParentSubscriptionReferenceXML());
                            }
                            String endpoint = findNotifyEndpoint(subscription);
                            log.info("endpoint=" + endpoint);

                            log.debug("extracting reference parameters from consumer reference");
                            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
                            ReferenceParametersElements referenceParametersElements = referenceParametersHelper
                                    .createReferenceParameterElementsFromConsumerReference(subscription
                                            .getSubscribeXML());
                            log.debug("extracted reference parameters from consumer reference");

                            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
                            targetSystem.setUrl(endpoint);

                            log.debug("building notify");
                            Element subscriptionReferenceElement = null;
                            try {
                                subscriptionReferenceElement = XmlUtility.convertXmlToElement(subscription
                                        .getSubscriptionReferenceXML());
                            } catch (Exception ex) {
                                Logger.getLogger(EntityNotifyProcessor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            NotifyBuilder builder = new NotifyBuilder();
                            Element notifyElement = builder.buildNotifyFromSubscribe(notificationMessageElement,
                                    subscriptionReferenceElement);

                            if (log.isDebugEnabled()) {
                                log.debug("### Notify to send to NHIN: "
                                        + XmlUtility.serializeElementIgnoreFaults(notifyElement));
                            }
                            NhinHiemNotifyProxy notifyProxy = new NhinHiemNotifyProxyObjectFactory()
                                    .getNhinHiemNotifyProxy();
                            notifyProxy.notify(notifyElement, referenceParametersElements, assertion, targetSystem);
                        }
                    }
                } catch (SubscriptionRepositoryException ex) {
                    log.error("Error collecting subscription records: " + ex.getMessage(), ex);
                }
            }
        }
    }

    private String findNotifyEndpoint(HiemSubscriptionItem subscription) {
        log.debug("Begin findNotifyEndpoint");
        String endpoint = "";
        if (subscription != null) {
            String rawSubscribeXml = subscription.getSubscribeXML();
            if (rawSubscribeXml != null) {
                try {
                    String xpathQuery = "//*[local-name()='Subscribe']/*[local-name()='ConsumerReference']/*[local-name()='Address']";
                    Node addressNode = XmlUtility.performXpathQuery(rawSubscribeXml, xpathQuery);
                    if (addressNode != null) {
                        endpoint = XmlUtility.getNodeValue(addressNode);
                        log.debug("Endpoint extracted from subscribe message: " + endpoint);
                    }
                } catch (XPathExpressionException ex) {
                    log.error("Error extracting the endpoint from a subscribe message: " + ex.getMessage(), ex);
                }
            }
        }
        log.debug("End findNotifyEndpoint");
        return endpoint;
    }

    private NodeList getNotificationMessageNodes(String rawNotifyXml) {
        NodeList msgNodes = null;
        try {
            javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
            javax.xml.xpath.XPath xpath = factory.newXPath();
            InputSource inputSource = new InputSource(new ByteArrayInputStream(rawNotifyXml.getBytes()));
            log.debug("About to perform notification message node xpath query");

            msgNodes = (NodeList) xpath.evaluate("//*[local-name()='Notify']/*[local-name()='NotificationMessage']",
                    inputSource, XPathConstants.NODESET);
            if ((msgNodes != null) && (msgNodes.getLength() > 0)) {
                log.debug("Message node list was not null/empty");
                for (int i = 0; i < msgNodes.getLength(); i++) {
                    Node childNode = msgNodes.item(i);
                    if (childNode != null) {
                        String nodeName = childNode.getLocalName();
                        log.debug("Node name: " + nodeName);
                    }
                }
            } else {
                log.debug("Message node or first child was null");
            }
        } catch (XPathExpressionException ex) {
            log.error(
                    "XPathExpressionException exception encountered loading the notify message body: "
                            + ex.getMessage(), ex);
        }
        return msgNodes;
    }

    /**
    * Create a generic log for Input messages.
    * @param notify The notify message to be audited
    * @param assertion The assertion element to be audited
    * @param direction The direction of the log to be audited (Inbound or Outbound)
    * @param logInterface The interface of the log to be audited (NHIN or Adapter)
    */
    private void auditInputMessage(Notify notify, AssertionType assertion, String direction,
            String logInterface) {
        log.debug("In NhinHiemNotifyWebServiceProxy.auditInputMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(notify);

            LogEventRequestType auditLogMsg = auditLogger.logNhinNotifyRequest(message,
                    direction, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }
}
