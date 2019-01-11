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
package gov.hhs.fha.nhinc.admingui.services;

import static gov.hhs.fha.nhinc.util.StreamUtils.closeStreamSilently;

import gov.hhs.fha.nhinc.admingui.managed.PatientSearchBean;
import gov.hhs.fha.nhinc.admingui.model.Document;
import gov.hhs.fha.nhinc.admingui.model.Patient;
import gov.hhs.fha.nhinc.admingui.services.exception.DocumentMetadataException;
import gov.hhs.fha.nhinc.admingui.services.exception.PatientSearchException;
import gov.hhs.fha.nhinc.admingui.services.impl.DocumentQueryServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.DocumentRetrieveServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.PatientCorrelationServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.PatientServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.admingui.util.XSLTransformHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.docquery.builder.impl.FindDocumentsAdhocQueryRequestBuilder;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResult;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResults;
import gov.hhs.fha.nhinc.docquery.model.builder.impl.DocumentMetadataResultsModelBuilderImpl;
import gov.hhs.fha.nhinc.docretrieve.model.DocumentRetrieve;
import gov.hhs.fha.nhinc.docretrieve.model.DocumentRetrieveResults;
import gov.hhs.fha.nhinc.messaging.builder.AssertionBuilder;
import gov.hhs.fha.nhinc.messaging.builder.impl.AssertionBuilderImpl;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.model.PatientSearchResults;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.II;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton Interface class between UI and the backend services. provides high level APIs for calling PD, DQ, RD etc.
 *
 *
 * @author Naresh Subramanyan
 */
