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
package gov.hhs.fha.nhinc.unsubscribe.entity;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.TargetBuilder;
import gov.hhs.fha.nhinc.hiem.processor.faults.SubscriptionManagerSoapFaultFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.UnableToDestroySubscriptionFaultType;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;

public class EntityUnsubscribeOrchImpl {

		private static Log log = LogFactory.getLog(EntityUnsubscribeOrchImpl.class);
		
		public EntityUnsubscribeOrchImpl(){
			log = getLogger();
		}
		
		protected Log getLogger() {
	        return log;
	    }
		
		/**
		 * This method performs the entity orchestration for an unsubscribe at the entity.
		 * @param unsubscribe - This request
		 * @param assertion - The assertion of the message
		 * @param targetCommunitites - The target of the request
		 * @return a subscription response of success or fail
		 * @throws Exception 
		 * @throws UnableToDestroySubscriptionFault 
		 */
		public UnsubscribeResponse processUnsubscribe(Unsubscribe unsubscribe, 
				ReferenceParametersElements referenceParameters, AssertionType assertion) 
				throws UnableToDestroySubscriptionFault, Exception {
			UnsubscribeResponse response = null;

	        auditRequestFromAdapter(unsubscribe, assertion);
	        
	     // retrieve by consumer reference
	        HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
	        HiemSubscriptionItem subscriptionItem = null;
	        try {
	            log.debug("lookup subscription by reference parameters");
	            subscriptionItem = repo.retrieveByLocalSubscriptionReferenceParameters(referenceParameters);
	            log.debug("subscriptionItem isnull? = " + (subscriptionItem == null));
	        } catch (SubscriptionRepositoryException ex) {
	            log.error(ex);
	            throw new SubscriptionManagerSoapFaultFactory().getErrorDuringSubscriptionRetrieveFault(ex);
	        }

	        if (subscriptionItem == null) {
	            throw new SubscriptionManagerSoapFaultFactory().getUnableToFindSubscriptionFault();
	        }

	        // todo: if has parent, retrieve parent, forward unsubscribe to agency
	        List<HiemSubscriptionItem> childSubscriptions = null;
	        try {
	            log.debug("checking to see if subscription has child subscription");
	            childSubscriptions = repo.retrieveByParentSubscriptionReference(subscriptionItem
	                    .getSubscriptionReferenceXML());
	            log.debug("childSubscriptions isnull? = " + (childSubscriptions == null));
	        } catch (SubscriptionRepositoryException ex) {
	            log.warn("failed to check for child subscription", ex);
	        }

	        if (NullChecker.isNotNullish(childSubscriptions)) {
	            log.debug("send unsubscribe(s) to child [" + childSubscriptions.size() + "]");
	            for (HiemSubscriptionItem childSubscription : childSubscriptions) {
	                log.debug("sending unsubscribe to child");
	                response = unsubscribeToChild(unsubscribe, childSubscription, assertion);
	            }
	        }

	        // "remove" from local repo
	        log.debug("invoking subscription storage service to delete subscription");
	        try {
	            repo.deleteSubscription(subscriptionItem);
	        } catch (SubscriptionRepositoryException ex) {
	            log.error("unable to delete subscription.  This should result in a unable to remove subscription fault", ex);
	            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
	        }
	        
	        auditResponseToAdapter(response, assertion);
	        return response;
	    }
		
		 private UnsubscribeResponse unsubscribeToChild(Unsubscribe parentUnsubscribe, HiemSubscriptionItem childSubscriptionItem,
		            AssertionType parentAssertion) throws UnableToDestroySubscriptionFault, ResourceUnknownFault, Exception {
		        UnsubscribeResponse response = null;
			 	try {
		            log.debug("unsubscribing to child subscription");

		            log.debug("building target");
		            NhinTargetSystemType target;

		            target = new TargetBuilder().buildSubscriptionManagerTarget(childSubscriptionItem
		                    .getSubscriptionReferenceXML());
		            log.debug("target url = " + target.getUrl());
		            if (NullChecker.isNullish(target.getUrl())) {
		                throw new UnableToDestroySubscriptionFault(
		                        "Unable to determine where to send the unsubscribe for the child subscription",
		                        new UnableToDestroySubscriptionFaultType());
		            }
		            
		            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
		            ReferenceParametersElements referenceParametersElements = referenceParametersHelper
		                    .createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem
		                            .getSubscriptionReferenceXML());
		            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

		            response = getResponseFromTarget(parentUnsubscribe,referenceParametersElements, 
		            		parentAssertion, target);
		            
		            HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
		            repo.deleteSubscription(childSubscriptionItem);
		            
		        } catch (SubscriptionRepositoryException ex) {
		            log.error("failed to remove child subscription for repository");
		            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
		        } catch (XPathExpressionException ex) {
		            log.error("failed to parse subscription reference");
		            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
		        }
			 	
