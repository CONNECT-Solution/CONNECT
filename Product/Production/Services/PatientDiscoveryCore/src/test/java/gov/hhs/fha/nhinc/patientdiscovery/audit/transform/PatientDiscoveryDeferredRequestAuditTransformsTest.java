/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
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
package gov.hhs.fha.nhinc.patientdiscovery.audit.transform;

import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditTransformsConstants;
import gov.hhs.fha.nhinc.patientdiscovery.parser.TestPatientDiscoveryMessageHelper;
import java.net.UnknownHostException;
import java.util.Properties;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class PatientDiscoveryDeferredRequestAuditTransformsTest extends AuditTransformsTest<
    PRPAIN201305UV02, MCCIIN000002UV01> {

    public PatientDiscoveryDeferredRequestAuditTransformsTest() {
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
        PatientDiscoveryDeferredRequestAuditTransforms transforms
            = new PatientDiscoveryDeferredRequestAuditTransforms() {
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
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(TestPatientDiscoveryMessageHelper.
            createPRPAIN201305UV02Request("Gallow", "Younger", "M", "01-12-2967", "1.1", "D123401", "2.2",
                "abd3453dcd24wkkks545"), assertion, createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, Boolean.TRUE, webContextProperties,
            NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);

        testGetEventIdentificationType(auditRequest, NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME,
            Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, webContextProperties, localIp);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, remoteObjectUrl);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertNotNull("ParticipantObjectIdentification is null", auditRequest.getAuditMessage().
            getParticipantObjectIdentification());
        assertFalse("ParticipantObjectIdentification is empty", auditRequest.getAuditMessage().
            getParticipantObjectIdentification().isEmpty());
        assertEquals("ParticipantObjectIdentification is empty", auditRequest.getAuditMessage().
            getParticipantObjectIdentification().size(), 2);
        assertPatientParticipantObjectIdentification(auditRequest.getAuditMessage().getParticipantObjectIdentification()
            .get(0));
        assertQueryParticipantObjectIdentification(auditRequest.getAuditMessage().getParticipantObjectIdentification().
            get(1));
        assertEquals("AuditMessage.Request ServiceName mismatch", auditRequest.getEventType(),
            NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
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
        webContextProperties.setProperty(NhincConstants.INBOUND_REPLY_TO,
            NhincConstants.WSA_REPLY_TO);

        PatientDiscoveryDeferredRequestAuditTransforms transforms
            = new PatientDiscoveryDeferredRequestAuditTransforms() {
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
        LogEventRequestType auditResponse = transforms.transformResponseToAuditMsg(
            TestPatientDiscoveryMessageHelper.createPRPAIN201305UV02Request("Gallow", "Younger", "M", "01-12-2967",
                "1.1", "D123401", "2.2", "abd3453dcd24wkkks545"), new MCCIIN000002UV01(), assertion, createNhinTarget(),
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, Boolean.FALSE,
            webContextProperties, NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);

        testGetEventIdentificationType(auditResponse, NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME,
            Boolean.FALSE);
        testGetActiveParticipantSource(auditResponse, Boolean.FALSE, webContextProperties, remoteIp);
        testGetActiveParticipantDestination(auditResponse, Boolean.FALSE, webContextProperties, wsRequestUrl);
        assertNotNull("ParticipantObjectIdentification is null", auditResponse.getAuditMessage().
            getParticipantObjectIdentification());
        assertFalse("ParticipantObjectIdentification is empty", auditResponse.getAuditMessage().
            getParticipantObjectIdentification().isEmpty());
        assertEquals("ParticipantObjectIdentification should have only one entry for response", auditResponse
            .getAuditMessage().getParticipantObjectIdentification().size(), 1);
        assertQueryParticipantObjectIdentification(auditResponse.getAuditMessage().getParticipantObjectIdentification().
            get(0));
        assertEquals("AuditMessage.Response ServiceName mismatch", auditResponse.getEventType(),
            NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
    }

    private void assertPatientParticipantObjectIdentification(ParticipantObjectIdentificationType participantPatient) {
        assertNotNull("participantPatient is null", participantPatient);
        assertEquals("ParticipantPatient.ParticipantObjectID mismatch", "D123401^^^&1.1&ISO",
            participantPatient.getParticipantObjectID());
        assertSame("ParticipantPatient.ParticipantObjectTypeCode object reference mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            participantPatient.getParticipantObjectTypeCode());
        assertSame("ParticipantPatient.ParticipantObjectTypeCodeRole object reference mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            participantPatient.getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.Code mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            participantPatient.getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.CodeSystemName mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            participantPatient.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.DisplayName mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            participantPatient.getParticipantObjectIDTypeCode().getDisplayName());
        assertNull("ParticipantPatient.ParticipantObjectName is not null",
            participantPatient.getParticipantObjectName());
    }

    private void assertQueryParticipantObjectIdentification(ParticipantObjectIdentificationType participantQuery) {

        assertNotNull("participantQuery is null", participantQuery);
        assertSame("ParticipantQuery.ParticipantObjectTypeCode object reference mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_SYSTEM,
            participantQuery.getParticipantObjectTypeCode());
        assertSame("ParticipantQuery.ParticipantObjectTypeCodeRole object reference mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_TYPE_CODE_ROLE,
            participantQuery.getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantQuery.ParticipantObjectIDTypeCode.Code mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE,
            participantQuery.getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantQuery.ParticipantObjectIDTypeCode.CodeSystemName mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_CODE_SYSTEM,
            participantQuery.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantQuery.ParticipantObjectIDTypeCode.DisplayName mismatch",
            PatientDiscoveryAuditTransformsConstants.PARTICIPANT_QUERYPARAMS_OBJ_ID_TYPE_DISPLAY_NAME,
            participantQuery.getParticipantObjectIDTypeCode().getDisplayName());
        assertNull("ParticipantQuery.ParticipantObjectName is not null",
            participantQuery.getParticipantObjectName());
        assertNotNull("ParticipantQuery.ParticipantObjectQuery is null",
            participantQuery.getParticipantObjectQuery());
    }

    @Override
    protected AuditTransforms<PRPAIN201305UV02, MCCIIN000002UV01> getAuditTransforms() {
        return new PatientDiscoveryDeferredRequestAuditTransforms();
    }
}
