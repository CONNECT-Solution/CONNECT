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
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.error.proxy.AdapterPatientDiscoveryDeferredReqErrorProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.properties.ServicePropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryDeferredReqOrchImpl implements NhinPatientDiscoveryDeferredReqOrch {
    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDeferredReqOrchImpl.class);

    private final ServicePropertyAccessor servicePropertyAccessor;

    private final PatientDiscoveryAuditor auditLogger;

    private final GenericFactory<AdapterPatientDiscoveryDeferredReqProxy> proxyFactory;

    private final GenericFactory<AdapterPatientDiscoveryDeferredReqErrorProxy> proxyErrorFactory;

    private final PolicyChecker<RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201305UV02> policyChecker;

    NhinPatientDiscoveryDeferredReqOrchImpl(ServicePropertyAccessor servicePropertyAccessor,
            PatientDiscoveryAuditor auditLogger, GenericFactory<AdapterPatientDiscoveryDeferredReqProxy> proxyFactory,
            GenericFactory<AdapterPatientDiscoveryDeferredReqErrorProxy> proxyErrorFactory,
            PolicyChecker<RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201305UV02> policyChecker) {
        super();
        this.servicePropertyAccessor = servicePropertyAccessor;
        this.auditLogger = auditLogger;
        this.proxyFactory = proxyFactory;
        this.proxyErrorFactory = proxyErrorFactory;
        this.policyChecker = policyChecker;
    }

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    @Override
    public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        // Audit the incoming Nhin 201305 Message
        auditLogger.auditNhinDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        if (isInPassThroughMode()) {
            // Send the 201305 to the Adapter Interface
            resp = sendToAgency(request, assertion);
        } else {
            resp = serviceInternal(request, assertion);
        }

        // Audit the responding ack Message
        auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    /**
     * Checks to see if the query should be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthroughAsyncReq property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode() {
        return servicePropertyAccessor.isInPassThroughMode();
    }

    /**
     * Checks the policy of the passed in request.
     *
     * @param request
     * @param assertion
     * @return
     */
    protected boolean checkPolicy(PRPAIN201305UV02 request, AssertionType assertion) {
        return policyChecker.checkIncomingPolicy(request, assertion);
    }

    /**
     *
     * @param request
     * @param assertion
     * @param homeCommunityId
     * @return <code>MCCIIN000002UV01</code>
     */
    private MCCIIN000002UV01 serviceInternal(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 response = null;
        // HomeCommunityType hcId = new HomeCommunityType();
        String errMsg = null;

        // hcId.setHomeCommunityId(homeCommunityId);
        if (checkPolicy(request, assertion)) {
            log.debug("Adapter patient discovery deferred policy check successful");
            response = sendToAgency(request, assertion);
        } else {
            errMsg = "Policy Check Failed";
            log.error(errMsg);
            response = sendToAgencyError(request, assertion, errMsg);
        }

        return response;
    }

    protected MCCIIN000002UV01 sendToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        auditLogger.auditAdapterDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        AdapterPatientDiscoveryDeferredReqProxy proxy = proxyFactory.create();

        MCCIIN000002UV01 resp = proxy.processPatientDiscoveryAsyncReq(request, assertion);

        auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected MCCIIN000002UV01 sendToAgencyError(PRPAIN201305UV02 request, AssertionType assertion, String errMsg) {
        auditLogger.auditAdapterDeferred201305(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PRPAIN201306UV02 response = new HL7PRPA201306Transforms().createPRPA201306ForPatientNotFound(request);

        MCCIIN000002UV01 adapterResp = proxyErrorFactory.create().processPatientDiscoveryAsyncReqError(request,
                response, assertion, errMsg);

        auditLogger.auditAck(adapterResp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return adapterResp;
    }

}
