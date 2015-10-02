/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
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
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class DocSubmissionAuditTransformsTest extends AuditTransformsTest<
    ProvideAndRegisterDocumentSetRequestType, RegistryResponseType> {

    private final String SUBMISSION_SET_UNIQUE_ID_SCHEME = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
    private final String PATIENT_ID_SCHEME = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    private final String JAXB_HL7_CONTEXT_NAME = "oasis.names.tc.ebxml_regrep.xsd.lcm._3";
    private final String PATIENT_ID = "D123401^^^&1.1&ISO";
    private final String SUBMISSION_SET_UNIQUE_ID = "1.3.6.1.4.1.21367.2005.3.9999.33";
    private final String USER_ID = "connectUser";
    private final String ROLE_CODE = "RoleCode";
    private final String ROLE_CODE_SYSTEM = "RoleCodeSystem";
    private final String HUMAN_REQUESTOR = "Human Requestor";

    @Test
    public void transformRequestToAuditMsg() throws ConnectionManagerException, UnknownHostException, JAXBException {
        final String localIP = "10.10.10.10";
        final String destinationIP = "16.14.13.12";
        final String soapUIEndpoint = "http://" + destinationIP + ":9090/AuditService";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, soapUIEndpoint);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, destinationIP);
        DocSubmissionAuditTransforms transforms = new DocSubmissionAuditTransforms() {
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
                return soapUIEndpoint;
            }
        };

        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();
        //TODO this needs to be removed ..only for dev
        StringWriter sw = new StringWriter();
        getMarshaller().marshal(request.getSubmitObjectsRequest(), sw);
        System.out.println(sw.toString());

        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
            Boolean.TRUE, webContextProperties, NhincConstants.XDR_REQUEST_ACTION);
        assertEventIdentificationTypeForRequest(auditRequest.getAuditMessage());
        assertActiveParticipantHumanRequestor(auditRequest.getAuditMessage());
        assertActiveParticipantSource(auditRequest.getAuditMessage());
        assertActiveParticipantDestinationForRequest(auditRequest.getAuditMessage(), soapUIEndpoint, destinationIP);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, webContextProperties, localIP);
        assertParticipantObjectIdentification(auditRequest.getAuditMessage());
    }

    @Test
    public void transformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        final String localIP = "10.10.10.10";
        final String destinationIP = "16.14.13.12";
        final String soapUIEndpoint = "http://" + destinationIP + ":9090/AuditService";
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, soapUIEndpoint);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, destinationIP);
        DocSubmissionAuditTransforms transforms = new DocSubmissionAuditTransforms() {
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
                return soapUIEndpoint;
            }
        };

        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();
        RegistryResponseType response = new RegistryResponseType();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditResponse = transforms.transformResponseToAuditMsg(request, response, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
            Boolean.TRUE, webContextProperties, NhincConstants.XDR_RESPONSE_ACTION);
        assertEventIdentificationTypeForResponse(auditResponse.getAuditMessage());
        testGetActiveParticipantSource(auditResponse, Boolean.TRUE, webContextProperties, localIP);
        assertActiveParticipantDestinationForResponse(auditResponse.getAuditMessage(), soapUIEndpoint, destinationIP);
        assertParticipantObjectIdentification(auditResponse.getAuditMessage());
    }

    @Override
    protected AuditTransforms<ProvideAndRegisterDocumentSetRequestType, RegistryResponseType> getAuditTransforms() {
        return new DocSubmissionAuditTransforms();
    }

    private void assertEventIdentificationTypeForRequest(AuditMessageType auditMsg) {
        EventIdentificationType eventType = auditMsg.getEventIdentification();
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_SOURCE, eventType.getEventActionCode());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_SOURCE, eventType.getEventID().getCode());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_SYSTEM,
            eventType.getEventID().getCodeSystemName());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_SOURCE,
            eventType.getEventID().getDisplayName());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE,
            eventType.getEventTypeCode().get(0).getCode());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM,
            eventType.getEventTypeCode().get(0).getCodeSystemName());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME,
            eventType.getEventTypeCode().get(0).getDisplayName());
    }

    private void assertActiveParticipantHumanRequestor(AuditMessageType auditMsg) {
        ActiveParticipant human = getActiveParticipant(HUMAN_REQUESTOR, auditMsg.getActiveParticipant());
        assertEquals(USER_ID, human.getUserID());
        assertEquals(Boolean.TRUE, human.isUserIsRequestor());
        assertEquals(ROLE_CODE, human.getRoleIDCode().get(0).getCode());
        assertEquals(ROLE_CODE_SYSTEM, human.getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(HUMAN_REQUESTOR, human.getRoleIDCode().get(0).getDisplayName());
    }

    private void assertActiveParticipantSource(AuditMessageType auditMsg) {
        ActiveParticipant source = getActiveParticipant(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME, auditMsg.getActiveParticipant());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE, source.getUserID());
        assertEquals(Boolean.TRUE, source.isUserIsRequestor());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE,
            source.getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            source.getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME,
            source.getRoleIDCode().get(0).getDisplayName());
    }

    private void assertActiveParticipantDestinationForRequest(AuditMessageType auditMsg, String remoteObjectIP,
        String destinationIP) {
        ActiveParticipant dest = getActiveParticipant(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME,
            auditMsg.getActiveParticipant());
        assertEquals(dest.getUserID(), remoteObjectIP);
        assertEquals(Boolean.FALSE, dest.isUserIsRequestor());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST,
            dest.getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            dest.getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME,
            dest.getRoleIDCode().get(0).getDisplayName());
        assertEquals(destinationIP, dest.getNetworkAccessPointID());
        assertEquals(new Long(2), new Long(dest.getNetworkAccessPointTypeCode()));
    }

    private void assertActiveParticipantDestinationForResponse(AuditMessageType auditMsg, String remoteObjectIP,
        String destinationIP) {
        ActiveParticipant dest = getActiveParticipant(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME,
            auditMsg.getActiveParticipant());
        //assertNotNull(dest.getAlternativeUserID());
        //assertEquals(ManagementFactory.getRuntimeMXBean().getName(), dest.getAlternativeUserID());
        assertActiveParticipantDestinationForRequest(auditMsg, remoteObjectIP, destinationIP);
    }

    private void assertParticipantObjectIdentification(AuditMessageType auditMsg) {
        assertEquals(PATIENT_ID, auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectID());
        assertSame(DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectTypeCode());
        assertSame(DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectTypeCodeRole());
        assertEquals(DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().getCode());
        assertEquals(DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals(DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().getDisplayName());
        assertEquals(SUBMISSION_SET_UNIQUE_ID, auditMsg.getParticipantObjectIdentification().get(1).
            getParticipantObjectID());

        assertSame(DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectTypeCode());
        assertSame(DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_ROLE,
            auditMsg.getParticipantObjectIdentification().get(1).
            getParticipantObjectTypeCodeRole());
        assertEquals(DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().getCode());
        assertEquals(DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals(DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_DISPLAY_NAME,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().getDisplayName());
    }

    private void assertEventIdentificationTypeForResponse(AuditMessageType auditMsg) {
        EventIdentificationType eventType = auditMsg.getEventIdentification();
        /*assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_RECIPIENT, eventType.getEventActionCode());
         assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_RCEIPIENT,
         eventType.getEventID().getCode
         assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_RECIPIENT,
         eventType.getEventID().getDisplayName());*/
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_SYSTEM,
            eventType.getEventID().getCodeSystemName());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE,
            eventType.getEventTypeCode().get(0).getCode());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM,
            eventType.getEventTypeCode().get(0).getCodeSystemName());
        assertEquals(DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME,
            eventType.getEventTypeCode().get(0).getDisplayName());
    }

    @Override
    protected AssertionType createAssertion() {
        AssertionType assertion = new AssertionType();
        UserType userType = new UserType();
        userType.setOrg(createHomeCommunityType());
        userType.setPersonName(createPersonNameType());
        userType.setRoleCoded(createRoleCodedCeType());
        userType.setUserName(USER_ID);
        assertion.setUserInfo(userType);
        return assertion;
    }

    private CeType createRoleCodedCeType() {
        CeType ceType = new CeType();
        ceType.setCode(ROLE_CODE);
        ceType.setCodeSystemName(ROLE_CODE_SYSTEM);
        ceType.setCodeSystemVersion("1.1");
        ceType.setDisplayName(HUMAN_REQUESTOR);
        return ceType;
    }

    private HomeCommunityType createTragetHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("2.2");
        homeCommunityType.setName("SSA");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    private ProvideAndRegisterDocumentSetRequestType createProvideAndRegisterDocumentSetRequestType() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitObjReq = new SubmitObjectsRequest();
        submitObjReq.setRegistryObjectList(new RegistryObjectListType());
        request.setSubmitObjectsRequest(submitObjReq);
        JAXBElement<RegistryObjectType> registryPackage = createIdentifiable(new RegistryObjectType());
        registryPackage.getValue().getExternalIdentifier().add(createExternalIdentifier(PATIENT_ID_SCHEME, PATIENT_ID,
            DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_PATIENT_ID));
        registryPackage.getValue().getExternalIdentifier().add(createExternalIdentifier(SUBMISSION_SET_UNIQUE_ID_SCHEME,
            SUBMISSION_SET_UNIQUE_ID, DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_UNIQUE_ID));

        request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().add(registryPackage);
        return request;
    }

    private JAXBElement<RegistryObjectType> createIdentifiable(RegistryObjectType registryObjectType) {
        JAXBElement<RegistryObjectType> element = new JAXBElement<>(new QName(
            "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage"), RegistryObjectType.class,
            registryObjectType);
        return element;
    }

    private ExternalIdentifierType createExternalIdentifier(String idScheme, String value, String displayName) {
        LocalizedStringType strLocalized = new LocalizedStringType();
        strLocalized.setValue(displayName);
        InternationalStringType strInternational = new InternationalStringType();
        strInternational.getLocalizedString().add(strLocalized);
        ExternalIdentifierType extIdentifierType = new ExternalIdentifierType();
        extIdentifierType.setIdentificationScheme(idScheme);
        extIdentifierType.setValue(value);
        extIdentifierType.setName(strInternational);

        return extIdentifierType;
    }

    private Marshaller getMarshaller() throws JAXBException {
        return new JAXBContextHandler().getJAXBContext(JAXB_HL7_CONTEXT_NAME).createMarshaller();
    }

    private ActiveParticipant getActiveParticipant(String type, List<ActiveParticipant> activeParticipant) {
        for (AuditMessageType.ActiveParticipant item : activeParticipant) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).getDisplayName().
                equals(type)) {
                return item;
            }
        }
        return null;
    }
}
