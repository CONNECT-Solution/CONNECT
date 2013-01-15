/**
 * 
 */
package gov.hhs.fha.nhinc.connectmgr.nhinendpointmanager;

import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.NhinEndpointManager;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author mweaver
 *
 */
@Ignore
public abstract class AbstractNhinEndpointManagerMockTest {
    
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    protected final ConnectionManagerCache mockConnectionManagerCache = context.mock(ConnectionManagerCache.class);
    protected final NhinEndpointManager mockNhinEndpointManager = createMockNhinEndpointManager();
    
    protected final String HCID = "1.1";
    
    /**
     * @return Overridden MockPurposeOfForDecider
     */
    protected NhinEndpointManager createMockNhinEndpointManager() {
        return new NhinEndpointManager() {
            
            @Override
            protected ConnectionManagerCache getConnectionManagerCache() {
                return mockConnectionManagerCache;
            }
        };
    }
    
    /*-----------------Test Methods---------------*/
    
    /**
     * Test target only supporting 2010
     */
    @Test
    public void testTargetIs10() {
        set2010Expectations();
        
        GATEWAY_API_LEVEL level = mockNhinEndpointManager.getApiVersion(HCID, getService());
        assertTrue(level == GATEWAY_API_LEVEL.LEVEL_g0);
        context.assertIsSatisfied();
    }
    
    /**
     * Test target only supporting 2011
     */
    @Test
    public void testTargetIs20() {
        set2011Expectations();
        
        GATEWAY_API_LEVEL level = mockNhinEndpointManager.getApiVersion(HCID, getService());
        assertTrue(level == GATEWAY_API_LEVEL.LEVEL_g1);
        context.assertIsSatisfied();
    }

    /**
     * Test target supporting 2010 and 2011
     */
    @Test
    public void testTargetIsBoth() {
        expectConnectionManagerCacheBoth();
        
        GATEWAY_API_LEVEL level = mockNhinEndpointManager.getApiVersion(HCID, getService());
        assertTrue(level == GATEWAY_API_LEVEL.LEVEL_g1);
        context.assertIsSatisfied();
    }    

    /*-----------------Expectation Methods---------------*/

    /**
     * Setup for no 1.0 specs (PDDeferred)
     */
    protected void expectNoConnectionManagerCache10() {
        expectConnectionManagerCache(null);
    }
    
    /**
     * Setup for 1.0 specs
     */
    protected void expectConnectionManagerCache10() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<UDDI_SPEC_VERSION>();
        list.add(UDDI_SPEC_VERSION.SPEC_1_0);
        
        expectConnectionManagerCache(list);
    }
    
    /**
     * Setup for 1.1 specs
     */
    protected void expectConnectionManagerCache11() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<UDDI_SPEC_VERSION>();
        list.add(UDDI_SPEC_VERSION.SPEC_1_1);
        
        expectConnectionManagerCache(list);
    }
    
    /**
     * Setup for 2.0 specs
     */
    protected void expectConnectionManagerCache20() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<UDDI_SPEC_VERSION>();
        list.add(UDDI_SPEC_VERSION.SPEC_2_0);
        
        expectConnectionManagerCache(list);
    }
    
    /**
     * Setup for 3.0 specs
     */
    protected void expectConnectionManagerCache30() {
        List<UDDI_SPEC_VERSION> list = new ArrayList<UDDI_SPEC_VERSION>();
        list.add(UDDI_SPEC_VERSION.SPEC_3_0);
        
        expectConnectionManagerCache(list);  
    }
    
    /**
     * Expectations for ConnectionManagerCache().
     */
    protected void expectConnectionManagerCache(final List<UDDI_SPEC_VERSION> list) {
        context.checking(new Expectations() {
            {
                exactly(1).of(mockConnectionManagerCache).getSpecVersions(with(any(String.class)), with(any(NHIN_SERVICE_NAMES.class)));
                will(returnValue(list));
            }
        });
    }
    
    /*-----------------Setup Methods---------------*/

    /**
     * @return NHIN_SERVICE_NAMES service name of the service under test
     */
    protected abstract NHIN_SERVICE_NAMES getService();
    
    protected abstract void set2010Expectations();
    
    protected abstract void set2011Expectations();

    protected abstract void expectConnectionManagerCacheBoth();
}
