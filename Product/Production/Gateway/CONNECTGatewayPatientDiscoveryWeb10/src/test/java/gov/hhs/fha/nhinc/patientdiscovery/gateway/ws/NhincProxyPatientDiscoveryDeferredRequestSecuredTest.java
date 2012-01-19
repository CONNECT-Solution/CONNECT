package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.NhincProxyPatientDiscoveryDeferredRequestImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhincProxyPatientDiscoveryDeferredRequestSecuredTest {

	
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test
    public void testDefaultConstructor() {
    	NhincProxyPatientDiscoveryDeferredRequestSecured ws = new NhincProxyPatientDiscoveryDeferredRequestSecured();
    	assertNotNull(ws);
    }
    
    @Test
    public void testMockService() {
    	final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
    	final ProxyPRPAIN201305UVProxySecuredRequestType mockRequest = context.mock(ProxyPRPAIN201305UVProxySecuredRequestType.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		
		final NhincProxyPatientDiscoveryDeferredRequestImpl mockService = context.mock(NhincProxyPatientDiscoveryDeferredRequestImpl.class);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).processPatientDiscoveryAsyncRequestSecured(with(same(mockRequest)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhincProxyPatientDiscoveryDeferredRequestImpl();
	        will(returnValue(mockService));
	    }});
    	
    	
		NhincProxyPatientDiscoveryDeferredRequestSecured ws = new NhincProxyPatientDiscoveryDeferredRequestSecured(mockFactory);
    	
		MCCIIN000002UV01 actualResponse = ws.proxyProcessPatientDiscoveryAsyncReq(mockRequest);
		
		assertSame(expectedResponse, actualResponse);
    }
    
    @Test
    public void testNullService() {
    	final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
    	final ProxyPRPAIN201305UVProxySecuredRequestType mockRequest = context.mock(ProxyPRPAIN201305UVProxySecuredRequestType.class);
		
		context.checking(new Expectations() {{
	        
	        oneOf(mockFactory).getNhincProxyPatientDiscoveryDeferredRequestImpl();
	        will(returnValue(null));

	    }});
    	
    	
		NhincProxyPatientDiscoveryDeferredRequestSecured ws = new NhincProxyPatientDiscoveryDeferredRequestSecured(mockFactory);
    	
		MCCIIN000002UV01 actualResponse = ws.proxyProcessPatientDiscoveryAsyncReq(mockRequest);
		
		assertNull( actualResponse);
    	
    }
}
