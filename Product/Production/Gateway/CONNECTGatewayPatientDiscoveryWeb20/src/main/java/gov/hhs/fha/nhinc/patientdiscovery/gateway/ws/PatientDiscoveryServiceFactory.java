package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.deferred.request.NhinPatientDiscoveryAsyncReqImpl;
import gov.hhs.fha.nhinc.patientdiscovery.deferred.response.NhinPatientDiscoveryAsyncRespImpl;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestImpl;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.EntityPatientDiscoveryDeferredResponseImpl;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.NhincProxyPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.NhincProxyPatientDiscoveryDeferredRequestImpl;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.NhincProxyPatientDiscoveryAsyncRespImpl;

public interface PatientDiscoveryServiceFactory {

	public abstract NhinPatientDiscoveryAsyncReqImpl getNhinPatientDiscoveryAsyncReqImpl();

	public abstract NhinPatientDiscoveryImpl getNhinPatientDiscoveryService();

	public abstract NhinPatientDiscoveryAsyncRespImpl getNhinPatientDiscoveryAsyncRespImpl();

	public abstract NhincProxyPatientDiscoveryAsyncRespImpl getNhincProxyPatientDiscoveryAsyncRespImpl();

	public abstract NhincProxyPatientDiscoveryDeferredRequestImpl getNhincProxyPatientDiscoveryDeferredRequestImpl();

	public abstract NhincProxyPatientDiscoveryImpl getNhincProxyPatientDiscoveryImpl();

	public abstract EntityPatientDiscoveryDeferredResponseImpl getEntityPatientDiscoveryDeferredResponseImpl();

	public abstract EntityPatientDiscoveryDeferredRequestImpl getEntityPatientDiscoveryDeferredRequestImpl();

	public abstract EntityPatientDiscoveryImpl getEntityPatientDiscoveryImpl();

}
