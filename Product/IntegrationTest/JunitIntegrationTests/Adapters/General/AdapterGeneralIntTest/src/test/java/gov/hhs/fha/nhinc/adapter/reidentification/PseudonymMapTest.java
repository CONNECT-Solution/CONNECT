/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.reidentification;

import gov.hhs.fha.nhinc.adapter.reidentification.data.PseudonymMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tester to validate the expected functionality of the Pseudonym management
 */
public class PseudonymMapTest {  
    private static Log log = LogFactory.getLog(PseudonymMapTest.class);
    private static final String pseudoPatientId_1 = "111";
    private static final String pseudoAuthorityId_1 = "Auth.A.B.C";
    private static final String realPatientId_1 = "999";
    private static final String realAuthorityId_1 = "Auth.X.Y.Z";
    
    private static final String pseudoPatientId_2 = "222";
    private static final String pseudoAuthorityId_2 = "Auth.A.B.C";
    private static final String realPatientId_2 = "999";
    private static final String realAuthorityId_2 = "Auth.X.Y.Z";
    
    private static final String pseudoPatientId_3 = "333";
    private static final String pseudoAuthorityId_3 = "Auth.A.B.C";
    private static final String realPatientId_3 = "888";
    private static final String realAuthorityId_3 = "Auth.X.Y.Z";
    
    private PseudonymMap pseudonymMap_1;
    private PseudonymMap pseudonymMap_2;
    private PseudonymMap pseudonymMap_3;

    public PseudonymMapTest() {
    }

    @Before
    public void setUp() {
        //create first test PseudonymMap 
        pseudonymMap_1 = new PseudonymMap();
        pseudonymMap_1.setPseudonymPatientId(pseudoPatientId_1);
        pseudonymMap_1.setPseudonymPatientIdAssigningAuthority(pseudoAuthorityId_1);
        pseudonymMap_1.setRealPatientId(realPatientId_1);
        pseudonymMap_1.setRealPatientIdAssigningAuthority(realAuthorityId_1);
        
        //create second test PseudonymMap - only difference is pseudoPatientId
        pseudonymMap_2 = new PseudonymMap();
        pseudonymMap_2.setPseudonymPatientId(pseudoPatientId_2);
        pseudonymMap_2.setPseudonymPatientIdAssigningAuthority(pseudoAuthorityId_2);
        pseudonymMap_2.setRealPatientId(realPatientId_2);
        pseudonymMap_2.setRealPatientIdAssigningAuthority(realAuthorityId_2);
        
        //create third test PseudonymMap - Ids are different
        pseudonymMap_3 = new PseudonymMap();
        pseudonymMap_3.setPseudonymPatientId(pseudoPatientId_3);
        pseudonymMap_3.setPseudonymPatientIdAssigningAuthority(pseudoAuthorityId_3);
        pseudonymMap_3.setRealPatientId(realPatientId_3);
        pseudonymMap_3.setRealPatientIdAssigningAuthority(realAuthorityId_3);
        
        //initialize contents of internal memory
        PseudonymMapManager.readMap();
    }

    @After
    public void tearDown() {
        //reset external memory
        PseudonymMapManager.writeMap();
    }

    /**
     * Test the creation of a Pseudonym Map XML file and the extraction of a stored map
     */
    @Test
    public void testStoreRetrieve() {
        log.debug("Entering PseudonymMapTest.testStoreRetrieve");
        // pseudonymMap should not previously exist
        assertNull(PseudonymMapManager.addPseudonymMap(pseudonymMap_1));
        PseudonymMapManager.writeMap();
        PseudonymMapManager.readMap();
        PseudonymMap searchMap = PseudonymMapManager.findPseudonymMap(pseudoAuthorityId_1, pseudoPatientId_1);

        // pseudonymMap should be located
        assertNotNull(searchMap);

        // and should be the same as what we created previously
        assertEquals(pseudonymMap_1.toString(), searchMap.toString());
        log.debug("Exiting PseudonymMapTest.testStoreRetrieve");
    }
    
    /**
     * Test the replacement of data given the same id
     */
    @Test
    public void testIdReplacement() {
        log.debug("Entering PseudonymMapTest.testIdReplacement");
        // pseudonymMap for this Id should previously exist and return the old one
        PseudonymMap prevMap = PseudonymMapManager.addPseudonymMap(pseudonymMap_2);
        assertNotNull(prevMap);
        // and should be the same as what we created previously
        assertEquals(pseudonymMap_1.toString(), prevMap.toString());
        
        // pseudonymMap for this Id should not previously exist 
        PseudonymMap newMap = PseudonymMapManager.addPseudonymMap(pseudonymMap_3);
        assertNull(newMap);
        
        PseudonymMapManager.writeMap();
        PseudonymMapManager.readMap();
        
        PseudonymMap searchMap = PseudonymMapManager.findPseudonymMap(pseudoAuthorityId_2, pseudoPatientId_2);
        // pseudonymMap should be located
        assertNotNull(searchMap);
        // and should be the same as what we created previously as 2
        assertEquals(pseudonymMap_2.toString(), searchMap.toString());
        
        searchMap = PseudonymMapManager.findPseudonymMap(pseudoAuthorityId_3, pseudoPatientId_3);
        // pseudonymMap should be located
        assertNotNull(searchMap);
        // and should be the same as what we created previously as 3
        assertEquals(pseudonymMap_3.toString(), searchMap.toString());
        
        log.debug("Exiting PseudonymMapTest.testIdReplacement");
    }
    
    /**
     * Test the removal of maps
     */
    @Test
    public void testRemoval() {
        log.debug("Entering PseudonymMapTest.testRemoval");
        // pseudonymMap_2 should exist and can be removed
        assertTrue(PseudonymMapManager.removePseudonymMap(pseudonymMap_2));
        // pseudonymMap_3 should exist and can be removed
        assertTrue(PseudonymMapManager.removePseudonymMap(pseudonymMap_3));
        
        PseudonymMapManager.writeMap();
        PseudonymMapManager.readMap();
        
        // neither 1, 2, or 3 should exist now
        assertFalse(PseudonymMapManager.removePseudonymMap(pseudonymMap_1));
        assertFalse(PseudonymMapManager.removePseudonymMap(pseudonymMap_2));
        assertFalse(PseudonymMapManager.removePseudonymMap(pseudonymMap_3));
        log.debug("Exiting PseudonymMapTest.testRemoval");
    }
}
