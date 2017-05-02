package gov.hhs.fha.nhinc.transform.policy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class AdhocQueryTransformHelperTest {

    private AdhocQueryTransformHelper instance;

    @Before
    public void setUp() {
        instance = new AdhocQueryTransformHelper();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    @Test
    public void testTransformAdhocQueryToCheckPolicy() {

        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
        eventA.setDirection("inbound");
        AdhocQueryRequestMessageType msg = new AdhocQueryRequestMessageType();
        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        SlotType1 slotType1 = new SlotType1();
        slotType1.setName("$XDSDocumentEntryPatientId");

        ValueListType valueListType = new ValueListType();
        valueListType.getValue().add("1111.4444^^^&amp;26.489.22&amp;ISO");
        slotType1.setValueList(valueListType);
        AdhocQueryType adhocQueryType = new AdhocQueryType();
        adhocQueryType.getSlot().add(slotType1);

        adhocQueryRequest.setAdhocQuery(adhocQueryType);
        msg.setAdhocQueryRequest(adhocQueryRequest);
        AssertionType assertionTypeTest = new AssertionType();
        assertionTypeTest.setMessageId("12345566");
        HomeCommunityType homeCom = new HomeCommunityType();
        homeCom.setHomeCommunityId("urn:oid:1.1.1.1.1.1.1");
        assertionTypeTest.setHomeCommunity(homeCom);
        msg.setAssertion(assertionTypeTest);
        eventA.setMessage(msg);

        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        verifycheckInboundPolicy(result);

    }
    
    @Test
    public void testTransformAdhocQueryToCheckPolicy_Outbound() {

        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
        eventA.setDirection("outbound");
        AdhocQueryRequestMessageType msg = new AdhocQueryRequestMessageType();
        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        SlotType1 slotType1 = new SlotType1();
        slotType1.setName("$XDSDocumentEntryPatientId");

        ValueListType valueListType = new ValueListType();
        valueListType.getValue().add("1111.4444^^^&amp;26.489.22&amp;ISO");
        slotType1.setValueList(valueListType);
        AdhocQueryType adhocQueryType = new AdhocQueryType();
        adhocQueryType.getSlot().add(slotType1);

        adhocQueryRequest.setAdhocQuery(adhocQueryType);
        msg.setAdhocQueryRequest(adhocQueryRequest);
        AssertionType assertionTypeTest = new AssertionType();
        assertionTypeTest.setMessageId("12345566");
        HomeCommunityType homeCom = new HomeCommunityType();
        homeCom.setHomeCommunityId("urn:oid:1.1.1.1.1.1.1");
        assertionTypeTest.setHomeCommunity(homeCom);
        msg.setAssertion(assertionTypeTest);
        eventA.setMessage(msg);

        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        verifycheckOutboundPolicy(result);

    }
    
    private void verifycheckOutboundPolicy(CheckPolicyRequestType actualCheckPolicy) {
        // Verify result
        assertNotNull(actualCheckPolicy);
        // AssertAssertion to make sure nothing change
        AssertionType assertTypeResult = actualCheckPolicy.getAssertion();

        assertEquals("messageId should not change", "12345566", assertTypeResult.getMessageId());
        // Assert Resource Session
        assertEquals("urn:oasis:names:tc:xacml:1.0:resource:resource-id", actualCheckPolicy.getRequest().getResource()
            .get(0).getAttribute().get(1).getAttributeId());
        String expectResourceId = "amp;26.489.22";
        assertEquals(expectResourceId, actualCheckPolicy.getRequest().getResource().get(0).getAttribute().get(0)
            .getAttributeValue().get(0).getContent().get(0));

        assertEquals("DocumentQueryOut", actualCheckPolicy.getRequest().getAction().getAttribute().get(0)
            .getAttributeValue().get(0).getContent().get(0));
        SubjectType subjectType = actualCheckPolicy.getRequest().getSubject().get(0);
        AttributeType attributeType = subjectType.getAttribute().get(1);
        assertEquals("http://www.hhs.gov/healthit/nhin#HomeCommunityId", attributeType.getAttributeId());
        assertEquals("http://www.w3.org/2001/XMLSchema#anyURI", attributeType.getDataType());
        assertEquals("urn:oid:1.1.1.1.1.1.1", attributeType.getAttributeValue().get(0).getContent().get(0));
    }
  

    private void verifycheckInboundPolicy(CheckPolicyRequestType actualCheckPolicy) {
        // Verify result
        assertNotNull(actualCheckPolicy);
        // AssertAssertion to make sure nothing change
        AssertionType assertTypeResult = actualCheckPolicy.getAssertion();

        assertEquals("messageId should not change", "12345566", assertTypeResult.getMessageId());
        // Assert Resource Session
        assertEquals("urn:oasis:names:tc:xacml:1.0:resource:resource-id", actualCheckPolicy.getRequest().getResource()
            .get(0).getAttribute().get(1).getAttributeId());
        String expectResourceId = "amp;26.489.22";
        assertEquals(expectResourceId, actualCheckPolicy.getRequest().getResource().get(0).getAttribute().get(0)
            .getAttributeValue().get(0).getContent().get(0));

        assertEquals("DocumentQueryIn", actualCheckPolicy.getRequest().getAction().getAttribute().get(0)
            .getAttributeValue().get(0).getContent().get(0));
        SubjectType subjectType = actualCheckPolicy.getRequest().getSubject().get(0);
        AttributeType attributeType = subjectType.getAttribute().get(1);
        assertEquals("http://www.hhs.gov/healthit/nhin#HomeCommunityId", attributeType.getAttributeId());
        assertEquals("http://www.w3.org/2001/XMLSchema#anyURI", attributeType.getDataType());
        assertEquals("urn:oid:1.1.1.1.1.1.1", attributeType.getAttributeValue().get(0).getContent().get(0));
    }
    
 
    @Test
    public void testTransformAdhocQueryToCheckPolicy_InValidDirection() {
        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
        eventA.setDirection("in_outbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        assertNull("We should expect null checkPolicyEngine ", result);
        eventA.setDirection(null);
        assertNull(instance.transformAdhocQueryToCheckPolicy(eventA));
        assertNull(instance.transformAdhocQueryToCheckPolicy(null));
    }
    
 
    @Test
    public void testTransformAdhocQueryToCheckPolicy_outbound() {
    	AdhocQueryTransformHelper instance = new AdhocQueryTransformHelper();
        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
     //   eventA.getDirection()= "inbound";
       
        eventA.setDirection("outbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        assertNotNull(result);
    }

    @Test
    public void testTransformAdhocQueryToCheckPolicy_AssertNotNull() {
    	AdhocQueryTransformHelper instance = new AdhocQueryTransformHelper();
        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
     //   eventA.getDirection()= "inbound";
       
        eventA.setDirection("inbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        assertNotNull(result);
    }

    @Test
    public void testTransformAdhocQueryResponseToCheckPolicy_OutBound() {
    	AdhocQueryTransformHelper instance = new AdhocQueryTransformHelper();
    	AdhocQueryResultEventType eventB = new AdhocQueryResultEventType();
     //   eventA.getDirection()= "inbound";
        eventB.setDirection("outbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryResponseToCheckPolicy(eventB);
        assertNotNull(result);
    }
    
    @Test
    public void testTransformAdhocQueryResponseToCheckPolicy_InBound() {
    	AdhocQueryTransformHelper instance = new AdhocQueryTransformHelper();
    	AdhocQueryResultEventType eventB = new AdhocQueryResultEventType();
     //   eventA.getDirection()= "inbound";
        eventB.setDirection("inbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryResponseToCheckPolicy(eventB);
        assertNotNull(result);
    }

    
}