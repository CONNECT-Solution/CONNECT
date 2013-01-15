/**
 * 
 */
package gov.hhs.fha.nhinc.connectmgr;

import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;

import java.util.ArrayList;

import org.junit.Test;

/**
 * @author mweaver
 *
 */
public class DSSpecVersionRegistryTest {
    
    @Test
    public void testGetSupportedSpecs_g0_DS() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g0;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_1_1));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
    }

    @Test
    public void testGetSupportedSpecs_g1_DS() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g1;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_1_1));
    }
   
    @Test
    public void testGetSupportedSpecsDSDeferredRequestService_g0() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g0;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_REQUEST);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_1_1));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
    }
    
    @Test
    public void testGetSupportedSpecsDSDeferredResponseService_g0() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g0;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_RESPONSE);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_1_1));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
    }
    
    @Test
    public void testGetSupportedSpecsDSDeferredRequestService_g1() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g1;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_REQUEST);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_1_1));
    }
    
    @Test
    public void testGetSupportedSpecsDSDeferredResponseService_g1() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g1;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_RESPONSE);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_1_1));
    }
}
