package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.NhincProxyPatientDiscoveryImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhincProxyPatientDiscoverySecuredTest {

	//NhincProxyPatientDiscoverySecured
	
	
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test 
    public void testDefaultConstructor() {
    	NhincProxyPatientDiscoverySecured patientDiscovery =  new NhincProxyPatientDiscoverySecured();
    	assertNotNull(patientDiscovery);
    }
	
	@Test
	public void testMockService() {
		
		final ProxyPRPAIN201305UVProxySecuredRequestType mockBody = context.mock(ProxyPRPAIN201305UVProxySecuredRequestType.class);
		final PRPAIN201306UV02 expectedResponse = context.mock(PRPAIN201306UV02.class);
		final NhincProxyPatientDiscoveryImpl mockService = context.mock(NhincProxyPatientDiscoveryImpl.class);
		final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
		
		NhincProxyPatientDiscoverySecured patientDiscovery =  new NhincProxyPatientDiscoverySecured(mockFactory);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).proxyPRPAIN201305UV(with(same(mockBody)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhincProxyPatientDiscoveryImpl();
	        will(returnValue(mockService));
	    }});
		
		PRPAIN201306UV02 actualResponse = patientDiscovery.proxyPRPAIN201305UV(mockBody);
		
		assertSame(expectedResponse, actualResponse);
	
	}
	
	
}
