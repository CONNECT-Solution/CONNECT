package gov.hhs.fha.nhinc.hiem.processor.entity.handler;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.hiem.processor.common.HiemProcessorConstants;
import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionItemUtil;
import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionStorage;
import gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe.NhinHiemSubscribeProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe.NhinHiemSubscribeProxyObjectFactory;
import javax.xml.bind.JAXBException;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;

/**
 * Base entity subscribe handler
 *
 * @author Neil Webb
 */
public abstract class BaseEntitySubscribeHandler implements EntitySubscribeHandler
{
    protected org.apache.commons.logging.Log log = null;
    protected QualifiedSubjectIdentifierType patientIdentifier = null;
    protected String xpathToPatientId = null;

    public BaseEntitySubscribeHandler()
    {
        log = org.apache.commons.logging.LogFactory.getLog(getClass());
    }

    public void setPatientIdentifier(QualifiedSubjectIdentifierType patientIdentifier)
    {
        this.patientIdentifier = patientIdentifier;
    }

    public void setPatientIdentiferLocation(String xpathToPatientId)
    {
        this.xpathToPatientId = xpathToPatientId;
    }

    protected Object getSubscriptionReference(SubscribeResponse subscribeResponse)
    {
        Object o = null;
        if(subscribeResponse != null)
        {
            Method[] methods = subscribeResponse.getClass().getDeclaredMethods();
            if(methods != null)
            {
                log.debug("Method count: " + methods.length);
                for(Method m : methods)
                {
                    log.debug("Looking at method: " + m.getName());
                    if(m.getName().equals("getSubscriptionReference"))
                    {
                        try {
                            log.debug("Return type of getSubscriptionReference method: " + m.getReturnType().getName());
                            Object[] params = {};
                            o = m.invoke(subscribeResponse, params);
                            break;
                        }
                        catch (IllegalAccessException ex) {
                            log.error("IllegalAccessException calling getSubscriptionReference method: " + ex.getMessage(), ex);
                        }
                        catch (IllegalArgumentException ex) {
                            log.error("IllegalArgumentException calling getSubscriptionReference method: " + ex.getMessage(), ex);
                        }
                        catch (InvocationTargetException ex) {
                            log.error("InvocationTargetException calling getSubscriptionReference method: " + ex.getMessage(), ex);
                        }
                    }
                }
            }
            else
            {
                log.debug("Methods were null");
            }
        }
        return o;
    }

    protected void storeChildSubscription(String subscribeXml, String subscriptionReference, String parentSubscriptionReference)
    {
        HiemSubscriptionItem subscriptionItem = new HiemSubscriptionItem();
        subscriptionItem.setSubscriptionReferenceXML(subscriptionReference);
        subscriptionItem.setParentSubscriptionReferenceXML(parentSubscriptionReference);
        subscriptionItem.setConsumer(HiemProcessorConstants.CONSUMER_GATEWAY);
        subscriptionItem.setProducer(HiemProcessorConstants.PRODUCER_NHIN);
        subscriptionItem.setSubscribeXML(subscribeXml);

        SubscriptionStorage storage = new SubscriptionStorage();
        storage.storeExternalSubscriptionItem(subscriptionItem);
    }

