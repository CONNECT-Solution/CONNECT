package gov.hhs.fha.nhinc.admingui.event.service;

import gov.hhs.fha.nhinc.admingui.event.model.EventNwhinOrganization;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.event.dao.DatabaseEventLoggerDao;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 *
 * @author jasonasmith
 */
public class EventCountServiceImplTest {
    
    private List daoInboundResults;
    private List daoOutboundResults;
    
    private EventCountService countService;
    
    private final ConnectionManager mockCM = mock(ConnectionManager.class);
    private final DatabaseEventLoggerDao mockDao = mock(DatabaseEventLoggerDao.class);
    
    private static final String ORG_1 = "Gateway 1";
    private static final String ORG_2 = "Gateway 2";
    
    @Before
    public void setUp() {
        
        countService = new EventCountServiceImpl(){
          
            @Override
            protected ConnectionManager getConnectionManager(){
                return mockCM;
            }
            
            @Override
            protected DatabaseEventLoggerDao getEventLoggerDao(){
                return mockDao;
            }
        };
        
        daoInboundResults = new ArrayList();
        daoInboundResults.add(new Object[]{ 2, "1.1", "Patient Discovery" });
        daoInboundResults.add(new Object[]{ 2, "1.1", "Document Query" });
        
        daoOutboundResults = new ArrayList();
        daoOutboundResults.add(new Object[]{ 2, "1.1", "Patient Discovery" });
        daoOutboundResults.add(new Object[]{ 2, "2.2", "Retrieve Document" });
        
    }
    
    private void setMockCounts() throws ConnectionManagerException {
        
        when(mockDao.getCounts(EventCountServiceImpl.INBOUND_EVENT_TYPE, EventCountServiceImpl.INBOUND_HCID_TYPE)).thenReturn(daoInboundResults);
        when(mockDao.getCounts(EventCountServiceImpl.OUTBOUND_EVENT_TYPE, EventCountServiceImpl.OUTBOUND_HCID_TYPE)).thenReturn(daoOutboundResults);
    
        when(mockCM.getBusinessEntityName("1.1")).thenReturn(ORG_1);
        when(mockCM.getBusinessEntityName("2.2")).thenReturn(ORG_2);
        
        countService.setCounts();
        
        verify(mockDao, times(2)).getCounts(anyString(), anyString());
        verify(mockCM, times(4)).getBusinessEntityName(anyString());
    }
    
    /**
     * Test of getTotalOrganizations method, of class EventCountServiceImpl.
     */
    @Test
    public void testGetTotalOrganizations() throws ConnectionManagerException {
        setMockCounts();
        
        List<EventNwhinOrganization> totalOrgs = countService.getTotalOrganizations();
        
        assertNotNull(totalOrgs);
        assertEquals(totalOrgs.size(), 2);
        
        EventNwhinOrganization org1 = totalOrgs.get(0);
        EventNwhinOrganization org2 = totalOrgs.get(1);
        
        assertTrue(!org1.getOrganizationName().equals(org2.getOrganizationName()));
        assertTrue(org1.getOrganizationName().equals(ORG_1) || org1.getOrganizationName().equals(ORG_2));
        assertTrue(org2.getOrganizationName().equals(ORG_1) || org2.getOrganizationName().equals(ORG_2));
        
        if(org1.getOrganizationName().equals(ORG_1)){
            assertTotals(org1, org2);
        }else {
            assertTotals(org2, org1);
        }
        
    }
    
    private void assertTotals(EventNwhinOrganization org1, EventNwhinOrganization org2){
        assertEquals(org1.getPdCount(), 4);
        assertEquals(org1.getDqCount(), 2);
        assertEquals(org1.getTotalCount(), 6);
        
        assertEquals(org2.getDrCount(), 2);
        assertEquals(org2.getTotalCount(), 2);
    }

    /**
     * Test of getInboundOrganizations method, of class EventCountServiceImpl.
     */
    @Test
    public void testGetInboundOrganizations() throws ConnectionManagerException {
        setMockCounts();
        
        List<EventNwhinOrganization> inboundOrgs = countService.getInboundOrganizations();
        
        assertNotNull(inboundOrgs);
        assertEquals(inboundOrgs.size(), 1);
        
        assertEquals(inboundOrgs.get(0).getPdCount(), 2);
        assertEquals(inboundOrgs.get(0).getDqCount(), 2);
        assertEquals(inboundOrgs.get(0).getTotalCount(), 4);
    }

    /**
     * Test of getOutboundOrganizations method, of class EventCountServiceImpl.
     */
    @Test
    public void testGetOutboundOrganizations() throws ConnectionManagerException {
        setMockCounts();
        
        List<EventNwhinOrganization> outboundOrgs = countService.getOutboundOrganizations();
        
        assertNotNull(outboundOrgs);
        assertEquals(outboundOrgs.size(), 2);
        
        EventNwhinOrganization org1 = outboundOrgs.get(0);
        EventNwhinOrganization org2 = outboundOrgs.get(1);
        
        assertTrue(!org1.getOrganizationName().equals(org2.getOrganizationName()));
        assertTrue(org1.getOrganizationName().equals(ORG_1) || org1.getOrganizationName().equals(ORG_2));
        assertTrue(org2.getOrganizationName().equals(ORG_1) || org2.getOrganizationName().equals(ORG_2));
        
        if(org1.getOrganizationName().equals(ORG_1)){
            assertEquals(org1.getPdCount(), 2);
            assertEquals(org2.getDrCount(), 2);
        }else {
            assertEquals(org1.getDrCount(), 2);
            assertEquals(org2.getPdCount(), 2);
        }
        
        assertEquals(org1.getTotalCount(), 2);
        assertEquals(org2.getTotalCount(), 2);
    }
    
}
