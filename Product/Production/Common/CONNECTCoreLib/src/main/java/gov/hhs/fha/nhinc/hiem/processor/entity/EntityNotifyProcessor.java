/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.processor.entity;

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

/**
 * 
 * 
 * @author Neil Webb
 */
public class EntityNotifyProcessor {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityNotifyProcessor.class);

    public void processNotify(Notify notify, AssertionType assertion, String rawNotifyXml) {
        log.debug("Received Notify: " + rawNotifyXml);
        if (assertion == null) {
            log.warn("EntityNotifyProcessor.processNotify - The assertion was null for the entity notify message");
        } else {
            log.warn("EntityNotifyProcessor.processNotify - The assertion was not null for the entity notify message");
        }

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

    private void processSingleNotify(Node notificationMessageNode, AssertionType assertion, String rawNotifyXml) throws XPathExpressionException {
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
//                NotificationMessageHolderType notifyMessage = buildNotificationMessageHolder(notificationMessageElement);
                HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
                try {
                    // TODO: Switch producer to "adapter" when NHIN Subscribe supports forwarding subscription to an adapter
                    List<HiemSubscriptionItem> subscriptions = service.RetrieveByNotificationMessage(notificationMessageElement, "gateway");
                    if (subscriptions != null) {
                        log.info("found " + subscriptions.size() + " matching subscriptions");

                        for (HiemSubscriptionItem subscription : subscriptions) {
                            log.info("processing subscription.  SubscriptionReference=[" + subscription.getSubscriptionReferenceXML() + "]");
                            if (subscription.getParentSubscriptionReferenceXML() != null) {
                                log.info("has parent - retrieving [" + subscription.getParentSubscriptionReferenceXML() + "]");
                                subscription = service.retrieveByLocalSubscriptionReference(subscription.getParentSubscriptionReferenceXML());
                            }
                            String endpoint = findNotifyEndpoint(subscription);
                            log.info("endpoint=" + endpoint);

                            log.debug("extracting reference parameters from consumer reference");
                            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
                            ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromConsumerReference(subscription.getSubscribeXML());
                            log.debug("extracted reference parameters from consumer reference");

                            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
                            targetSystem.setUrl(endpoint);

                            log.debug("building notify");
                            Element subscriptionReferenceElement = null;
                            try {
                                subscriptionReferenceElement = XmlUtility.convertXmlToElement(subscription.getSubscriptionReferenceXML());
                            } catch (Exception ex) {
                                Logger.getLogger(EntityNotifyProcessor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            NotifyBuilder builder = new NotifyBuilder();
                            Element notifyElement = builder.buildNotifyFromSubscribe(notificationMessageElement, subscriptionReferenceElement);


                            if (log.isDebugEnabled()) {
                                log.debug("### Notify to send to NHIN: " + XmlUtility.serializeElementIgnoreFaults(notifyElement));
                            }
                            NhinHiemNotifyProxy notifyProxy = new NhinHiemNotifyProxyObjectFactory().getNhinHiemNotifyProxy();
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

            msgNodes = (NodeList) xpath.evaluate("//*[local-name()='Notify']/*[local-name()='NotificationMessage']", inputSource, XPathConstants.NODESET);
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
            log.error("XPathExpressionException exception encountered loading the notify message body: " + ex.getMessage(), ex);
        }
        return msgNodes;
    }
}