    protected SubscribeResponse sendSubscribeRequest(Element subscribeElement, AssertionType assertion, CMUrlInfo target)
    {
        SubscribeResponse subscribeResponse = null;
        try {

            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            if(target != null)
            {
                targetSystem.setUrl(target.getUrl());
            }

            NhinHiemSubscribeProxy subscribeProxy = new NhinHiemSubscribeProxyObjectFactory().getNhinHiemSubscribeProxy();
            Element responseElement = subscribeProxy.subscribe(subscribeElement, assertion, targetSystem);

            SubscribeResponseMarshaller responseMarshaller = new SubscribeResponseMarshaller();
            subscribeResponse = responseMarshaller.unmarshal(responseElement);
        }
        catch (InvalidFilterFault ex) {
            log.error("InvalidFilterFault: " + ex.getMessage(), ex);
        }
        catch (InvalidMessageContentExpressionFault ex) {
            log.error("InvalidMessageContentExpressionFault: " + ex.getMessage(), ex);
        }
        catch (InvalidProducerPropertiesExpressionFault ex) {
            log.error("InvalidProducerPropertiesExpressionFault: " + ex.getMessage(), ex);
        }
        catch (InvalidTopicExpressionFault ex) {
            log.error("InvalidTopicExpressionFault: " + ex.getMessage(), ex);
        }
        catch (NotifyMessageNotSupportedFault ex) {
            log.error("NotifyMessageNotSupportedFault: " + ex.getMessage(), ex);
        }
        catch (ResourceUnknownFault ex) {
            log.error("ResourceUnknownFault: " + ex.getMessage(), ex);
        }
        catch (SubscribeCreationFailedFault ex) {
            log.error("SubscribeCreationFailedFault: " + ex.getMessage(), ex);
        }
        catch (TopicExpressionDialectUnknownFault ex) {
            log.error("TopicExpressionDialectUnknownFault: " + ex.getMessage(), ex);
        }
        catch (TopicNotSupportedFault ex) {
            log.error("TopicNotSupportedFault: " + ex.getMessage(), ex);
        }
        catch (UnacceptableInitialTerminationTimeFault ex) {
            log.error("UnacceptableInitialTerminationTimeFault: " + ex.getMessage(), ex);
        }
        catch (UnrecognizedPolicyRequestFault ex) {
            log.error("UnrecognizedPolicyRequestFault: " + ex.getMessage(), ex);
        }
        catch (UnsupportedPolicyRequestFault ex) {
            log.error("UnsupportedPolicyRequestFault: " + ex.getMessage(), ex);
        }
        return subscribeResponse;
    }

//    private SubscribeRequestType buildSubscribeRequest(Subscribe subscribe, AssertionType assertion, NhinTargetCommunityType community )
//    {
//        SubscribeRequestType subscribeRequest = new SubscribeRequestType();
//        subscribeRequest.setSubscribe(subscribe);
//        subscribeRequest.setAssertion(assertion);
//        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
//        targetSystem.setHomeCommunity(community.getHomeCommunity());
//        subscribeRequest.setNhinTargetSystem(targetSystem);
//
//        return subscribeRequest;
//    }

    protected void replacePatient(Subscribe subscribe, HomeCommunityType homeCommunity)
    {
        // TODO: Replace patient identifier in the provided subscribe message
    }

    protected void updateSubscribeNotificationConsumerEndpointAddress(Element subscribeElement)
    {
        try
        {
            // TODO: Replace Notification endpoint address
            String notificationConsumerEndpointAddress = PropertyAccessor.getProperty("gateway", "NotificationConsumerEndpointAddress");
            String xpathToAddress = "//*[local-name()='Subscribe']/*[local-name()='ConsumerReference']/*[local-name()='Address']";
            Node targetNode = XpathHelper.performXpathQuery(subscribeElement, xpathToAddress);
            if(targetNode != null)
            {
                log.debug("Address node found - setting address to: " + notificationConsumerEndpointAddress);
                targetNode.setTextContent(notificationConsumerEndpointAddress);
            }
            else
            {
                log.warn("EntitySubscribeHandler.updateSubscribeNotificationConsumerEndpointAddress - address node not found");
            }
        }
        catch (PropertyAccessException ex)
        {
            log.error("Error retrieving the notification consumer endpoint address: " + ex.getMessage(), ex);
        }
        catch (XPathExpressionException ex)
        {
            log.error("Error accessing the notification consumer endpoint address node: " + ex.getMessage(), ex);
        }
    }

