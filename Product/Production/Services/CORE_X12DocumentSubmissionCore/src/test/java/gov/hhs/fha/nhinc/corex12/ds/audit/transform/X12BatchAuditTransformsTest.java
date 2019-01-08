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
package gov.hhs.fha.nhinc.corex12.ds.audit.transform;

import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.corex12.ds.audit.X12AuditDataTransformConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.net.UnknownHostException;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 *
 * @author achidamb
 */
public class X12BatchAuditTransformsTest extends
    AuditTransformsTest<COREEnvelopeBatchSubmission, COREEnvelopeBatchSubmissionResponse> {

    public X12BatchAuditTransformsTest() {
    }

    @Test
    public void transformRequestToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIp = "10.10.10.10";
        final String remoteIp = "16.14.13.12";
        final String remoteObjectUrl = "http://" + remoteIp + ":9090/source/AuditService";
        final String wsRequestUrl = "http://" + remoteIp + ":9090/AuditService";

        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, wsRequestUrl);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, remoteIp);

        X12BatchAuditTransforms transforms = new X12BatchAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return localIp;
            }

            @Override
            protected String getRemoteHostAddress(Properties webContextProperties) {
                if (webContextProperties != null && !webContextProperties.isEmpty() && webContextProperties
                    .getProperty(NhincConstants.REMOTE_HOST_ADDRESS) != null) {

                    return webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
                }
                return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return remoteObjectUrl;
            }
        };

        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(
            createCOREEnvelopeBatchSubmissionRequest(), assertion, createNhinTarget(),
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE,
            webContextProperties, NhincConstants.CORE_X12DS_GENERICBATCH_REQUEST_SERVICE_NAME);

        testGetEventIdentificationType(auditRequest, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, webContextProperties, localIp);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, remoteObjectUrl);
        testAuditSourceIdentification(auditRequest.getAuditMessage().getAuditSourceIdentification(), assertion);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticipantObjectIdentification(auditRequest);
    }

    @Test
    public void transformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIp = "10.10.10.10";
        final String remoteIp = "16.14.13.12";
        final String remoteObjectUrl = "http://" + remoteIp + ":9090/source/AuditService";
        final String wsRequestUrl = "http://" + remoteIp + ":9090/AuditService";

        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, wsRequestUrl);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, remoteIp);

        X12BatchAuditTransforms transforms = new X12BatchAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return localIp;
            }

            @Override
            protected String getRemoteHostAddress(Properties webContextProperties) {
                if (webContextProperties != null && !webContextProperties.isEmpty() && webContextProperties
                    .getProperty(NhincConstants.REMOTE_HOST_ADDRESS) != null) {

                    return webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
                }
                return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return remoteObjectUrl;
            }
        };

        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformResponseToAuditMsg(
            createCOREEnvelopeBatchSubmissionRequest(), createCOREEnvelopeBatchSubmissionResponse(), assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.FALSE, webContextProperties, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME);

        testGetEventIdentificationType(auditRequest, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME, Boolean.FALSE);
        testGetActiveParticipantSource(auditRequest, Boolean.FALSE, webContextProperties, localIp);
        testGetActiveParticipantDestination(auditRequest, Boolean.FALSE, webContextProperties, remoteObjectUrl);
        testAuditSourceIdentification(auditRequest.getAuditMessage().getAuditSourceIdentification(), assertion);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.FALSE, assertion);
        assertParticipantObjectIdentification(auditRequest);
    }

    // TODO: Too many hard-coded inline values below
    private COREEnvelopeBatchSubmission createCOREEnvelopeBatchSubmissionRequest() {
        COREEnvelopeBatchSubmission request = new COREEnvelopeBatchSubmission();
        request.setCORERuleVersion("2.2.0");
        request.setPayloadID("5");
        request.setPayloadType("X12_270_Request_005010X279A1");
        request.setProcessingMode("RealTime");
        request.setPayloadID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        request.setSenderID("HospitalA");
        request.setReceiverID("PayerB");
        return request;
    }

    private COREEnvelopeBatchSubmissionResponse createCOREEnvelopeBatchSubmissionResponse() {
        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();
        response.setCORERuleVersion("2.2.0");
        response.setPayloadID("5");
        response.setPayloadType("X12_270_Request_005010X279A1");
        response.setProcessingMode("RealTime");
        response.setPayloadID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        response.setSenderID("HospitalA");
        response.setReceiverID("PayerB");
        return response;
    }

    private void assertParticipantObjectIdentification(LogEventRequestType auditRequest) {
        assertNotNull("auditRequest is null", auditRequest);
        assertNotNull("AuditMessage is null", auditRequest.getAuditMessage());
        assertNotNull("ParticipantObjectIdentification is null", auditRequest.getAuditMessage()
            .getParticipantObjectIdentification());
        assertFalse("ParticipantObjectIdentification list is missing test values", auditRequest.getAuditMessage()
            .getParticipantObjectIdentification().isEmpty());

        ParticipantObjectIdentificationType participantPatient = auditRequest.getAuditMessage()
            .getParticipantObjectIdentification().get(0);

        assertEquals("ParticipantPatient.ParticipantObjectID mismatch", "f81d4fae-7dec-11d0-a765-00a0c91e6bf6",
            participantPatient.getParticipantObjectID());

        // TODO: assertSame vs assertEquals consistency when returning constants
        assertSame("ParticipantPatient.ParticipantObjectTypeCode object reference mismatch",
            X12AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_SYSTEM,
            participantPatient.getParticipantObjectTypeCode());
        assertSame("ParticipantPatient.ParticipantObjectTypeCodeRole object reference mismatch",
            X12AuditDataTransformConstants.PARTICIPANT_OBJ_TYPE_CODE_ROLE_X12,
            participantPatient.getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.Code mismatch",
            "f81d4fae-7dec-11d0-a765-00a0c91e6bf6",
            participantPatient.getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.CodeSystemName mismatch",
            X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_NAME,
            participantPatient.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.DisplayName mismatch",
            X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_DISPLAY_NAME,
            participantPatient.getParticipantObjectIDTypeCode().getDisplayName());
    }

    @Override
    protected AuditTransforms<COREEnvelopeBatchSubmission, COREEnvelopeBatchSubmissionResponse> getAuditTransforms() {
        return new X12BatchAuditTransforms();
    }
}
