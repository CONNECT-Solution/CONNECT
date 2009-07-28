/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.policyengine.adapterpip;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author svalluripalli
 */
public class PatientConsentManagerTest {

    public PatientConsentManagerTest() {
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

    @Test
    public void testCheckCPPMetaFromRepositoryUsingXDSb()
            throws AdapterPIPException
    {
        System.out.println("Begin testCheckCPPMetaFromRepositoryUsingXDSb");
        PatientConsentManager oPatientConsentManager = new PatientConsentManager();
        assertNotNull(oPatientConsentManager.checkCPPMetaFromRepositoryUsingXDSb("123406", "1.1"));
        System.out.println("End testCheckCPPMetaFromRepositoryUsingXDSb");
    }

}
