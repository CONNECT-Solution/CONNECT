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
package gov.hhs.fha.nhinc.subscribe.entity;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.common.HiemProcessorConstants;
import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionItemUtil;
import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionStorage;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsrf.rw_2.ResourceUnknownFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

public class EntitySubscribeOrchImpl {

    private static Log log = LogFactory.getLog(EntitySubscribeOrchImpl.class);

    public EntitySubscribeOrchImpl() {
        log = getLogger();
    }

    protected Log getLogger() {
        return log;
    }

    /**
     * This method performs the entity orchestration for a subscribe at the entity.
     * 
     * @param subscribe - This request
     * @param assertion - The assertion of the message
     * @param targetCommunitites - The target of the request
     * @return a subscription response of success or fail
     * @throws SubscribeCreationFailedFault
     * @throws ResourceUnknownFault
     */
    public SubscribeResponse processSubscribe(Subscribe subscribe, AssertionType assertion,
            NhinTargetCommunitiesType targetCommunities) throws SubscribeCreationFailedFault {
        SubscribeResponse response = null;

        auditInputMessage(subscribe, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        
        auditRequestFromAdapter(subscribe, assertion);

        // save parent subscription
        EndpointReferenceType parentSubscriptionReference = storeSubscription(subscribe, assertion, targetCommunities);

        // update parent subscription
        updateSubscribeNotificationConsumerEndpointAddress(subscribe);

        for (NhinTargetCommunityType targetCommunity : targetCommunities.getNhinTargetCommunity()) {
            if (isPolicyValid(subscribe, assertion)) {
                log.info("Policy check successful");
                // send request to nhin proxy
                response = getResponseFromTarget(subscribe, assertion, targetCommunity);

                // save child subscription
                if (response != null) {
                    storeChildSubscription(subscribe, response, parentSubscriptionReference);
                }
            } else {
                log.error("Failed policy check.  Sending error response.");
                response = createFailedPolicyCheckResponse();
            }
        }

        // Return the parent subscription reference to the adapter
        response = new SubscribeResponse();
        response.setSubscriptionReference(parentSubscriptionReference);

        auditResponseToAdapter(response, assertion);
        
        auditResponseMessage(response, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return response;
    }

    private SubscribeResponse createFailedPolicyCheckResponse() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Audit the request from the adapter.
     * 
     * @param request The request to be audited
     * @param assertion The assertion to be audited
     */
    private void auditRequestFromAdapter(Subscribe subscribe, AssertionType assertion) {
        log.debug("In EntitysubscribeOrchImpl.auditInputMessage");

        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message,
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
     * Audit the request to the adapter.
     * 
     * @param response The response to be audited
     * @param assertion The assertion to be audited
     */
    private void auditResponseToAdapter(SubscribeResponse response, AssertionType assertion) {
        log.debug("In EntitysubscribeOrchImpl.auditResponseMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType message = new gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message,
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe response message: " + t.getMessage(), t);
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
    private SubscribeResponse getResponseFromTarget(Subscribe request, AssertionType assertion,
            NhinTargetCommunityType targetCommunity) {

        SubscribeResponse nhinResponse = new SubscribeResponse();
        if (hasNhinTargetHomeCommunityId(targetCommunity)) {
            try {
                nhinResponse = sendToNhinProxy(request, assertion, targetCommunity);
            } catch (Exception e) {
                // TODO nhinResponse = createFailedNhinSendResponse(hcid);
                String hcid = targetCommunity.getHomeCommunity().getHomeCommunityId();
                log.error("Fault encountered while trying to send message to the nhin " + hcid, e);
            }
        } else {
            log.warn("The request to the Nhin did not contain a target home community id.");
        }

        return nhinResponse;
    }

    private SubscribeResponse sendToNhinProxy(Subscribe subscribe, AssertionType assertion,
            NhinTargetCommunityType nhinTargetCommunity) {

        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        nhinTargetSystem.setHomeCommunity(nhinTargetCommunity.getHomeCommunity());

        OutboundSubscribeDelegate dsDelegate = getOutboundSubscribeDelegate();
        OutboundSubscribeOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, subscribe, assertion,
                nhinTargetSystem);
        SubscribeResponse response = ((OutboundSubscribeOrchestratable) dsDelegate.process(dsOrchestratable))
                .getResponse();

        return response;
    }

    protected OutboundSubscribeDelegate getOutboundSubscribeDelegate() {
        return new OutboundSubscribeDelegate();
    }

    private OutboundSubscribeOrchestratable createOrchestratable(OutboundSubscribeDelegate delegate, Subscribe request,
            AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {

        OutboundSubscribeOrchestratable dsOrchestratable = new OutboundSubscribeOrchestratable(delegate);
        dsOrchestratable.setAssertion(assertion);
        dsOrchestratable.setRequest(request);
        dsOrchestratable.setTarget(nhinTargetSystem);

        return dsOrchestratable;
    }

    /**
     * Check if policy for message is valid.
     * 
     * @param subscribe The message to be checked.
     * @param assertion The assertion to be checked.
     * @return
     */
    private boolean isPolicyValid(Subscribe subscribe, AssertionType assertion) {
        log.debug("In NhinHiemSubscribeWebServiceProxy.checkPolicy");
        boolean policyIsValid = false;

        SubscribeEventType policyCheckReq = new SubscribeEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType();
        request.setAssertion(assertion);
        request.setSubscribe(subscribe);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubscribe(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemSubscribeWebServiceProxy.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    /**
     * Check if there is a valid target to send the request to.
     * 
     * @param targetCommunities The communities object to check for targets
     * @return true if there is a valid target
     */
    protected boolean hasNhinTargetHomeCommunityId(NhinTargetCommunityType targetCommunity) {

        if (targetCommunity != null && targetCommunity.getHomeCommunity() != null
                && NullChecker.isNotNullish(targetCommunity.getHomeCommunity().getHomeCommunityId())) {
            return true;
        }

        return false;
    }

    protected EndpointReferenceType storeSubscription(Subscribe subscribe, AssertionType assertion,
            NhinTargetCommunitiesType targetCommunitites) {
        EndpointReferenceType subscriptionReference = null;

        String targetCommunitiesXml = serializeTargetCommunities(targetCommunitites);

        SubscriptionItemUtil subscriptionItemUtil = new SubscriptionItemUtil();
        HiemSubscriptionItem subscriptionItem = subscriptionItemUtil.createSubscriptionItem(subscribe, null,
                HiemProcessorConstants.CONSUMER_ADAPTER, HiemProcessorConstants.PRODUCER_GATEWAY, targetCommunitiesXml);
        SubscriptionStorage storage = new SubscriptionStorage();
        subscriptionReference = storage.storeSubscriptionItem(subscriptionItem);
        return subscriptionReference;
    }

    protected void storeChildSubscription(Subscribe subscribe, SubscribeResponse response,
            EndpointReferenceType parentSubscriptionReference) {
        // ******Convert Parent Reference to XML
        String parentSubscriptionReferenceXml = null;
        if (parentSubscriptionReference != null) {
            parentSubscriptionReferenceXml = serializeEndpointReferenceType(parentSubscriptionReference);
        }

        // ******Convert Subscription Reference (response) to XML
        String childSubscriptionReference = null;

        // Use reflection to get the correct subscription reference object
        Object subRef = getSubscriptionReference(response);
        if (subRef != null) {
            if (subRef.getClass().isAssignableFrom(EndpointReferenceType.class)) {
                childSubscriptionReference = serializeEndpointReferenceType((EndpointReferenceType) subRef);
            } else if (subRef.getClass().isAssignableFrom(W3CEndpointReference.class)) {
                childSubscriptionReference = serializeW3CEndpointReference((W3CEndpointReference) subRef);
            } else {
                log.error("Unknown subscription reference type: " + subRef.getClass().getName());
            }
        } else {
            log.error("Subscription reference was null");
        }

        // ******Convert Subscription (subscribe) to XML
        String childSubscribeXml;
        try {
            SubscribeMarshaller marshaller = new SubscribeMarshaller();
            Element subscribeElement = marshaller.marshalSubscribe(subscribe);
            childSubscribeXml = XmlUtility.serializeElement(subscribeElement);
        } catch (Exception ex) {
            log.error("failed to process subscribe xml", ex);
            childSubscribeXml = null;
        }

        HiemSubscriptionItem subscriptionItem = new HiemSubscriptionItem();
        subscriptionItem.setSubscriptionReferenceXML(childSubscriptionReference);
        subscriptionItem.setParentSubscriptionReferenceXML(parentSubscriptionReferenceXml);
        subscriptionItem.setConsumer(HiemProcessorConstants.CONSUMER_GATEWAY);
        subscriptionItem.setProducer(HiemProcessorConstants.PRODUCER_NHIN);
        subscriptionItem.setSubscribeXML(childSubscribeXml);

        SubscriptionStorage storage = new SubscriptionStorage();
        storage.storeExternalSubscriptionItem(subscriptionItem);
    }

    protected String serializeTargetCommunities(NhinTargetCommunitiesType targetCommunitites) {
        String targetCommunitiesXml = null;
        if (targetCommunitites != null) {
            try {
                gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory ncCommonObjFact = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(ncCommonObjFact.createNhinTargetCommunities(targetCommunitites), swXML);
                targetCommunitiesXml = swXML.toString();
                log.debug("Marshaled subscription reference: " + targetCommunitiesXml);
            } catch (JAXBException ex) {
                log.error("Error serializing the target communitites: " + ex.getMessage(), ex);
            }
        }
        return targetCommunitiesXml;
    }

    protected String serializeEndpointReferenceType(EndpointReferenceType endpointRefernece) {
        String endpointReferenceXml = null;
        if (endpointRefernece != null) {
            try {
                org.w3._2005._08.addressing.ObjectFactory wsaObjFact = new org.w3._2005._08.addressing.ObjectFactory();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("org.w3._2005._08.addressing");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(wsaObjFact.createEndpointReference(endpointRefernece), swXML);
                endpointReferenceXml = swXML.toString();
                log.debug("Marshaled endpoint reference: " + endpointReferenceXml);
            } catch (JAXBException ex) {
                log.error("Error serializing the endpoint reference: " + ex.getMessage(), ex);
            }
        }
        return endpointReferenceXml;
    }

    protected void updateSubscribeNotificationConsumerEndpointAddress(Subscribe subscribe) {
        try {
            String notificationConsumerEndpointAddress = PropertyAccessor.getInstance().getProperty("gateway",
                    "NotificationConsumerEndpointAddress");
            subscribe.getConsumerReference().getAddress().setValue(notificationConsumerEndpointAddress);
        } catch (PropertyAccessException ex) {
            log.error("Error retrieving the notification consumer endpoint address: " + ex.getMessage(), ex);
        }
    }

    protected Object getSubscriptionReference(SubscribeResponse subscribeResponse) {
        Object o = null;
        if (subscribeResponse != null) {
            Method[] methods = subscribeResponse.getClass().getDeclaredMethods();
            if (methods != null) {
                log.debug("Method count: " + methods.length);
                for (Method m : methods) {
                    log.debug("Looking at method: " + m.getName());
                    if (m.getName().equals("getSubscriptionReference")) {
                        try {
                            log.debug("Return type of getSubscriptionReference method: " + m.getReturnType().getName());
                            Object[] params = {};
                            o = m.invoke(subscribeResponse, params);
                            break;
                        } catch (IllegalAccessException ex) {
                            log.error(
                                    "IllegalAccessException calling getSubscriptionReference method: "
                                            + ex.getMessage(), ex);
                        } catch (IllegalArgumentException ex) {
                            log.error(
                                    "IllegalArgumentException calling getSubscriptionReference method: "
                                            + ex.getMessage(), ex);
                        } catch (InvocationTargetException ex) {
                            log.error(
                                    "InvocationTargetException calling getSubscriptionReference method: "
                                            + ex.getMessage(), ex);
                        }
                    }
                }
            } else {
                log.debug("Methods were null");
            }
        }
        return o;
    }

    protected String serializeW3CEndpointReference(W3CEndpointReference endpointRefernece) {
        String endpointReferenceXml = null;
        if (endpointRefernece != null) {
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("javax.xml.ws.wsaddressing");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(endpointRefernece, swXML);
                endpointReferenceXml = swXML.toString();
                log.debug("Marshaled W3C endpoint reference: " + endpointReferenceXml);
            } catch (JAXBException ex) {
                log.error("Error serializing the W3C endpoint reference: " + ex.getMessage(), ex);
            }
        }
        return endpointReferenceXml;
    }

    /**
     * Create generic audit log for Input requests.
     * 
     * @param subscribe The subscribe message to be audited
     * @param assertion The assertion to be audited
     * @param logDirection The direction of the log being audited (Inbound or Outbound)
     * @param logInterface The interface of the log being audited (NHIN or Adapter)
     */
    private void auditInputMessage(Subscribe subscribe, AssertionType assertion, String logDirection,
            String logInterface) {
        log.debug("In EntitySubscribeProcessor.auditInputMessage");
        AcknowledgementType ack = null;
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType();
            message.setAssertion(assertion);
            message.setSubscribe(subscribe);

            LogEventRequestType auditLogMsg = auditLogger.logNhinSubscribeRequest(message, logDirection, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

    /**
     * Create generic audit log for response messages.
     * 
     * @param response the response to be audited
     * @param assertion the assertion to be audited
     * @param logDirection the direction of the log to be audited (Inbound or Outbound)
     * @param logInterface the interface of the log being audited (NHIN or Adapter)
     */
    private void auditResponseMessage(SubscribeResponse response, AssertionType assertion, String logDirection,
            String logInterface) {
        log.debug("In EntitySubscribeProcessor.auditResponseMessage");
        try {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType message = new gov.hhs.fha.nhinc.common.hiemauditlog.SubscribeResponseMessageType();
            message.setAssertion(assertion);
            message.setSubscribeResponse(response);

            LogEventRequestType auditLogMsg = auditLogger.logSubscribeResponse(message, logDirection, logInterface);

            if (auditLogMsg != null) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                proxy.auditLog(auditLogMsg, assertion);
            }
        } catch (Throwable t) {
            log.error("Error logging subscribe response message: " + t.getMessage(), t);
        }
    }
}
