package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
//import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class AdhocQueryTransformHelperTest {


	
    public AdhocQueryTransformHelperTest() {
    }

    @Before
    public void setUp() {
    }
    
    @Test
    public void testTransformAdhocQueryToCheckPolicy() {
    	AdhocQueryTransformHelper instance = new AdhocQueryTransformHelper();
        AdhocQueryRequestEventType eventA = new AdhocQueryRequestEventType();
     //   eventA.getDirection()= "inbound";
        eventA.setDirection("inbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventA);
        assertNotNull(result);
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