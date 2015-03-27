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
package gov.hhs.fha.nhinc.admingui.event.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * POJO class to render the results back to the UI
 *
 * @author Naresh Subramanyan
 */
public class Document {

    private Date creationTime;
    private String hash;
    //used in the UI
    private int documentIndex;

    private String intendedRecipient;
    private String languageCode;
    private String legalAuthenticator;
    private Date serviceStartTime;
    private Date serviceStopTime;
    //private int size;
    private double size;
    private String sourcePatientId;
    //not sure if we need this
    private String sourcePatientInfo;
    private String URI;
    private String repositoryUniqueId;
    private String Name;
    private String Description;
    private String authorPerson;

    private String authorInstitution;

    private String authorRole;
    private String authorSpecialty;
    private String codingScheme;
    private String codingSchemeName;
    //unique ID from document
    private String documentId;
    private String patientId;

    private byte[] documentContent;
    private boolean documentRetrieved;

    private String document;

    private String contentType;
    // document content type PDF, XLSX, TXT, XML
    private String documentType;

    private String confidentialityCode;
    private String entryUUID;
    private String healthcareFacilityTypeCode;
    private String documentTypeCode;
    private String documentClassCode;
    private String documentFormatCode;

    public Document() {
        this.documentRetrieved = false;
    }

    /**
     * @return the creationTime
     */
    public Date getCreationTime() {
        return creationTime;

    }

    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     *
     * @return the formattedDate
     */
    public String getCreationTimeUiDisplay() {
        String formattedDate = null;
        if (this.getCreationTime() != null) {
            Date currentDate = this.getCreationTime();
            SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
            formattedDate = dateformat.format(currentDate);
        }
        return formattedDate;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * @return the intendedRecipient
     */
    public String getIntendedRecipient() {
        return intendedRecipient;
    }

    /**
     * @param intendedRecipient the intendedRecipient to set
     */
    public void setIntendedRecipient(String intendedRecipient) {
        this.intendedRecipient = intendedRecipient;
    }

    /**
     * @return the languageCode
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * @param languageCode the languageCode to set
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * @return the legalAuthenticator
     */
    public String getLegalAuthenticator() {
        return legalAuthenticator;
    }

    /**
     * @param legalAuthenticator the legalAuthenticator to set
     */
    public void setLegalAuthenticator(String legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }

    /**
     * @return the serviceStartTime
     */
    public Date getServiceStartTime() {
        return serviceStartTime;
    }

    /**
     * @param serviceStartTime the serviceStartTime to set
     */
    public void setServiceStartTime(Date serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    /**
     * @return the serviceStopTime
     */
    public Date getServiceStopTime() {
        return serviceStopTime;
    }

    /**
     * @param serviceStopTime the serviceStopTime to set
     */
    public void setServiceStopTime(Date serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }

    /**
     * @return the size
     */
    public double getSize() {
        return Double.parseDouble(new DecimalFormat("##.##").format(size / 1024));
    }

    /**
     * @param size the size to set
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * @return the sourcePatientId
     */
    public String getSourcePatientId() {
        return sourcePatientId;
    }

    /**
     * @param sourcePatientId the sourcePatientId to set
     */
    public void setSourcePatientId(String sourcePatientId) {
        this.sourcePatientId = sourcePatientId;
    }

    /**
     * @return the sourcePatientInfo
     */
    public String getSourcePatientInfo() {
        return sourcePatientInfo;
    }

    /**
     * @param sourcePatientInfo the sourcePatientInfo to set
     */
    public void setSourcePatientInfo(String sourcePatientInfo) {
        this.sourcePatientInfo = sourcePatientInfo;
    }

    /**
     * @return the URI
     */
    public String getURI() {
        return URI;
    }

    /**
     * @param URI the URI to set
     */
    public void setURI(String URI) {
        this.URI = URI;
    }

    /**
     * @return the repositoryUniqueId
     */
    public String getRepositoryUniqueId() {
        return repositoryUniqueId;
    }

    /**
     * @param repositoryUniqueId the repositoryUniqueId to set
     */
    public void setRepositoryUniqueId(String repositoryUniqueId) {
        this.repositoryUniqueId = repositoryUniqueId;
    }

    /**
     * @return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return the authorPerson
     */
    public String getAuthorPerson() {
        return authorPerson;
    }

    /**
     * @param authorPerson the authorPerson to set
     */
    public void setAuthorPerson(String authorPerson) {
        this.authorPerson = authorPerson;
    }

    /**
     * @return the authorInstitution
     */
    public String getAuthorInstitution() {
        return authorInstitution;
    }

    /**
     * @param authorInstitution the authorInstitution to set
     */
    public void setAuthorInstitution(String authorInstitution) {
        this.authorInstitution = authorInstitution;
    }

    /**
     * @return the authorRole
     */
    public String getAuthorRole() {
        return authorRole;
    }

    /**
     * @param authorRole the authorRole to set
     */
    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    /**
     * @return the authorSpecialty
     */
    public String getAuthorSpecialty() {
        return authorSpecialty;
    }

    /**
     * @param authorSpecialty the authorSpecialty to set
     */
    public void setAuthorSpecialty(String authorSpecialty) {
        this.authorSpecialty = authorSpecialty;
    }

    /**
     * @return the codingScheme
     */
    public String getCodingScheme() {
        return codingScheme;
    }

    /**
     * @param codingScheme the codingScheme to set
     */
    public void setCodingScheme(String codingScheme) {
        this.codingScheme = codingScheme;
    }

    /**
     * @return the codingSchemeName
     */
    public String getCodingSchemeName() {
        return codingSchemeName;
    }

    /**
     * @param codingSchemeName the codingSchemeName to set
     */
    public void setCodingSchemeName(String codingSchemeName) {
        this.codingSchemeName = codingSchemeName;
    }

    /**
     * @return the documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the documentContent
     */
    public byte[] getDocumentContent() {
        return documentContent;
    }

    /**
     * @param documentContent the documentContent to set
     */
    public void setDocumentContent(byte[] documentContent) {
        this.documentContent = documentContent;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @return the document
     */
    public String getDocument() {
        String decodedValue = new String(documentContent);
        return decodedValue;
    }

    /**
     * @return the documentRetrieved
     */
    public boolean isDocumentRetrieved() {
        return documentRetrieved;
    }

    /**
     * @param documentRetrieved the documentRetrieved to set
     */
    public void setDocumentRetrieved(boolean documentRetrieved) {
        this.documentRetrieved = documentRetrieved;
    }

    /**
     * @return the documentIndex
     */
    public int getDocumentIndex() {
        return documentIndex;
    }

    /**
     * @param documentIndex the documentIndex to set
     */
    public void setDocumentIndex(int documentIndex) {
        this.documentIndex = documentIndex;
    }

    public String getConfidentialityCode() {
        return confidentialityCode;
    }

    public void setConfidentialityCode(String confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    public String getEntryUUID() {
        return entryUUID;
    }

    public void setEntryUUID(String entryUUID) {
        this.entryUUID = entryUUID;
    }

    public String getHealthcareFacilityTypeCode() {
        return healthcareFacilityTypeCode;
    }

    public void setHealthcareFacilityTypeCode(String healthcareFacilityTypeCode) {
        this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentClassCode() {
        return documentClassCode;
    }

    public void setDocumentClassCode(String documentClassCode) {
        this.documentClassCode = documentClassCode;
    }

    public String getDocumentFormatCode() {
        return documentFormatCode;
    }

    public void setDocumentFormatCode(String documentFormatCode) {
        this.documentFormatCode = documentFormatCode;
    }

}
