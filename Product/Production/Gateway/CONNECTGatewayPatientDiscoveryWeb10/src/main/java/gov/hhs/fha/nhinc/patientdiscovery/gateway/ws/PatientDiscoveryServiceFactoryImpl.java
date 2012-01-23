package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.deferred.request.NhinPatientDiscoveryAsyncReqImpl;
import gov.hhs.fha.nhinc.patientdiscovery.deferred.response.NhinPatientDiscoveryAsyncRespImpl;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestImpl;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.EntityPatientDiscoveryDeferredResponseImpl;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.NhinPatientDiscoveryOrchFactory;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.NhincProxyPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.NhincProxyPatientDiscoveryDeferredRequestImpl;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.NhincProxyPatientDiscoveryAsyncRespImpl;

public class PatientDiscoveryServiceFactoryImpl implements
		PatientDiscoveryServiceFactory {

	@Override
	public NhinPatientDiscoveryImpl getNhinPatientDiscoveryService() {
		return new NhinPatientDiscoveryImpl(new PatientDiscoveryAuditLogger(), NhinPatientDiscoveryOrchFactory.getInstance());
	}

	@Override
	public NhinPatientDiscoveryAsyncReqImpl getNhinPatientDiscoveryAsyncReqImpl() {
		return new NhinPatientDiscoveryAsyncReqImpl();
	}

	@Override
	public NhinPatientDiscoveryAsyncRespImpl getNhinPatientDiscoveryAsyncRespImpl() {
		return new NhinPatientDiscoveryAsyncRespImpl();
	}

	@Override
	public EntityPatientDiscoveryImpl getEntityPatientDiscoveryImpl() {
		return new EntityPatientDiscoveryImpl();
	}

	@Override
	public EntityPatientDiscoveryDeferredRequestImpl getEntityPatientDiscoveryDeferredRequestImpl() {
		return new EntityPatientDiscoveryDeferredRequestImpl();
	}

	@Override
	public EntityPatientDiscoveryDeferredResponseImpl getEntityPatientDiscoveryDeferredResponseImpl() {
		return new EntityPatientDiscoveryDeferredResponseImpl();
	}

	@Override
	public NhincProxyPatientDiscoveryImpl getNhincProxyPatientDiscoveryImpl() {
		return new NhincProxyPatientDiscoveryImpl();
	}

	@Override
	public NhincProxyPatientDiscoveryDeferredRequestImpl getNhincProxyPatientDiscoveryDeferredRequestImpl() {
		return new NhincProxyPatientDiscoveryDeferredRequestImpl();
	}
	
	@Override
	public NhincProxyPatientDiscoveryAsyncRespImpl getNhincProxyPatientDiscoveryAsyncRespImpl() {
		return new NhincProxyPatientDiscoveryAsyncRespImpl();
	}
}
