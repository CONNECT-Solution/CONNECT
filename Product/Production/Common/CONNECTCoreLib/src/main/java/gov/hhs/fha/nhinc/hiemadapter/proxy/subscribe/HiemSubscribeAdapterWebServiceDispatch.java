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
package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe;

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
public class HiemSubscribeAdapterWebServiceDispatch implements HiemSubscribeAdapterProxy {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
            .getLog(HiemSubscribeAdapterWebServiceDispatch.class);

    public Element subscribe(Element subscribe, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Document subscribeRequestDocument = buildSubscribeRequestMessage(subscribe, assertion);

        Dispatch<Source> dispatch = getAdapterNotificationProducerDispatch(target);

        Source input = new DOMSource(subscribeRequestDocument);
        Source result = dispatch.invoke(input);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        javax.xml.transform.Result streamResult = new javax.xml.transform.stream.StreamResult(bos);

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(result, streamResult);

        String resultXml = new String(bos.toByteArray());
        Element resultNode = XmlUtility.convertXmlToElement(resultXml);

        return resultNode;
    }

    private Document buildSubscribeRequestMessage(Node subscribe, AssertionType assertion)
            throws ParserConfigurationException, DOMException {
        Document document = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        document = docBuilder.newDocument();
        Element subscribeRequestElement = null;
        subscribeRequestElement = document.createElementNS("urn:gov:hhs:fha:nhinc:common:nhinccommonadapter",
                "SubscribeRequest");
        Node subscribeNode = document.importNode(subscribe, true);
        subscribeRequestElement.appendChild(subscribeNode);
        document.appendChild(subscribeRequestElement);
        return document;
    }

    private Dispatch<Source> getAdapterNotificationProducerDispatch(NhinTargetSystemType target)
            throws ConnectionManagerException {
        AdapterNotificationProducer adapterNotificationProducerService = null;
        try {
            adapterNotificationProducerService = new AdapterNotificationProducer();
        } catch (Exception ex) {
            log.error("Failed to init adapterNotificationProducerService");
            log.error(ex);
        }

        QName portQName = new QName("urn:gov:hhs:fha:nhinc:adaptersubscriptionmanagement",
                "AdapterNotificationProducerPortSoap11");
        Dispatch<Source> dispatch = getGenericDispath(adapterNotificationProducerService, portQName,
                NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SERVICE_NAME, target);
        return dispatch;
    }

    private Dispatch<Source> getGenericDispath(Service service, QName portQName, String serviceName,
            NhinTargetSystemType target) throws ConnectionManagerException {
        Dispatch<Source> dispatch = null;
        dispatch = service.createDispatch(portQName, Source.class, Service.Mode.PAYLOAD);
        String url = getUrl(target, serviceName);
        dispatch.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return dispatch;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) throws ConnectionManagerException {
        String url = null;
        url = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTarget(target, serviceName);
        if (NullChecker.isNullish(url)) {
            url = ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(serviceName);
        }
        return url;
    }
}
