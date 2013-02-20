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
package gov.hhs.fha.nhinc.transform.policy;

//import com.sun.jmx.remote.internal.Unmarshal;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
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
import org.apache.log4j.Logger;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import org.w3c.dom.Element;

/**
 *
 * @author svalluripalli
 */
public class SubscribeTransformHelper {

    private static final Logger LOG = Logger.getLogger(SubscribeTransformHelper.class);
    private static final String ActionInValue = "HIEMSubscriptionRequestIn";
    private static final String ActionOutValue = "HIEMSubscriptionRequestOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;
    private static final String ATTRIBUTE_ID_TOPIC = Constants.ATTRIBUTE_ID_SUBSCRIPTION_TOPIC;

    public static CheckPolicyRequestType transformSubscribeToCheckPolicy(SubscribeEventType event) {
        CheckPolicyRequestType genericPolicyRequest = new CheckPolicyRequestType();
        RequestType request = new RequestType();
        if (InboundOutboundChecker.isInbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionInValue));
        }
        if (InboundOutboundChecker.isOutbound(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ActionOutValue));
        }

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(), event.getMessage()
            .getAssertion());
        request.getSubject().add(subject);

        AdhocQueryRequest adhocReq = new AdhocQueryRequest();
        AdhocQueryType adhocQuery = null;
        adhocQuery = getAdhocQuery(event.getMessage().getSubscribe());
        adhocReq.setAdhocQuery(adhocQuery);
        String patId = AdhocQueryTransformHelper.extractPatientIdentifierId(adhocReq);
        String assignAuth = AdhocQueryTransformHelper.extractPatientIdentifierAssigningAuthority(adhocReq);

        ResourceType resource = new ResourceType();
        AttributeHelper attrHelper = new AttributeHelper();

        if (NullChecker.isNotNullish(assignAuth)) {
            resource.getAttribute().add(
                attrHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString,
                assignAuth));
        }

        if (NullChecker.isNotNullish(patId)) {
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(patId);
            LOG.debug("transformSubscribeToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(
                attrHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
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
            LOG.debug("######## BEGIN TOPIC EXTRACTION ########");
            JAXBElement<TopicExpressionType> jbElement = (JAXBElement<TopicExpressionType>) event.getMessage()
                .getSubscribe().getFilter().getAny().get(0);
            TopicExpressionType topicExpression = jbElement.getValue();
            topic = (String) topicExpression.getContent().get(0);
            LOG.debug("Topic extracted: " + topic);
        } catch (Throwable t) {
            LOG.error("Error extracting the topic: " + t.getMessage(), t);
        }
        if (NullChecker.isNotNullish(topic)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Adding topic (" + topic + ") as attribute (" + ATTRIBUTE_ID_TOPIC + ") and type: "
                    + Constants.DataTypeString);
            }
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(
                attrHelper.attributeFactory(ATTRIBUTE_ID_TOPIC, Constants.DataTypeString, topic));
        }
    }

    public static AdhocQueryType getAdhocQuery(Subscribe nhinSubscribe) {
        AdhocQueryType adhocQuery = null;
        List<Object> any = nhinSubscribe.getAny();

        for (Object anyItem : any) {
            LOG.debug("SubscribeTransformHelper.getAdhocQuery - type of any in list: " + anyItem.getClass().getName());
            if (anyItem instanceof oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType) {
                LOG.debug("Any item was oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType");
                adhocQuery = (AdhocQueryType) anyItem;
            } else if (anyItem instanceof JAXBElement) {
                LOG.debug("Any item was JAXBElement");
                if (((JAXBElement) anyItem).getValue() instanceof AdhocQueryType) {
                    LOG.debug("Any item - JAXBElement value was AdhocQueryType");
                    adhocQuery = (AdhocQueryType) ((JAXBElement) anyItem).getValue();
                }
            } else if (anyItem instanceof Element) {
                LOG.debug("Any item was Element");
                Element element = (Element) anyItem;
                LOG.debug("SubscribeTransformHelper.getAdhocQuery - element name of any in list: "
                    + element.getLocalName());
                adhocQuery = unmarshalAdhocQuery(element);
            } else {
                LOG.debug("Any type did not fit any expected value");
            }
        }
        return adhocQuery;
    }

    public static AdhocQueryType unmarshalAdhocQuery(Element adhocQueryElement) {
        AdhocQueryType unmarshalledObject = null;
        String contextPath = "oasis.names.tc.ebxml_regrep.xsd.rim._3";
        LOG.debug("begin unmarshal");

        if (adhocQueryElement == null) {
            LOG.warn("element to unmarshal is null");
        } else {
            try {
                LOG.debug("desializing element");
                String serializedElement = XmlUtility.serializeElement(adhocQueryElement);
                LOG.debug("serializedElement=[" + serializedElement + "]");
                LOG.debug("get instance of JAXBContext [contextPath='" + contextPath + "']");
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext(contextPath);
                LOG.debug("get instance of unmarshaller");
                javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
                LOG.debug("init stringReader");
                StringReader stringReader = new StringReader(serializedElement);
                LOG.debug("Calling unmarshal");
                JAXBElement<AdhocQueryType> jaxbElement = (JAXBElement<AdhocQueryType>) unmarshaller
                    .unmarshal(stringReader);
                LOG.debug("unmarshalled to JAXBElement");
                unmarshalledObject = jaxbElement.getValue();
                LOG.debug("end unmarshal");
            } catch (Exception e) {
                // "java.security.PrivilegedActionException: java.lang.ClassNotFoundException: com.sun.xml.bind.v2.ContextFactory"
                // use jaxb element
                LOG.error("Failed to unmarshall: " + e.getMessage(), e);
                unmarshalledObject = null;
            }
        }

        return unmarshalledObject;
    }
}
