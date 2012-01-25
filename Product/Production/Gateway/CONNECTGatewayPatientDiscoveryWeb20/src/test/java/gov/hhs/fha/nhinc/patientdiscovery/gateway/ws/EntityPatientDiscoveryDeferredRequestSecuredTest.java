package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class EntityPatientDiscoveryDeferredRequestSecuredTest {
	
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test
    public void testDefaultConstructor() {
    	EntityPatientDiscoveryDeferredRequestSecured ws = new EntityPatientDiscoveryDeferredRequestSecured();
    	assertNotNull(ws);
    }
    
    @Test
    public void testMockService() {
    	final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
    	final RespondingGatewayPRPAIN201305UV02SecuredRequestType mockRequest = context.mock(RespondingGatewayPRPAIN201305UV02SecuredRequestType.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		final EntityPatientDiscoveryDeferredRequestImpl mockService = context.mock(EntityPatientDiscoveryDeferredRequestImpl.class);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).processPatientDiscoveryAsyncRequestSecured(with(same(mockRequest)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getEntityPatientDiscoveryDeferredRequestImpl();
	        will(returnValue(mockService));
	    }});
    	
    	
    	EntityPatientDiscoveryDeferredRequestSecured ws = new EntityPatientDiscoveryDeferredRequestSecured(mockFactory);
    	
		MCCIIN000002UV01 actualResponse = ws.processPatientDiscoveryAsyncReq(mockRequest);
		
		assertSame(expectedResponse, actualResponse);
    }
    
    @Test
    public void testNullService() {
    	final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
    	final RespondingGatewayPRPAIN201305UV02SecuredRequestType mockRequest = context.mock(RespondingGatewayPRPAIN201305UV02SecuredRequestType.class);
		
		context.checking(new Expectations() {{
	        
	        oneOf(mockFactory).getEntityPatientDiscoveryDeferredRequestImpl();
	        will(returnValue(null));

	    }});
    	
    	
    	EntityPatientDiscoveryDeferredRequestSecured ws = new EntityPatientDiscoveryDeferredRequestSecured(mockFactory);
    	
		MCCIIN000002UV01 actualResponse = ws.processPatientDiscoveryAsyncReq(mockRequest);
		
		assertNull( actualResponse);
    	
    }
}
