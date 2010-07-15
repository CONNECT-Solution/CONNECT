/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseWebServiceProxyTest {

    public AdapterXDRResponseWebServiceProxyTest() {
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
     * Test of provideAndRegisterDocumentSetBResponse method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBResponse() {
        System.out.println("provideAndRegisterDocumentSetBResponse");

        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        AdapterXDRResponseWebServiceProxy adapterXDRResponseWebServiceProxy = new AdapterXDRResponseWebServiceProxy(){

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String getAdapterXDRResponseSecuredUrl() {
                return "URL";
            }

            @Override
            protected AdapterXDRResponseSecuredPortType getAdapterXDRResponseSecuredPort(String url) {
                AdapterXDRResponseSecuredPortType mockPort = new AdapterXDRResponseSecuredPortType()
                {
                    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType arg0)
                    {
                        return ack;
                    }
                };
                return mockPort;

            }

            @Override
            protected void setRequestContext(AssertionType assertion, String adapterXDRResponseSecuredEndPointURL, AdapterXDRResponseSecuredPortType port) {

            }
        };


        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        RegistryResponseType body = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRResponseWebServiceProxy.provideAndRegisterDocumentSetBResponse(body, assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }

    /**
     * Test of provideAndRegisterDocumentSetBResponse method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBResponseFailureCase() {
        System.out.println("provideAndRegisterDocumentSetBResponse");

        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        AdapterXDRResponseWebServiceProxy adapterXDRResponseWebServiceProxy = new AdapterXDRResponseWebServiceProxy(){

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String getAdapterXDRResponseSecuredUrl() {
                return null;
            }

            @Override
            protected AdapterXDRResponseSecuredPortType getAdapterXDRResponseSecuredPort(String url) {
                AdapterXDRResponseSecuredPortType mockPort = new AdapterXDRResponseSecuredPortType()
                {
                    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType arg0)
                    {
                        return ack;
                    }
                };
                return mockPort;

            }

            @Override
            protected void setRequestContext(AssertionType assertion, String adapterXDRResponseSecuredEndPointURL, AdapterXDRResponseSecuredPortType port) {

            }
        };


        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
            }
        });

        RegistryResponseType body = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRResponseWebServiceProxy.provideAndRegisterDocumentSetBResponse(body, assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }

    /**
     * Test of setRequestContext method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Ignore
    public void testSetRequestContext() {
        System.out.println("setRequestContext");
        AssertionType assertion = null;
        String adapterXDRResponseSecuredEndPointURL = "";
        AdapterXDRResponseSecuredPortType port = null;
        AdapterXDRResponseWebServiceProxy instance = new AdapterXDRResponseWebServiceProxy();
        instance.setRequestContext(assertion, adapterXDRResponseSecuredEndPointURL, port);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdapterXDRResponseSecuredUrl method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Ignore
    public void testGetAdapterXDRResponseSecuredUrl() {
        System.out.println("getAdapterXDRResponseSecuredUrl");
        AdapterXDRResponseWebServiceProxy instance = new AdapterXDRResponseWebServiceProxy();
        String expResult = "";
        String result = instance.getAdapterXDRResponseSecuredUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdapterXDRResponseSecuredPort method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Ignore
    public void testGetAdapterXDRResponseSecuredPort() {
        System.out.println("getAdapterXDRResponseSecuredPort");
        String url = "";
        AdapterXDRResponseWebServiceProxy instance = new AdapterXDRResponseWebServiceProxy();
        AdapterXDRResponseSecuredPortType expResult = null;
        AdapterXDRResponseSecuredPortType result = instance.getAdapterXDRResponseSecuredPort(url);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLogger method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Ignore
    public void testGetLogger() {
        System.out.println("getLogger");
        AdapterXDRResponseWebServiceProxy instance = new AdapterXDRResponseWebServiceProxy();
        Log expResult = null;
        Log result = instance.getLogger();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdapterXDRResponseSecuredService method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Ignore
    public void testGetAdapterXDRResponseSecuredService() {
        System.out.println("getAdapterXDRResponseSecuredService");
        AdapterXDRResponseWebServiceProxy instance = new AdapterXDRResponseWebServiceProxy();
        AdapterXDRResponseSecuredService expResult = null;
        AdapterXDRResponseSecuredService result = instance.getAdapterXDRResponseSecuredService();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}