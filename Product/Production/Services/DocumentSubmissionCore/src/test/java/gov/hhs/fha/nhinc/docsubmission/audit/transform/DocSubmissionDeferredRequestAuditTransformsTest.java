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
package gov.hhs.fha.nhinc.docsubmission.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class DocSubmissionDeferredRequestAuditTransformsTest extends AuditTransformsTest<
    ProvideAndRegisterDocumentSetRequestType, XDRAcknowledgementType> {

    private final String SUBMISSION_SET_UNIQUE_ID_SCHEME = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
    private final String PATIENT_ID_SCHEME = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    private final String PATIENT_ID = "D123401^^^&1.1&ISO";
    private final String SUBMISSION_SET_UNIQUE_ID = "1.3.6.1.4.1.21367.2005.3.9999.33";
    private final String LOCAL_IP = "10.10.10.10";
    private final String REMOTE_IP = "16.14.13.12";
    private final String WS_REQUEST_URL = "http://" + REMOTE_IP + ":9090/AuditService";

    public DocSubmissionDeferredRequestAuditTransformsTest() {
    }

    @Test
    public void transformRequestToAuditMsg() throws ConnectionManagerException, UnknownHostException, JAXBException {
        DocSubmissionAuditTransforms transforms = new DocSubmissionAuditTransforms() {
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
                return WS_REQUEST_URL;
            }
        };

        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();

        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.TRUE, null, NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
        testGetEventIdentificationType(auditRequest, NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME, Boolean.TRUE);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, null, WS_REQUEST_URL, REMOTE_IP);
        testAuditSourceIdentification(auditRequest.getAuditMessage().getAuditSourceIdentification(), assertion);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, null, LOCAL_IP);
        assertParticipantObjectIdentification(auditRequest.getAuditMessage());
        assertEquals("AuditMessage.Request direction mismatch", auditRequest.getDirection(),
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        assertEquals("AuditMessage.Request ServiceName mismatch", auditRequest.getEventType(),
            NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
    }

    @Test
    public void transformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {
        Properties webContextProperties = new Properties();
        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, WS_REQUEST_URL);
        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, REMOTE_IP);
        webContextProperties.setProperty(NhincConstants.INBOUND_REPLY_TO, NhincConstants.WSA_REPLY_TO);
        DocSubmissionAuditTransforms transforms = new DocSubmissionAuditTransforms() {
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
                return WS_REQUEST_URL;
            }
        };

        ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequestType();
        RegistryResponseType response = new RegistryResponseType();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditResponse = transforms.transformResponseToAuditMsg(request, response, assertion, null,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.FALSE, webContextProperties, NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
        testGetEventIdentificationType(auditResponse, NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME, Boolean.FALSE);
        testGetActiveParticipantDestination(auditResponse, Boolean.FALSE, webContextProperties, WS_REQUEST_URL);
        testAuditSourceIdentification(auditResponse.getAuditMessage().getAuditSourceIdentification(), assertion);
        testGetActiveParticipantSource(auditResponse, Boolean.FALSE, webContextProperties, REMOTE_IP);
        assertParticipantObjectIdentification(auditResponse.getAuditMessage());
        assertEquals("AuditMessage.Response direction mismatch", auditResponse.getDirection(),
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        assertEquals("AuditMessage.Response ServiceName mismatch", auditResponse.getEventType(),
            NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
    }

    @Override
    protected AuditTransforms<ProvideAndRegisterDocumentSetRequestType, XDRAcknowledgementType> getAuditTransforms() {
        return new DocSubmissionDeferredRequestAuditTransforms();
    }

    private void assertParticipantObjectIdentification(AuditMessageType auditMsg) {
        assertEquals("ParticipantObjectIdentification.Patient.ParticipantObjectID mismatch", PATIENT_ID,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectID());
        assertSame("ParticipantObjectIdentification.Patient.ParticipantObjectTypeCode mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectTypeCode());
        assertSame("ParticipantObjectIdentification.Patient.ParticipantObjectTypeCodeRole mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_TYPE_CODE_ROLE,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantObjectIdentification.Patient.ParticipantObjectIDTypeCode mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantObjectIdentification.Patient.ParticipantObjectIDTypeCodeSystem mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantObjectIdentification.Patient.ParticipantObjectIDTypeDisplayName mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_PATIENT_OBJ_ID_TYPE_DISPLAY_NAME,
            auditMsg.getParticipantObjectIdentification().get(0).getParticipantObjectIDTypeCode().getDisplayName());

        assertEquals("ParticipantObjectIdentification.SubmissionSet.ParticipantObjectID mismatch",
            SUBMISSION_SET_UNIQUE_ID, auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectID());
        assertSame("ParticipantObjectIdentification.SubmissionSet.ParticipantObjectTypeCode mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectTypeCode());
        assertSame("ParticipantObjectIdentification.SubmissionSet.ParticipantObjectTypeCodeRole mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_TYPE_CODE_ROLE,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectTypeCodeRole());
        assertEquals("ParticipantObjectIdentification.SubmissionSet.ParticipantObjectIDTypeCode mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().getCode());
        assertEquals("ParticipantObjectIdentification.SubmissionSet.ParticipantObjectIDTypeCodeSystem mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_CODE_SYSTEM,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().getCodeSystemName());
        assertEquals("ParticipantObjectIdentification.SubmissionSet.ParticipantObjectTypeDisplayName mismatch",
            DocSubmissionAuditTransformsConstants.PARTICIPANT_SUBMISSION_SET_OBJ_ID_TYPE_DISPLAY_NAME,
            auditMsg.getParticipantObjectIdentification().get(1).getParticipantObjectIDTypeCode().getDisplayName());
    }

    private ProvideAndRegisterDocumentSetRequestType createProvideAndRegisterDocumentSetRequestType() {
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest submitObjReq = new SubmitObjectsRequest();
        submitObjReq.setRegistryObjectList(new RegistryObjectListType());
        request.setSubmitObjectsRequest(submitObjReq);
        JAXBElement<RegistryPackageType> registryPackage = createIdentifiable(new RegistryPackageType());
        registryPackage.getValue().getExternalIdentifier().add(createExternalIdentifier(PATIENT_ID_SCHEME, PATIENT_ID,
            DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_PATIENT_ID));
        registryPackage.getValue().getExternalIdentifier().add(createExternalIdentifier(SUBMISSION_SET_UNIQUE_ID_SCHEME,
            SUBMISSION_SET_UNIQUE_ID, DocSubmissionAuditTransformsConstants.XDS_SUBMISSIONSET_UNIQUE_ID));

        request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable().add(registryPackage);
        return request;
    }

    private JAXBElement<RegistryPackageType> createIdentifiable(RegistryPackageType registryPackage) {
        JAXBElement<RegistryPackageType> element = new JAXBElement<>(new QName(
            "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage"), RegistryPackageType.class,
            registryPackage);
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

}
