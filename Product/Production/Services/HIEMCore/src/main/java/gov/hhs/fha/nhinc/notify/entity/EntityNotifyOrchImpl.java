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
package gov.hhs.fha.nhinc.notify.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.NotifyBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class EntityNotifyOrchImpl {

    private static Log log = LogFactory.getLog(EntityNotifyOrchImpl.class);

    /**
     * Generic constructor.
     */
    public EntityNotifyOrchImpl() {
        log = getLogger();
    }

    /**
     * Return the logger.
     * 
     * @return the logger
     */
    protected Log getLogger() {
        return log;
    }

    /**
     * This method performs the entity orchestration for an notify at the entity.
     * 
     * @param notify - This request
     * @param assertion - The assertion of the message
     * @param rawNotifyXml - The target of the request
     * @throws Exception
     */
    public void processNotify(Notify notify, AssertionType assertion, String rawNotifyXml) throws Exception {

        auditRequestFromAdapter(notify, assertion);

        log.debug("Received Notify: " + rawNotifyXml);

        auditInputMessage(notify, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        
        NodeList notificationMessageNodes = getNotificationMessageNodes(rawNotifyXml);
        if (notificationMessageNodes != null) {
            for (int i = 0; i < notificationMessageNodes.getLength(); i++) {
                try {
                    Node notificationMessageNode = notificationMessageNodes.item(i);
                    processSingleNotify(notificationMessageNode, assertion);
                } catch (XPathExpressionException ex) {
                    log.error("failed to process notify", ex);
                }
            }
        }

        // auditResponseToAdapter(response, assertion);
    }

    private void processSingleNotify(Node notificationMessageNode, AssertionType assertion)
            throws XPathExpressionException {

        if (notificationMessageNode != null) {
            String nodeName = notificationMessageNode.getLocalName();
            log.debug("Node name: " + nodeName);
            if (notificationMessageNode instanceof Element) {
                Element notificationMessageElement = (Element) notificationMessageNode;
                HiemSubscriptionRepositoryService serviceDAO = new HiemSubscriptionRepositoryService();
                try {
                    // TODO: Switch producer to "adapter" when NHIN Subscribe supports forwarding subscription
                    // to an adapter
                    List<HiemSubscriptionItem> subscriptions = serviceDAO.RetrieveByNotificationMessage(
                            notificationMessageElement, "gateway");
                    if (subscriptions != null) {
                        log.info("found " + subscriptions.size() + " matching subscriptions");

                        for (HiemSubscriptionItem subscription : subscriptions) {
                            String subscriptionRef = subscription.getSubscriptionReferenceXML();
                            String parentSubscriptionRef = subscription.getParentSubscriptionReferenceXML();
                            
                            log.info("processing subscription.  SubscriptionReference=[" + subscriptionRef + "]");
                            if (parentSubscriptionRef != null) {
                                log.info("has parent - retrieving [" + parentSubscriptionRef + "]");
                                subscription = serviceDAO.retrieveByLocalSubscriptionReference(parentSubscriptionRef);
                            }
                            String endpoint = findNotifyEndpoint(subscription);
                            log.info("endpoint=" + endpoint);

                            log.debug("extracting reference parameters from consumer reference");
                            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
                            SoapMessageElements referenceParametersElements = referenceParametersHelper
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
                                Logger.getLogger(EntityNotifyOrchImpl.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            NotifyBuilder builder = new NotifyBuilder();
                            Notify notifyElement = builder.buildNotifyFromSubscribe(notificationMessageElement,
                                    subscriptionReferenceElement);

                            sendRequestToTarget(notifyElement, referenceParametersElements, assertion, targetSystem);
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
                    String xpathQuery = "//*[local-name()='Subscribe']/*[local-name()='ConsumerReference']"
                            + "/*[local-name()='Address']";
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
     * Audit the request from the adapter.
     * 
     * @param request The request to be audited
     * @param assertion The assertion to be audited
     */
    private void auditRequestFromAdapter(Notify request, AssertionType assertion) {
        log.debug("In EntitysubscribeOrchImpl.auditInputMessage");

        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            NotifyRequestType message = new NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(request);

            LogEventRequestType auditLogMsg = auditLogger.logNhinNotifyRequest(message,
                    NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    /**
     * Send subscription response to target.
     * 
     * @param request The subscribe to send.
     * @param assertion The assertion to send
     * @param targetCommunitites The targets to be sent to
     * @return the response from the foreign entity
     */
    private void sendRequestToTarget(Notify request, SoapMessageElements referenceParameters,
            AssertionType assertion, NhinTargetSystemType targetSystem) {
        if (isPolicyValid(request, assertion)) {
            log.info("Policy check successful");
            // send request to nhin proxy
            try {
                sendToNhinProxy(request, referenceParameters, assertion, targetSystem);
            } catch (Exception e) {
                // TODO nhinResponse = createFailedNhinSendResponse(hcid);
                String hcid = targetSystem.getHomeCommunity().getHomeCommunityId();
                log.error("Fault encountered while trying to send message to the nhin " + hcid, e);
            }
        } else {
            log.error("Failed policy check.  Sending error response.");
        }
    }

    /**
     * Sends the request to the nhin proxy.
     * 
     * @param request the request to be sent
     * @param referenceParameters the reference parameters to be used
     * @param assertion the assertion to be used
     * @param nhinTargetSystem the nhin target system to be used
     */
    private void sendToNhinProxy(Notify request, SoapMessageElements referenceParameters,
            AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {

        OutboundNotifyDelegate dsDelegate = getOutboundNotifyDelegate();
        OutboundNotifyOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, request, referenceParameters,
                assertion, nhinTargetSystem);
        dsDelegate.process(dsOrchestratable);
    }

    /**
     * Returns the OutboundNotifyDelegate.
     * 
     * @return the OutboundNotifyDelegate
     */
    protected OutboundNotifyDelegate getOutboundNotifyDelegate() {
        return new OutboundNotifyDelegate();
    }

    /**
     * Create a notify orchestratable.
     * 
     * @param delegate The delegate to be used by the orchestratable
     * @param request The request to be added to the orchestrtable
     * @param referenceParameters The reference parameters to be added to the orchestratable
     * @param assertion The assertion to be added to the orchestratable
     * @param nhinTargetSystem The target system to be added to the orchestratable
     * @return the orchestratable object
     */
    private OutboundNotifyOrchestratable createOrchestratable(OutboundNotifyDelegate delegate, Notify request,
            SoapMessageElements referenceParameters, AssertionType assertion,
            NhinTargetSystemType nhinTargetSystem) {

        OutboundNotifyOrchestratable dsOrchestratable = new OutboundNotifyOrchestratable(delegate);
        dsOrchestratable.setAssertion(assertion);
        dsOrchestratable.setRequest(request);
        dsOrchestratable.setReferenceParameters(referenceParameters);
        dsOrchestratable.setTarget(nhinTargetSystem);

        return dsOrchestratable;
    }

    /**
     * Check if policy for message is valid.
     * 
     * @param notify The message to be checked.
     * @param assertion The assertion to be checked.
     * @return
     */
    private boolean isPolicyValid(Notify notifyRequest, AssertionType assertion) {
        log.debug("In HiemNotifyImpl.checkPolicy");
        boolean policyIsValid = false;

        NotifyEventType policyCheckReq = new NotifyEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType();
        request.setAssertion(assertion);
        request.setNotify(notifyRequest);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyNotify(policyCheckReq);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished HiemNotifyImpl.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    /**
     * Check if there is a valid target to send the request to.
     * 
     * @param targetCommunity The community object to check for targets
     * @return true if there is a valid target
     */
    protected boolean hasNhinTargetHomeCommunityId(NhinTargetCommunityType targetCommunity) {

        if (targetCommunity != null && targetCommunity.getHomeCommunity() != null
                && NullChecker.isNotNullish(targetCommunity.getHomeCommunity().getHomeCommunityId())) {
            return true;
        }

        return false;
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
