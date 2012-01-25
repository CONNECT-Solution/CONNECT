package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PatientDiscoveryServiceFactoryImplTest {
	
	private PatientDiscoveryServiceFactoryImpl serviceFactory = new PatientDiscoveryServiceFactoryImpl();
	

	@Test
	public void getNhinPatientDiscoveryAsyncReqImpl() {
		assertNotNull(serviceFactory.getNhinPatientDiscoveryAsyncReqImpl());
	}

	@Test
	public void getNhinPatientDiscoveryService() {
		assertNotNull(serviceFactory.getNhinPatientDiscoveryService());
	}

	@Test
	public void getNhinPatientDiscoveryAsyncRespImpl() {
		assertNotNull(serviceFactory.getNhinPatientDiscoveryAsyncRespImpl());
	}

	@Test
	public void getNhincProxyPatientDiscoveryAsyncRespImpl() {
		assertNotNull(serviceFactory.getNhincProxyPatientDiscoveryAsyncRespImpl());
	}

	@Test
	public void getNhincProxyPatientDiscoveryDeferredRequestImpl() {
		assertNotNull(serviceFactory.getNhincProxyPatientDiscoveryDeferredRequestImpl());
	}

	@Test
	public void getNhincProxyPatientDiscoveryImpl() {
		assertNotNull(serviceFactory.getNhincProxyPatientDiscoveryImpl());		
	}

	@Test
	public void getEntityPatientDiscoveryDeferredResponseImpl() {
		assertNotNull(serviceFactory.getEntityPatientDiscoveryDeferredResponseImpl());
	}

	@Test
	public void getEntityPatientDiscoveryDeferredRequestImpl() {
		assertNotNull(serviceFactory.getEntityPatientDiscoveryDeferredRequestImpl());
	}

	@Test
	public void getEntityPatientDiscoveryImpl() {
		assertNotNull(serviceFactory.getEntityPatientDiscoveryImpl());
	}
}
