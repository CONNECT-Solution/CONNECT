package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhinPatientDiscoveryTest {
	
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test 
    public void testDefaultConstructor() {
    	NhinPatientDiscovery patientDiscovery =  new NhinPatientDiscovery();
    	assertNotNull(patientDiscovery);
    }
	
	@Test
	public void testMockService() {
		
		final PRPAIN201305UV02 mockBody = context.mock(PRPAIN201305UV02.class);
		final PRPAIN201306UV02 expectedResponse = context.mock(PRPAIN201306UV02.class);
		final NhinPatientDiscoveryImpl mockService = context.mock(NhinPatientDiscoveryImpl.class);
		final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
		
		 NhinPatientDiscovery patientDiscovery =  new NhinPatientDiscovery(mockFactory);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).respondingGatewayPRPAIN201305UV02(with(same(mockBody)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhinPatientDiscoveryService();
	        will(returnValue(mockService));
	    }});
		
		PRPAIN201306UV02 actualResponse = patientDiscovery.respondingGatewayPRPAIN201305UV02(mockBody);
		
		assertSame(expectedResponse, actualResponse);
	
	}
	
	
	
	
}
