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
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy.PassthruPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy.PassthruPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;
import java.util.List;

import org.hl7.v3.II;

/**
 * 
 * @author dunnek
 */
public class EntityPatientDiscoveryDeferredResponseOrchImpl implements EntityPatientDiscoveryDeferredResponseOrch {

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryDeferredResponseOrchImpl.class);

    private GenericFactory<PassthruPatientDiscoveryDeferredRespProxy> proxyFactory;// = new
                                                                                   // PassthruPatientDiscoveryDeferredRespProxyObjectFactory();
    private PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker;
    private WebServiceProxyHelper webserviceProxyhelper;
    private PatientDiscovery201306Processor pd201306Processor;

    EntityPatientDiscoveryDeferredResponseOrchImpl(
            GenericFactory<PassthruPatientDiscoveryDeferredRespProxy> proxyFactory,
            PolicyChecker<RespondingGatewayPRPAIN201306UV02RequestType, PRPAIN201306UV02> policyChecker) {
        this.proxyFactory = proxyFactory;
        this.policyChecker = policyChecker;

        this.webserviceProxyhelper = new WebServiceProxyHelper();
        this.pd201306Processor = new PatientDiscovery201306Processor();
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return this.webserviceProxyhelper;
    }

    @Override
    public MCCIIN000002UV01 processPatientDiscoveryAsyncRespOrch(PRPAIN201306UV02 body, AssertionType assertion,
            NhinTargetCommunitiesType target) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        List<UrlInfo> urlInfoList = null;
        PatientDiscoveryAuditor auditLog = new PatientDiscoveryAuditLogger();

        if (body != null && assertion != null) {
            urlInfoList = getTargets(target);

            // loop through the communities and send request if results were not null
            if (urlInfoList != null && urlInfoList != null) {
                for (UrlInfo urlInfo : urlInfoList) {

                    // Log the start of the performance record
                    Timestamp starttime = new Timestamp(System.currentTimeMillis());
                    Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime,
                            "Deferred" + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                            NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                            HomeCommunityMap.getLocalHomeCommunityId());

                    // create a new request to send out to each target community
                    RespondingGatewayPRPAIN201306UV02RequestType newRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
                    PRPAIN201306UV02 new201306 = pd201306Processor.createNewRequest(body, urlInfo.getHcid());

                    newRequest.setAssertion(assertion);
                    newRequest.setPRPAIN201306UV02(new201306);
                    newRequest.setNhinTargetCommunities(target);

                    AcknowledgementType ackMsg = auditLog.auditEntity201306(newRequest, assertion,
                            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

                    // check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);

                    if (bIsPolicyOk) {
                        ack = sendToProxy(newRequest, urlInfo);
                    } else {
                        ack = HL7AckTransforms.createAckFrom201306(body, "Policy Failed");
                    }

                    // Log the end of the performance record
                    Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);

                    ackMsg = auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                            NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
                }
            } else {
                log.warn("No targets were found for the Patient Discovery Response");
                ack = HL7AckTransforms.createAckFrom201306(body, "No Targets Found");
            }
        }

        return ack;
    }

    protected List<UrlInfo> getTargets(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getInstance().getEndpointURLFromNhinTargetCommunities(
                    targetCommunities, NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service "
                    + NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201306UV02RequestType request) {
        return policyChecker.checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(PRPAIN201306UV02 body, AssertionType assertion,
            NhinTargetCommunitiesType target, UrlInfo urlInfo) {
        RespondingGatewayPRPAIN201306UV02RequestType request = new RespondingGatewayPRPAIN201306UV02RequestType();

        request.setAssertion(assertion);
        request.setPRPAIN201306UV02(body);
        request.setNhinTargetCommunities(target);

        return sendToProxy(request, urlInfo);
    }

    protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201306UV02RequestType request, UrlInfo urlInfo) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        PassthruPatientDiscoveryDeferredRespProxy proxy = proxyFactory.create();

        resp = proxy.proxyProcessPatientDiscoveryAsyncResp(request.getPRPAIN201306UV02(), request.getAssertion(),
                oTargetSystemType);

        return resp;
    }
}
