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
public class CMBusinessServiceTest {

    public CMBusinessServiceTest() {
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
     * Test of get and set for CMBindingTemplates, of class CMBusinessService.
     */
    @Test
    public void testSetGetBindingTemplates() {

        CMBusinessService instance = new CMBusinessService();
        CMBindingTemplates testBindingTemplates = generateTestCMBindingTemplates();
        instance.setBindingTemplates(testBindingTemplates);

        CMBindingTemplates result = instance.getBindingTemplates();
        assertTrue(testBindingTemplates.equals(result));
    }

    /**
     * Test of get and set for Descriptions, of class CMBusinessService.
     */
    @Test
    public void testSetGetDescriptions() {

        CMBusinessService instance = new CMBusinessService();
        CMBindingDescriptions testBindingDescriptions = generateTestCMBindingDescriptions();
        instance.setDescriptions(testBindingDescriptions);

        CMBindingDescriptions result = instance.getDescriptions();
        assertTrue(testBindingDescriptions.equals(result));
    }

    /**
     * Test of get and set for InternalWebService, of class CMBusinessService.
     */
    @Test
    public void testSetGetInternalWebService() {

        CMBusinessService instance = new CMBusinessService();
        instance.setInternalWebService(true);

        assertTrue(instance.isInternalWebService());
    }

    /**
     * Test of get and set for CMBindingNames, of class CMBusinessService.
     */
    @Test
    public void testSetGetNames() {

        CMBusinessService instance = new CMBusinessService();
        CMBindingNames testBindingNames = generateTestCMBindingNames();
        instance.setNames(testBindingNames);

        CMBindingNames result = instance.getNames();
        assertTrue(testBindingNames.equals(result));
    }

    /**
     * Test of get and set for UniformServiceName, of class CMBusinessService.
     */
    @Test
    public void testSetGetUniformServiceName() {

        CMBusinessService instance = new CMBusinessService();
        String testUniformServiceName = "testUniformServiceName";
        instance.setUniformServiceName(testUniformServiceName);

        String result = instance.getUniformServiceName();
        assertTrue(testUniformServiceName.equals(result));
    }

    /**
     * Test of get and set for ServiceKey, of class CMBusinessService.
     */
    @Test
    public void testSetGetServiceKey() {

        CMBusinessService instance = new CMBusinessService();
        String testServiceKey = "testServiceKey";
        instance.setServiceKey(testServiceKey);

        String result = instance.getServiceKey();
        assertTrue(testServiceKey.equals(result));
    }

    /**
     * Test of createCopy method, of class CMBusinessService.
     */
    @Test
    public void testCreateCopy() {

        CMBusinessService origCMBusinessService = generateTestCMBusinessService();
        CMBusinessService copyCMBusinessService = origCMBusinessService.createCopy();
        assertTrue(origCMBusinessService.equals(copyCMBusinessService));
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

    /**
     * Create a generic CMBindingTemplates filled with test data
     *
     * @return The test CMBindingTemplates
     */
    private CMBindingTemplates generateTestCMBindingTemplates() {
        CMBindingTemplates testBindingTemplates = new CMBindingTemplates();
        CMBindingTemplate testBindingTemplate = generateTestCMBindingTemplate();
        testBindingTemplates.getBindingTemplate().add(testBindingTemplate);
        return testBindingTemplates;
    }

    /**
     * Create a generic CMBindingDescriptions filled with test data
     *
     * @return The test CMBindingDescriptions
     */
    private CMBindingDescriptions generateTestCMBindingDescriptions() {
        CMBindingDescriptions testBindingDescriptions = new CMBindingDescriptions();
        testBindingDescriptions.getDescription().add("testBindingDescription");
        return testBindingDescriptions;
    }

    /**
     * Create a generic CMBindingNames filled with test data
     *
     * @return The test CMBindingNames
     */
    private CMBindingNames generateTestCMBindingNames() {
        CMBindingNames testBindingNames = new CMBindingNames();
        testBindingNames.getName().add("testBindingName");
        return testBindingNames;
    }

    /**
     * Create a generic CMBusiness Service filled with test data
     *
     * @return The test CMBusiness Service
     */
    private CMBusinessService generateTestCMBusinessService() {

        CMBusinessService testBusinessService = new CMBusinessService();

        CMBindingTemplates testBindingTemplates = generateTestCMBindingTemplates();
        testBusinessService.setBindingTemplates(testBindingTemplates);

        CMBindingDescriptions testBindingDescriptions = generateTestCMBindingDescriptions();
        testBusinessService.setDescriptions(testBindingDescriptions);

        testBusinessService.setInternalWebService(false);

        CMBindingNames testBindingNames = generateTestCMBindingNames();
        testBusinessService.setNames(testBindingNames);

        testBusinessService.setServiceKey("testServiceKey");

        testBusinessService.setUniformServiceName("testUniformServiceName");

        return testBusinessService;
    }
}
