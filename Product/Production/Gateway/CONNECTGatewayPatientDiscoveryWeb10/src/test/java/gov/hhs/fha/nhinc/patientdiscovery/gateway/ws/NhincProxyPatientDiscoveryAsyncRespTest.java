package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;

import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.NhincProxyPatientDiscoveryAsyncRespImpl;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;
import org.junit.Test;

public class NhincProxyPatientDiscoveryAsyncRespTest {
	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test 
    public void testDefaultConstructor() {
    	NhincProxyPatientDiscoveryAsyncResp patientDiscovery =  new NhincProxyPatientDiscoveryAsyncResp();
    	assertNotNull(patientDiscovery);
    }
	
	@Test
	public void testMockService() {
		
		final ProxyPRPAIN201306UVProxyRequestType mockBody = context.mock(ProxyPRPAIN201306UVProxyRequestType.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		final NhincProxyPatientDiscoveryAsyncRespImpl mockService = context.mock(NhincProxyPatientDiscoveryAsyncRespImpl.class);
		final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
		
		NhincProxyPatientDiscoveryAsyncResp patientDiscovery =  new NhincProxyPatientDiscoveryAsyncResp(mockFactory);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).proxyProcessPatientDiscoveryAsyncResp(with(same(mockBody)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getNhincProxyPatientDiscoveryAsyncRespImpl();
	        will(returnValue(mockService));
	    }});
		
		MCCIIN000002UV01 actualResponse = patientDiscovery.proxyProcessPatientDiscoveryAsyncResp(mockBody);
		
		assertSame(expectedResponse, actualResponse);
	
	}
	
	@Test
	public void verifyWebServiceAnnotation() {
		WebService webServiceAnnotation = NhincProxyPatientDiscoveryAsyncResp.class.getAnnotation(WebService.class);
		assertEquals("NhincProxyPatientDiscoveryAsyncRespPortType", webServiceAnnotation.portName());
		assertEquals("NhincProxyPatientDiscoveryAsyncResp", webServiceAnnotation.serviceName());
		assertEquals("gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncresp.NhincProxyPatientDiscoveryAsyncRespPortType", webServiceAnnotation.endpointInterface());
		assertEquals("urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncresp", webServiceAnnotation.targetNamespace());
		assertEquals("WEB-INF/wsdl/NhincProxyPatientDiscoveryAsyncResp/NhincProxyPatientDiscoveryAsyncResp.wsdl", webServiceAnnotation.wsdlLocation());		
	}
	
	
	@Test
	public void verifyAddressingIsEnabled() {
		Addressing addressingAnnotation = NhincProxyPatientDiscoveryAsyncResp.class.getAnnotation(Addressing.class);
		assertTrue(addressingAnnotation.enabled());
	}
	
}
