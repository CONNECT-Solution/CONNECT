/**
 * 
 */
package gov.hhs.fha.nhinc.transform.audit;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import junit.framework.Assert;

import org.hl7.v3.MCCIIN000002UV01;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author msw
 * 
 */
public class PatientDiscoveryAckTransformsHCIDTest extends PatientDiscoveryTransformsBase {

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.MCCIIN000002UV01, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdMCCIIN000002UV01AdapterInbound() {
        // Setup Inputs
        MCCIIN000002UV01 message = Mockito.mock(MCCIIN000002UV01.class);
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface);
        
        Assert.assertEquals(expected, result);
    }

}
