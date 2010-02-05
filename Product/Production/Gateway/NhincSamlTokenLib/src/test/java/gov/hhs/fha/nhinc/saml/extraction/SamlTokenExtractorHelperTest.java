/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.saml.extraction;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class SamlTokenExtractorHelperTest {
    private static Log log = LogFactory.getLog(SamlTokenExtractorHelperTest.class);

    public SamlTokenExtractorHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ConnectionManagerCache.overrideFileLocations("uddiConnectionInfo.xml", "internalConnectionInfo.xml");
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
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetSDEndpointURL() {
        log.debug("testGetSDEndpointURL");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = SamlTokenExtractorHelper.INTERNAL_SUBJECT_DISCOVERY;
        String expResult = "http://localhost:9080/NhinConnect/NhincSubjectDiscovery";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetDQEndpointURL() {
        log.debug("testGetDQEndpointURL");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = SamlTokenExtractorHelper.INTERNAL_DOC_QUERY;
        String expResult = "http://localhost:9080/NhinConnect/NhincDocumentQuery";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetDREndpointURL() {
        log.debug("testGetDREndpointURL");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = SamlTokenExtractorHelper.INTERNAL_DOC_RETRIEVE;
        String expResult = "http://localhost:9080/NhinConnect/NhincDocumentRetrieve";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertEquals(expResult, result);  
    }
    
    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetAQEndpointURL() {
        log.debug("testGetAQEndpointURL");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = SamlTokenExtractorHelper.INTERNAL_AUDIT_QUERY;
        String expResult = "http://localhost:9080/NhinConnect/NhincAuditQuery";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertEquals(expResult, result);      
    }
    
    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetHIEMSubscriptionEndpointURL() {
        log.debug("testGetHIEMSubscriptionEndpointURL");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = SamlTokenExtractorHelper.INTERNAL_HIEM_SUBSCRIBE;
        String expResult = "http://localhost:9080/NhinConnect/NhincSubscription";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertEquals(expResult, result);      
    }
    
    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetHIEMUnsubscribeEndpointURL() {
        log.debug("testGetHIEMUnsubscribeEndpointURL");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = SamlTokenExtractorHelper.INTERNAL_HIEM_UNSUBSCRIBE;
        String expResult = "http://localhost:9080/NhinConnect/NhincUnsubscription";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertEquals(expResult, result);      
    }
    
    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetHIEMNotifyEndpointURL() {
        log.debug("testGetHIEMNotifyEndpointURL");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = SamlTokenExtractorHelper.INTERNAL_HIEM_NOTIFY;
        String expResult = "http://localhost:9080/NhinConnect/NhincNotify";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertEquals(expResult, result);      
    }
    
    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetEndpointURLInvalidService() {
        log.debug("testGetEndpointURLInvalidService");
        
        String homeCommunityId = "2.16.840.1.113883.3.200";
        String service = "Invalid";
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertNull(result);      
    }

    /**
     * Test of GetEndpointURL method, of class SamlTokenExtractorHelper.
     */
    @Test
    public void testGetEndpointURLInvalidHcid() {
        log.debug("testGetEndpointURLInvalidHcid");
        
        String homeCommunityId = "2.16.840.1.113883.3.198";
        String service = SamlTokenExtractorHelper.INTERNAL_HIEM_NOTIFY;
        String result = SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, service);
        assertNull(result);      
    }
}