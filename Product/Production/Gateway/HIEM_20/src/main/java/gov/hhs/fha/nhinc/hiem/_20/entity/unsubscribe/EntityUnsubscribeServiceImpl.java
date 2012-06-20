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
package gov.hhs.fha.nhinc.hiem._20.entity.unsubscribe;

import java.util.List;

import javax.xml.ws.WebServiceContext;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.UnableToDestroySubscriptionFaultType;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.TargetBuilder;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.faults.SubscriptionManagerSoapFaultFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxyObjectFactory;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author rayj
 */
public class EntityUnsubscribeServiceImpl {

    private static Log log = LogFactory.getLog(EntityUnsubscribeServiceImpl.class);

    public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context)
            throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault,
            gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault, Exception {

        // Log the start of the entity performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                NhincConstants.HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());

        UnsubscribeResponse response = null;
        try {
            response = unsubscribeOps(unsubscribeRequest, context);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault(ex.getMessage(), null);
        } catch (UnableToDestroySubscriptionFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault(ex.getMessage(),
                    null);
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            throw e;
        }
        // Log the end of the entity performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                NhincConstants.HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        return response;
    }

    private UnsubscribeResponse unsubscribeOps(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context)
            throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault,
            gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault,
            org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, UnableToDestroySubscriptionFault, Exception {
        log.debug("Entering EntityUnsubscribeServiceImpl.unsubscribe");

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper
                .createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        log.debug("extracting assertion");
        AssertionType assertion = unsubscribeRequest.getAssertion();
        log.debug("extracted assertion");

        // retrieve by consumer reference
        HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
        HiemSubscriptionItem subscriptionItem = null;
        try {
            log.debug("lookup subscription by reference parameters");
            subscriptionItem = repo.retrieveByLocalSubscriptionReferenceParameters(referenceParametersElements);
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
                unsubscribeToChild(unsubscribeRequest, childSubscription, assertion);
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

        UnsubscribeResponse response = new UnsubscribeResponse();

        log.debug("Exiting HiemUnsubscribeImpl.unsubscribe");
        return response;
    }

    private void unsubscribeToChild(UnsubscribeRequestType parentUnsubscribeRequest,
            HiemSubscriptionItem childSubscriptionItem, AssertionType parentAssertion)
            throws UnableToDestroySubscriptionFault, ResourceUnknownFault, Exception {
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

            log.debug("unmarshalling unsubscribe");
            Unsubscribe parentUnsubscribe = parentUnsubscribeRequest.getUnsubscribe();
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper
                    .createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem
                            .getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
            NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
            log.debug("initialized proxy [" + proxy.toString() + "]");

            log.debug("sending unsubscribe");
            Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, referenceParametersElements,
                    parentAssertion, target);
            log.debug("unsubscribe response received");

            log.debug("unmarshalling response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            unsubscribeResponseMarshaller.unmarshal(unsubscribeResponseElement);
            log.debug("unmarshalled response");

            log.debug("invoking subscription repository to remove child subscription");
            HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
            repo.deleteSubscription(childSubscriptionItem);
            log.debug("child subscription deleted");
        } catch (SubscriptionRepositoryException ex) {
            log.error("failed to remove child subscription for repository");
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        } catch (XPathExpressionException ex) {
            log.error("failed to parse subscription reference");
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        }
    }

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, WebServiceContext context)
            throws gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault,
            gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault, Exception {
        UnsubscribeResponse response = null;

        // Log the start of the entity performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                NhincConstants.HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        try {
            response = unsubscribeOps(unsubscribeRequest, context);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault(ex.getMessage(), null);
        } catch (UnableToDestroySubscriptionFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault(
                    ex.getMessage(), null);
        } catch (Exception e) {
            log.error("Exception occured: " + e.getMessage());
            throw e;
        }
        // Log the end of the entity performance record
        PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                NhincConstants.HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        return response;
    }

    private UnsubscribeResponse unsubscribeOps(Unsubscribe unsubscribeRequest, WebServiceContext context)
            throws gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnableToDestroySubscriptionFault,
            gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault,
            org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, UnableToDestroySubscriptionFault, Exception {
        log.debug("Entering EntityUnsubscribeServiceImpl.unsubscribe");

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper
                .createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        log.debug("extracting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        log.debug("extracted assertion");

        // retrieve by consumer reference
        HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
        HiemSubscriptionItem subscriptionItem = null;
        try {
            log.debug("lookup subscription by reference parameters");
            subscriptionItem = repo.retrieveByLocalSubscriptionReferenceParameters(referenceParametersElements);
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
                unsubscribeToChild(unsubscribeRequest, childSubscription, assertion);
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

        UnsubscribeResponse response = new UnsubscribeResponse();

        log.debug("Exiting HiemUnsubscribeImpl.unsubscribe");
        return response;
    }

    private void unsubscribeToChild(Unsubscribe parentUnsubscribe, HiemSubscriptionItem childSubscriptionItem,
            AssertionType parentAssertion) throws UnableToDestroySubscriptionFault, ResourceUnknownFault, Exception {
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

            log.debug("unmarshalling unsubscribe");
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper
                    .createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem
                            .getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
            NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
            log.debug("initialized proxy [" + proxy.toString() + "]");

            log.debug("sending unsubscribe");
            Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, referenceParametersElements,
                    parentAssertion, target);
            log.debug("unsubscribe response received");

            log.debug("unmarshalling response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            UnsubscribeResponse unsubscribeResponse = unsubscribeResponseMarshaller
                    .unmarshal(unsubscribeResponseElement);
            log.debug("unmarshalled response");

            log.debug("invoking subscription repository to remove child subscription");
            HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
            repo.deleteSubscription(childSubscriptionItem);
            log.debug("child subscription deleted");
        } catch (SubscriptionRepositoryException ex) {
            log.error("failed to remove child subscription for repository");
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        } catch (XPathExpressionException ex) {
            log.error("failed to parse subscription reference");
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        }
    }
}
