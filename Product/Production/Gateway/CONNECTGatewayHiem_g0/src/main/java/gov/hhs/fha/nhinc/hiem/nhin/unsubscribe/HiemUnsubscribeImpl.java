/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.nhin.unsubscribe;

import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType;
import gov.hhs.fha.nhinc.common.subscription.UnsubscribeType;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe.HiemUnsubscribeAdapterProxy;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
//import gov.hhs.fha.nhinc.common.subscription.;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
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
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
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
import gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe.HiemUnsubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;

/**
 *
 * @author jhoppesc
 */
public class HiemUnsubscribeImpl {

    private static Log log = LogFactory.getLog(HiemUnsubscribeImpl.class);

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        UnsubscribeResponse response;
        try {
            response = unsubscribeOps(unsubscribeRequest, context);
        } catch (Exception ex) {
            throw new SubscriptionManagerSoapFaultFactory().getGenericProcessingExceptionFault(ex);
        }
        return response;
    }

    private UnsubscribeResponse unsubscribeOps(Unsubscribe unsubscribeRequest, WebServiceContext context) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        log.debug("Entering HiemUnsubscribeImpl.unsubscribe");

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HIEM_UNSUBSCRIBE_SOAP_HDR_ATTR_TAG);
        log.debug("extracted reference parameters from soap header");

        log.debug("extracting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        log.debug("extracted assertion");

        //retrieve by consumer reference
        HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
        HiemSubscriptionItem subscriptionItem = null;
        try {
            subscriptionItem = repo.retrieveByLocalSubscriptionReferenceParameters(referenceParametersElements);
        } catch (SubscriptionRepositoryException ex) {
            log.error(ex);
            throw new SubscriptionManagerSoapFaultFactory().getErrorDuringSubscriptionRetrieveFault(ex);
        }

        if (subscriptionItem == null) {
            throw new SubscriptionManagerSoapFaultFactory().getUnableToFindSubscriptionFault();
        }

        //todo: if has parent, retrieve parent, forward unsubscribe to agency
        List<HiemSubscriptionItem> childSubscriptions = null;
        try {
            childSubscriptions = repo.retrieveByParentSubscriptionReference(subscriptionItem.getSubscriptionReferenceXML());
        } catch (SubscriptionRepositoryException ex) {
            log.warn("failed to check for child subscription", ex);
        }

        if (NullChecker.isNotNullish(childSubscriptions)) {
            log.debug("send unsubscribe(s) to child");
            for (HiemSubscriptionItem childSubscription : childSubscriptions) {
                unsubscribeToChild(unsubscribeRequest, childSubscription, assertion);
            }
        } else if (isForwardUnsubscribeToAdapter()) {
            log.debug("forward unsubscribe to adapter");
            forwardUnsubscribeToAdapter(unsubscribeRequest, referenceParametersElements, assertion);
        } else {
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

    private boolean isForwardUnsubscribeToAdapter() {
        boolean forward = false;
        ConfigurationManager config = new ConfigurationManager();
        String mode = null;
        try {
            mode = config.getAdapterSubscriptionMode();
        } catch (ConfigurationException ex) {
            log.warn("unable to determine adapter subscription mode");
            forward =
                    false;
        }

        if (NullChecker.isNotNullish(mode)) {
            if (!mode.contentEquals(NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_DISABLED)) {
                forward = true;
            }

        }
        return forward;
    }

    private void forwardUnsubscribeToAdapter(Unsubscribe parentUnsubscribe, ReferenceParametersElements parentReferenceParametersElements, AssertionType parentAssertion) throws UnableToDestroySubscriptionFault {
//        try {
        log.debug("forwarding unsubscribe to adapter");

        log.debug("target to be filled in by proxy using connection manager");
        NhinTargetSystemType target = null;

        log.debug("unmarshalling unsubscribe");
        WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
        log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

        log.debug("using reference parameters from parent message [" + parentReferenceParametersElements.getElements().size() + " element(s)]");

        log.debug("initialize proxy");
        HiemUnsubscribeAdapterProxyObjectFactory factory = new HiemUnsubscribeAdapterProxyObjectFactory();
        HiemUnsubscribeAdapterProxy proxy = factory.getHiemSubscribeAdapterProxy();
        log.debug("initialized proxy");

        log.debug("sending unsubscribe");
        Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, parentReferenceParametersElements, parentAssertion, target);
        log.debug("unsubscribe response received");

        log.debug("unmarshalling response");
        WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
        UnsubscribeResponse unsubscribeResponse = unsubscribeResponseMarshaller.unmarshal(unsubscribeResponseElement);
        log.debug("unmarshalled response");

//        } catch (SubscriptionRepositoryException ex) {
//            log.error("failed to remove child subscription for repository");
//            throw new SoapFaultFactory().getUnableToDeleteSubscriptionFault(ex);
//        } catch (XPathExpressionException ex) {
//            log.error("failed to parse subscription reference");
//            throw new SoapFaultFactory().getUnableToDeleteSubscriptionFault(ex);
//        }
    }

    private void unsubscribeToChild(Unsubscribe parentUnsubscribe, HiemSubscriptionItem childSubscriptionItem, AssertionType parentAssertion) throws UnableToDestroySubscriptionFault {
        try {
            log.debug("unsubscribing to child subscription");

            log.debug("building target");
            NhinTargetSystemType target;

            target =
                    new TargetBuilder().buildSubscriptionManagerTarget(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("target url = " + target.getUrl());

            log.debug("unmarshalling unsubscribe");
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem.getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            HiemUnsubscribeAdapterProxyObjectFactory factory = new HiemUnsubscribeAdapterProxyObjectFactory();
            HiemUnsubscribeAdapterProxy proxy = factory.getHiemSubscribeAdapterProxy();
            log.debug("initialized proxy");

            log.debug("sending unsubscribe");
            Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, referenceParametersElements, parentAssertion, target);
            log.debug("unsubscribe response received");

            log.debug("unmarshalling response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            UnsubscribeResponse unsubscribeResponse = unsubscribeResponseMarshaller.unmarshal(unsubscribeResponseElement);
            log.debug("unmarshalled response");

            log.debug("invoking subscription repository to remove child subscription");
            HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
            repo.deleteSubscription(childSubscriptionItem);
            log.debug("child subscription deleted");
        } catch (SubscriptionRepositoryException ex) {
            log.error("failed to remove child subscription for repository");
            throw new  SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        } catch (XPathExpressionException ex) {
            log.error("failed to parse subscription reference");
            throw new  SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        }
    }
}
