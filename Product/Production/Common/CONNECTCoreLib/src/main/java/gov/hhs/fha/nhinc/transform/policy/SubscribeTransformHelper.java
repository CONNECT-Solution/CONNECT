/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.policy;

//import com.sun.jmx.remote.internal.Unmarshal;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeMessageType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.StringReader;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import org.w3c.dom.Element;

/**
 *
 * @author svalluripalli
 */
public class SubscribeTransformHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(SubscribeTransformHelper.class);
    private static final String ActionInValue = "HIEMSubscriptionRequestIn";
    private static final String ActionOutValue = "HIEMSubscriptionRequestOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;
    private static final String ATTRIBUTE_ID_TOPIC = Constants.ATTRIBUTE_ID_SUBSCRIPTION_TOPIC;

    public static CheckPolicyRequestType transformSubscribeToCheckPolicy(SubscribeEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        SubscribeMessageType message = event.getMessage();
        RequestType request = new RequestType();
        if (InboundOutboundChecker.IsInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }
        if (InboundOutboundChecker.IsOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        }

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        request.getSubject().add(subject);

        AdhocQueryRequest adhocReq = new AdhocQueryRequest();
        AdhocQueryType adhocQuery = new AdhocQueryType();
        adhocQuery = getAdhocQuery(event.getMessage().getSubscribe());
        adhocReq.setAdhocQuery(adhocQuery);
        String patId = AdhocQueryTransformHelper.extractPatientIdentifierId(adhocReq);
        String assignAuth = AdhocQueryTransformHelper.extractPatientIdentifierAssigningAuthority(adhocReq);

        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();

        if (NullChecker.isNotNullish(assignAuth)) {
            resource.getAttribute().add(attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, assignAuth));
        }

        if (NullChecker.isNotNullish(patId)) {
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(patId);
            log.debug("transformSubscribeToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
        }

        setTopic(event, resource);

        request.getResource().add(resource);

        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());


        genericPolicyRequest.setRequest(request);
        genericPolicyRequest.setAssertion(event.getMessage().getAssertion());
        return genericPolicyRequest;
    }

    private static void setTopic(SubscribeEventType event, ResourceType resource) {
        String topic = null;
        try {
            log.debug("######## BEGIN TOPIC EXTRACTION ########");
            JAXBElement<TopicExpressionType> jbElement = (JAXBElement<TopicExpressionType>) event.getMessage().getSubscribe().getFilter().getAny().get(0);
            TopicExpressionType topicExpression = jbElement.getValue();
            topic = (String) topicExpression.getContent().get(0);
            log.debug("Topic extracted: " + topic);
        } catch (Throwable t) {
            log.error("Error extracting the topic: " + t.getMessage(), t);
        }
        if (NullChecker.isNotNullish(topic)) {
            if (log.isDebugEnabled()) {
                log.debug("Adding topic (" + topic + ") as attribute (" + ATTRIBUTE_ID_TOPIC + ") and type: " + Constants.DataTypeString);
            }
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(attrHelper.attributeFactory(ATTRIBUTE_ID_TOPIC, Constants.DataTypeString, topic));
        }
    }

    public static AdhocQueryType getAdhocQuery(Subscribe nhinSubscribe) {
        AdhocQueryType adhocQuery = null;
        List<Object> any = nhinSubscribe.getAny();


        for (Object anyItem : any) {
            log.debug("SubscribeTransformHelper.getAdhocQuery - type of any in list: " + anyItem.getClass().getName());
            if (anyItem instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType) {
                log.debug("Any item was oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType");
                adhocQuery = (AdhocQueryType) anyItem;
            } else if (anyItem instanceof JAXBElement) {
                log.debug("Any item was JAXBElement");
                if (((JAXBElement) anyItem).getValue() instanceof AdhocQueryType) {
                    log.debug("Any item - JAXBElement value was AdhocQueryType");
                    adhocQuery = (AdhocQueryType) ((JAXBElement) anyItem).getValue();
                }
            } else if (anyItem instanceof Element) {
                log.debug("Any item was Element");
                Element element = (Element) anyItem;
                log.debug("SubscribeTransformHelper.getAdhocQuery - element name of any in list: " + element.getLocalName());
                adhocQuery = unmarshalAdhocQuery(element);
                //Object o = (JAXBElement<oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType>) anyItem;

                //  Object o = (JAXBElement<oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType>) anyItem;
            } else {
                log.debug("Any type did not fit any expected value");
            }
        }
        return adhocQuery;
    }

    public static AdhocQueryType unmarshalAdhocQuery(Element adhocQueryElement) {
        AdhocQueryType unmarshalledObject = null;
        String contextPath = "oasis.names.tc.ebxml_regrep.xsd.rim._3";
        log.debug("begin unmarshal");

        if (adhocQueryElement == null) {
            log.warn("element to unmarshal is null");
        } else if (contextPath == null) {
            log.warn("no contextPath supplied");
        } else {
            try {
                log.debug("desializing element");
                String serializedElement = XmlUtility.serializeElement(adhocQueryElement);
                log.debug("serializedElement=[" + serializedElement + "]");
                log.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                log.debug("get instance of unmarshaller");
                javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
                log.debug("init stringReader");
                StringReader stringReader = new StringReader(serializedElement);
                log.debug("Calling unmarshal");
                JAXBElement<AdhocQueryType> jaxbElement = (JAXBElement<AdhocQueryType>) unmarshaller.unmarshal(stringReader);
                log.debug("unmarshalled to JAXBElement");
                unmarshalledObject = jaxbElement.getValue();
                log.debug("end unmarshal");
            } catch (Exception e) {
                //"java.security.PrivilegedActionException: java.lang.ClassNotFoundException: com.sun.xml.bind.v2.ContextFactory"
                //use jaxb element
                log.error("Failed to unmarshall: " + e.getMessage(), e);
                unmarshalledObject = null;
            }
        }

        return unmarshalledObject;
    }
}
