package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

public class PatientDiscoveryBase {
	private PatientDiscoveryServiceFactory serviceFactory;
	
	public PatientDiscoveryBase() {
		this.serviceFactory = new PatientDiscoveryServiceFactoryImpl();
	}

	public PatientDiscoveryBase(PatientDiscoveryServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	public PatientDiscoveryServiceFactory getServiceFactory() {
		return serviceFactory;
	}
	
	
	 
}
