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
import org.netbeans.xml.schema.endpoint.CreateEPRRequest;
import org.netbeans.xml.schema.endpoint.CreateEPRResponse;

/**
 *
 * @author Jerry Goodnough
 */
public class ServiceEndpointProviderTest {

    public ServiceEndpointProviderTest() {
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
     * Test of bosFindEndpoint method, of class ServiceEndpointProvider.
     */
    @Test
    public void testBosFindEndpoint()
    {
        System.out.println("bosFindEndpoint");
        CreateEPRRequest bosFindEndPointRequest = null;
        ServiceEndpointProvider instance = new ServiceEndpointProvider();
        CreateEPRResponse expResult = null;
        CreateEPRResponse result =
                          instance.findEndpoint(bosFindEndPointRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}