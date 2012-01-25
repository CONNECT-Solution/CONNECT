package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.NhincProxyPatientDiscoveryAsyncRespImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhincProxyPatientDiscoverySecuredAsyncRespTest {

	//NhincProxyPatientDiscoverySecuredAsyncResp
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test 
    public void testDefaultConstructor() {
    	NhincProxyPatientDiscoverySecuredAsyncResp patientDiscovery =  new NhincProxyPatientDiscoverySecuredAsyncResp();
    	assertNotNull(patientDiscovery);
    }
	
	@Test
	public void testMockService() {
		
		final ProxyPRPAIN201306UVProxySecuredRequestType mockBody = context.mock(ProxyPRPAIN201306UVProxySecuredRequestType.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		final NhincProxyPatientDiscoveryAsyncRespImpl mockService = context.mock(NhincProxyPatientDiscoveryAsyncRespImpl.class);
		final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
		
		NhincProxyPatientDiscoverySecuredAsyncResp patientDiscovery =  new NhincProxyPatientDiscoverySecuredAsyncResp(mockFactory);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).proxyProcessPatientDiscoveryAsyncResp(with(same(mockBody)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhincProxyPatientDiscoveryAsyncRespImpl();
	        will(returnValue(mockService));
	    }});
		
		MCCIIN000002UV01 actualResponse = patientDiscovery.proxyProcessPatientDiscoveryAsyncResp(mockBody);
		
		assertSame(expectedResponse, actualResponse);
	
	}
}
