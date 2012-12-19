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
public class PDSpecVersionRegistryTest {

    @Test
    public void testGetSupportedSpecs_g0_PD() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g0;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_1_0));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
    }

    @Test
    public void testGetSupportedSpecs_g1_PD() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g1;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_1_0));
    }
    
    @Test
    public void testGetSupportedSpecsPDDeferredRequestService_g0() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g0;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY_DEFERRED_REQUEST);
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_1_0));
    }
    
    @Test
    public void testGetSupportedSpecsPDDeferredResponseService_g0() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g0;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY_DEFERRED_RESPONSE);
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_1_0));
    }
    
    @Test
    public void testGetSupportedSpecsPDDeferredRequestService_g1() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g1;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY_DEFERRED_REQUEST);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_1_0));
    }
    
    @Test
    public void testGetSupportedSpecsPDDeferredResponseService_g1() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g1;
        ArrayList<UDDI_SPEC_VERSION> list = UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY_DEFERRED_RESPONSE);
        assertTrue(list.contains(UDDI_SPEC_VERSION.SPEC_2_0));
        assertTrue(!list.contains(UDDI_SPEC_VERSION.SPEC_1_0));
    }
}
