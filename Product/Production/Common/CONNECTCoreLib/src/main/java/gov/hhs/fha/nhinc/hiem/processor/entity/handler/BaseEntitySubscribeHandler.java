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
package gov.hhs.fha.nhinc.hiem.processor.entity.handler;

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import javax.xml.xpath.XPathExpressionException;

import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.common.HiemProcessorConstants;
import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionItemUtil;
import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionStorage;
import gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe.NhinHiemSubscribeProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe.NhinHiemSubscribeProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;

/**
 * Base entity subscribe handler
 *
 * @author Neil Webb
 */
public abstract class BaseEntitySubscribeHandler implements EntitySubscribeHandler {
    protected org.apache.commons.logging.Log log = null;
    protected QualifiedSubjectIdentifierType patientIdentifier = null;
    protected String xpathToPatientId = null;

    public BaseEntitySubscribeHandler() {
        log = org.apache.commons.logging.LogFactory.getLog(getClass());
    }

    @Override
    public void setPatientIdentifier(QualifiedSubjectIdentifierType patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    @Override
    public void setPatientIdentiferLocation(String xpathToPatientId) {
        this.xpathToPatientId = xpathToPatientId;
    }

    protected Object getSubscriptionReference(SubscribeResponse subscribeResponse) {
        Object o = null;
        if (subscribeResponse != null) {
            Method[] methods = subscribeResponse.getClass().getDeclaredMethods();
            if (methods != null) {
                log.debug("Method count: " + methods.length);
                for (Method m : methods) {
                    log.debug("Looking at method: " + m.getName());
                    if (m.getName().equals("getSubscriptionReference")) {
                        try {
                            log.debug("Return type of getSubscriptionReference method: " + m.getReturnType().getName());
                            Object[] params = {};
                            o = m.invoke(subscribeResponse, params);
                            break;
                        } catch (IllegalAccessException ex) {
                            log.error(
                                    "IllegalAccessException calling getSubscriptionReference method: "
                                            + ex.getMessage(), ex);
                        } catch (IllegalArgumentException ex) {
                            log.error(
                                    "IllegalArgumentException calling getSubscriptionReference method: "
                                            + ex.getMessage(), ex);
                        } catch (InvocationTargetException ex) {
                            log.error(
                                    "InvocationTargetException calling getSubscriptionReference method: "
                                            + ex.getMessage(), ex);
                        }
                    }
                }
            } else {
                log.debug("Methods were null");
            }
        }
        return o;
    }

    protected void storeChildSubscription(String subscribeXml, String subscriptionReference,
            String parentSubscriptionReference) {
        HiemSubscriptionItem subscriptionItem = new HiemSubscriptionItem();
        subscriptionItem.setSubscriptionReferenceXML(subscriptionReference);
        subscriptionItem.setParentSubscriptionReferenceXML(parentSubscriptionReference);
        subscriptionItem.setConsumer(HiemProcessorConstants.CONSUMER_GATEWAY);
        subscriptionItem.setProducer(HiemProcessorConstants.PRODUCER_NHIN);
        subscriptionItem.setSubscribeXML(subscribeXml);

        SubscriptionStorage storage = new SubscriptionStorage();
        storage.storeExternalSubscriptionItem(subscriptionItem);
    }

    protected SubscribeResponse sendSubscribeRequest(Element subscribeElement, AssertionType assertion, UrlInfo target) {
        SubscribeResponse subscribeResponse = null;
        try {

            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            if (target != null) {
                targetSystem.setUrl(target.getUrl());
            }

            NhinHiemSubscribeProxy subscribeProxy = new NhinHiemSubscribeProxyObjectFactory()
                    .getNhinHiemSubscribeProxy();
            Element responseElement = subscribeProxy.subscribe(subscribeElement, assertion, targetSystem);

            SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
            subscribeResponse = responseMarshaller.unmarshal(responseElement);
        } catch (Exception ex) {
            log.error("Exception: " + ex.getMessage(), ex);
        }
        return subscribeResponse;
    }

    // private SubscribeRequestType buildSubscribeRequest(Subscribe subscribe, AssertionType assertion,
    // NhinTargetCommunityType community )
    // {
    // SubscribeRequestType subscribeRequest = new SubscribeRequestType();
    // subscribeRequest.setSubscribe(subscribe);
    // subscribeRequest.setAssertion(assertion);
    // NhinTargetSystemType targetSystem = new NhinTargetSystemType();
    // targetSystem.setHomeCommunity(community.getHomeCommunity());
    // subscribeRequest.setNhinTargetSystem(targetSystem);
    //
    // return subscribeRequest;
    // }

    protected void replacePatient(Subscribe subscribe, HomeCommunityType homeCommunity) {
        // TODO: Replace patient identifier in the provided subscribe message
    }

    protected void updateSubscribeNotificationConsumerEndpointAddress(Element subscribeElement) {
        try {
            // TODO: Replace Notification endpoint address
            String notificationConsumerEndpointAddress = PropertyAccessor.getInstance().getProperty("gateway",
                    "NotificationConsumerEndpointAddress");
            String xpathToAddress = "//*[local-name()='Subscribe']/*[local-name()='ConsumerReference']/*[local-name()='Address']";
            Node targetNode = XpathHelper.performXpathQuery(subscribeElement, xpathToAddress);
            if (targetNode != null) {
                log.debug("Address node found - setting address to: " + notificationConsumerEndpointAddress);
                targetNode.setTextContent(notificationConsumerEndpointAddress);
            } else {
                log.warn("EntitySubscribeHandler.updateSubscribeNotificationConsumerEndpointAddress - address node not found");
            }
        } catch (PropertyAccessException ex) {
            log.error("Error retrieving the notification consumer endpoint address: " + ex.getMessage(), ex);
        } catch (XPathExpressionException ex) {
            log.error("Error accessing the notification consumer endpoint address node: " + ex.getMessage(), ex);
        }
    }

    protected void updateSubscribe(Element subscribeElement, QualifiedSubjectIdentifierType correlation) {
        log.debug("In updateSubscribe");
        try {
            // Replace the patient identifier if present
            if (correlation != null) {
                log.debug("Correlation was not null, adding remote patient identifier to the subscribe message using patient location: "
                        + xpathToPatientId);
                String encodedPatientIdentifier = PatientIdFormatUtil.hl7EncodePatientId(
                        correlation.getSubjectIdentifier(), correlation.getAssigningAuthorityIdentifier());
                log.debug("Encoded patient identifier to add to subscribe message: " + encodedPatientIdentifier);

                Node targetNode = XpathHelper.performXpathQuery(subscribeElement, xpathToPatientId);
                if (targetNode != null) {
                    log.debug("Patient identifier node found - setting identifier");
                    targetNode.setTextContent(encodedPatientIdentifier);
                } else {
                    log.warn("EntitySubscribeHandler.buildSubscribe - patient identifier node not found");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Subscribe message after remote patient id added: "
                            + XmlUtility.serializeElementIgnoreFaults(subscribeElement));
                }

            }

            updateSubscribeNotificationConsumerEndpointAddress(subscribeElement);
        } catch (XPathExpressionException ex) {
            log.error("Error locating the patient identifier node: " + ex.getMessage(), ex);
        }
    }

