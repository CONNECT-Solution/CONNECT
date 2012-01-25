/**
 * 
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.AbstractServicePropertyAccessor;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;

/**
 * @author bhumphrey
 * 
 */
public final class NhinPatientDiscoveryDeferredReqOrchFactory implements
		GenericFactory<NhinPatientDiscoveryDeferredReqOrch> {

	private static NhinPatientDiscoveryDeferredReqOrchFactory INSTANCE = new NhinPatientDiscoveryDeferredReqOrchFactory();

	NhinPatientDiscoveryDeferredReqOrchFactory() {

	}

	@Override
	public NhinPatientDiscoveryDeferredReqOrch create() {
		return new NhinPatientDiscoveryDeferredReqOrchImpl(
				new AbstractServicePropertyAccessor() {

					@Override
					protected String getServiceName() {
						return  NhincConstants.NHINC_PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME;
					}

					@Override
					protected String getPassThruName() {
						return  NhincConstants.PATIENT_DISCOVERY_SERVICE_ASYNC_REQ_PASSTHRU_PROPERTY;
					} },
				new PatientDiscoveryAuditLogger(),
		new AdapterPatientDiscoveryDeferredReqProxyObjectFactory(),
		new AdapterPatientDiscoveryDeferredReqErrorProxyObjectFactory(),
		PatientDiscoveryPolicyChecker.getInstance());
	}

	public static NhinPatientDiscoveryDeferredReqOrchFactory getInstance() {
		return INSTANCE;
	}

}
