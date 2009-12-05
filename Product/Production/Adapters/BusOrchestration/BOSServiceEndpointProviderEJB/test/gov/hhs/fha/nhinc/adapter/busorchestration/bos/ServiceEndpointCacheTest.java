/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.xml.schema.endpoint.EPR;

/**
 *
 * @author Jerry Goodnough
 */
public class ServiceEndpointCacheTest {

    public ServiceEndpointCacheTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of loadServiceEndPoints method, of class ServiceEndpointCache.
     */
    @Test
    public void testLoadServiceEndPoints() throws Exception
    {
        System.out.println("loadServiceEndPoints");
        // Simple test for exception
        ServiceEndpointCache.loadServiceEndPoints();
    }

    /**
     * Test of forceCacheRefresh method, of class ServiceEndpointCache.
     */
    @Test
    public void testForceCacheRefresh() throws Exception
    {
        System.out.println("forceCacheRefresh");
        // Simple test for exception
        ServiceEndpointCache.forceCacheRefresh();
    }

    /**
     * Test of findEndpointByServiceName method, of class ServiceEndpointCache.
     */
    @Test
    public void testFindEndpointByServiceName() throws Exception
    {
        System.out.println("findEndpointByServiceName");
        String name = ServiceNameConstants.DOCUMENT_ASSEMBLY;
        EPR result = ServiceEndpointCache.findEndpointByServiceName(name);
        assertNotNull(result);
        System.out.println("findEndpointByServiceName - Found "+result.getEndpointReference().getAddress().getValue());
        name = "A:Nonexistent:Name";
        System.out.println("findEndpointByServiceName - Check non existent name case");
        result = ServiceEndpointCache.findEndpointByServiceName(name);
        assertNull(result);

    }

    /**
     * Test of findEndpointByServiceURL method, of class ServiceEndpointCache.
     */
    @Test
    public void testFindEndpointByServiceURL() throws Exception
    {
        System.out.println("findEndpointByServiceURL");
        //Test based on Example Service Mappings File
        String url = "http://localhost:8080/ExampleQuery/ExampleQuery?WSDL";
        EPR result = ServiceEndpointCache.findEndpointByServiceURL(url);
        assertNotNull(result);
        System.out.println("findEndpointByServiceURL - Found "+result.getEndpointReference().getAddress().getValue());
        System.out.println("findEndpointByServiceURL - Check non existent URL");
        url = "http://unknownhost:8080/ExampleQuery/ExampleQuery?WSDL";
        result = ServiceEndpointCache.findEndpointByServiceURL(url);
        assertNull(result);

    }

}