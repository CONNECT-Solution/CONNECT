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
package gov.hhs.fha.nhinc.docquery.audit.transform;

import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docquery.audit.DocQueryAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.junit.AfterClass;
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
public class DocQueryAuditTransformsTest extends AuditTransformsTest<AdhocQueryRequest, AdhocQueryResponse> {

    private final String REMOTE_HCID_WITHOUT_PREFIX = "2.2";
    private final String REMOTE_HCID_WITH_PREFIX = "urn:oid:2.2";
    private final String LOCAL_HCID_WITH_PREFIX = "urn:oid:1.1";
    private final String LOCAL_IP = "10.10.10.10";
    private final String REMOTE_IP = "16.14.13.12";
    private final String REMOTE_OBJECT_URL = "http://" + REMOTE_IP + ":9090/source/AuditService";
    private final String WS_REEQUEST_URL = "http://" + REMOTE_IP + ":9090/AuditService";

    public DocQueryAuditTransformsTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testRequestAuditMsgHCIDWithOutPrefix() throws ConnectionManagerException, UnknownHostException,
        CertificateManagerException {
        transformRequestToAuditMsg(createAdhocQueryRequest(), createNhinTarget(REMOTE_HCID_WITHOUT_PREFIX),
            REMOTE_HCID_WITH_PREFIX);
    }

    @Test
    public void testRequestAuditMsgHCIDWithPrefix() throws ConnectionManagerException, UnknownHostException,
        CertificateManagerException {
        transformRequestToAuditMsg(createAdhocQueryRequest(), createNhinTarget(REMOTE_HCID_WITH_PREFIX),
            REMOTE_HCID_WITH_PREFIX);
    }

    @Test
    public void testResponseAuditMsgHCIDWithOutPrefix() throws ConnectionManagerException, UnknownHostException {
        transformResponseToAuditMsg(createAdhocQueryRequest(), createAdhocQueryResponse(), LOCAL_HCID_WITH_PREFIX);
    }

    @Test
    public void testResponseAuditMsgHCIDWithPrefix() throws ConnectionManagerException, UnknownHostException {
        transformResponseToAuditMsg(createAdhocQueryRequest(), createAdhocQueryResponse(), LOCAL_HCID_WITH_PREFIX);
    }

