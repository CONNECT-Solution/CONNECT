/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.entity.unsubscribe;

import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType;
import gov.hhs.fha.nhinc.common.subscription.UnsubscribeType;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
//import gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe.HiemUnsubscribeAdapterProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxy;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
//import gov.hhs.fha.nhinc.common.subscription.;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
//import org.xmlsoap.schemas.ws._2004._08.addressing.ReferenceParametersType;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.TargetBuilder;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.faults.SubscriptionManagerSoapFaultFactory;
//import gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe.HiemUnsubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryService;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.entitysubscriptionmanagement.EntitySubscriptionManagerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.HandlerChain;
import javax.xml.ws.WebServiceContext;


import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.UnsubscribeRequestSecuredType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType;
import gov.hhs.fha.nhinc.common.subscription.UnsubscribeType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import java.util.List;
import java.util.Map;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe.NhinHiemUnsubscribeProxyObjectFactory;
import org.oasis_open.docs.wsn.b_2.UnableToDestroySubscriptionFaultType;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;


/**
 *
 * @author rayj
 */
public class EntityUnsubscribeServiceImpl {

    private static Log log = LogFactory.getLog(EntityUnsubscribeServiceImpl.class);

    public UnsubscribeResponse unsubscribe(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault, gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault {
        gov.hhs.fha.nhinc.genericheaderhandler.SoapHeaderHandler handler;

        UnsubscribeResponse response = null;
        try {
            response = unsubscribeOps(unsubscribeRequest, context);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault(ex.getMessage(), null);
        } catch (UnableToDestroySubscriptionFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault(ex.getMessage(), null);
        }
        return response;
    }

    private UnsubscribeResponse unsubscribeOps(UnsubscribeRequestType unsubscribeRequest, WebServiceContext context) throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault, gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, UnableToDestroySubscriptionFault {
        log.debug("Entering EntityUnsubscribeServiceImpl.unsubscribe");

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        log.debug("extracting assertion");
        AssertionType assertion = unsubscribeRequest.getAssertion();
        log.debug("extracted assertion");

        //retrieve by consumer reference
        SubscriptionRepositoryService repo = new SubscriptionRepositoryService();
        SubscriptionItem subscriptionItem = null;
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

        //todo: if has parent, retrieve parent, forward unsubscribe to agency
        List<SubscriptionItem> childSubscriptions = null;
        try {
            log.debug("checking to see if subscription has child subscription");
            childSubscriptions = repo.retrieveByParentSubscriptionReference(subscriptionItem.getSubscriptionReferenceXML());
            log.debug("childSubscriptions isnull? = " + (childSubscriptions == null));
        } catch (SubscriptionRepositoryException ex) {
            log.warn("failed to check for child subscription", ex);
        }

        if (NullChecker.isNotNullish(childSubscriptions)) {
            log.debug("send unsubscribe(s) to child [" + childSubscriptions.size() + "]");
            for (SubscriptionItem childSubscription : childSubscriptions) {
                log.debug("sending unsubscribe to child");
                unsubscribeToChild(unsubscribeRequest, childSubscription, assertion);
            }
        }

        //"remove" from local repo
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

    private void unsubscribeToChild(UnsubscribeRequestType parentUnsubscribeRequest, SubscriptionItem childSubscriptionItem, AssertionType parentAssertion) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        try {
            log.debug("unsubscribing to child subscription");

            log.debug("building target");
            NhinTargetSystemType target;

            target = new TargetBuilder().buildSubscriptionManagerTarget(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("target url = " + target.getUrl());
            if (NullChecker.isNullish(target.getUrl())) {
                throw new UnableToDestroySubscriptionFault("Unable to determine where to send the unsubscribe for the child subscription", new UnableToDestroySubscriptionFaultType());
            }

            log.debug("unmarshalling unsubscribe");
            Unsubscribe parentUnsubscribe = parentUnsubscribeRequest.getUnsubscribe();
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
            NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
            log.debug("initialized proxy [" + proxy.toString() + "]");

            log.debug("sending unsubscribe");
            Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, referenceParametersElements, parentAssertion, target);
            log.debug("unsubscribe response received");

            log.debug("unmarshalling response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            UnsubscribeResponse unsubscribeResponse = unsubscribeResponseMarshaller.unmarshal(unsubscribeResponseElement);
            log.debug("unmarshalled response");

            log.debug("invoking subscription repository to remove child subscription");
            SubscriptionRepositoryService repo = new SubscriptionRepositoryService();
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

    public UnsubscribeResponse unsubscribe(UnsubscribeRequestSecuredType unsubscribeRequest, WebServiceContext context) throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault, gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault {
        gov.hhs.fha.nhinc.genericheaderhandler.SoapHeaderHandler handler;

        UnsubscribeResponse response = null;
        try {
            response = unsubscribeOps(unsubscribeRequest, context);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault(ex.getMessage(), null);
        } catch (UnableToDestroySubscriptionFault ex) {
            throw new gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault(ex.getMessage(), null);
        }
        return response;
    }

    private UnsubscribeResponse unsubscribeOps(UnsubscribeRequestSecuredType unsubscribeRequest, WebServiceContext context) throws gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnableToDestroySubscriptionFault, gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault, org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault, UnableToDestroySubscriptionFault {
        log.debug("Entering EntityUnsubscribeServiceImpl.unsubscribe");

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");

        log.debug("extracting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        log.debug("extracted assertion");

        //retrieve by consumer reference
        SubscriptionRepositoryService repo = new SubscriptionRepositoryService();
        SubscriptionItem subscriptionItem = null;
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

        //todo: if has parent, retrieve parent, forward unsubscribe to agency
        List<SubscriptionItem> childSubscriptions = null;
        try {
            log.debug("checking to see if subscription has child subscription");
            childSubscriptions = repo.retrieveByParentSubscriptionReference(subscriptionItem.getSubscriptionReferenceXML());
            log.debug("childSubscriptions isnull? = " + (childSubscriptions == null));
        } catch (SubscriptionRepositoryException ex) {
            log.warn("failed to check for child subscription", ex);
        }

        if (NullChecker.isNotNullish(childSubscriptions)) {
            log.debug("send unsubscribe(s) to child [" + childSubscriptions.size() + "]");
            for (SubscriptionItem childSubscription : childSubscriptions) {
                log.debug("sending unsubscribe to child");
                unsubscribeToChild(unsubscribeRequest, childSubscription, assertion);
            }
        }

        //"remove" from local repo
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

    private void unsubscribeToChild(UnsubscribeRequestSecuredType parentUnsubscribeRequest, SubscriptionItem childSubscriptionItem, AssertionType parentAssertion) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        try {
            log.debug("unsubscribing to child subscription");

            log.debug("building target");
            NhinTargetSystemType target;

            target = new TargetBuilder().buildSubscriptionManagerTarget(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("target url = " + target.getUrl());
            if (NullChecker.isNullish(target.getUrl())) {
                throw new UnableToDestroySubscriptionFault("Unable to determine where to send the unsubscribe for the child subscription", new UnableToDestroySubscriptionFaultType());
            }

            log.debug("unmarshalling unsubscribe");
            Unsubscribe parentUnsubscribe = parentUnsubscribeRequest.getUnsubscribe();
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            NhinHiemUnsubscribeProxyObjectFactory factory = new NhinHiemUnsubscribeProxyObjectFactory();
            NhinHiemUnsubscribeProxy proxy = factory.getNhinHiemSubscribeProxy();
            log.debug("initialized proxy [" + proxy.toString() + "]");

            log.debug("sending unsubscribe");
            Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, referenceParametersElements, parentAssertion, target);
            log.debug("unsubscribe response received");

            log.debug("unmarshalling response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            UnsubscribeResponse unsubscribeResponse = unsubscribeResponseMarshaller.unmarshal(unsubscribeResponseElement);
            log.debug("unmarshalled response");

            log.debug("invoking subscription repository to remove child subscription");
            SubscriptionRepositoryService repo = new SubscriptionRepositoryService();
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