			 	return response;
		    }

		private UnsubscribeResponse createFailedPolicyCheckResponse() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * Audit the request from the adapter.
		 * @param request The request to be audited
		 * @param assertion The assertion to be audited
		 */
		private void auditRequestFromAdapter(Unsubscribe unsubscribe,
		        AssertionType assertion) {
			log.debug("In EntitysubscribeOrchImpl.auditInputMessage");
	        
	        try {
	            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

	            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType();
	            message.setAssertion(assertion);
	            message.setUnsubscribe(unsubscribe);

	            LogEventRequestType auditLogMsg = auditLogger.logNhinUnsubscribeRequest(message,
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
		 * @param response The response to be audited
		 * @param assertion The assertion to be audited
		 */
		private void auditResponseToAdapter(UnsubscribeResponse response, AssertionType assertion) {
		    log.debug("In EntitysubscribeOrchImpl.auditResponseMessage");
	        try {
	            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

	            gov.hhs.fha.nhinc.common.hiemauditlog.UnsubscribeResponseMessageType message = new gov.hhs.fha.nhinc.common.hiemauditlog.UnsubscribeResponseMessageType();
	            message.setAssertion(assertion);
	            message.setUnsubscribeResponse(response);

	            LogEventRequestType auditLogMsg = auditLogger.logUnsubscribeResponse(message,
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
		 * @param request The subscribe to send.
		 * @param assertion The assertion to send
		 * @param targetCommunitites The targets to be sent to
		 * @return the response from the foreign entity
		 */
		private UnsubscribeResponse getResponseFromTarget(
				Unsubscribe request, ReferenceParametersElements referenceParameters,
				AssertionType assertion, NhinTargetSystemType targetSystem) {
			UnsubscribeResponse nhinResponse = new UnsubscribeResponse();
		    if(isPolicyValid(request, assertion)){
	        	log.info("Policy check successful");
	            //send request to nhin proxy
	        	try {
	                nhinResponse = sendToNhinProxy(request, referenceParameters, assertion, targetSystem);
	            } catch (Exception e) {
	                //TODO nhinResponse = createFailedNhinSendResponse(hcid);
	            	String hcid = targetSystem.getHomeCommunity().getHomeCommunityId();
	                log.error("Fault encountered while trying to send message to the nhin " + hcid, e);
	            }
	        } else {
	            log.error("Failed policy check.  Sending error response.");
	            nhinResponse = createFailedPolicyCheckResponse();
	        }

	        return nhinResponse;
	    }
		
		private UnsubscribeResponse sendToNhinProxy(
	            Unsubscribe request, ReferenceParametersElements referenceParameters,
	            AssertionType assertion, NhinTargetSystemType nhinTargetSystem) {
			
	        OutboundUnsubscribeDelegate dsDelegate = getOutboundUnsubscribeDelegate();
	        OutboundUnsubscribeOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, request,
	        		referenceParameters, assertion, nhinTargetSystem);
	        UnsubscribeResponse response = ((OutboundUnsubscribeOrchestratable) dsDelegate
	        		.process(dsOrchestratable)).getResponse();

	     	return response;
	    }
		
		protected OutboundUnsubscribeDelegate getOutboundUnsubscribeDelegate() {
	        return new OutboundUnsubscribeDelegate();
	    }
		
		private OutboundUnsubscribeOrchestratable createOrchestratable(
	            OutboundUnsubscribeDelegate delegate, Unsubscribe request, 
	            ReferenceParametersElements referenceParameters, AssertionType assertion,
	            NhinTargetSystemType nhinTargetSystem) {

	        OutboundUnsubscribeOrchestratable dsOrchestratable = new OutboundUnsubscribeOrchestratable(delegate);
	        dsOrchestratable.setAssertion(assertion);
	        dsOrchestratable.setRequest(request);
	        dsOrchestratable.setReferenceParameters(referenceParameters);
	        dsOrchestratable.setTarget(nhinTargetSystem);

	        return dsOrchestratable;
	    }
		
		/**
		 * Check if policy for message is valid.
		 * @param subscribe The message to be checked.
		 * @param assertion The assertion to be checked.
		 * @return
		 */
		private boolean isPolicyValid(Unsubscribe unsubscribe,
				AssertionType assertion) {
			log.debug("In NhinHiemSubscribeWebServiceProxy.checkPolicy");
	        boolean policyIsValid = false;

	        UnsubscribeEventType policyCheckReq = new UnsubscribeEventType();
	        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
	        UnsubscribeMessageType request = new UnsubscribeMessageType();
	        request.setAssertion(assertion);
	        request.setUnsubscribe(unsubscribe);
	        policyCheckReq.setMessage(request);

	        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
	        CheckPolicyRequestType policyReq = policyChecker.checkPolicyUnsubscribe(policyCheckReq);
	        policyReq.setAssertion(assertion);
	        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
	        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
	        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

	        if (policyResp.getResponse() != null && NullChecker.isNotNullish(policyResp.getResponse().getResult())
	                && policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
	            policyIsValid = true;
	        }

	        log.debug("Finished NhinHiemUnsubscribeWebServiceProxy.checkPolicy - valid: " + policyIsValid);
	        return policyIsValid;
		}
		
		/**
		 * Check if there is a valid target to send the request to.
		 * @param targetCommunities The communities object to check for targets
		 * @return true if there is a valid target
		 */
		protected boolean hasNhinTargetHomeCommunityId(
	            NhinTargetCommunityType targetCommunity) {

	        if (targetCommunity != null
	        		&& targetCommunity.getHomeCommunity() != null
	                && NullChecker.isNotNullish(targetCommunity.getHomeCommunity().getHomeCommunityId())) {
	            return true;
	        }

	        return false;
	    }
}