    private void transformRequestToAuditMsg(AdhocQueryRequest request, NhinTargetSystemType target, String hcid) throws
        ConnectionManagerException, UnknownHostException, CertificateManagerException {

        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, WS_REEQUEST_URL);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, REMOTE_IP);
        DocQueryAuditTransforms transforms = new DocQueryAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return LOCAL_IP;
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
                return REMOTE_OBJECT_URL;
            }
        };

        AssertionType assertion = createAssertion();

        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion, target,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null,
            NhincConstants.DOC_QUERY_SERVICE_NAME);

        testGetEventIdentificationType(auditRequest, NhincConstants.DOC_QUERY_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, webContextProperties, LOCAL_IP);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, REMOTE_OBJECT_URL);
        testAuditSourceIdentification(auditRequest.getAuditMessage().getAuditSourceIdentification(), assertion);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticipantObjectIdentification(auditRequest, hcid);
        assertEquals("AuditMessage.Request ServiceName mismatch", auditRequest.getEventType(),
            NhincConstants.DOC_QUERY_SERVICE_NAME);
    }

    private void transformResponseToAuditMsg(AdhocQueryRequest request, AdhocQueryResponse response, String hcid) throws
        ConnectionManagerException, UnknownHostException {
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, WS_REEQUEST_URL);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, REMOTE_IP);

        DocQueryAuditTransforms transforms = new DocQueryAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return LOCAL_IP;
            }

            @Override
            protected String getRemoteHostAddress(Properties webContextProperties) {
                if (webContextProperties != null && !webContextProperties.isEmpty() && webContextProperties.
                    getProperty(NhincConstants.REMOTE_HOST_ADDRESS) != null) {

                    return webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
                }
                return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return REMOTE_OBJECT_URL;
            }
        };

        AssertionType assertion = createAssertion();
        LogEventRequestType auditResponse = transforms.transformResponseToAuditMsg(request, response, assertion, null,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.FALSE,
            webContextProperties, NhincConstants.DOC_QUERY_SERVICE_NAME);

        testGetEventIdentificationType(auditResponse, NhincConstants.DOC_QUERY_SERVICE_NAME, Boolean.FALSE);
        testAuditSourceIdentification(auditResponse.getAuditMessage().getAuditSourceIdentification(), assertion);
        testGetActiveParticipantSource(auditResponse, Boolean.FALSE, webContextProperties, LOCAL_IP);
        testGetActiveParticipantDestination(auditResponse, Boolean.FALSE, webContextProperties, REMOTE_OBJECT_URL);
        assertParticipantObjectIdentification(auditResponse, hcid);
        assertEquals("AuditMessage.Response ServiceName mismatch", auditResponse.getEventType(),
            NhincConstants.DOC_QUERY_SERVICE_NAME);
    }

    private void assertParticipantObjectIdentification(LogEventRequestType auditRequest, String hcid) {
        assertNotNull("auditRequest is null", auditRequest);
        assertNotNull("AuditMessage is null", auditRequest.getAuditMessage());
        assertNotNull("ParticipantObjectIdentification is null", auditRequest.getAuditMessage()
            .getParticipantObjectIdentification());
        assertFalse("ParticipantObjectIdentification list is missing test values", auditRequest.getAuditMessage()
            .getParticipantObjectIdentification().size() < 2);

        ParticipantObjectIdentificationType participantPatient = auditRequest.getAuditMessage()
            .getParticipantObjectIdentification().get(0);
        ParticipantObjectIdentificationType participantQuery = auditRequest.getAuditMessage()
            .getParticipantObjectIdentification().get(1);

        assertEquals("ParticipantPatient.ParticipantObjectID mismatch", "D123401^^^&1.1&ISO",
            participantPatient.getParticipantObjectID());

        // TODO: assertSame vs assertEquals consistency when returning constants
        assertSame("ParticipantPatient.ParticipantObjectTypeCode object reference mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            participantPatient.getParticipantObjectTypeCode());
        assertSame("ParticipantPatient.ParticipantObjectTypeCodeRole object reference mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            participantPatient.getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.Code mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            participantPatient.getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.CodeSystemName mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            participantPatient.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantPatient.ParticipantObjectIDTypeCode.DisplayName mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            participantPatient.getParticipantObjectIDTypeCode().getDisplayName());

        assertSame("ParticipantQuery.ParticipantObjectTypeCode object reference mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_SYSTEM,
            participantQuery.getParticipantObjectTypeCode());
        assertSame("ParticipantQuery.ParticipantObjectTypeCodeRole object reference mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_ROLE,
            participantQuery.getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantQuery.ParticipantObjectIDTypeCode.Code mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE,
            participantQuery.getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantQuery.ParticipantObjectIDTypeCode.CodeSystemName mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE_SYSTEM,
            participantQuery.getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantQuery.ParticipantObjectIDTypeCode.DisplayName mismatch",
            DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_DISPLAY_NAME,
            participantQuery.getParticipantObjectIDTypeCode().getDisplayName());
        assertNull("ParticipantPatient.ParticipantObjectName is not null",
            participantPatient.getParticipantObjectName());
        assertNull("ParticipantQuery.ParticipantObjectName is not null",
            participantQuery.getParticipantObjectName());
        assertNotNull("ParticipantQuery.ParticipantObjectQuery is null",
            participantQuery.getParticipantObjectQuery());
        assertEquals("ParticipantQuery.ParticipantObjectDetail HomeCommunityId mismatch", hcid,
            new String(participantQuery.getParticipantObjectDetail().get(1).getValue()));
    }

    private AdhocQueryResponse createAdhocQueryResponse() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        response.setRegistryObjectList(new RegistryObjectListType());
        response.getRegistryObjectList().getIdentifiable().add(createIdentifiable(new RegistryObjectType()));
        return response;
    }

    private AdhocQueryRequest createAdhocQueryRequest() {
        ValueListType val = new ValueListType();
        val.getValue().add("D123401^^^&1.1&ISO");

        SlotType1 pidSlot = new SlotType1();
        pidSlot.setName(DocQueryAuditTransformsConstants.XDS_DOCUMENT_ENTRY_PATIENT_ID);
        pidSlot.setValueList(val);

        AdhocQueryRequest request = new AdhocQueryRequest();
        request.setAdhocQuery(new AdhocQueryType());
        request.getAdhocQuery().getSlot().add(pidSlot);
        request.getAdhocQuery().setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
        return request;
    }

    /**
     * @param registryObjectType
     * @return
     */
    private JAXBElement<RegistryObjectType> createIdentifiable(RegistryObjectType registryObjectType) {
        return new JAXBElement<>(new QName(
            "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "Identifiable"), RegistryObjectType.class,
            registryObjectType);
    }

    @Override
    protected AuditTransforms<AdhocQueryRequest, AdhocQueryResponse> getAuditTransforms() {
        return new DocQueryAuditTransforms();
    }

    private NhinTargetSystemType createNhinTarget(String hcid) {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setHomeCommunity(createTargetHomeCommunityType(hcid));
        return targetSystem;
    }

    private HomeCommunityType createTargetHomeCommunityType(String hcid) {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId(hcid);
        homeCommunityType.setName("SSA");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }
}
