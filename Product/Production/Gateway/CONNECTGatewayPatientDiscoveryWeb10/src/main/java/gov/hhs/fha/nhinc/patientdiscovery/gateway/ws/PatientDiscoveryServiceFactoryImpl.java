/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
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
