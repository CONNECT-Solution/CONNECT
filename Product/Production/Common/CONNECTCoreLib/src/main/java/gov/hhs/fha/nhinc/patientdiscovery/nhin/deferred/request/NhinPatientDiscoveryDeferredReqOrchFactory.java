/**
 * 
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.AbstractServicePropertyAccessor;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;

/**
 * @author bhumphrey
 * 
 */
public final class NhinPatientDiscoveryDeferredReqOrchFactory implements
		GenericFactory<NhinPatientDiscoveryDeferredReqOrchImpl> {
	
	private static NhinPatientDiscoveryDeferredReqOrchFactory INSTANCE = new NhinPatientDiscoveryDeferredReqOrchFactory();
	
	NhinPatientDiscoveryDeferredReqOrchFactory() {
		
	}

	@Override
	public NhinPatientDiscoveryDeferredReqOrchImpl create() {
		return new NhinPatientDiscoveryDeferredReqOrchImpl(
				new AbstractServicePropertyAccessor() {

					@Override
					protected String getServiceName() {
						return  NhincConstants.NHINC_PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME;
					} },
				new PatientDiscoveryAuditLogger(),
				new AdapterPatientDiscoveryDeferredReqProxyObjectFactory());
	}
	
	public static NhinPatientDiscoveryDeferredReqOrchFactory getInstance() {
		return INSTANCE;
	}

}
