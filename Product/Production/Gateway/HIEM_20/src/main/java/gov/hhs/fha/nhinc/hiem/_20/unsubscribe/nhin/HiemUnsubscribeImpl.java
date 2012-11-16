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
package gov.hhs.fha.nhinc.hiem._20.unsubscribe.nhin;

import java.util.List;

import javax.xml.ws.WebServiceContext;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapHeaderHelper;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.hiem.dte.TargetBuilder;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.processor.faults.SubscriptionManagerSoapFaultFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.unsubscribe.adapter.proxy.HiemUnsubscribeAdapterProxy;
import gov.hhs.fha.nhinc.unsubscribe.adapter.proxy.HiemUnsubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author jhoppesc
 */
public class HiemUnsubscribeImpl {

    private static Log log = LogFactory.getLog(HiemUnsubscribeImpl.class);

    public UnsubscribeResponse unsubscribe(Unsubscribe unsubscribeRequest, WebServiceContext context)
            throws UnableToDestroySubscriptionFault {
        UnsubscribeResponse response;
        try {
            response = unsubscribeOps(unsubscribeRequest, context);
        } catch (Exception ex) {
            throw new SubscriptionManagerSoapFaultFactory().getGenericProcessingExceptionFault(ex);
        }
        return response;
    }

    /**
     * @param unsubscribeRequest
     * @param context
     * @return
     * @throws UnableToDestroySubscriptionFault
     * @throws Exception
     */
    private UnsubscribeResponse unsubscribeOps(Unsubscribe unsubscribeRequest, WebServiceContext context)
            throws UnableToDestroySubscriptionFault, Exception {
        log.debug("Entering HiemUnsubscribeImpl.unsubscribe");

        SoapMessageElements soapHeaderElements = new SoapHeaderHelper().getSoapHeaderElements(context);
        AssertionType assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
        HiemSubscriptionRepositoryService repo = new HiemSubscriptionRepositoryService();
        HiemSubscriptionItem subscriptionItem = null;

        subscriptionItem = populateSubscriptionItem(soapHeaderElements, repo, subscriptionItem);

        //if has parent, retrieve parent, forward unsubscribe to agency
        List<HiemSubscriptionItem> childSubscriptions = null;
        try {
            childSubscriptions = repo.retrieveByParentSubscriptionReference(subscriptionItem
                    .getSubscriptionReferenceXML());
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
            forwardUnsubscribeToAdapter(unsubscribeRequest, soapHeaderElements, assertion);
        }

        removeFromLocalRepo(repo, subscriptionItem);
        log.debug("Exiting HiemUnsubscribeImpl.unsubscribe");

        return new UnsubscribeResponse();
    }

    /**
     * @param soapHeaderElements
     * @param repo
     * @param subscriptionItem
     * @return
     * @throws UnableToDestroySubscriptionFault
     * @throws Exception
     */
    private HiemSubscriptionItem populateSubscriptionItem(SoapMessageElements soapHeaderElements,
            HiemSubscriptionRepositoryService repo, HiemSubscriptionItem subscriptionItem)
            throws UnableToDestroySubscriptionFault, Exception {
        try {
            subscriptionItem = repo.retrieveByLocalSubscriptionReferenceParameters(soapHeaderElements);
        } catch (SubscriptionRepositoryException ex) {
            log.error(ex);
            throw new SubscriptionManagerSoapFaultFactory().getGenericProcessingExceptionFault(ex);
        }

        if (subscriptionItem == null) {
            throw new SubscriptionManagerSoapFaultFactory().getUnableToFindSubscriptionFault();
        }
        return subscriptionItem;
    }

    /**
     * @param repo
     * @param subscriptionItem
     * @throws UnableToDestroySubscriptionFault
     */
    private void removeFromLocalRepo(HiemSubscriptionRepositoryService repo, HiemSubscriptionItem subscriptionItem)
            throws UnableToDestroySubscriptionFault {
        log.debug("invoking subscription storage service to delete subscription");
        try {
            repo.deleteSubscription(subscriptionItem);
        } catch (SubscriptionRepositoryException ex) {
            log.error("unable to delete subscription.  This should result in a unable to remove subscription fault", ex);
            throw new SubscriptionManagerSoapFaultFactory().getFailedToRemoveSubscriptionFault(ex);
        }
    }

    private boolean isForwardUnsubscribeToAdapter() {
        boolean forward = false;
        ConfigurationManager config = new ConfigurationManager();
        String mode = null;
        try {
            mode = config.getAdapterSubscriptionMode();
        } catch (ConfigurationException ex) {
            log.warn("unable to determine adapter subscription mode");
            forward = false;
        }

        if (NullChecker.isNotNullish(mode)) {
            if (!mode.contentEquals(NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_DISABLED)) {
                forward = true;
            }

        }
        return forward;
    }

    private void forwardUnsubscribeToAdapter(Unsubscribe parentUnsubscribe,
            SoapMessageElements parentReferenceParametersElements, AssertionType parentAssertion)
                    throws UnableToDestroySubscriptionFault {

        log.debug("forwarding unsubscribe to adapter");

        log.debug("target to be filled in by proxy using connection manager");
        NhinTargetSystemType target = null;

        log.debug("unmarshalling unsubscribe");
        WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
        Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
        log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

        log.debug("using reference parameters from parent message ["
                + parentReferenceParametersElements.getElements().size() + " element(s)]");

        log.debug("initialize proxy");
        HiemUnsubscribeAdapterProxyObjectFactory factory = new HiemUnsubscribeAdapterProxyObjectFactory();
        HiemUnsubscribeAdapterProxy proxy = factory.getHiemSubscribeAdapterProxy();
        log.debug("initialized proxy");

        log.debug("sending unsubscribe");
        Element unsubscribeResponseElement = proxy.unsubscribe(unsubscribeElement, parentReferenceParametersElements,
                parentAssertion, target);
        log.debug("unsubscribe response received");

        log.debug("unmarshalling response");
        WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
        unsubscribeResponseMarshaller.unmarshal(unsubscribeResponseElement);
        log.debug("unmarshalled response");
    }

    private void unsubscribeToChild(Unsubscribe parentUnsubscribe, HiemSubscriptionItem childSubscriptionItem,
            AssertionType parentAssertion) throws UnableToDestroySubscriptionFault {
        try {
            log.debug("unsubscribing to child subscription");

            log.debug("building target");
            NhinTargetSystemType target;

            target = new TargetBuilder().buildSubscriptionManagerTarget(childSubscriptionItem
                    .getSubscriptionReferenceXML());
            log.debug("target url = " + target.getUrl());

            log.debug("unmarshalling unsubscribe");
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Element unsubscribeElement = unsubscribeMarshaller.marshal(parentUnsubscribe);
            log.debug(XmlUtility.formatElementForLogging(null, unsubscribeElement));

            log.debug("extracting reference parameters from subscription reference");
            ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
            SoapMessageElements referenceParametersElements = referenceParametersHelper
                    .createReferenceParameterElementsFromSubscriptionReference(childSubscriptionItem
                            .getSubscriptionReferenceXML());
            log.debug("extracted " + referenceParametersElements.getElements().size() + " element(s)");

            log.debug("initialize proxy");
            HiemUnsubscribeAdapterProxyObjectFactory factory = new HiemUnsubscribeAdapterProxyObjectFactory();
            HiemUnsubscribeAdapterProxy proxy = factory.getHiemSubscribeAdapterProxy();
            log.debug("initialized proxy");

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
}
