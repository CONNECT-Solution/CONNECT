package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.deferred.request.NhinPatientDiscoveryAsyncReqImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhinPatientDiscoveryAsyncReqTest {
	//NhinPatientDiscoveryAsyncReq

	
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test 
    public void testDefaultConstructor() {
    	NhinPatientDiscoveryAsyncReq patientDiscovery =  new NhinPatientDiscoveryAsyncReq();
    	assertNotNull(patientDiscovery);
    }
	
	@Test
	public void testMockService() {
		
		final PRPAIN201305UV02 mockBody = context.mock(PRPAIN201305UV02.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		final NhinPatientDiscoveryAsyncReqImpl mockService = context.mock(NhinPatientDiscoveryAsyncReqImpl.class);
		final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
		
		NhinPatientDiscoveryAsyncReq patientDiscovery =  new NhinPatientDiscoveryAsyncReq(mockFactory);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).respondingGatewayPRPAIN201305UV02(with(same(mockBody)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhinPatientDiscoveryAsyncReqImpl();
	        will(returnValue(mockService));
	    }});
		
		MCCIIN000002UV01 actualResponse = patientDiscovery.respondingGatewayDeferredPRPAIN201305UV02(mockBody);
		
		assertSame(expectedResponse, actualResponse);
	
	}
}
