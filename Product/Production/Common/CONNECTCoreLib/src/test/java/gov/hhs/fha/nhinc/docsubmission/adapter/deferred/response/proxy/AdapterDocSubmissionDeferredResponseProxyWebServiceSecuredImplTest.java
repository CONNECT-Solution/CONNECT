package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
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
import org.junit.Test;
import static org.junit.Assert.*;
import java.net.MalformedURLException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

/**
 *
 * @author patlollav
 */
public class AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImplTest
{
    public AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImplTest()
    {
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
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of provideAndRegisterDocumentSetBResponse method, of class AdapterXDRResponseWebServiceProxy.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBResponse()
    {
        System.out.println("provideAndRegisterDocumentSetBResponse");

        Mockery mockery = new Mockery()
        {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final AdapterXDRResponseSecuredPortType mockPort = mockery.mock(AdapterXDRResponseSecuredPortType.class);
        final Service mockService = mockery.mock(Service.class);
        final WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }
            @Override
            public String getUrlLocalHomeCommunity(String sServiceName)
            {
                return "url";
            }
            @Override
            public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
            throws Exception
            {
                XDRAcknowledgementType response = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                response.setMessage(regResp);
                return response;
            }
            @Override
            public void initializeSecurePort(BindingProvider port, String url, String serviceAction,
            String wsAddressingAction, AssertionType assertion)
            {

            }
            @Override
            public Service createService(String wsdlFile, String namespaceURI, String serviceLocalPart) throws MalformedURLException
            {
                return mockService;
            }
        };

        AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl adapterXDRResponseWebServiceProxy = new AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper()
            {
                return proxyHelper;
            }

            @Override
            protected AdapterXDRResponseSecuredPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
            {
                return mockPort;
            }
        };

        mockery.checking(new Expectations()
        {
            {
                allowing(mockLogger).isDebugEnabled();
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
    public void testProvideAndRegisterDocumentSetBResponseFailureCase()
    {
        System.out.println("provideAndRegisterDocumentSetBResponse");

        Mockery mockery = new Mockery()
        {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final AdapterXDRResponseSecuredPortType mockPort = mockery.mock(AdapterXDRResponseSecuredPortType.class);
        final Service mockService = mockery.mock(Service.class);
        final WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }
            @Override
            public String getUrlLocalHomeCommunity(String sServiceName)
            {
                return "url";
            }
            @Override
            public Object invokePort(Object portObject, Class portClass, String methodName, Object operationInput)
            throws Exception
            {
                XDRAcknowledgementType response = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                response.setMessage(regResp);
                return response;
            }
            @Override
            public void initializeSecurePort(BindingProvider port, String url, String serviceAction,
            String wsAddressingAction, AssertionType assertion)
            {

            }
            @Override
            public Service createService(String wsdlFile, String namespaceURI, String serviceLocalPart) throws MalformedURLException
            {
                return mockService;
            }
        };

        AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl adapterXDRResponseWebServiceProxy = new AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }

            @Override
            protected WebServiceProxyHelper createWebServiceProxyHelper()
            {
                return proxyHelper;
            }

            @Override
            protected AdapterXDRResponseSecuredPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
            {
                return mockPort;
            }
        };

        mockery.checking(new Expectations()
        {
            {
                allowing(mockLogger).isDebugEnabled();
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
            }
        });

        RegistryResponseType body = new RegistryResponseType();
        AssertionType assertion = new AssertionType();
        XDRAcknowledgementType result = adapterXDRResponseWebServiceProxy.provideAndRegisterDocumentSetBResponse(body, assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());
    }
}
