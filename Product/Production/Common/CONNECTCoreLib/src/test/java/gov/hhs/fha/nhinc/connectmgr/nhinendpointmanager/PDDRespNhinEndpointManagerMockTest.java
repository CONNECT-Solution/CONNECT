/**
 * 
 */
package gov.hhs.fha.nhinc.connectmgr.nhinendpointmanager;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;

/**
 * @author mweaver
 *
 */
public class PDDRespNhinEndpointManagerMockTest extends AbstractNhinEndpointManagerMockTest {

    /*-----------------Test Methods---------------*/
    
    /**
     * Test target only supporting 2010
     */
    @Override
    @Test
    public void testTargetIs10() {
        set2010Expectations();
        
        GATEWAY_API_LEVEL level = mockNhinEndpointManager.getApiVersion(HCID, getService());
        assertTrue(level == GATEWAY_API_LEVEL.LEVEL_g1);
        context.assertIsSatisfied();
    }
    
    /*-----------------Setup Methods---------------*/
    
    @Override
    protected NHIN_SERVICE_NAMES getService() {
        return NHIN_SERVICE_NAMES.PATIENT_DISCOVERY_DEFERRED_RESPONSE;
    }
    
    /**
     * Setup expectations for 1.0 specs
     */
    @Override
    protected void set2010Expectations() {
        expectNoConnectionManagerCache10();        
    }


    /**
     * Setup expectations for 2.0 specs
     */
    @Override
    protected void set2011Expectations() {
        expectConnectionManagerCache20();
    }

    /**
     * Setup for both PDDResp specs
     */
    @Override
    protected void expectConnectionManagerCacheBoth() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<UDDI_SPEC_VERSION>();
        list.add(UDDI_SPEC_VERSION.SPEC_1_0);
        list.add(UDDI_SPEC_VERSION.SPEC_2_0);
        
        expectConnectionManagerCache(list);
    }


}
