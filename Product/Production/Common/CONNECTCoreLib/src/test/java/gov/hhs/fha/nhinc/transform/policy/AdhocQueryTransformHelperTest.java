/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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

import org.junit.Test;

public class AdhocQueryTransformHelperTest {

    private AdhocQueryTransformHelper instance;
    private static final String TEST_HC_VAL = "urn:oid:1.1.1.1.1.1.1";
    private static final String MESSAGE_ID = "12345566";
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
        String direction = "DocumentQueryIn";
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
        assertionTypeTest.setMessageId(MESSAGE_ID);
        HomeCommunityType homeCom = new HomeCommunityType();
        homeCom.setHomeCommunityId(TEST_HC_VAL);
        assertionTypeTest.setHomeCommunity(homeCom);
        msg.setAssertion(assertionTypeTest);
        eventA.setMessage(msg);

        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        verifycheckPolicy(result, direction);

    }
    
    @Test
    public void testTransformAdhocQueryToCheckPolicy_Outbound() {
    	 String direction = "DocumentQueryOut";
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
        assertionTypeTest.setMessageId(MESSAGE_ID);
        HomeCommunityType homeCom = new HomeCommunityType();
        homeCom.setHomeCommunityId(TEST_HC_VAL);
        assertionTypeTest.setHomeCommunity(homeCom);
        msg.setAssertion(assertionTypeTest);
        eventA.setMessage(msg);
        
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        verifycheckPolicy(result, direction );

    }
    
    private void verifycheckPolicy(CheckPolicyRequestType actualCheckPolicy, String direction) {
        
        assertNotNull(actualCheckPolicy);
        
        AssertionType assertTypeResult = actualCheckPolicy.getAssertion();

        assertEquals("messageId should not change", MESSAGE_ID, assertTypeResult.getMessageId());
        
        assertEquals("urn:oasis:names:tc:xacml:1.0:resource:resource-id", actualCheckPolicy.getRequest().getResource()
            .get(0).getAttribute().get(1).getAttributeId());
        String expectResourceId = "amp;26.489.22";
        assertEquals(expectResourceId, actualCheckPolicy.getRequest().getResource().get(0).getAttribute().get(0)
            .getAttributeValue().get(0).getContent().get(0));

        assertEquals(direction, actualCheckPolicy.getRequest().getAction().getAttribute().get(0)
            .getAttributeValue().get(0).getContent().get(0));
        SubjectType subjectType = actualCheckPolicy.getRequest().getSubject().get(0);
        AttributeType attributeType = subjectType.getAttribute().get(1);
        assertEquals("http://www.hhs.gov/healthit/nhin#HomeCommunityId", attributeType.getAttributeId());
        assertEquals("http://www.w3.org/2001/XMLSchema#anyURI", attributeType.getDataType());
        assertEquals(TEST_HC_VAL, attributeType.getAttributeValue().get(0).getContent().get(0));
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
    public void testTransformAdhocQueryToCheckPolicy_DefaultOutBound() {
    	
        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
    
       
        eventA.setDirection("outbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        assertNotNull(result);
    }

    @Test
    public void testTransformAdhocQueryToCheckPolicy_DefaultInBound() {
    	
        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
    
       
        eventA.setDirection("inbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        assertNotNull(result);
    }

    @Test
    public void testTransformAdhocQueryResponseToCheckPolicy_DefaultOutBound() {
    	
    	AdhocQueryResultEventType eventB = new AdhocQueryResultEventType();
     
        eventB.setDirection("outbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryResponseToCheckPolicy(eventB);
        assertNotNull(result);
    }
    
    @Test
    public void testTransformAdhocQueryResponseToCheckPolicy_DefaultInBound() {
    	
    	AdhocQueryResultEventType eventB = new AdhocQueryResultEventType();
     
        eventB.setDirection("inbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryResponseToCheckPolicy(eventB);
        assertNotNull(result);
    }

    
}