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
package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.xml;

import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducer;
import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterSubscriptionManager;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.ByteArrayOutputStream;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.*;

/**
 *
 * @author rayj
 */
public class HiemSubscribeXmlAdapterWebServiceProxy implements HiemSubscribeXmlAdapterProxy {
    //todo: how am i going to append the assertion?
    //todo: need to make alternate implementations, hook into factory method, spring, etc
    //todo: throw proper exceptions
    //todo: do we want to pass the url and assertion in as seperate params?

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(HiemSubscribeXmlAdapterWebServiceProxy.class);
//    static AdapterNotificationProducer adapterNotificationProducerService = new AdapterNotificationProducer();
//    static AdapterSubscriptionManager adapterUnsubscribeService = new AdapterSubscriptionManager();

    public Node subscribe(Node subscribe, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Document subscribeRequestDocument = buildSubscribeRequestMessage(subscribe, assertion);

        Dispatch<Source> dispatch = getAdapterNotificationProducerDispatch(target);

        Source input = new DOMSource(subscribeRequestDocument);
        Source result = dispatch.invoke(input);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        javax.xml.transform.Result streamResult = new javax.xml.transform.stream.StreamResult(bos);

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(result, streamResult);

        String resultXml = new String(bos.toByteArray());
        Node resultNode = XmlUtility.convertXmlToElement(resultXml);

        return resultNode;
    }

    private Document buildSubscribeRequestMessage(Node subscribe, AssertionType assertion) throws ParserConfigurationException, DOMException {
        Document document = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        document = docBuilder.newDocument();
        Element subscribeRequestElement = null;
        subscribeRequestElement = document.createElementNS("urn:gov:hhs:fha:nhinc:common:nhinccommonadapter", "SubscribeRequest");
        Node subscribeNode = document.importNode(subscribe, true);
        subscribeRequestElement.appendChild(subscribeNode);
        document.appendChild(subscribeRequestElement);
        return document;
    }

    private Dispatch<Source> getAdapterNotificationProducerDispatch(NhinTargetSystemType target) throws ConnectionManagerException {
        AdapterNotificationProducer adapterNotificationProducerService = null;
        try {
            adapterNotificationProducerService = new AdapterNotificationProducer();
        } catch (Exception ex) {
            log.error("Failed to init adapterNotificationProducerService");
            log.error(ex);
        }

        QName portQName = new QName("urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagement", "AdapterNotificationProducerPortSoap11");
        Dispatch<Source> dispatch = getGenericDispath(adapterNotificationProducerService, portQName, NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SERVICE_NAME, target);
        return dispatch;
    }

    private Dispatch<Source> getGenericDispath(Service service, QName portQName, String serviceName, NhinTargetSystemType target) throws ConnectionManagerException {
        Dispatch<Source> dispatch = null;
        dispatch = service.createDispatch(portQName, Source.class, Service.Mode.PAYLOAD);
        String url = getUrl(target, serviceName);
        dispatch.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return dispatch;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) throws ConnectionManagerException {
        String url = null;
        url =ConnectionManagerCache.getEndpontURLFromNhinTarget(target, serviceName);
        if (NullChecker.isNullish(url)) url = ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName) ;
        return url;
    }
}
