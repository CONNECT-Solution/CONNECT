/**
 * 
 */
package gov.hhs.fha.nhinc.transform.audit;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author msw
 * 
 */
public class PatientDiscoveryResponseTransformsHCIDTest extends PatientDiscoveryTransformsBase {

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02AdapterInbound() {
        // Setup Inputs
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02AdapterOutbound() {
        // Setup Inputs
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02EntityInbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02EntityOutbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = NON_TRUSTWORTHY_VALUE;
        SENDER_VALUE = NON_TRUSTWORTHY_VALUE;

        // expected result
        String expected = LOCAL_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02NhinInbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = LOCAL_HCID;
        SENDER_VALUE = REMOTE_HCID;

        // expected result
        String expected = REMOTE_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for
     * {@link gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms#getPatientDiscoveryMessageCommunityId(org.hl7.v3.PRPAIN201305UV02, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testGetPatientDiscoveryMessageCommunityIdPRPAIN201305UV02NhinOutbound() {
        PRPAIN201306UV02 message = Mockito.mock(PRPAIN201306UV02.class);
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        String type = StringUtils.EMPTY;

        // set up test params
        RECEIVER_VALUE = REMOTE_HCID;
        SENDER_VALUE = LOCAL_HCID;

        // expected result
        String expected = REMOTE_HCID;

        PatientDiscoveryTransforms instance = getPatientDiscoveryTransforms();
        String result = instance.getPatientDiscoveryMessageCommunityId(message, direction, _interface, type);
        Assert.assertEquals(expected, result);
    }

}
