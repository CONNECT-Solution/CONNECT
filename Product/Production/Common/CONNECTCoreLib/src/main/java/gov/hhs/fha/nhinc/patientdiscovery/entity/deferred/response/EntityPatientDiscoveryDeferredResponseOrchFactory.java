package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response;

import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy.PassthruPatientDiscoveryDeferredRespProxyObjectFactory;


public class EntityPatientDiscoveryDeferredResponseOrchFactory implements 
		GenericFactory<EntityPatientDiscoveryDeferredResponseOrch> {

	@Override
	public EntityPatientDiscoveryDeferredResponseOrch create() {
		return new EntityPatientDiscoveryDeferredResponseOrchImpl(new PassthruPatientDiscoveryDeferredRespProxyObjectFactory(), PatientDiscovery201306PolicyChecker.getInstance()); 
		
	}

}
