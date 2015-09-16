/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrieve.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import java.net.UnknownHostException;
import java.util.Properties;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.lang.management.ManagementFactory;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;

/**
 * This class is designed to test Retrieve Document Service based on @link
 * <a href="https://cgiinterop.atlassian.net/wiki/pages/viewpage.action?pageId=18776072"> </a>.
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
        final String localIP = "10.10.10.10";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, "http://16.14.13.12:9090/AuditService");
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, "16.14.13.12");
        final String remoteObjectIP = "http://16.14.13.12:9090/AuditService";
        DocRetrieveAuditTransforms transforms = new DocRetrieveAuditTransforms() {
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

        RetrieveDocumentSetRequestType request = createRetrieveDocumentSetRequest();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion, createNhinTarget(),
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, Boolean.TRUE,
            webContextProperties, NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
        testGetEventIdentificationType(auditRequest, NhincConstants.DOC_RETRIEVE_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.FALSE, localIP, webContextProperties, remoteObjectIP);
        testGetActiveParticipantDestination(auditRequest, Boolean.FALSE, localIP, webContextProperties, remoteObjectIP);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticiopantObjectIdentification(auditRequest, assertion);
    }

    @Test
    public void transformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIP = "10.10.10.10";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, "http://16.14.13.12:9090/AuditService");
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, "16.14.13.12");
        final String remoteObjectIP = "http://16.14.13.12:9090/AuditService";
        DocRetrieveAuditTransforms transforms = new DocRetrieveAuditTransforms() {
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

        RetrieveDocumentSetRequestType request = createRetrieveDocumentSetRequest();
        RetrieveDocumentSetResponseType response = createRetrieveDocumentSetResponse();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditResponse = transforms.transformResponseToAuditMsg(request, response, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
            Boolean.TRUE, webContextProperties, NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
        testGetEventIdentificationType(auditResponse, NhincConstants.DOC_RETRIEVE_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditResponse, Boolean.FALSE, localIP, webContextProperties, remoteObjectIP);
        testGetActiveParticipantDestination(auditResponse, Boolean.FALSE, localIP, webContextProperties, remoteObjectIP);
        testCreateActiveParticipantFromUser(auditResponse, Boolean.TRUE, assertion);
        assertParticiopantObjectIdentification(auditResponse, assertion);
    }

    private void assertParticiopantObjectIdentification(LogEventRequestType auditRequest, AssertionType assertion) {
        ParticipantObjectIdentificationType participantPatientObject;

        if (assertion.getUniquePatientId() != null && assertion.getUniquePatientId().size() > 0 && assertion.getUniquePatientId().get(0).length() > 0) {

            participantPatientObject = auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0);
            assertParticipantPatientObjectIdentification(participantPatientObject);

            //test for Participant Document Object Identification
            ParticipantObjectIdentificationType participantDocumentObject = auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1);
            assertParticipantDocumentParticipantObjectIdentification(participantDocumentObject);
        } else {
            participantPatientObject = auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0);
            assertParticipantDocumentParticipantObjectIdentification(participantPatientObject);

        }

    }

    private void assertParticipantDocumentParticipantObjectIdentification(ParticipantObjectIdentificationType participantObject) {
        assertSame(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE,
            participantObject.getParticipantObjectTypeCode());
        assertSame(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_TYPE_CODE_ROLE,
            participantObject.getParticipantObjectTypeCodeRole());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE,
            participantObject.getParticipantObjectIDTypeCode().
            getCode());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_CODE_SYSTEM,
            participantObject.getParticipantObjectIDTypeCode().
            getCodeSystemName());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_DOCUMENT_OBJ_ID_TYPE_DISPLAY_NAME,
            participantObject.getParticipantObjectIDTypeCode().
            getDisplayName());
    }

    private void assertParticipantPatientObjectIdentification(ParticipantObjectIdentificationType participantObject) {
        assertEquals("D123401",
            participantObject.getParticipantObjectID());
        assertSame(DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE,
            participantObject.getParticipantObjectTypeCode());
        assertSame(DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            participantObject.
            getParticipantObjectTypeCodeRole());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            participantObject.
            getParticipantObjectIDTypeCode().getCode());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            participantObject.getParticipantObjectIDTypeCode().
            getCodeSystemName());
        assertEquals(DocRetrieveAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            participantObject.getParticipantObjectIDTypeCode().
            getDisplayName());
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
     * @param localIP
     * @param webContextProperties
     * @param remoteObjectIP
     * @throws UnknownHostException
     */
    protected void testGetActiveParticipantSource(LogEventRequestType request, Boolean isRequesting, String localIP,
        Properties webContextProperties, String remoteObjectIP) throws UnknownHostException {

        ActiveParticipant sourceActiveParticipant = null;
        List<ActiveParticipant> activeParticipant = request.getAuditMessage().getActiveParticipant();
        for (ActiveParticipant item : activeParticipant) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).getDisplayName().
                equals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME)) {
                sourceActiveParticipant = item;
            }
        }

        if (isRequesting) {
            assertEquals(remoteObjectIP, sourceActiveParticipant.getUserID());
        }

        if (isRequesting) {
            assertEquals(ManagementFactory.getRuntimeMXBean().getName(), sourceActiveParticipant.getAlternativeUserID());
        }
        assertEquals(isRequesting, sourceActiveParticipant.isUserIsRequestor());
        if (isRequesting) {
            assertEquals(localIP, sourceActiveParticipant.getNetworkAccessPointID());
        } else {
            assertEquals(webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS),
                sourceActiveParticipant.getNetworkAccessPointID());
        }
        assertEquals(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME,
            sourceActiveParticipant.getNetworkAccessPointTypeCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE,
            sourceActiveParticipant.getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            sourceActiveParticipant.getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME,
            sourceActiveParticipant.getRoleIDCode().get(0).getDisplayName());
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
    protected void testGetActiveParticipantDestination(LogEventRequestType request, Boolean isRequesting, String localIP,
        Properties webContextProperties, String remoteObjectIP) {
        ActiveParticipant destinationActiveParticipant = null;
        for (ActiveParticipant item : request.getAuditMessage().getActiveParticipant()) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).
                getDisplayName().equals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME)) {
                destinationActiveParticipant = item;
            }
        }
        if (!isRequesting) {
            assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE, destinationActiveParticipant.getUserID());
        } else {
            assertEquals(webContextProperties.getProperty(NhincConstants.WEB_SERVICE_REQUEST_URL),
                destinationActiveParticipant.getUserID());
        }

        assertEquals(!(isRequesting), destinationActiveParticipant.isUserIsRequestor());
        assertEquals(localIP,
            destinationActiveParticipant.getNetworkAccessPointID());
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
