/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.processor.nhin.handler;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeRequestType;
import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.dte.ConsumerReferenceHelper;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.EndpointReferenceMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.SubscriptionReferenceHelper;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxy;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxyObjectFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.service.HiemSubscriptionRepositoryService;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
class ChildSubscriptionModeSubscriptionHandler extends BaseSubscriptionHandler {

    @Override
    public SubscribeResponse handleSubscribe(Element subscribe) throws SubscribeCreationFailedFault {
        ConfigurationManager config = new ConfigurationManager();
        HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();

        AssertionType assertion = null;
        NhinTargetSystemType target = null;

        log.debug("In ChildSubscriptionModeSubscriptionHandler.handleSubscribe");
        SubscribeResponse response = null;

        // Build subscription item
        log.debug("create nhin->adapter subscription");
        //serialize childSubscribe to rawChildSubscribe
        HiemSubscriptionItem parentSubscriptionItem = createSubscriptionItem(subscribe, "gateway", "nhin");

        // Store subscription
        log.debug("Calling storeSubscriptionItem");
        EndpointReferenceType parentSubscriptionReference = storeSubscriptionItem(parentSubscriptionItem);
        log.debug("subscription stored");

        SubscribeResponse subscribeResponseFromAdapter = null;
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
            //serialize childSubscribe to rawChildSubscribe
            HiemSubscriptionItem childSubscriptionItem = new HiemSubscriptionItem();
            childSubscriptionItem.setSubscribeXML(XmlUtility.serializeElement(childSubscribe));
            childSubscriptionItem.setSubscriptionReferenceXML(XmlUtility.serializeElement(subscribeResponseElementFromAdapter));
            childSubscriptionItem.setRootTopic(null);  //gets filled in by repository

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
        log.debug("starting with parent subscribe [" + XmlUtility.serializeElementIgnoreFaults(parentSubscribeElement) + "]");

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
        EndpointReferenceType consumerReferenceEndpointReference = consumerReferenceHelper.createConsumerReferenceEndpointReference(entityNotificationConsumerAddress);

//        log.debug("marshall consumer reference to element");
//        EndpointReferenceMarshaller endpointReferenceMarshaller = new EndpointReferenceMarshaller();
//        Element consumerReferenceEndpointReferenceElement = endpointReferenceMarshaller.marshal(consumerReferenceEndpointReference);
//        log.debug("marshalled consumer reference endpoint [" + XmlUtility.serializeElementIgnoreFaults(consumerReferenceEndpointReferenceElement) + "]");
        log.debug("set consumer reference endpoint");
        childSubscribe.setConsumerReference(consumerReferenceEndpointReference);

//        Element parentConsumerReference = XmlUtility.getSingleChildElement(childSubscribe, "http://docs.oasis-open.org/wsn/b-2", "ConsumerReference");
//        childSubscribe.removeChild(parentConsumerReference);
//        consumerReferenceEndpointReferenceElement = (Element) childSubscribe.getOwnerDocument().importNode(consumerReferenceEndpointReferenceElement, true);
//
//        log.debug("adding consumer reference endpoint to child subscribe");
//        childSubscribe.appendChild(consumerReferenceEndpointReferenceElement);

        log.debug("marshal subscribe to element");
        childSubscribeElement = subscribeMarshaller.marshalSubscribe(childSubscribe);
        log.debug("built child subscribe [" + XmlUtility.serializeElementIgnoreFaults(childSubscribeElement) + "]");

        return childSubscribeElement;

    }

    /**
     * Use reflection to set the subscription reference. The runtime parameter
     * type of the setSubscriptionReference method of SubscriptionResponse is
     * checked and the correct parameter type is created and the method is
     * invoked with the correct type.
     *
     * This is necessary because the buildtime type and runtime type are
     * different for the method called.
     *
     * @param response Subscription response method.
     * @param subRef Subscription reference
     */
    @SuppressWarnings("unchecked")
    private void setSubscriptionReference(SubscribeResponse response, EndpointReferenceType subRef) {
        log.debug("In setSubscriptionReference");
        if ((response != null) && (subRef != null)) {
            try {
                Method[] methods = response.getClass().getDeclaredMethods();
                if (methods != null) {
                    log.debug("Method count: " + methods.length);
                    for (Method m : methods) {
                        log.debug("Looking at method: " + m.getName());
                        if (m.getName().equals("setSubscriptionReference")) {
                            Class[] paramTypes = m.getParameterTypes();
                            if (paramTypes != null) {
                                log.debug("Parameter count: " + paramTypes.length);
                                for (Class paramType : paramTypes) {
                                    log.debug("Param type: " + paramType.getName());
                                    if (paramType.isAssignableFrom(EndpointReferenceType.class)) {
                                        log.debug("Param type is EndpointReferenceType");
                                        Object[] params = {subRef};
                                        log.debug("Invoking EndpointReferenceType param method");
                                        m.invoke(response, params);
                                        break;
                                    } else if (paramType.isAssignableFrom(W3CEndpointReference.class)) {
                                        log.debug("Param type is W3CEndpointReference");
                                        Object[] params = {convertEndpointReferenceToW3cEndpointReference(subRef)};
                                        log.debug("Invoking W3CEndpointReference param method");
                                        m.invoke(response, params);
                                        break;
                                    }

                                }
                            } else {
                                log.debug("Parameter types was null");
                            }
                            break;
                        }
                    }
                } else {
                    log.debug("Methods were null");
                }
            } catch (IllegalAccessException ex) {
                log.error("IllegalAccessException encountered: " + ex.getMessage(), ex);
            } catch (IllegalArgumentException ex) {
                log.error("IllegalArgumentException encountered: " + ex.getMessage(), ex);
            } catch (InvocationTargetException ex) {
                log.error("InvocationTargetException encountered: " + ex.getMessage(), ex);
            }
        }
    }

    private EndpointReference convertEndpointReferenceToW3cEndpointReference(EndpointReferenceType epr) {
        log.info("begin CreateSubscriptionReference");
        W3CEndpointReference subRef = null;

        if (epr != null) {
            W3CEndpointReferenceBuilder resultBuilder = new W3CEndpointReferenceBuilder();

            if (epr.getAddress() != null) {
                log.info("subscriptionManagerUrl=" + epr.getAddress().getValue());
                resultBuilder.address(epr.getAddress().getValue());
            }
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            Document doc = null;
            try {
                doc = docBuilderFactory.newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            }
            doc.setXmlStandalone(true);

            if ((epr.getReferenceParameters() != null) &&
                    (epr.getReferenceParameters().getAny() != null) &&
                    (!epr.getReferenceParameters().getAny().isEmpty())) {
                List<Object> refParams = epr.getReferenceParameters().getAny();
                for (Object o : refParams) {
                    log.debug("Processing a reference parameter");
                    if (o instanceof Element) {
                        Element refParam = (Element) o;
                        resultBuilder.referenceParameter(refParam);
                    } else {
                        log.warn("Reference parameter was not of type Element - was " + o.getClass());
                    }
                }
            } else {
                log.warn("Reference parameters or ref param list was null or empty");
            }

            log.info("building.. resultBuilder.build()");
            subRef = resultBuilder.build();
        } else {
            log.warn("The endpoint reference was null");
        }

        log.info("end CreateSubscriptionReference");
        return subRef;
    }
}
