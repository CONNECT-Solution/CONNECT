/*
 *  Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.admingui.event.model.Document;
import gov.hhs.fha.nhinc.admingui.event.model.Patient;
import gov.hhs.fha.nhinc.admingui.managed.PatientSearchBean;
import gov.hhs.fha.nhinc.admingui.services.exception.DocumentMetadataException;
import gov.hhs.fha.nhinc.admingui.services.impl.DocumentQueryServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.DocumentRetrieveServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.PatientServiceImpl;
import gov.hhs.fha.nhinc.docquery.builder.impl.FindDocumentsAdhocQueryRequestBuilder;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResult;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResults;
import gov.hhs.fha.nhinc.docquery.model.builder.impl.DocumentMetadataResultsModelBuilderImpl;
import gov.hhs.fha.nhinc.docretrieve.model.DocumentRetrieve;
import gov.hhs.fha.nhinc.docretrieve.model.DocumentRetrieveResults;
import gov.hhs.fha.nhinc.patientdiscovery.model.PatientSearchResults;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 * Singleton Interface class between UI and the backend services. provides high level APIs for calling PD, DQ, RD etc.
 *
 *
 * @author Naresh Subramanyan
 */
public class GatewayService {

    private static final Logger LOG = Logger.getLogger(GatewayService.class);

    private static final String PATIENT_DOB_FORMAT = "yyyyMMdd";

    private PatientService patientService;
    private DocumentQueryService documentQueryService;
    private DocumentRetrieveService documentRetrieveService;

