package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxyObjectFactory;

final public class NhinPatientDiscoveryOrchFactory implements
		GenericFactory<NhinPatientDiscoveryOrchestration> {

	private static NhinPatientDiscoveryOrchFactory INSTANCE = new NhinPatientDiscoveryOrchFactory();

	NhinPatientDiscoveryOrchFactory() {
	}

	@Override
	public NhinPatientDiscoveryOrchestration create() {
		return new NhinPatientDiscoveryOrchImpl(
				new AbstractServicePropertyAccessor() {

					@Override
					protected String getServiceName() {
						return  NhincConstants.NHINC_PATIENT_DISCOVERY_SERVICE_NAME;
					}

					@Override
					protected String getPassThruName() {
						return  NhincConstants.NHINC_PASSTHRU_PATIENT_DISCOVERY_SERVICE_NAME;//?
					} },
				new PatientDiscoveryAuditLogger(),
				new PatientDiscovery201305Processor(),
				new AdapterPatientDiscoveryProxyObjectFactory()
		);
	}

	public static NhinPatientDiscoveryOrchFactory getInstance() {
		return INSTANCE;
	}
}
