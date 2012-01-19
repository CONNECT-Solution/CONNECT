package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.deferred.response.NhinPatientDiscoveryAsyncRespImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhinPatientDiscoveryAsyncRespTest {
	
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test 
    public void testDefaultConstructor() {
    	NhinPatientDiscoveryAsyncResp patientDiscovery =  new NhinPatientDiscoveryAsyncResp();
    	assertNotNull(patientDiscovery);
    }
	
	@Test
	public void testMockService() {
		
		final PRPAIN201306UV02 mockBody = context.mock(PRPAIN201306UV02.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		final NhinPatientDiscoveryAsyncRespImpl mockService = context.mock(NhinPatientDiscoveryAsyncRespImpl.class);
		final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
		
		NhinPatientDiscoveryAsyncResp patientDiscovery =  new NhinPatientDiscoveryAsyncResp(mockFactory);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).respondingGatewayPRPAIN201306UV02(with(same(mockBody)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhinPatientDiscoveryAsyncRespImpl();
	        will(returnValue(mockService));
	    }});
		
		MCCIIN000002UV01 actualResponse = patientDiscovery.respondingGatewayDeferredPRPAIN201306UV02(mockBody);
		
		assertSame(expectedResponse, actualResponse);
	
	}
	
	
}
