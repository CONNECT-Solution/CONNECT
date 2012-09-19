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
package gov.hhs.fha.nhinc.subscribe.nhin;

import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.dte.ConsumerReferenceHelper;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.EndpointReferenceMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.subscribe.adapter.proxy.HiemSubscribeAdapterProxy;
import gov.hhs.fha.nhinc.subscribe.adapter.proxy.HiemSubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author rayj
 */
class ChildSubscriptionModeSubscriptionHandler extends BaseSubscriptionHandler {

    @Override
    public SubscribeResponse handleSubscribe(Element subscribe) throws SubscribeCreationFailedFault {
        HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();

        AssertionType assertion = null;
        NhinTargetSystemType target = null;

        log.debug("In ChildSubscriptionModeSubscriptionHandler.handleSubscribe");
        SubscribeResponse response = null;

        // Build subscription item
        log.debug("create nhin->adapter subscription");
        // serialize childSubscribe to rawChildSubscribe
        HiemSubscriptionItem parentSubscriptionItem = createSubscriptionItem(subscribe, "gateway", "nhin");

        // Store subscription
        log.debug("Calling storeSubscriptionItem");
        EndpointReferenceType parentSubscriptionReference = storeSubscriptionItem(parentSubscriptionItem);
        log.debug("subscription stored");

        Element subscribeResponseElementFromAdapter = null;
        Element childSubscribe = null;
        try {
            log.info("build message to send to adapter proxy");
            childSubscribe = createChildSubscribe(subscribe);

            log.info("initialize HiemSubscribeAdapterProxyObjectFactory");
            HiemSubscribeAdapterProxyObjectFactory adapterFactory = new HiemSubscribeAdapterProxyObjectFactory();
            log.info("initialize HIEM subscribe adapter proxy");
            HiemSubscribeAdapterProxy adapterProxy = adapterFactory.getHiemSubscribeAdapterProxy();

            log.info("begin invoke HIEM subscribe adapter proxy");
            subscribeResponseElementFromAdapter = adapterProxy.subscribe(childSubscribe, assertion, target);
            log.info("end invoke HIEM subscribe adapter proxy");
        } catch (Exception ex) {
            log.error("failed to forward subscribe to adapter", ex);
            throw new SoapFaultFactory().getFailedToForwardSubscribeToAgencyFault(ex);
        }

        try {
            // Build subscription item
            log.debug("create gateway->adapter subscription");
            // serialize childSubscribe to rawChildSubscribe
            HiemSubscriptionItem childSubscriptionItem = new HiemSubscriptionItem();
            childSubscriptionItem.setSubscribeXML(XmlUtility.serializeElement(childSubscribe));
            childSubscriptionItem.setSubscriptionReferenceXML(XmlUtility
                    .serializeElement(subscribeResponseElementFromAdapter));
            childSubscriptionItem.setRootTopic(null); // gets filled in by repository

            EndpointReferenceMarshaller endpointReferenceMarshaller = new EndpointReferenceMarshaller();
            Element parentSubscriptionElement = endpointReferenceMarshaller.marshal(parentSubscriptionReference);
            String parentSubscriptionReferenceXml = XmlUtility.serializeElementIgnoreFaults(parentSubscriptionElement);

            childSubscriptionItem.setParentSubscriptionReferenceXML(parentSubscriptionReferenceXml);
            childSubscriptionItem.setProducer("adapter");
            childSubscriptionItem.setConsumer("gateway");

            // Store subscription
            log.debug("saving child subscription reference");
            service.saveSubscriptionToExternal(childSubscriptionItem);
            log.debug("saved child subscription reference");

        } catch (Exception ex) {
            log.error("throw proper error", ex);
        }

        response = new SubscribeResponse();
        response.setSubscriptionReference(parentSubscriptionReference);
        return response;
    }

    private Element createChildSubscribe(Element parentSubscribeElement) throws ConfigurationException {
        log.debug("build child subscribe");
        log.debug("starting with parent subscribe [" + XmlUtility.serializeElementIgnoreFaults(parentSubscribeElement)
                + "]");

        Element childSubscribeElement = parentSubscribeElement;
        Subscribe childSubscribe = null;

        log.debug("unmarshal subscribe to element");
        SubscribeMarshaller subscribeMarshaller = new SubscribeMarshaller();
        childSubscribe = subscribeMarshaller.unmarshalSubscribe(childSubscribeElement);

        log.debug("determine notification consumer address (entity interface)");
        ConfigurationManager config = new ConfigurationManager();
        String entityNotificationConsumerAddress = config.getEntityNotificationConsumerAddress();
        log.debug("entityNotificationConsumerAddress=" + entityNotificationConsumerAddress);

        log.debug("creating consumer reference endpoint");
        ConsumerReferenceHelper consumerReferenceHelper = new ConsumerReferenceHelper();
        EndpointReferenceType consumerReferenceEndpointReference = consumerReferenceHelper
                .createConsumerReferenceEndpointReference(entityNotificationConsumerAddress);

        // log.debug("marshall consumer reference to element");
        // EndpointReferenceMarshaller endpointReferenceMarshaller = new EndpointReferenceMarshaller();
        // Element consumerReferenceEndpointReferenceElement =
        // endpointReferenceMarshaller.marshal(consumerReferenceEndpointReference);
        // log.debug("marshalled consumer reference endpoint [" +
        // XmlUtility.serializeElementIgnoreFaults(consumerReferenceEndpointReferenceElement) + "]");
        log.debug("set consumer reference endpoint");
        childSubscribe.setConsumerReference(consumerReferenceEndpointReference);

        // Element parentConsumerReference = XmlUtility.getSingleChildElement(childSubscribe,
        // "http://docs.oasis-open.org/wsn/b-2", "ConsumerReference");
        // childSubscribe.removeChild(parentConsumerReference);
        // consumerReferenceEndpointReferenceElement = (Element)
        // childSubscribe.getOwnerDocument().importNode(consumerReferenceEndpointReferenceElement, true);
        //
        // log.debug("adding consumer reference endpoint to child subscribe");
        // childSubscribe.appendChild(consumerReferenceEndpointReferenceElement);

        log.debug("marshal subscribe to element");
        childSubscribeElement = subscribeMarshaller.marshalSubscribe(childSubscribe);
        log.debug("built child subscribe [" + XmlUtility.serializeElementIgnoreFaults(childSubscribeElement) + "]");

        return childSubscribeElement;

    }
}
