/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquery.audit.transform;

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
import static org.junit.Assert.assertSame;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class DocQueryAuditTransformsTest extends AuditTransformsTest<AdhocQueryRequest, AdhocQueryResponse> {

    public DocQueryAuditTransformsTest() {
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
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL,
            "http://16.14.13.12:9090/AuditService");
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, "16.14.13.12");
        final String remoteObjectIP = "http://16.14.13.12:9090/source/AuditService";
        DocQueryAuditTransforms transforms = new DocQueryAuditTransforms() {
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

        AdhocQueryRequest request = createAdhocQueryRequest();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
            Boolean.TRUE, webContextProperties, NhincConstants.DOC_QUERY_SERVICE_NAME);
        testGetEventIdentificationType(auditRequest, NhincConstants.DOC_QUERY_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, localIP, webContextProperties);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, remoteObjectIP);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticiopantObjectIdentification(auditRequest);
    }

    @Test
    public void transformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIP = "10.10.10.10";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL,
            "http://16.14.13.12:9090/AuditService");
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, "16.14.13.12");
        final String remoteObjectIP = "http://16.14.13.12:9090/source/AuditService";
        DocQueryAuditTransforms transforms = new DocQueryAuditTransforms() {
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

        AdhocQueryRequest request = createAdhocQueryRequest();
        AdhocQueryResponse response = createAdhocQueryResponse();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformResponseToAuditMsg(request, response, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
            Boolean.TRUE, webContextProperties, NhincConstants.DOC_QUERY_SERVICE_NAME);
        testGetEventIdentificationType(auditRequest, NhincConstants.DOC_QUERY_SERVICE_NAME, Boolean.TRUE);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, localIP, webContextProperties);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, webContextProperties, remoteObjectIP);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        assertParticiopantObjectIdentification(auditRequest);
    }

    private void assertParticiopantObjectIdentification(LogEventRequestType auditRequest) {
        assertEquals("D123401^^^&1.1&ISO",
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).getParticipantObjectID());
        assertSame(DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectTypeCode());
        assertSame(DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectTypeCodeRole());
        assertEquals(DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectIDTypeCode().getCode());
        assertEquals(DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals(DocQueryAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(0).
            getParticipantObjectIDTypeCode().getDisplayName());
        assertSame(DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).getParticipantObjectTypeCode());
        assertSame(DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_TYPE_CODE_ROLE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).
            getParticipantObjectTypeCodeRole());
        assertEquals(DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).
            getParticipantObjectIDTypeCode().getCode());
        assertEquals(DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_CODE_SYSTEM,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).
            getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals(DocQueryAuditTransformsConstants.PARTICIPANT_QUERY_OBJ_ID_TYPE_DISPLAY_NAME,
            auditRequest.getAuditMessage().getParticipantObjectIdentification().get(1).
            getParticipantObjectIDTypeCode().getDisplayName());
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

    private AdhocQueryResponse createAdhocQueryResponse() {
        AdhocQueryResponse response = new AdhocQueryResponse();
        RegistryObjectListType registryList = new RegistryObjectListType();
        response.setRegistryObjectList(registryList);
        response.getRegistryObjectList().getIdentifiable().add(createIdentifiable(new RegistryObjectType()));
        return response;
    }

    private AdhocQueryRequest createAdhocQueryRequest() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AdhocQueryType queryType = new AdhocQueryType();
        request.setAdhocQuery(queryType);
        SlotType1 pidSlot = new SlotType1();
        pidSlot.setName(DocQueryAuditTransformsConstants.XDS_DOCUMENT_ENTRY_PATIENT_ID);
        ValueListType val = new ValueListType();
        val.getValue().add("D123401^^^&1.1&ISO");
        pidSlot.setValueList(val);
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
}
