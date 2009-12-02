package gov.hhs.fha.nhinc.hibernate;

import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.logging.*;
import java.util.ArrayList;
import org.junit.Ignore;

/**
 *
 * @author MFLYNN02
 */
@Ignore // TODO: Move to an integration test
public class AuditRepositoryDAOTest {
    private AuditRepositoryDAO auditDao =null;
    private static Log log = LogFactory.getLog(AuditRepositoryDAOTest.class);
    public AuditRepositoryDAOTest() {
        auditDao = AuditRepositoryDAO.getAuditRepositoryDAOInstance();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAuditRepositoryDAOInstance method, of class AuditRepositoryDAO.
     */
    @Test
    public void testGetAuditRepositoryDAOInstance() 
	{
        AuditRepositoryDAO result = AuditRepositoryDAO.getAuditRepositoryDAOInstance();
    }

    /**
     * Test of queryAuditRepository method, of class AuditRepositoryDAO.
     */
    @Test
    public void testQueryAuditRepository() 
	{
        log.debug("Begin - testQueryAuditRepository");
        
        String query = "select * from auditRepository";
        List result = auditDao.queryAuditRepository(query);
        assertNotNull(result);
        log.debug("getAuditRepository number of records : " + result.size());
        
        log.debug("End - testQueryAuditRepository");
    }

    /**
     * Test of insertAuditRepository method, of class AuditRepositoryDAO.
     */
    @Test
    public void testInsertAuditRepository() 
	{
        log.debug("Begin - testInsertAuditRepository");
        List<AuditRepositoryRecord> eventLogList = new ArrayList();
        Date now = new Date();
        
        AuditRepositoryRecord record = new AuditRepositoryRecord();
        record.setMessageType("Record 1 - Message Type");
        record.setTimeStamp(now);
        record.setUserId("UnitTest1");
        eventLogList.add(record);
        
        boolean expResult = true;
        boolean result = auditDao.insertAuditRepository(eventLogList);
        assertEquals(expResult, result);
        log.debug ("End - testInsertAuditRepository");
    }

    /**
     * Test of queryAuditRepositoryOnCriteria method, of class AuditRepositoryDAO.
     */
    @Test
    public void testQueryAuditRepositoryOnCriteria() 
	{
        log.debug("Begin - testQueryAuditRepositoryOnCriteria");
        String eUserId = "UnitTest1";
        String ePatientId = "";
        String eCommunityId = "";
        Date startDate = null;
        Date endDate = null;
        List result = auditDao.queryAuditRepositoryOnCriteria(eUserId, ePatientId,  startDate, endDate);
        assertNotNull(result);
        log.debug("Number of records returned : " + result.size());
        log.debug("End - testQueryAuditRepositoryOnCriteria");
    }

}