public class GatewayService {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayService.class);

    private final PatientService patientService;
    private final PatientCorrelationService correlationService;
    private final DocumentQueryService documentQueryService;
    private final DocumentRetrieveService documentRetrieveService;
    private final AssertionBuilder assertionBuilder;
    private AssertionType assertion;

    // Should be moved to a constant file later
    public static final String CONTENT_TYPE_IMAGE_PNG = org.springframework.http.MediaType.IMAGE_PNG.toString();
    public static final String CONTENT_TYPE_IMAGE_JPEG = org.springframework.http.MediaType.IMAGE_JPEG.toString();
    public static final String CONTENT_TYPE_IMAGE_GIF = org.springframework.http.MediaType.IMAGE_GIF.toString();

    public static final String CONTENT_TYPE_TEXT_XML = MediaType.TEXT_XML;
    public static final String CONTENT_TYPE_TEXT_PLAIN = MediaType.TEXT_PLAIN;
    public static final String CONTENT_TYPE_TEXT_HTML = MediaType.TEXT_HTML;
    public static final String CONTENT_TYPE_APPLICATION_XML = MediaType.APPLICATION_XML;
    public static final String CONTENT_TYPE_APPLICATION_XHTML_XML = MediaType.APPLICATION_XHTML_XML;
    public static final String CONTENT_TYPE_APPLICATION_OCTET_STREAM = MediaType.APPLICATION_OCTET_STREAM;
    public static final String CONTENT_TYPE_APPLICATION_PDF = "application/pdf";
    public static final String DEFAULT_XSL_FILE = "/WEB-INF/CDA.xsl";
    private final XSLTransformHelper transformer = new XSLTransformHelper();
    private II localCorrelation;

    private GatewayService() {
        // create the Service implementation instances
        patientService = new PatientServiceImpl();
        correlationService = new PatientCorrelationServiceImpl();
        documentQueryService = new DocumentQueryServiceImpl(new FindDocumentsAdhocQueryRequestBuilder(),
            new DocumentMetadataResultsModelBuilderImpl());
        documentRetrieveService = new DocumentRetrieveServiceImpl();
        assertionBuilder = new AssertionBuilderImpl();
        assertionBuilder.build();
        assertion = assertionBuilder.getAssertion();
    }

    private static class SingletonHolder {

        private static final GatewayService INSTANCE = new GatewayService();
    }

    public static GatewayService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Calls the NwHIN Patient Discovery service and returns Patient information
     *
     * @param patientQuerySearch
     *
     * @return true if the patient is found else false
     */
    public boolean discoverPatient(final PatientSearchBean patientQuerySearch) {

        assertion = addUserInfoAndPurposeOf(assertion, patientQuerySearch.getUser(), patientQuerySearch.
            getSelectedPurposeOf(),
            patientQuerySearch.getPurposeOfDescription());

        // Create the patient bean that needs to be passed to the service layer
        final gov.hhs.fha.nhinc.patientdiscovery.model.Patient patientBean
        = new gov.hhs.fha.nhinc.patientdiscovery.model.Patient();
        // set the orgainization
        patientBean.setOrganization(patientQuerySearch.getOrganization());
        // set birth date in YYYYMMDD
        final DateFormat df = new SimpleDateFormat(UTCDateUtil.DATE_ONLY_FORMAT);
        patientBean.setBirthDate(df.format(patientQuerySearch.getDateOfBirth()));
        // set the first name
        patientBean.setFirstName(patientQuerySearch.getFirstName());
        // set last name
        patientBean.setLastName(patientQuerySearch.getLastName());
        // set the gender
        patientBean.setGender(patientQuerySearch.getGender());

        try {
            // Call the entity/gateway Patient Discovery service
            final PatientSearchResults patientDiscoveryResults = patientService.queryPatient(patientBean, assertion);
            LOG.debug("Patient Discovery call successful. Total number of patients found: {}",
                patientDiscoveryResults.getPatientList().size());

            // Return false if no patient found
            if (patientDiscoveryResults.getPatientList().isEmpty()) {
                return false;
            }

            localCorrelation = correlationService.retrieveOrGenerateCorrelation(patientDiscoveryResults, assertion);
            // populate the UI patient object with the results data
            populatePatientBean(patientDiscoveryResults, patientQuerySearch);
            return true;
        } catch (final PatientSearchException ex) {
            LOG.error("Failed to Retrieve Patient Data: {}", ex.getLocalizedMessage(), ex);
            // TODO: notify the UI or somehow inform the user
            return false;
        }
    }

    /**
     * Calls the NwHIN Query Document service and returns requested documents for the given patient
     *
     * @param patientQuerySearch
     *
     * @return true if the documents found else false
     */
    public boolean queryDocument(final PatientSearchBean patientQuerySearch) {
        // set the document retrieve request values document id, organization, respository id, documenttypes, start time
        // and end time
        final DocumentMetadata document = new DocumentMetadata();
        // document type
        document.setDocumentType(patientQuerySearch.getQueryDocuments());
        // set the document range from date
        document.setStartTime(patientQuerySearch.getDocumentRangeFrom());
        // set the document range to date
        document.setEndTime(patientQuerySearch.getDocumentRangeTo());
        // set the organization
        document.setOrganization(patientQuerySearch.getSelectedCurrentPatient().getOrganization());
        // set the Patient Id
        document.setPatientId(patientQuerySearch.getSelectedCurrentPatient().getPatientId());
        // set the assigning authority id
        document.setPatientIdRoot(patientQuerySearch.getSelectedCurrentPatient().getAssigningAuthorityId());

        try {
            final DocumentMetadataResults documentQueryResults = documentQueryService.queryForDocuments(document,
                localCorrelation, assertion);

            // Check the number of documents
            if (documentQueryResults.getResults().isEmpty()) {
                return false;
            }
            populatePatientBeanWithDQResults(documentQueryResults, patientQuerySearch);
            return true;

        } catch (final DocumentMetadataException ex) {
            LOG.error("discoverPatient() failed: {}", ex.getLocalizedMessage(), ex);
            return false;
        }
    }

    /**
     * Calls the NwHIN Retrieve Document service and updates the patient bean with the document binary, content type
     * etc.
     *
     * @param patientQuerySearch
     *
     * @return true if the document retrieval is successful else false
     */
    public boolean retrieveDocument(final PatientSearchBean patientQuerySearch) {
        // set the document retrieve request values document id, organization and respository id
        final DocumentRetrieve docRetrieve = new DocumentRetrieve();
        docRetrieve.setDocumentId(patientQuerySearch.getSelectedCurrentDocument().getDocumentId());
        docRetrieve.setHCID(patientQuerySearch.getOrganization());
        docRetrieve.setRepositoryId(patientQuerySearch.getSelectedCurrentDocument().getRepositoryUniqueId());

        // Call the NwHIN service to retrieve the document
        final DocumentRetrieveResults response = documentRetrieveService.retrieveDocuments(docRetrieve, assertion);
        // set the retrieved document to the UI patient bean
        if (response.getDocument() != null) {
            if (response.getContentType() != null && (response.getContentType().equals(CONTENT_TYPE_APPLICATION_XML)
                || response.getContentType().equals(CONTENT_TYPE_TEXT_HTML)
                || response.getContentType().equals(CONTENT_TYPE_TEXT_PLAIN)
                || response.getContentType().equals(CONTENT_TYPE_TEXT_XML))) {
                final InputStream xsl = FacesContext.getCurrentInstance().getExternalContext()
                    .getResourceAsStream(DEFAULT_XSL_FILE);
                final InputStream xml = new ByteArrayInputStream(response.getDocument());
                byte[] convertXmlToHtml = null;
                if (xsl != null) {
                    convertXmlToHtml = transformer.convertXMLToHTML(xml, xsl);
                    closeStreamSilently(xsl);
                }
                patientQuerySearch.getSelectedCurrentDocument().setDocumentContent(convertXmlToHtml);
            } else {
                patientQuerySearch.getSelectedCurrentDocument().setDocumentContent(response.getDocument());
            }
            patientQuerySearch.getSelectedCurrentDocument().setDocumentRetrieved(true);
            LOG.debug("Successfully retrieved the content of document with documentid: {}", response.getContentType());
            return true;
        }
        return false;
    }

    public void clearLocalCorrelation() {
        localCorrelation = null;
    }

    /**
     * Internal method to populate the patient data to the Patient bean used in the UI.
     * <p>
     */
    private void populatePatientBean(final PatientSearchResults patientQueryResults,
        final PatientSearchBean patientQuerySearch) {
        final int patientIndex = 0;
        // start with a clean slate
        patientQuerySearch.getPatientList().clear();
        // loop through Patient Discovery results and set the UI patient bean
        for (final gov.hhs.fha.nhinc.patientdiscovery.model.Patient retrievedPatient : patientQueryResults
            .getPatientList()) {
            final Patient patient = new Patient();
            // Patient personal Information
            patient.setDateOfBirth(patientQuerySearch.getDateOfBirth());
            patient.setFirstName(retrievedPatient.getFirstName());
            patient.setMiddleName(retrievedPatient.getMiddleName());
            patient.setLastName(retrievedPatient.getLastName());
            patient.setGender(retrievedPatient.getGender());
            // Should have only the last four digits
            patient.setSSN(retrievedPatient.getSsn());
            patient.setPatientIndex(patientIndex);
            patient.setDrivinglicense(retrievedPatient.getdLicense());
            patient.setStreetname(retrievedPatient.getStreetAddr());
            patient.setCity(retrievedPatient.getCity());
            patient.setState(retrievedPatient.getState());
            patient.setZip(retrievedPatient.getZip());
            patient.setPhone(retrievedPatient.getPhone());
            // Other patient information
            patient.setDomain(retrievedPatient.getDomain());
            patient.setOrganization(patientQuerySearch.getOrganization());
            patient.setOrganizationName(getCommunityName(patientQuerySearch, patientQuerySearch.getOrganization()));
            patient.setPatientId(retrievedPatient.getPid());
            patient.setAssigningAuthorityId(retrievedPatient.getAaId());
            patientQuerySearch.getPatientList().add(patient);
            // TODO: Currently only one patient is possibe/supported, need to figure out if we are planning to handle
            // multiple patients received from the PD service in the future
            break;
        }

        if (NullChecker.isNotNullish(patientQuerySearch.getPatientList())) {
            patientQuerySearch.getPatientList().get(0).setCorrelation(localCorrelation.getExtension());
        }
    }

    /**
     * Internal method to populate the patient data to the Patient bean used in the UI.
     * <p>
     */
    private static void populatePatientBeanWithDQResults(final DocumentMetadataResults DocumentQueryResults,
        final PatientSearchBean patientQuerySearch) {
        int documentIndex = 0;
        // start with a clean slate
        patientQuerySearch.getSelectedCurrentPatient().getDocumentList().clear();
        for (final DocumentMetadataResult documentMetadataResult : DocumentQueryResults.getResults()) {
            final Document patientDocument = new Document();
            patientDocument.setAuthorInstitution(documentMetadataResult.getAuthorInstitution());
            patientDocument.setAuthorPerson(documentMetadataResult.getAuthorPerson());
            patientDocument.setAuthorRole(documentMetadataResult.getAuthorRole());
            patientDocument.setAuthorSpecialty(documentMetadataResult.getAuthorSpecialty());

            patientDocument.setName(documentMetadataResult.getName());
            patientDocument.setDescription(documentMetadataResult.getCareSummary());

            patientDocument.setDocumentId(documentMetadataResult.getDocumentId());
            patientDocument.setDocumentType(documentMetadataResult.getDocumentTypeCode());
            patientDocument.setRepositoryUniqueId(documentMetadataResult.getRepositoryId());
            patientDocument.setDocumentIndex(documentIndex);
            patientDocument.setDocumentClassCode(documentMetadataResult.getDocumentClassCode());
            patientDocument.setUri(documentMetadataResult.getUri());

            // populate document type name from the static list
            // this logic needs to be revisited after the demo
            if (patientDocument.getDocumentClassCode() != null) {
                patientDocument.setDocumentTypeName(
                    patientQuerySearch.getDocumentTypeNameFromTheStaticList(patientDocument.getDocumentClassCode()));
            }
            // for the demo set the value from the patient
            patientDocument.setSourcePatientId(patientQuerySearch.getSelectedCurrentPatient().getPatientId());
            patientDocument.setSize(documentMetadataResult.getSize());
            patientDocument.setHash(documentMetadataResult.getHash());
            patientDocument.setContentType(documentMetadataResult.getMimeType());

            patientDocument.setIntendedRecipient(documentMetadataResult.getIntendedRecipient());
            patientDocument.setLanguageCode(documentMetadataResult.getLanguageCode());
            patientDocument.setLegalAuthenticator(documentMetadataResult.getLegalAuthenticator());

            patientDocument.setCreationTime(documentMetadataResult.getCreationDate());
            patientDocument.setServiceStartTime(documentMetadataResult.getServiceStartTime());
            patientDocument.setServiceStopTime(documentMetadataResult.getServiceStopTime());

            patientQuerySearch.getSelectedCurrentPatient().getDocumentList().add(patientDocument);
            documentIndex++;
        }
    }

    private static String getCommunityName(final PatientSearchBean searchBean, final String hcid) {
        for (final String name : searchBean.getOrganizationList().keySet()) {
            // Find the Home community ID of the BusinessEntity to compare with hcid.
            final String busEntityHcid = searchBean.getOrganizationList().get(name);
            if (StringUtils.isNotBlank(busEntityHcid) && hcid.equals(busEntityHcid)) {
                return name;
            }
        }
        return null;
    }

    private static AssertionType addUserInfoAndPurposeOf(AssertionType assertion, UserLogin user, String purposeOfRole,
        String purposeOfDesc) {
        PersonNameType personName = new PersonNameType();
        personName.setGivenName(user.getFirstName());
        personName.setFamilyName(user.getLastName());
        personName.setSecondNameOrInitials(user.getMiddleName());
        assertion.getUserInfo().setUserName(user.getUserName());
        assertion.getUserInfo().setPersonName(personName);

        CeType userRole = assertion.getUserInfo().getRoleCoded();
        userRole.setCode(user.getTransactionRole());
        userRole.setDisplayName(user.getTransactionRoleDesc());

        CeType purposeOfType = assertion.getPurposeOfDisclosureCoded();
        purposeOfType.setCode(purposeOfRole);
        purposeOfType.setDisplayName(purposeOfDesc);

        return assertion;
    }
}
