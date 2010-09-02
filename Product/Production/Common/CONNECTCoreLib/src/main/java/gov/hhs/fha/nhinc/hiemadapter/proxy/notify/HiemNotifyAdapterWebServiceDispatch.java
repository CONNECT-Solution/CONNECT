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
package gov.hhs.fha.nhinc.hiemadapter.proxy.notify;

import gov.hhs.fha.nhinc.adapternotificationconsumer.AdapterNotificationConsumer;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.ByteArrayOutputStream;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Dispatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.*;
import javax.xml.ws.Service;

/**
 *
 * @author jhoppesc
 */
public class HiemNotifyAdapterWebServiceDispatch implements HiemNotifyAdapterProxy {

    private static Log log = LogFactory.getLog(HiemNotifyAdapterWebServiceDispatch.class);
    static AdapterNotificationConsumer adapterNotifyService = new AdapterNotificationConsumer();

    public Element notify(Element notify,ReferenceParametersElements referenceParametersElements, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Document notifyRequestDocument = buildNotifyRequestMessage(notify, assertion);

        Dispatch<Source> dispatch = getAdapterNotificationConsumerDispatch(target);

        Source input = new DOMSource(notifyRequestDocument);
        Source result = dispatch.invoke(input);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        javax.xml.transform.Result streamResult = new javax.xml.transform.stream.StreamResult(bos);

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(result, streamResult);

        String resultXml = new String(bos.toByteArray());
        Element resultElement = XmlUtility.convertXmlToElement(resultXml);

        return resultElement;
    }

    public Element notifySubscribersOfDocument(Element docNotify, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Element notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Document buildNotifyRequestMessage(Element notify, AssertionType assertion) throws ParserConfigurationException, DOMException {
        Document document = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        document = docBuilder.newDocument();
        Element notifyRequestElement = null;
        notifyRequestElement = document.createElementNS("urn:gov:hhs:fha:nhinc:common:nhinccommonadapter", "NotifyRequest");
        Node notifyNode = document.importNode(notify, true);
        notifyRequestElement.appendChild(notifyNode);
        document.appendChild(notifyRequestElement);
        return document;
    }

    private Dispatch<Source> getAdapterNotificationConsumerDispatch(NhinTargetSystemType target) throws ConnectionManagerException {
        QName portQName = new QName("urn:gov:hhs:fha:nhinc:adapternotificationconsumer", "AdapterNotificationConsumerPortSoap11");
        Dispatch<Source> dispatch = getGenericDispatch(adapterNotifyService, portQName, NhincConstants.HIEM_NOTIFY_ADAPTER_SERVICE_NAME, target);
        return dispatch;
    }

    private Dispatch<Source> getGenericDispatch(Service service, QName portQName, String serviceName, NhinTargetSystemType target) throws ConnectionManagerException {
        Dispatch<Source> dispatch = null;
        dispatch = service.createDispatch(portQName, Source.class, Service.Mode.PAYLOAD);
        String url = getUrl(target, serviceName);
        log.info("Setting endpoint address to Adapter Hiem Notify Service to " + url);
        dispatch.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return dispatch;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) throws ConnectionManagerException {
        String url = null;
        url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, serviceName);
        if (NullChecker.isNullish(url)) {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
        }
        return url;
    }
}
