/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xdr._2007.AcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;


/**
 *
 * @author patlollav
 */
public class AdapterXDRRequestWebServiceProxyTest {

    public AdapterXDRRequestWebServiceProxyTest() {
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
     * Test of provideAndRegisterDocumentSetBRequest method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequest() {
        System.out.println("provideAndRegisterDocumentSetBRequest");
        
        Mockery mockery = new Mockery() {
            
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("SUCCESS");

        AdapterXDRRequestWebServiceProxy adapterXDRRequestWebServiceProxy = new AdapterXDRRequestWebServiceProxy(){

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String getAdapterXDRRequestSecuredUrl() {
                return "URL";
            }

            @Override
            protected AdapterXDRRequestSecuredPortType getAdapterXDRRequestSecuredPort(String url) {
                AdapterXDRRequestSecuredPortType mockPort = new AdapterXDRRequestSecuredPortType()
                {
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType arg0)
                    {
                        return ack;
                    }
                };
                return mockPort;

            }

            @Override
            protected void setRequestContext(AssertionType assertion, String adapterXDRRequestSecuredEndPointURL, AdapterXDRRequestSecuredPortType port) {
                
            }
        };


        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        AcknowledgementType result = adapterXDRRequestWebServiceProxy.provideAndRegisterDocumentSetBRequest(body, assertion);
        assertEquals("SUCCESS", result.getMessage());
    }

    /**
     * Test of provideAndRegisterDocumentSetBRequest method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBRequestFailureCase() {
        System.out.println("provideAndRegisterDocumentSetBRequest");

        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("SUCCESS");

        AdapterXDRRequestWebServiceProxy adapterXDRRequestWebServiceProxy = new AdapterXDRRequestWebServiceProxy(){

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String getAdapterXDRRequestSecuredUrl() {
                return null;
            }

            @Override
            protected AdapterXDRRequestSecuredPortType getAdapterXDRRequestSecuredPort(String url) {
                AdapterXDRRequestSecuredPortType mockPort = new AdapterXDRRequestSecuredPortType()
                {
                    public AcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType arg0)
                    {
                        return ack;
                    }
                };
                return mockPort;

            }

            @Override
            protected void setRequestContext(AssertionType assertion, String adapterXDRRequestSecuredEndPointURL, AdapterXDRRequestSecuredPortType port) {

            }
        };


        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
            }
        });

        ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        AcknowledgementType result = adapterXDRRequestWebServiceProxy.provideAndRegisterDocumentSetBRequest(body, assertion);
        assertEquals("ERROR: AdapterXDRRequestSecured EndPointURL is null", result.getMessage());
    }

    /**
     * Test of getAdapterXDRRequestSecuredUrl method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Ignore
    public void testGetAdapterXDRRequestSecuredUrl() {
        System.out.println("getAdapterXDRRequestSecuredUrl");
        AdapterXDRRequestWebServiceProxy instance = new AdapterXDRRequestWebServiceProxy();
        String expResult = "";
        String result = instance.getAdapterXDRRequestSecuredUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLogger method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Ignore
    public void testGetLogger() {
        System.out.println("getLogger");
        AdapterXDRRequestWebServiceProxy instance = new AdapterXDRRequestWebServiceProxy();
        Log expResult = null;
        Log result = instance.getLogger();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdapterXDRRequestSecuredService method, of class AdapterXDRRequestWebServiceProxy.
     */
    @Ignore
    public void testGetAdapterXDRRequestSecuredService() {
        System.out.println("getAdapterXDRRequestSecuredService");
        AdapterXDRRequestWebServiceProxy instance = new AdapterXDRRequestWebServiceProxy();
        AdapterXDRRequestSecuredService expResult = null;
        AdapterXDRRequestSecuredService result = instance.getAdapterXDRRequestSecuredService();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}