    private GatewayService() {
        //create the Service implementation instances
        this.patientService = new PatientServiceImpl();
        this.documentQueryService = new DocumentQueryServiceImpl();
        this.documentRetrieveService = new DocumentRetrieveServiceImpl();
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
    public boolean discoverPatient(PatientSearchBean patientQuerySearch) {

        //Create the patient bean that needs to be passed to the service layer
        gov.hhs.fha.nhinc.patientdiscovery.model.Patient patientBean = new gov.hhs.fha.nhinc.patientdiscovery.model.Patient();
        //set the orgainization
        patientBean.setOrganization(patientQuerySearch.getOrganization());
        //set birth date in YYYYMMDD
        DateFormat df = new SimpleDateFormat(PATIENT_DOB_FORMAT);
        patientBean.setBirthDate(df.format(patientQuerySearch.getDateOfBirth()));
        //set the first name
        patientBean.setFirstName(patientQuerySearch.getFirstName());
        //set last name
        patientBean.setLastName(patientQuerySearch.getLastName());
        //set the gender
        patientBean.setGender(patientQuerySearch.getGender());

        try {
            //Call the entity/gateway Patient Discovery service
            PatientSearchResults patientDiscoveryResults = patientService.queryPatient(patientBean);
            LOG.debug("Patient Discovery call successful. Total number of patients found:" + patientDiscoveryResults.getPatientList().size());

            //Return false if no patient found
            if (patientDiscoveryResults.getPatientList().isEmpty()) {
                return false;
            }
            //populate the UI patient object with the results data
            populatePatientBean(patientDiscoveryResults, patientQuerySearch);
            return true;
        } catch (Exception ex) {
            LOG.error("Failed to Retrieve Patient Data" + ex.getMessage());
            //TODO: notify the UI or somehow inform the user
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
    public boolean queryDocument(PatientSearchBean patientQuerySearch) {
        //set the document retrieve request values document id, organization, respository id, documenttypes, start time
        //and end time
        DocumentMetadata document = new DocumentMetadata();
        //document type
        //document.setDocumentType("");
        //document.setStartTime(null);
        //document.setEndTime(null);
        //set the organization
        document.setOrganization(patientQuerySearch.getSelectedCurrentPatient().getOrganization());
        //set the Patient Id
        document.setPatientId(patientQuerySearch.getSelectedCurrentPatient().getPatientId());
        //set the assigning authority id
        document.setPatientIdRoot(patientQuerySearch.getSelectedCurrentPatient().getAssigningAuthorityId());

        try {
            DocumentQueryServiceImpl dqService = new DocumentQueryServiceImpl(new FindDocumentsAdhocQueryRequestBuilder(),
                new DocumentMetadataResultsModelBuilderImpl());
            DocumentMetadataResults documentQueryResults = dqService.queryForDocuments(document);

            //Check the number of documents
            if (documentQueryResults.getResults().isEmpty()) {
                return false;
            }
            populatePatientBeanWithDQResults(documentQueryResults, patientQuerySearch);
            return true;

        } catch (DocumentMetadataException ex) {
            System.out.println("discoverPatient() failed:" + ex.getMessage());
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
    public boolean retrieveDocument(PatientSearchBean patientQuerySearch) {
        //set the document retrieve request values document id, organization and respository id
        DocumentRetrieve docRetrieve = new DocumentRetrieve();
        docRetrieve.setDocumentId(patientQuerySearch.getSelectedCurrentDocument().getDocumentId());
        docRetrieve.setHCID(patientQuerySearch.getOrganization());
        docRetrieve.setRepositoryId(patientQuerySearch.getSelectedCurrentDocument().getRepositoryUniqueId());

        //Call the NwHIN service to retrieve the document
        DocumentRetrieveResults response = documentRetrieveService.retrieveDocuments(docRetrieve);
        //set the retrieved document to the UI patient bean 
        if (response.getDocument() != null) {
            //patientQuerySearch.getSelectedCurrentDocument().setContentType(response.getContentType());
            patientQuerySearch.getSelectedCurrentDocument().setDocumentContent(response.getDocument());
            patientQuerySearch.getSelectedCurrentDocument().setDocumentRetrieved(true);
            LOG.debug("Successfully retrieved the content of document with documentid:" + response.getContentType());
            return true;
        }
        return false;
    }

    /**
     * Internal method to populate the patient data to the Patient bean used in the UI.
     *
     */
    private void populatePatientBean(PatientSearchResults patientQueryResults, PatientSearchBean patientQuerySearch) {
        int patientIndex = 0;
        //start with a clean slate
        patientQuerySearch.getPatientList().clear();
        //loop through Patient Discovery results and set the UI patient bean
        for (gov.hhs.fha.nhinc.patientdiscovery.model.Patient retrievedPatient : patientQueryResults.getPatientList()) {
            Patient patient = new Patient();
            //Patient personal Information
            patient.setDateOfBirth(patientQuerySearch.getDateOfBirth());
            patient.setFirstName(retrievedPatient.getFirstName());
            patient.setMiddleName(retrievedPatient.getMiddleName());
            patient.setLastName(retrievedPatient.getLastName());
            patient.setGender(retrievedPatient.getGender());
            //Should have only the last four digits
            patient.setSSN(retrievedPatient.getSsn());
            patient.setPatientIndex(patientIndex);
            patient.setDrivinglicense(retrievedPatient.getdLicense());
            patient.setStreetname(retrievedPatient.getStreetAddr());
            patient.setCity(retrievedPatient.getCity());
            patient.setState(retrievedPatient.getState());
            patient.setZip(retrievedPatient.getZip());
            patient.setPhone(retrievedPatient.getPhone());
            //Other patient information
            patient.setDomain(retrievedPatient.getDomain());
            patient.setOrganization(patientQuerySearch.getOrganization());
            patient.setPatientId(retrievedPatient.getPid());
            patient.setAssigningAuthorityId(retrievedPatient.getAaId());
            patientQuerySearch.getPatientList().add(patient);
            //TODO: Currently only one patient is possibe/supported, need to figure out if we are planning to handle
            //multiple patients received from the PD service in the future
            break;
        }
    }

    /**
     * Internal method to populate the patient data to the Patient bean used in the UI.
     *
     */
    private void populatePatientBeanWithDQResults(DocumentMetadataResults DocumentQueryResults, PatientSearchBean patientQuerySearch) {
        int documentIndex = 0;
        //start with a clean slate
        patientQuerySearch.getSelectedCurrentPatient().getDocumentList().clear();
        for (DocumentMetadataResult documentMetadataResult : DocumentQueryResults.getResults()) {
            Document patientDocument = new Document();
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
}
