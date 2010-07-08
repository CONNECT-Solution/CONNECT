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
public class CMBindingTemplateTest {

    public CMBindingTemplateTest() {
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
     * Test of get and set for BindingKey, of class CMBindingTemplate.
     */
    @Test
    public void testSetGetBindingKey() {

        CMBindingTemplate instance = new CMBindingTemplate();
        String testBindingKey = "testBindingKey";
        instance.setBindingKey(testBindingKey);

        String result = instance.getBindingKey();
        assertTrue(testBindingKey.equals(result));
    }

    /**
     * Test of get and set for EndpointURL, of class CMBindingTemplate.
     */
    @Test
    public void testSetGetEndpointURL() {

        CMBindingTemplate instance = new CMBindingTemplate();
        String testEndpointURL = "testEndpointURL";
        instance.setEndpointURL(testEndpointURL);

        String result = instance.getEndpointURL();
        assertTrue(testEndpointURL.equals(result));
    }

    /**
     * Test of get and set for WsdlURL, of class CMBindingTemplate.
     */
    @Test
    public void testSetGetWsdlURL() {

        CMBindingTemplate instance = new CMBindingTemplate();
        String testWsdlURL = "testWsdlURL";
        instance.setWsdlURL(testWsdlURL);

        String result = instance.getWsdlURL();
        assertTrue(testWsdlURL.equals(result));
    }

    /**
     * Test of get and set for ServiceVersion, of class CMBindingTemplate.
     */
    @Test
    public void testSetGetServiceVersion() {

        CMBindingTemplate instance = new CMBindingTemplate();
        String testServiceVersion = "testServiceVersion";
        instance.setServiceVersion(testServiceVersion);

        String result = instance.getServiceVersion();
        assertTrue(testServiceVersion.equals(result));
    }

    /**
     * Test of createCopy method, of class CMBindingTemplate.
     */
    @Test
    public void testCreateCopy() {

        CMBindingTemplate origCMBindingTemplate = generateTestCMBindingTemplate();
        CMBindingTemplate copyCMBindingTemplate = origCMBindingTemplate.createCopy();
        assertTrue(origCMBindingTemplate.equals(copyCMBindingTemplate));
    }

    /**
     * Create a generic CMBindingTemplate filled with test data
     *
     * @return The test CMBindingTemplate
     */
    private CMBindingTemplate generateTestCMBindingTemplate() {
        CMBindingTemplate testBindingTemplate = new CMBindingTemplate();
        testBindingTemplate.setBindingKey("testBindingKey");
        testBindingTemplate.setEndpointURL("testEndpointURL");
        testBindingTemplate.setServiceVersion("testServiceVersion");
        testBindingTemplate.setWsdlURL("testWsdlURL");
        return testBindingTemplate;
    }
}
