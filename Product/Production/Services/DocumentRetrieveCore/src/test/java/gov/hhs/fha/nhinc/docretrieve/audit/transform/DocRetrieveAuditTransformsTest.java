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
package gov.hhs.fha.nhinc.docretrieve.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class is designed to test Retrieve Document Service.
 *
 * @author vimehta
 */
public class DocRetrieveAuditTransformsTest extends AuditTransformsTest<RetrieveDocumentSetRequestType, RetrieveDocumentSetResponseType> {

    public DocRetrieveAuditTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
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

        DocRetrieveAuditTransforms transforms = new DocRetrieveAuditTransforms() {
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
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(createRetrieveDocumentSetRequest(),
            assertion, createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, Boolean.TRUE, webContextProperties,
            NhincConstants.DOC_RETRIEVE_SERVICE_NAME);

        testGetEventIdentificationType(auditRequest, NhincConstants.DOC_RETRIEVE_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.FALSE, localIp, webContextProperties, remoteObjectUrl);
        testGetActiveParticipantDestination(auditRequest, Boolean.FALSE, localIp, webContextProperties,
            remoteObjectUrl);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticipantObjectIdentification(auditRequest, assertion);
    }

    @Test
    public void transformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIP = "10.10.10.10";
        final String remoteIp = "16.14.13.12";
        final String remoteObjectUrl = "http://" + remoteIp + ":9090/source/AuditService";
        final String wsRequestUrl = "http://" + remoteIp + ":9090/AuditService";

        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, wsRequestUrl);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, remoteIp);

        DocRetrieveAuditTransforms transforms = new DocRetrieveAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return localIP;
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
        LogEventRequestType auditResponse = transforms.transformResponseToAuditMsg(createRetrieveDocumentSetRequest(),
            createRetrieveDocumentSetResponse(), assertion, createNhinTarget(),
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, Boolean.TRUE,
            webContextProperties, NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
        testGetEventIdentificationType(auditResponse, NhincConstants.DOC_RETRIEVE_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditResponse, Boolean.FALSE, localIP, webContextProperties, remoteObjectUrl);
        testGetActiveParticipantDestination(auditResponse, Boolean.FALSE, localIP, webContextProperties,
            remoteObjectUrl);
        testCreateActiveParticipantFromUser(auditResponse, Boolean.TRUE, assertion);
        assertParticipantObjectIdentification(auditResponse, assertion);
    }

    private void assertParticipantObjectIdentification(LogEventRequestType auditRequest, AssertionType assertion) {
        ParticipantObjectIdentificationType participantPatientObject;

        participantPatientObject = auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0);

        if (assertion.getUniquePatientId() != null && !assertion.getUniquePatientId().isEmpty()
            && assertion.getUniquePatientId().get(0) != null && assertion.getUniquePatientId().get(0).length() > 0) {

            assertParticipantPatientObjectIdentification(participantPatientObject);

            //test for Participant Document Object Identification
            ParticipantObjectIdentificationType participantDocumentObject = auditRequest.getAuditMessage()
                .getParticipantObjectIdentification().get(1);
            assertParticipantDocumentParticipantObjectIdentification(participantDocumentObject);
        } else {
            assertParticipantDocumentParticipantObjectIdentification(participantPatientObject);
        }
    }

    private void assertParticipantDocumentParticipantObjectIdentification(
        ParticipantObjectIdentificationType participantObject) {

        assertSame(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE,
            participantObject.getParticipantObjectTypeCode());
        assertSame(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE_ROLE,
            participantObject.getParticipantObjectTypeCodeRole());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE,
            participantObject.getParticipantObjectIDTypeCode().getCode());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE_SYSTEM,
            participantObject.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_DISPLAY_NAME,
            participantObject.getParticipantObjectIDTypeCode().getDisplayName());
    }

    private void assertParticipantPatientObjectIdentification(ParticipantObjectIdentificationType participantObject) {
        assertEquals("ParticipantPatient.ParticipantObjectID mismatch", "D123401",
            participantObject.getParticipantObjectID());

        // TODO: assertSame vs assertEquals consistency when returning constants
        assertSame("ParticipantPatient.ParticipantObjectTypeCode object reference mismatch",
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE,
            participantObject.getParticipantObjectTypeCode());
        assertSame("ParticipantPatient.ParticipantObjectTypeCodeRole object reference mismatch",
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            participantObject.getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.Code mismatch",
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            participantObject.getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.CodeSystemName mismatch",
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            participantObject.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.DisplayName mismatch",
            DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            participantObject.getParticipantObjectIDTypeCode().getDisplayName());
    }

    private RetrieveDocumentSetRequestType createRetrieveDocumentSetRequest() {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        DocumentRequest docRequest = new DocumentRequest();
        docRequest.setDocumentUniqueId("D123401");
        docRequest.setHomeCommunityId("ihe:homeCommunityID");
        docRequest.setRepositoryUniqueId("ihe:RepositoryUniqueId");
        request.getDocumentRequest().add(docRequest);
        return request;
    }

    private RetrieveDocumentSetResponseType createRetrieveDocumentSetResponse() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        DocumentResponse docResponse = new DocumentResponse();
        docResponse.setDocumentUniqueId("D123401");
        docResponse.setHomeCommunityId("ihe:homeCommunityID");
        docResponse.setRepositoryUniqueId("ihe:RepositoryUniqueId");
        response.getDocumentResponse().add(docResponse);
        return response;
    }

    @Override
    protected AuditTransforms<RetrieveDocumentSetRequestType, RetrieveDocumentSetResponseType> getAuditTransforms() {
        return new DocRetrieveAuditTransforms();
    }

    /**
     * This method is override in this test class in order to check RD specific testing
     *
     * @param request
     * @param isRequesting
     * @param localIp
     * @param webContextProperties
     * @param remoteObjectUrl
     * @throws UnknownHostException
     */
    protected void testGetActiveParticipantSource(LogEventRequestType request, Boolean isRequesting, String localIp,
        Properties webContextProperties, String remoteObjectUrl) throws UnknownHostException {

        ActiveParticipant sourceActiveParticipant = null;
        List<ActiveParticipant> activeParticipant = request.getAuditMessage().getActiveParticipant();
        for (ActiveParticipant item : activeParticipant) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).getDisplayName()
                .equals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME)) {

                sourceActiveParticipant = item;
            }
        }

        assertNotNull("destinationActiveParticipant is null", sourceActiveParticipant);

        if (isRequesting) {
            assertEquals(remoteObjectUrl, sourceActiveParticipant.getUserID());
            assertEquals(ManagementFactory.getRuntimeMXBean().getName(),
                sourceActiveParticipant.getAlternativeUserID());
            assertEquals(localIp, sourceActiveParticipant.getNetworkAccessPointID());
        } else {
            assertEquals(webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS),
                sourceActiveParticipant.getNetworkAccessPointID());
        }

        assertEquals(isRequesting, sourceActiveParticipant.isUserIsRequestor());
        assertEquals(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME, sourceActiveParticipant
            .getNetworkAccessPointTypeCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE, sourceActiveParticipant
            .getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME, sourceActiveParticipant
            .getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME, sourceActiveParticipant
            .getRoleIDCode().get(0).getDisplayName());
    }

    /**
     * This method is override in this test class in order to check RD specific testing
     *
     * @param request
     * @param isRequesting
     * @param localIP
     * @param webContextProperties
     * @param remoteObjectIP
     */
    protected void testGetActiveParticipantDestination(LogEventRequestType request, Boolean isRequesting,
        String localIP, Properties webContextProperties, String remoteObjectIP) {

        ActiveParticipant destinationActiveParticipant = null;
        for (ActiveParticipant item : request.getAuditMessage().getActiveParticipant()) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).getDisplayName()
                .equals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME)) {

                destinationActiveParticipant = item;
            }
        }

        assertNotNull("destinationActiveParticipant is null", destinationActiveParticipant);

        if (!isRequesting) {
            assertEquals(NhincConstants.WSA_REPLY_TO,
                destinationActiveParticipant.getUserID());
        } else {
            assertEquals(webContextProperties.getProperty(NhincConstants.WEB_SERVICE_REQUEST_URL),
                destinationActiveParticipant.getUserID());
        }

        assertEquals(!isRequesting, destinationActiveParticipant.isUserIsRequestor());
        assertEquals(localIP, destinationActiveParticipant.getNetworkAccessPointID());
        assertEquals(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME, destinationActiveParticipant.
            getNetworkAccessPointTypeCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST, destinationActiveParticipant.
            getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME, destinationActiveParticipant.
            getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME,
            destinationActiveParticipant.getRoleIDCode().get(0).getDisplayName());
    }
}
