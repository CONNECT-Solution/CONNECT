/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vvickers
 */
public class CmBindingTemplatesTest {

    public CmBindingTemplatesTest() {
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
     * Test of createCopy method, of class CMBindingTemplates.
     */
    @Test
    public void testCreateCopy() {

        CMBindingTemplates origCMBindingTemplates = generateTestCMBindingTemplates();
        CMBindingTemplates copyCMBindingTemplates = origCMBindingTemplates.createCopy();
        assertTrue(origCMBindingTemplates.equals(copyCMBindingTemplates));
    }

    /**
     * Create a generic CMBindingTemplates filled with test data
     *
     * @return The test CMBindingTemplates
     */
    private CMBindingTemplates generateTestCMBindingTemplates() {
        CMBindingTemplates testBindingTemplates = new CMBindingTemplates();
        CMBindingTemplate testBindingTemplate = new CMBindingTemplate();
        testBindingTemplate.setBindingKey("testBindingKey");
        testBindingTemplate.setEndpointURL("testEndpointURL");
        testBindingTemplate.setServiceVersion("testServiceVersion");
        testBindingTemplate.setWsdlURL("testWsdlURL");
        testBindingTemplates.getBindingTemplate().add(testBindingTemplate);
        return testBindingTemplates;
    }

}