/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

package gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform;

import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12AuditDataTransformConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.net.UnknownHostException;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 *
 * @author achidamb
 */
public class COREX12RealTimeAuditTransformTest extends AuditTransformsTest<COREEnvelopeRealTimeRequest, 
    COREEnvelopeRealTimeResponse> {
    
    @Test
    public void transformRequestToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIP = "10.10.10.10";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, "http://16.14.13.12:9090/AuditService");
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, "16.14.13.12");
        final String remoteObjectIP = "http://16.14.13.12:9090/source/AuditService";
        COREX12RealTimeAuditTransforms transforms = new COREX12RealTimeAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return localIP;
            }

            @Override
            protected String getRemoteHostAddress(Properties webContextProeprties) {
                if (webContextProeprties != null && !webContextProeprties.isEmpty() && webContextProeprties.
                    getProperty(NhincConstants.REMOTE_HOST_ADDRESS) != null) {
                    return webContextProeprties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
                }
                return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return remoteObjectIP;
            }

        };

        COREEnvelopeRealTimeRequest request = createCOREEnvelopeRealTimeRequest();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion, createNhinTarget(),
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE,
            webContextProperties, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME);
        testGetEventIdentificationType(auditRequest, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, localIP, webContextProperties);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, remoteObjectIP);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticiopantObjectIdentification(auditRequest);
    }

    @Test
    public void transformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIP = "10.10.10.10";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, "http://16.14.13.12:9090/AuditService");
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, "16.14.13.12");
        final String remoteObjectIP = "http://16.14.13.12:9090/source/AuditService";
        COREX12RealTimeAuditTransforms transforms = new COREX12RealTimeAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return localIP;
            }

            @Override
            protected String getRemoteHostAddress(Properties webContextProeprties) {
                if (webContextProeprties != null && !webContextProeprties.isEmpty() && webContextProeprties.
                    getProperty(NhincConstants.REMOTE_HOST_ADDRESS) != null) {
                    return webContextProeprties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
                }
                return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return remoteObjectIP;
            }

        };

        COREEnvelopeRealTimeRequest request = createCOREEnvelopeRealTimeRequest();
        COREEnvelopeRealTimeResponse response = createCOREEnvelopeRealTimeResponse();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformResponseToAuditMsg(request, response, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.TRUE, webContextProperties, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME);
        testGetEventIdentificationType(auditRequest, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, localIP, webContextProperties);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, remoteObjectIP);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticiopantObjectIdentification(auditRequest);
    }

    @Override
    protected AuditTransforms<COREEnvelopeRealTimeRequest, COREEnvelopeRealTimeResponse> getAuditTransforms() {
        return new COREX12RealTimeAuditTransforms();
    }
    
    private AssertionType createAssertion() {
        AssertionType assertion = new AssertionType();
        UserType userType = new UserType();
        userType.setOrg(createHomeCommunityType());
        userType.setPersonName(createPersonNameType());
        userType.setRoleCoded(createCeType());
        userType.setUserName("Wanderson");
        assertion.setUserInfo(userType);
        return assertion;
    }
    
    private HomeCommunityType createHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("1.1");
        homeCommunityType.setName("DOD");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    private PersonNameType createPersonNameType() {
        PersonNameType personNameType = new PersonNameType();
        personNameType.setFamilyName("Tamney");
        personNameType.setFullName("Erica");
        personNameType.setGivenName("Jasmine");
        personNameType.setPrefix("Ms");
        return personNameType;
    }

    private CeType createCeType() {
        CeType ceType = new CeType();
        ceType.setCode("Code");
        ceType.setCodeSystem("CodeSystem");
        ceType.setCodeSystemVersion("1.1");
        ceType.setDisplayName("DisplayName");
        return ceType;
    }
    
    private NhinTargetSystemType createNhinTarget() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setHomeCommunity(createTragetHomeCommunityType());
        return targetSystem;
    }

    private HomeCommunityType createTragetHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("2.2");
        homeCommunityType.setName("SSA");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    private COREEnvelopeRealTimeRequest createCOREEnvelopeRealTimeRequest() {
        COREEnvelopeRealTimeRequest request = new COREEnvelopeRealTimeRequest();
        request.setCORERuleVersion("2.2.0");
        request.setPayloadID("5");
        request.setPayloadType("X12_270_Request_005010X279A1");
        request.setProcessingMode("RealTime");
        request.setPayloadID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        request.setSenderID("HospitalA");
        request.setReceiverID("PayerB");
        return request;
    }

    private COREEnvelopeRealTimeResponse createCOREEnvelopeRealTimeResponse() {
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();
        response.setCORERuleVersion("2.2.0");
        response.setPayloadID("5");
        response.setPayloadType("X12_270_Request_005010X279A1");
        response.setProcessingMode("RealTime");
        response.setPayloadID("f81d4fae-7dec-11d0-a765-00a0c91e6bf6");
        response.setSenderID("HospitalA");
        response.setReceiverID("PayerB");
        return response;
    }

    private void assertParticiopantObjectIdentification(LogEventRequestType auditRequest) {
        assertEquals("f81d4fae-7dec-11d0-a765-00a0c91e6bf6",
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        assertSame(CORE_X12AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectTypeCode());
        assertSame(CORE_X12AuditDataTransformConstants.PARTICIPANT_OBJ_TYPE_CODE_ROLE_X12,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectTypeCodeRole());
        assertEquals("f81d4fae-7dec-11d0-a765-00a0c91e6bf6",
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectIDTypeCode().getCode());
        assertEquals(CORE_X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_NAME,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().
            getCodeSystemName());
        assertEquals(CORE_X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_DISPLAY_NAME,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().
            getDisplayName());
    }
}
