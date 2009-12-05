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
public class ServiceEndpointProviderImplTest {

    public ServiceEndpointProviderImplTest() {
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
     * Test of getInstance method, of class ServiceEndpointProviderImpl.
     */
    @Test
    public void testGetInstance()
    {
        System.out.println("getInstance");
        ServiceEndpointProviderImpl result =
                                    ServiceEndpointProviderImpl.getInstance();
        assertNotNull(result);
    }

    /**
     * Test of bosFindEndpoint method, of class ServiceEndpointProviderImpl.
     */
    @Test
    public void testFindEndpoint()
    {
        System.out.println("bosFindEndpoint");

        System.out.println("bosFindEndpoint - Try Endpoint by good service name");

        CreateEPRRequest bosFindEndPointRequest = new CreateEPRRequest();
        bosFindEndPointRequest.setServiceName(ServiceNameConstants.DOCUMENT_ASSEMBLY);


        ServiceEndpointProviderImpl instance = new ServiceEndpointProviderImpl();
        CreateEPRResponse result =
                          instance.findEndpoint(bosFindEndPointRequest);

        assertNotNull(result);
        assertNotNull(result.getEPR());
        assertNotNull(result.getEPR().getEndpointReference());
        assertNotNull(result.getEPR().getEndpointReference().getAddress());
        assertNotNull(result.getEPR().getEndpointReference().getAddress().getValue());
        assertFalse(result.getEPR().getEndpointReference().getAddress().getValue().isEmpty());

        System.out.println("bosFindEndpoint - Try bad service name");
        bosFindEndPointRequest.setServiceName("A:Nonexistent:Name");
        result = instance.findEndpoint(bosFindEndPointRequest);
        assertNotNull(result);
        assertNull(result.getEPR());

        System.out.println("bosFindEndpoint - Try by URL");
        bosFindEndPointRequest = new CreateEPRRequest();

        bosFindEndPointRequest.setEndpointURL("http://localhost:8080/ExampleQuery/ExampleQuery?WSDL");
        result = instance.findEndpoint(bosFindEndPointRequest);
        assertNotNull(result);
        assertNotNull(result.getEPR());
        assertNotNull(result.getEPR().getEndpointReference());
        assertNotNull(result.getEPR().getEndpointReference().getAddress());
        assertNotNull(result.getEPR().getEndpointReference().getAddress().getValue());
        assertFalse(result.getEPR().getEndpointReference().getAddress().getValue().isEmpty());

        System.out.println("bosFindEndpoint - Try bad URL, Check that it reflects");
        String missingURL="http://unknownhost:8080/ExampleQuery/ExampleQuery?WSDL";
        bosFindEndPointRequest.setEndpointURL(missingURL);
        result = instance.findEndpoint(bosFindEndPointRequest);
        assertNotNull(result.getEPR());
        assertNotNull(result.getEPR().getEndpointReference());
        assertNotNull(result.getEPR().getEndpointReference().getAddress());
        assertNotNull(result.getEPR().getEndpointReference().getAddress().getValue());
        assertFalse(result.getEPR().getEndpointReference().getAddress().getValue().isEmpty());
        //When the URL is not found the ServieEndpoint provider should reflect it.
        assertTrue(result.getEPR().getEndpointReference().getAddress().getValue().compareTo(missingURL)==0);

        System.out.println("bosFindEndpoint - Try missing service, but good URL");
        bosFindEndPointRequest.setEndpointURL("http://localhost:8080/ExampleQuery/ExampleQuery?WSDL");
        bosFindEndPointRequest.setServiceName("A:Nonexistent:Name");
        result = instance.findEndpoint(bosFindEndPointRequest);
        assertNotNull(result);
        assertNotNull(result.getEPR());
        assertNotNull(result.getEPR().getEndpointReference());
        assertNotNull(result.getEPR().getEndpointReference().getAddress());
        assertNotNull(result.getEPR().getEndpointReference().getAddress().getValue());
        assertFalse(result.getEPR().getEndpointReference().getAddress().getValue().isEmpty());


    }


}