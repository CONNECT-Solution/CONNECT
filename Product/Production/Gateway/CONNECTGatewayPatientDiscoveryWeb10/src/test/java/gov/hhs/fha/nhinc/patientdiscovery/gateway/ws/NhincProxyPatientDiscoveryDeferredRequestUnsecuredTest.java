package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.NhincProxyPatientDiscoveryDeferredRequestImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhincProxyPatientDiscoveryDeferredRequestUnsecuredTest {

	//NhincProxyPatientDiscoveryDeferredRequestUnsecured
	
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test
    public void testDefaultConstructor() {
    	NhincProxyPatientDiscoveryDeferredRequestUnsecured ws = new NhincProxyPatientDiscoveryDeferredRequestUnsecured();
    	assertNotNull(ws);
    }
    
    @Test
    public void testMockService() {
    	final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
    	final ProxyPRPAIN201305UVProxyRequestType mockRequest = context.mock(ProxyPRPAIN201305UVProxyRequestType.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		
		final NhincProxyPatientDiscoveryDeferredRequestImpl mockService = context.mock(NhincProxyPatientDiscoveryDeferredRequestImpl.class);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).processPatientDiscoveryAsyncRequestUnsecured(with(same(mockRequest)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhincProxyPatientDiscoveryDeferredRequestImpl();
	        will(returnValue(mockService));
	    }});
    	
    	
		NhincProxyPatientDiscoveryDeferredRequestUnsecured ws = new NhincProxyPatientDiscoveryDeferredRequestUnsecured(mockFactory);
    	
		MCCIIN000002UV01 actualResponse = ws.proxyProcessPatientDiscoveryAsyncReq(mockRequest);
		
		assertSame(expectedResponse, actualResponse);
    }
    
    @Test
    public void testNullService() {
    	final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
    	final ProxyPRPAIN201305UVProxyRequestType mockRequest = context.mock(ProxyPRPAIN201305UVProxyRequestType.class);
		
		context.checking(new Expectations() {{
	        
	        oneOf(mockFactory).getNhincProxyPatientDiscoveryDeferredRequestImpl();
	        will(returnValue(null));

	    }});
    	
    	
		NhincProxyPatientDiscoveryDeferredRequestUnsecured ws = new NhincProxyPatientDiscoveryDeferredRequestUnsecured(mockFactory);
    	
		MCCIIN000002UV01 actualResponse = ws.proxyProcessPatientDiscoveryAsyncReq(mockRequest);
		
		assertNull( actualResponse);
    	
    }
}
