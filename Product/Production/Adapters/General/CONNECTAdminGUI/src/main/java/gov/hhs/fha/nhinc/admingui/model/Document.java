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
package gov.hhs.fha.nhinc.admingui.model;

import java.util.Date;

/**
 * POJO class to render the results back to the UI
 *
 * @author Naresh Subramanyan
 */
public class Document {

    private static final String KILOBYTE_LABEL = "KB";
    private Date creationTime;
    private String hash;
    // used in the UI
    private int documentIndex;

    private String intendedRecipient;
    private String languageCode;
    private String legalAuthenticator;
    private Date serviceStartTime;
    private Date serviceStopTime;
    private int size;
    private String sourcePatientId;
    // not sure if we need this
    private String sourcePatientInfo;
    private String uri;
    private String repositoryUniqueId;
    private String name;
    private String description;
    private String authorPerson;

    private String authorInstitution;

    private String authorRole;
    private String authorSpecialty;
    private String codingScheme;
    private String codingSchemeName;
    // unique ID from document
    private String documentId;
    private String patientId;

    private byte[] documentContent;
    private boolean documentRetrieved;

    private String contentType;
    private String documentType;

    private String documentClassCode;
    private String confidentialityCode;
    private String entryUUID;
    private String healthcareFacilityTypeCode;
    private String documentTypeCode;
    private String documentFormatCode;
    private String documentTypeName;

    public Document() {
        documentRetrieved = false;
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
    public int getSize() {
        return size / 1024;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    public String getSizeAsString() {
        return getSize() + " " + KILOBYTE_LABEL;
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
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
        // workaround for now, once the CONNECT FHIR adapter supports
        // documentId, we will remove this logic
        return documentId == null ? uri : documentId;
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
        // workaround for now, once the CONNECT FHIR adapter supports
        // documentType, we will remove this logic
        return documentType == null ? documentClassCode : documentType;
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
        return new String(documentContent);
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

    /**
     * @return the documentClassCode
     */
    public String getDocumentClassCode() {
        return documentClassCode;
    }

    /**
     * @param documentClassCode the documentClassCode to set
     */
    public void setDocumentClassCode(String documentClassCode) {
        this.documentClassCode = documentClassCode;
    }

    /**
     *
     * @return the confidentialityCode
     */
    public String getConfidentialityCode() {
        return confidentialityCode;
    }

    /**
     *
     * @param confidentialityCode the confidentialityCode to set
     */
    public void setConfidentialityCode(String confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    /**
     *
     * @return the entry UUID
     */
    public String getEntryUUID() {
        return entryUUID;
    }

    /**
     *
     * @param entryUUID the entryUUID to set
     */
    public void setEntryUUID(String entryUUID) {
        this.entryUUID = entryUUID;
    }

    /**
     *
     * @return healthcareFacilityTypeCode
     */
    public String getHealthcareFacilityTypeCode() {
        return healthcareFacilityTypeCode;
    }

    /**
     *
     * @param healthcareFacilityTypeCode the healcareFacilityTypeCode to set
     */
    public void setHealthcareFacilityTypeCode(String healthcareFacilityTypeCode) {
        this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
    }

    /**
     *
     * @return documentTypeCode
     */
    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    /**
     *
     * @param documentTypeCode the documentTypeCode to set
     */
    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    /**
     *
     * @return documentFormatCode
     */
    public String getDocumentFormatCode() {
        return documentFormatCode;
    }

    /**
     *
     * @param documentFormatCode the documentFormatCode to set
     */
    public void setDocumentFormatCode(String documentFormatCode) {
        this.documentFormatCode = documentFormatCode;
    }

    /**
     * @return the documentTypeName
     */
    public String getDocumentTypeName() {
        return documentTypeName;
    }

    /**
     * @param documentTypeName the documentTypeName to set
     */
    public void setDocumentTypeName(String documentTypeName) {
        this.documentTypeName = documentTypeName;
    }

}
