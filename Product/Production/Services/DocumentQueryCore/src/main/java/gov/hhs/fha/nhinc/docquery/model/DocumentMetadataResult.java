/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.  * All rights reserved. * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the United States Government nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
     * The event codes.
     */
    private String eventCodes;

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
}