    protected EndpointReferenceType storeSubscription(Subscribe subscribe, Element subscribeElement,
            AssertionType assertion, NhinTargetCommunitiesType targetCommunitites) {
        EndpointReferenceType subscriptionReference = null;

        String targetCommunititesXml = serializeTargetCommunities(targetCommunitites);

        SubscriptionItemUtil subscriptionItemUtil = new SubscriptionItemUtil();
        String subscribeXml = XmlUtility.serializeElementIgnoreFaults(subscribeElement);
        HiemSubscriptionItem subscriptionItem = subscriptionItemUtil.createSubscriptionItem(subscribe, subscribeXml,
                null, HiemProcessorConstants.CONSUMER_ADAPTER, HiemProcessorConstants.PRODUCER_GATEWAY,
                targetCommunititesXml);
        SubscriptionStorage storage = new SubscriptionStorage();
        subscriptionReference = storage.storeSubscriptionItem(subscriptionItem);
        return subscriptionReference;
    }

    protected String serializeTargetCommunities(NhinTargetCommunitiesType targetCommunitites) {
        String targetCommunitiesXml = null;
        if (targetCommunitites != null) {
            try {
                gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory ncCommonObjFact = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(ncCommonObjFact.createNhinTargetCommunities(targetCommunitites), swXML);
                targetCommunitiesXml = swXML.toString();
                log.debug("Marshaled subscription reference: " + targetCommunitiesXml);
            } catch (JAXBException ex) {
                log.error("Error serializing the target communitites: " + ex.getMessage(), ex);
            }
        }
        return targetCommunitiesXml;
    }

    protected String serializeEndpointReferenceType(EndpointReferenceType endpointRefernece) {
        String endpointReferenceXml = null;
        if (endpointRefernece != null) {
            try {
                org.w3._2005._08.addressing.ObjectFactory wsaObjFact = new org.w3._2005._08.addressing.ObjectFactory();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("org.w3._2005._08.addressing");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(wsaObjFact.createEndpointReference(endpointRefernece), swXML);
                endpointReferenceXml = swXML.toString();
                log.debug("Marshaled endpoint reference: " + endpointReferenceXml);
            } catch (JAXBException ex) {
                log.error("Error serializing the endpoint reference: " + ex.getMessage(), ex);
            }
        }
        return endpointReferenceXml;
    }

    protected String serializeW3CEndpointReference(W3CEndpointReference endpointRefernece) {
        String endpointReferenceXml = null;
        if (endpointRefernece != null) {
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("javax.xml.ws.wsaddressing");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(endpointRefernece, swXML);
                endpointReferenceXml = swXML.toString();
                log.debug("Marshaled W3C endpoint reference: " + endpointReferenceXml);
            } catch (JAXBException ex) {
                log.error("Error serializing the W3C endpoint reference: " + ex.getMessage(), ex);
            }
        }
        return endpointReferenceXml;
    }

    /**
     * Use reflection to set the subscription reference. The runtime parameter type of the setSubscriptionReference
     * method of SubscriptionResponse is checked and the correct parameter type is created and the method is invoked
     * with the correct type.
     *
     * This is necessary because the buildtime type and runtime type are different for the method called.
     *
     * @param response Subscription response method.
     * @param subRef Subscription reference
     */
    @SuppressWarnings("unchecked")
    protected void setSubscriptionReference(SubscribeResponse response, EndpointReferenceType subRef) {
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
                                        Object[] params = { subRef };
                                        log.debug("Invoking EndpointReferenceType param method");
                                        m.invoke(response, params);
                                        break;
                                    } else if (paramType.isAssignableFrom(W3CEndpointReference.class)) {
                                        log.debug("Param type is W3CEndpointReference");
                                        Object[] params = { convertEndpointReferenceToW3cEndpointReference(subRef) };
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

    protected W3CEndpointReference convertEndpointReferenceToW3cEndpointReference(EndpointReferenceType epr) {
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

            if ((epr.getReferenceParameters() != null) && (epr.getReferenceParameters().getAny() != null)
                    && (!epr.getReferenceParameters().getAny().isEmpty())) {
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