    protected void updateSubscribe(Element subscribeElement, QualifiedSubjectIdentifierType correlation)
    {
        log.debug("In updateSubscribe");
        try
        {
            // Replace the patient identifier if present
            if(correlation != null)
            {
                log.debug("Correlation was not null, adding remote patient identifier to the subscribe message using patient location: " + xpathToPatientId);
                String encodedPatientIdentifier = PatientIdFormatUtil.hl7EncodePatientId(correlation.getSubjectIdentifier(), correlation.getAssigningAuthorityIdentifier());
                log.debug("Encoded patient identifier to add to subscribe message: " + encodedPatientIdentifier);
                
                Node targetNode = XpathHelper.performXpathQuery(subscribeElement, xpathToPatientId);
                if(targetNode != null)
                {
                    log.debug("Patient identifier node found - setting identifier");
                    targetNode.setTextContent(encodedPatientIdentifier);
                }
                else
                {
                    log.warn("EntitySubscribeHandler.buildSubscribe - patient identifier node not found");
                }
                if(log.isDebugEnabled())
                {
                    log.debug("Subscribe message after remote patient id added: " + XmlUtility.serializeElementIgnoreFaults(subscribeElement));
                }

            }

            updateSubscribeNotificationConsumerEndpointAddress(subscribeElement);
        }
        catch (XPathExpressionException ex)
        {
            log.error("Error locating the patient identifier node: " + ex.getMessage(), ex);
        }
    }

    protected EndpointReferenceType storeSubscription(Subscribe subscribe, Element subscribeElement, AssertionType assertion, NhinTargetCommunitiesType targetCommunitites)
    {
        EndpointReferenceType subscriptionReference = null;

        String targetCommunititesXml = serializeTargetCommunities(targetCommunitites);

        SubscriptionItemUtil subscriptionItemUtil = new SubscriptionItemUtil();
        String subscribeXml = XmlUtility.serializeElementIgnoreFaults(subscribeElement);
        HiemSubscriptionItem subscriptionItem = subscriptionItemUtil.createSubscriptionItem(subscribe, subscribeXml, null, HiemProcessorConstants.CONSUMER_ADAPTER, HiemProcessorConstants.PRODUCER_GATEWAY, targetCommunititesXml);
        SubscriptionStorage storage = new SubscriptionStorage();
        subscriptionReference = storage.storeSubscriptionItem(subscriptionItem);
        return subscriptionReference;
    }

    protected String serializeTargetCommunities(NhinTargetCommunitiesType targetCommunitites)
    {
        String targetCommunitiesXml = null;
        if(targetCommunitites != null)
        {
            try
            {
                gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory ncCommonObjFact = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(ncCommonObjFact.createNhinTargetCommunities(targetCommunitites), swXML);
                targetCommunitiesXml = swXML.toString();
                log.debug("Marshaled subscription reference: " + targetCommunitiesXml);
            }
            catch (JAXBException ex)
            {
                log.error("Error serializing the target communitites: " + ex.getMessage(), ex);
            }
        }
        return targetCommunitiesXml;
    }

    protected String serializeEndpointReferenceType(EndpointReferenceType endpointRefernece)
    {
        String endpointReferenceXml = null;
        if(endpointRefernece != null)
        {
            try
            {
                org.w3._2005._08.addressing.ObjectFactory wsaObjFact = new org.w3._2005._08.addressing.ObjectFactory();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("org.w3._2005._08.addressing");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(wsaObjFact.createEndpointReference(endpointRefernece), swXML);
                endpointReferenceXml = swXML.toString();
                log.debug("Marshaled endpoint reference: " + endpointReferenceXml);
            }
            catch (JAXBException ex)
            {
                log.error("Error serializing the endpoint reference: " + ex.getMessage(), ex);
            }
        }
        return endpointReferenceXml;
    }

