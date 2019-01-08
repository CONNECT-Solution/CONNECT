/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientlocationquery.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayPatientLocationQueryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.patientlocationquery.audit.PatientLocationQueryAuditLogger;
import gov.hhs.fha.nhinc.patientlocationquery.entity.OutboundPatientLocationQueryDelegate;
import gov.hhs.fha.nhinc.patientlocationquery.entity.OutboundPatientLocationQueryOrchestratable;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;

/**
 *
 * @author tjafri
 */
public class PassthroughOutboundPatientLocationQuery implements OutboundPatientLocationQuery {

    PatientLocationQueryAuditLogger auditLogger = new PatientLocationQueryAuditLogger();

    @Override
    public RespondingGatewayPatientLocationQueryResponseType processPatientLocationQuery(
        PatientLocationQueryRequestType request, AssertionType assertion, NhinTargetCommunitiesType target) {
        auditRequest(request, assertion, target);
        RespondingGatewayPatientLocationQueryResponseType response = sendToNhinProxy(request, assertion, target);
        processPatientLocationQueryPLQ(response, assertion);

        return response;
    }

    protected RespondingGatewayPatientLocationQueryResponseType sendToNhinProxy(PatientLocationQueryRequestType request,
        AssertionType assertion, NhinTargetCommunitiesType target) {

        OutboundPatientLocationQueryDelegate ddsDelegate = new OutboundPatientLocationQueryDelegate();
        OutboundPatientLocationQueryOrchestratable ddsOrchestratable = createOrchestratable(ddsDelegate, request,
            assertion, target);

        PatientLocationQueryResponseType response = ((OutboundPatientLocationQueryOrchestratable) ddsDelegate.process(
            ddsOrchestratable)).getResponse();

        RespondingGatewayPatientLocationQueryResponseType responseType
            = new RespondingGatewayPatientLocationQueryResponseType();
        responseType.setPatientLocationQueryResponse(response);
        return responseType;

    }

    protected OutboundPatientLocationQueryOrchestratable createOrchestratable(
        OutboundPatientLocationQueryDelegate delegate,
        PatientLocationQueryRequestType request, AssertionType assertion, NhinTargetCommunitiesType target) {

        OutboundPatientLocationQueryOrchestratable ddsOrchestratable = new OutboundPatientLocationQueryOrchestratable(
            delegate);
        ddsOrchestratable.setAssertion(assertion);
        ddsOrchestratable.setRequest(request);
        ddsOrchestratable.setTarget(target);
        return ddsOrchestratable;
    }

    protected void auditRequest(PatientLocationQueryRequestType request, AssertionType assertion,
        NhinTargetCommunitiesType target) {
        NhinTargetSystemType targetType = MessageGeneratorUtils.getInstance().convertFirstToNhinTargetSystemType(target);
        auditLogger.auditRequestMessage(request, assertion, targetType, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null, NhincConstants.PLQ_NHIN_SERVICE_NAME);
    }

    protected void processPatientLocationQueryPLQ(RespondingGatewayPatientLocationQueryResponseType response, AssertionType assertion) {

        PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
        PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();
        patCorrelationProxy.addPatientCorrelationPLQ(response.getPatientLocationQueryResponse(), assertion);
    }
}
