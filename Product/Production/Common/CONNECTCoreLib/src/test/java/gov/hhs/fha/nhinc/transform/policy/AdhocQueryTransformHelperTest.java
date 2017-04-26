package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    public void testTransformAdhocQueryToCheckPolicy_OutBound() {
    	AdhocQueryTransformHelper instance = new AdhocQueryTransformHelper();
        AdhocQueryRequestEventType eventB = new AdhocQueryRequestEventType();
     //   eventA.getDirection()= "inbound";
        eventB.setDirection("outbound");
        CheckPolicyRequestType result = instance.transformAdhocQueryToCheckPolicy(eventB);
        assertNotNull(result);
    }

    
}