    protected String serializeW3CEndpointReference(W3CEndpointReference endpointRefernece)
    {
        String endpointReferenceXml = null;
        if(endpointRefernece != null)
        {
            try
            {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("javax.xml.ws.wsaddressing");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                log.debug("Calling marshal");
                marshaller.marshal(endpointRefernece, swXML);
                endpointReferenceXml = swXML.toString();
                log.debug("Marshaled W3C endpoint reference: " + endpointReferenceXml);
            }
            catch (JAXBException ex)
            {
                log.error("Error serializing the W3C endpoint reference: " + ex.getMessage(), ex);
            }
        }
        return endpointReferenceXml;
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
    protected void setSubscriptionReference(SubscribeResponse response, EndpointReferenceType subRef)
    {
        log.debug("In setSubscriptionReference");
        if((response != null) && (subRef != null))
        {
            try
            {
                Method[] methods = response.getClass().getDeclaredMethods();
                if(methods != null)
                {
                    log.debug("Method count: " + methods.length);
                    for(Method m : methods)
                    {
                        log.debug("Looking at method: " + m.getName());
                        if(m.getName().equals("setSubscriptionReference"))
                        {
                            Class[] paramTypes = m.getParameterTypes();
                            if(paramTypes != null)
                            {
                                log.debug("Parameter count: " + paramTypes.length);
                                for(Class paramType : paramTypes)
                                {
                                    log.debug("Param type: " + paramType.getName());
                                    if(paramType.isAssignableFrom(EndpointReferenceType.class))
                                    {
                                        log.debug("Param type is EndpointReferenceType");
                                        Object[] params = {subRef};
                                        log.debug("Invoking EndpointReferenceType param method");
                                        m.invoke(response, params);
                                        break;
                                    }
                                    else if (paramType.isAssignableFrom(W3CEndpointReference.class))
                                    {
                                        log.debug("Param type is W3CEndpointReference");
                                        Object[] params = {convertEndpointReferenceToW3cEndpointReference(subRef)};
                                        log.debug("Invoking W3CEndpointReference param method");
                                        m.invoke(response, params);
                                        break;
                                    }

                                }
                            }
                            else
                            {
                                log.debug("Parameter types was null");
                            }
                            break;
                        }
                    }
                }
                else
                {
                    log.debug("Methods were null");
                }
            }
            catch (IllegalAccessException ex)
            {
                log.error("IllegalAccessException encountered: " + ex.getMessage(), ex);
            }
            catch (IllegalArgumentException ex)
            {
                log.error("IllegalArgumentException encountered: " + ex.getMessage(), ex);
            }
            catch (InvocationTargetException ex)
            {
                log.error("InvocationTargetException encountered: " + ex.getMessage(), ex);
            }
        }
    }

    protected W3CEndpointReference convertEndpointReferenceToW3cEndpointReference(EndpointReferenceType epr)
    {
        log.info("begin CreateSubscriptionReference");
        W3CEndpointReference subRef = null;

        if(epr != null)
        {
            W3CEndpointReferenceBuilder resultBuilder = new W3CEndpointReferenceBuilder();

            if(epr.getAddress() != null)
            {
                log.info("subscriptionManagerUrl=" + epr.getAddress().getValue());
                resultBuilder.address(epr.getAddress().getValue());
            }
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            Document doc = null;
            try
            {
                doc = docBuilderFactory.newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException ex)
            {
                throw new RuntimeException(ex);
            }
            doc.setXmlStandalone(true);

            if((epr.getReferenceParameters() != null) &&
                    (epr.getReferenceParameters().getAny() != null) &&
                    (!epr.getReferenceParameters().getAny().isEmpty()))
            {
                List<Object> refParams = epr.getReferenceParameters().getAny();
                for(Object o : refParams)
                {
                    log.debug("Processing a reference parameter");
                    if(o instanceof Element)
                    {
                        Element refParam = (Element)o;
                        resultBuilder.referenceParameter(refParam);
                    }
                    else
                    {
                        log.warn("Reference parameter was not of type Element - was " + o.getClass());
                    }
                }
            }
            else
            {
                log.warn("Reference parameters or ref param list was null or empty");
            }

            log.info("building.. resultBuilder.build()");
            subRef = resultBuilder.build();
        }
        else
        {
            log.warn("The endpoint reference was null");
        }

        log.info("end CreateSubscriptionReference");
        return subRef;
    }
    
}
