/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscovery;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
public class AdapterPatientDiscoveryWebServiceUnSecuredProxyTest {

    public AdapterPatientDiscoveryWebServiceUnSecuredProxyTest() {
    }


    private Mockery context;

    @Before
    public void setUp()
    {
        context = new Mockery()
        {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }


    @Test
    public void testRespondingGatewayPRPAIN201305UV02_noURL() {

        System.out.println("testRespondingGatewayPRPAIN201305UV02_noURL");

        final Log mockLogger = context.mock(Log.class);
        final AdapterPatientDiscovery mockService = context.mock(AdapterPatientDiscovery.class);

        AdapterPatientDiscoveryWebServiceUnSecuredProxy instance = new AdapterPatientDiscoveryWebServiceUnSecuredProxy()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }
            @Override
            protected String getUrl()
            {
                return "";
            }
            @Override
            protected AdapterPatientDiscovery createService()
            {
                return mockService;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with("Fail"));
                will(returnValue(null));
            }
        });
        PRPAIN201305UV02 request = null;
        AssertionType assertion = null;
        PRPAIN201306UV02 expResult = new PRPAIN201306UV02();
        PRPAIN201306UV02 result = instance.respondingGatewayPRPAIN201305UV02(request, assertion);
        assertNotNull(result);
        assertNotNull(result.getAcknowledgement());
        assertEquals(0, result.getAcknowledgement().size());

    }
/*
    @Test
    public void testRespondingGatewayPRPAIN201305UV02_noRequest() {

        System.out.println("testRespondingGatewayPRPAIN201305UV02_noRequest");

        final Log mockLogger = context.mock(Log.class);
        final AdapterPatientDiscovery mockService = context.mock(AdapterPatientDiscovery.class);

        AdapterPatientDiscoveryWebServiceUnSecuredProxy instance = new AdapterPatientDiscoveryWebServiceUnSecuredProxy()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }
            @Override
            protected String getUrl()
            {
                return "URL";
            }
            @Override
            protected AdapterPatientDiscovery createService()
            {
                return mockService;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with("Fail"));
                will(returnValue(null));
            }
        });
        PRPAIN201305UV02 request = null;
        AssertionType assertion = null;
        PRPAIN201306UV02 expResult = new PRPAIN201306UV02();
        PRPAIN201306UV02 result = instance.respondingGatewayPRPAIN201305UV02(request, assertion);
        assertNotNull(result);
        assertNotNull(result.getAcknowledgement());
        assertEquals(0, result.getAcknowledgement().size());

    }
    @Test
    public void testRespondingGatewayPRPAIN201305UV02_validRequest() {

        System.out.println("testRespondingGatewayPRPAIN201305UV02_validRequest");
        final RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType assertion = new AssertionType();
        final Log mockLogger = context.mock(Log.class);
        final AdapterPatientDiscoveryPortType mockPort = context.mock(AdapterPatientDiscoveryPortType.class);
        final AdapterPatientDiscovery mockService = context.mock(AdapterPatientDiscovery.class);
        final PRPAIN201305UV02 body = new PRPAIN201305UV02();

        request.setAssertion(assertion);
        request.setPRPAIN201305UV02(body);
        
        AdapterPatientDiscoveryWebServiceUnSecuredProxy instance = new AdapterPatientDiscoveryWebServiceUnSecuredProxy()
        {
            @Override
            protected Log createLogger()
            {
                return mockLogger;
            }
            @Override
            protected String getUrl()
            {
                return "URL";
            }
            @Override
            protected AdapterPatientDiscovery createService()
            {
                return mockService;
            }
            @Override
            protected AdapterPatientDiscoveryPortType getPort(String url, AssertionType assertion)
            {
                return mockPort;
            }

        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockPort).respondingGatewayPRPAIN201305UV02(with(request));
                will(returnValue(null));
            }
        });

        PRPAIN201306UV02 expResult = new PRPAIN201306UV02();
        PRPAIN201306UV02 result = instance.respondingGatewayPRPAIN201305UV02(body, assertion);
        assertNull(result);

    }
 */
}