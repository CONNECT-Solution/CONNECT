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
package gov.hhs.fha.nhinc.docquery.model;

import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author tjafri
 */
public class DocumentMetadataResult {

    /**
     * The document id.
     */
    private String documentId;

    /**
     * The repository id.
     */
    private String repositoryId;

    /**
     * The Patient Id.
     */
    private String patientId;

    /**
     * The document title.
     */
    private String documentTitle;

    /**
     * The creation date.
     */
    private Date creationDate;

    /**
     * The service start time.
     */
    private Date serviceStartTime;

    /**
     * The service stop time.
     */
    private Date serviceStopTime;

    /**
     * The availability status.
     */
    private String availabilityStatus;

    /**
     * The document type code.
     */
    private String documentTypeCode;

    /**
     * The document class code.
     */
    private String documentClassCode;

    /**
     * The document format code.
     */
    private String documentFormatCode;

    /**
     * The author person.
     */
    private String authorPerson;

    /**
     * The author institution.
     */
    private String authorInstitution;

    /**
     * The author Role.
     */
    private String authorRole;

    /**
     * The author Specialty.
     */
    private String authorSpecialty;

    /**
     * The event codes.
     */
    private String eventCodes;

    /**
     * The Coding Scheme.
     */
    private String codingScheme;

    /**
     * The Coding Scheme Name.
     */
    private String codingSchemeName;

    /**
     * The Hash.
     */
    private String hash;

    /**
     * The intendedRecipient.
     */
    private String intendedRecipient;

    /**
     * The Language Code.
     */
    private String languageCode;

    /**
     * The Legal Authenticator.
     */
    private String legalAuthenticator;

    /**
     * The name.
     */
    private String name;

    private String description;

    /**
     * The mimeType.
     */
    private String mimeType;

    private boolean Opaque;

    private int size;

    private String sourcePatientId;

    private String uri;

    private String status;

    private String objectType;

    private String home;

    private String id;



    /**
     * Gets the patient id.
     *
     * @return the patient id
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient id.
     *
     * @param patientId the new patient id
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Gets the document id.
     *
     * @return the documentId
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Sets the document id.
     *
     * @param documentId the documentId to set
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * Gets the repository id.
     *
     * @return the repositoryId
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     * Sets the repository id.
     *
     * @param repositoryId the repositoryId to set
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * Gets the document title.
     *
     * @return the documentTitle
     */
    public String getDocumentTitle() {
        return documentTitle;
    }

    /**
     * Sets the document title.
     *
     * @param documentTitle the documentTitle to set
     */
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    /**
     * Gets the creation date.
     *
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the service start time.
     *
     * @return the serviceStartTime
     */
    public Date getServiceStartTime() {
        return serviceStartTime;
    }

    /**
     * Sets the service start time.
     *
     * @param serviceStartTime the serviceStartTime to set
     */
    public void setServiceStartTime(Date serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    /**
     * Gets the service stop time.
     *
     * @return the serviceStopTime
     */
    public Date getServiceStopTime() {
        return serviceStopTime;
    }

    /**
     * Sets the service stop time.
     *
     * @param serviceStopTime the serviceStopTime to set
     */
    public void setServiceStopTime(Date serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }

    /**
     * Gets the availability status.
     *
     * @return the availabilityStatus
     */
    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     * Sets the availability status.
     *
     * @param availabilityStatus the availabilityStatus to set
     */
    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * Gets the document type code.
     *
     * @return the documentTypeCode
     */
    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    /**
     * Sets the document type code.
     *
     * @param documentTypeCode the documentTypeCode to set
     */
    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    /**
     * Gets the document class code.
     *
     * @return the documentClassCode
     */
    public String getDocumentClassCode() {
        return documentClassCode;
    }

    /**
     * Sets the document class code.
     *
     * @param documentClassCode the documentClassCode to set
     */
    public void setDocumentClassCode(String documentClassCode) {
        this.documentClassCode = documentClassCode;
    }

    /**
     * @return the documentFormatCode
     */
    public String getDocumentFormatCode() {
        return documentFormatCode;
    }

    /**
     * @param documentFormatCode the documentFormatCode to set
     */
    public void setDocumentFormatCode(String documentFormatCode) {
        this.documentFormatCode = documentFormatCode;
    }

    /**
     * Gets the author person.
     *
     * @return the authorPerson
     */
    public String getAuthorPerson() {
        return authorPerson;
    }

    /**
     * Sets the author person.
     *
     * @param authorPerson the authorPerson to set
     */
    public void setAuthorPerson(String authorPerson) {
        this.authorPerson = authorPerson;
    }

    /**
     * Gets the author institution.
     *
     * @return the authorInstitution
     */
    public String getAuthorInstitution() {
        return authorInstitution;
    }

    /**
     * Sets the author institution.
     *
     * @param authorInstitution the authorInstitution to set
     */
    public void setAuthorInstitution(String authorInstitution) {
        this.authorInstitution = authorInstitution;
    }

    /**
     * Gets the event codes.
     *
     * @return the eventCodes
     */
    public String getEventCodes() {
        return eventCodes;
    }

    /**
     * Sets the event codes.
     *
     * @param eventCodes the eventCodes to set
     */
    public void setEventCodes(String eventCodes) {
        this.eventCodes = eventCodes;
    }

    /**
     * Gets the care summary.
     *
     * @return the care summary
     */
    public String getCareSummary() {
        StringBuilder builder = new StringBuilder();
        if (!StringUtils.isBlank(eventCodes)) {
            builder.append(eventCodes);
        } else {
            builder.append(documentClassCode);
            builder.append(" - ");
            builder.append(documentTypeCode);
        }

        if (serviceStartTime != null && serviceStopTime != null) {
            builder.append(" from ");
            builder.append(serviceStartTime);
            builder.append(" to ");
            builder.append(serviceStopTime);
        }
        return builder.toString();
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
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @return the Opague
     */
    public boolean isOpague() {
        return Opaque;
    }

    /**
     * @param Opague the Opague to set
     */
    public void setOpague(boolean Opague) {
        this.Opaque = Opague;
    }

    /**
     * @return the Size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param Size the Size to set
     */
    public void setSize(int Size) {
        this.size = Size;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the objectType
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * @param objectType the objectType to set
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * @return the home
     */
    public String getHome() {
        return home;
    }

    /**
     * @param home the home to set
     */
    public void setHome(String home) {
        this.home = home;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
