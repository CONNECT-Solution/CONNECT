package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.AbstractServicePropertyAccessor;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;

public final class NhinPatientDiscoveryDeferredRespOrchFactory implements
		GenericFactory<NhinPatientDiscoveryDeferredRespOrchImpl> {
	
	
	private static NhinPatientDiscoveryDeferredRespOrchFactory INSTANCE = new NhinPatientDiscoveryDeferredRespOrchFactory();
	
	NhinPatientDiscoveryDeferredRespOrchFactory() {
		
	}

	@Override
	public NhinPatientDiscoveryDeferredRespOrchImpl create() {
		// TODO Auto-generated method stub
		return new NhinPatientDiscoveryDeferredRespOrchImpl(
				new AbstractServicePropertyAccessor() {

					@Override
					protected String getServiceName() {
						return NhincConstants.NHINC_PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME;
					}
				}, new PatientDiscoveryAuditLogger(),
				new AdapterPatientDiscoveryDeferredRespProxyObjectFactory());
	}
	
	
	public static NhinPatientDiscoveryDeferredRespOrchFactory getInstance() {
		return INSTANCE;
	}
}
