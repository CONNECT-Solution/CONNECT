/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecured.AdapterPatientDiscoverySecured;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
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
public class AdapterPatientDiscoveryWebServiceProxyTest {
private Mockery context;
    public AdapterPatientDiscoveryWebServiceProxyTest() {
    }

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
    /**
     * Test of respondingGatewayPRPAIN201305UV02 method, of class AdapterPatientDiscoveryWebServiceProxy.
     */
    @Test
    public void testRespondingGatewayPRPAIN201305UV02() {
        System.out.println("respondingGatewayPRPAIN201305UV02");
        PRPAIN201305UV02 body = null;
        AssertionType assertion = null;
        final Log mockLogger = context.mock(Log.class);
        
        AdapterPatientDiscoveryWebServiceProxy instance = new AdapterPatientDiscoveryWebServiceProxy()
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
            protected AdapterPatientDiscoverySecured createService()
            {
                return null;
            }
            
        };
       
        PRPAIN201306UV02 expResult = null;
        PRPAIN201306UV02 result = instance.respondingGatewayPRPAIN201305UV02(body, assertion);

    }

}