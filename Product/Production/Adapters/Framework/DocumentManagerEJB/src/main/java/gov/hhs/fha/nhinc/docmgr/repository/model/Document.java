/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.

 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information. *
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.docmgr.repository.model;

import java.util.Date;
import java.util.Set;

/**
 * Data class for a document.
 * 
 * @author Neil Webb
 */
public class Document {

    private Long documentid;
    private String documentUniqueId;
    private String documentTitle;
    private String authorPerson;
    private String authorInstitution;
    private String authorRole;
    private String authorSpecialty;
    private String availablityStatus;
    private String classCode;
    private String classCodeScheme;
    private String classCodeDisplayName;
    private String confidentialityCode;
    private String confidentialityCodeScheme;
    private String confidentialityCodeDisplayName;
    private Date creationTime;
    private String formatCode;
    private String formatCodeScheme;
    private String formatCodeDisplayName;
    private String patientId;
    private Date serviceStartTime;
    private Date serviceStopTime;
    private String status;
    private String comments;
    private String hash;
    private String facilityCode;
    private String facilityCodeScheme;
    private String facilityCodeDisplayName;
    private String intendedRecipientPerson;
    private String intendedRecipientOrganization;
    private String languageCode;
    private String legalAuthenticator;
    private String mimeType;
    private String parentDocumentId;
    private String parentDocumentRelationship;
    private String practiceSetting;
    private String practiceSettingScheme;
    private String practiceSettingDisplayName;
    private Long documentSize;
    private String sourcePatientId;
    private String pid3;
    private String pid5;
    private String pid8;
    private String pid7;
    private String pid11;
    private String typeCode;
    private String typeCodeScheme;
    private String typeCodeDisplayName;
    private String documentUri;
    private byte[] rawData;
    private boolean persistent;
    private String repositoryId;
    private Set<EventCode> eventCodes;
    private Set<ExtraSlot> extraSlots;

    /**
     *
     */
    public Document() {
        persistent = false;
    }

    /**
     *
     * @return authorInstitution as String
     */
    public String getAuthorInstitution() {
        return authorInstitution;
    }

    /**
     *
     * @param authorInstitution as String
     */
    public void setAuthorInstitution(String authorInstitution) {
        this.authorInstitution = authorInstitution;
    }

    /**
     *
     * @return authorPerson as String
     */
    public String getAuthorPerson() {
        return authorPerson;
    }

    /**
     *
     * @param authorPerson as String
     */
    public void setAuthorPerson(String authorPerson) {
        this.authorPerson = authorPerson;
    }

    /**
     *
     * @return suthorRole String
     */
    public String getAuthorRole() {
        return authorRole;
    }

    /**
     *
     * @param authorRole as String
     */
    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    /**
     *
     * @return authorSpecialty String
     */
    public String getAuthorSpecialty() {
        return authorSpecialty;
    }

    /**
     *
     * @param authorSpecialty as String
     */
    public void setAuthorSpecialty(String authorSpecialty) {
        this.authorSpecialty = authorSpecialty;
    }

    /**
     *
     * @return availablityStatus as String
     */
    public String getAvailablityStatus() {
        return availablityStatus;
    }

    /**
     *
     * @param availablityStatus as String
     */
    public void setAvailablityStatus(String availablityStatus) {
        this.availablityStatus = availablityStatus;
    }

    /**
     *
     * @return classCode as String
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     *
     * @param classCode as String
     */
    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    /**
     *
     * @return classCodeDisplayName as String
     */
    public String getClassCodeDisplayName() {
        return classCodeDisplayName;
    }

    /**
     *
     * @param classCodeDisplayName as String
     */
    public void setClassCodeDisplayName(String classCodeDisplayName) {
        this.classCodeDisplayName = classCodeDisplayName;
    }

    /**
     *
     * @return classCodeScheme as String
     */
    public String getClassCodeScheme() {
        return classCodeScheme;
    }

    /**
     *
     * @param classCodeScheme as String
     */
    public void setClassCodeScheme(String classCodeScheme) {
        this.classCodeScheme = classCodeScheme;
    }

    /**
     *
     * @return comments as String
     */
    public String getComments() {
        return comments;
    }

    /**
     *
     * @param comments as String
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     *
     * @return confidentialityCode as String
     */
    public String getConfidentialityCode() {
        return confidentialityCode;
    }

    /**
     *
     * @param confidentialityCode as String
     */
    public void setConfidentialityCode(String confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    /**
     *
     * @return confidentialityCodeDisplayName as String
     */
    public String getConfidentialityCodeDisplayName() {
        return confidentialityCodeDisplayName;
    }

    /**
     *
     * @param confidentialityCodeDisplayName as String
     */
    public void setConfidentialityCodeDisplayName(String confidentialityCodeDisplayName) {
        this.confidentialityCodeDisplayName = confidentialityCodeDisplayName;
    }

    /**
     *
     * @return confidentialityCodeScheme as String
     */
    public String getConfidentialityCodeScheme() {
        return confidentialityCodeScheme;
    }

    /**
     *
     * @param confidentialityCodeScheme as String
     */
    public void setConfidentialityCodeScheme(String confidentialityCodeScheme) {
        this.confidentialityCodeScheme = confidentialityCodeScheme;
    }

    /**
     *
     * @return documentTitle as String
     */
    public String getDocumentTitle() {
        return documentTitle;
    }

    /**
     *
     * @param documentTitle as String
     */
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    /**
     *
     * @return documentUniqueId as String
     */
    public String getDocumentUniqueId() {
        return documentUniqueId;
    }

    /**
     *
     * @param documentUniqueId as String
     */
    public void setDocumentUniqueId(String documentUniqueId) {
        this.documentUniqueId = documentUniqueId;
    }

    /**
     *
     * @return documentUri as String
     */
    public String getDocumentUri() {
        return documentUri;
    }

    /**
     *
     * @param documentUri as String
     */
    public void setDocumentUri(String documentUri) {
        this.documentUri = documentUri;
    }

    /**
     *
     * @return documentid as Long
     */
    public Long getDocumentid() {
        return documentid;
    }

    /**
     *
     * @param documentid as Long
     */
    public void setDocumentid(Long documentid) {
        this.documentid = documentid;
    }

    /**
     *
     * @return facilityCode as String
     */
    public String getFacilityCode() {
        return facilityCode;
    }

    /**
     *
     * @param facilityCode as String
     */
    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    /**
     *
     * @return facilityCodeDisplayName as String
     */
    public String getFacilityCodeDisplayName() {
        return facilityCodeDisplayName;
    }

    /**
     *
     * @param facilityCodeDisplayName as String
     */
    public void setFacilityCodeDisplayName(String facilityCodeDisplayName) {
        this.facilityCodeDisplayName = facilityCodeDisplayName;
    }

    /**
     *
     * @return facilityCodeScheme as String
     */
    public String getFacilityCodeScheme() {
        return facilityCodeScheme;
    }

    /**
     *
     * @param facilityCodeScheme as String
     */
    public void setFacilityCodeScheme(String facilityCodeScheme) {
        this.facilityCodeScheme = facilityCodeScheme;
    }

    /**
     *
     * @return formatCode as String
     */
    public String getFormatCode() {
        return formatCode;
    }

    /**
     *
     * @param formatCode as String
     */
    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }

    /**
     *
     * @return formatCodeDisplayName as String
     */
    public String getFormatCodeDisplayName() {
        return formatCodeDisplayName;
    }

    /**
     *
     * @param formatCodeDisplayName as String
     */
    public void setFormatCodeDisplayName(String formatCodeDisplayName) {
        this.formatCodeDisplayName = formatCodeDisplayName;
    }

    /**
     *
     * @return formatCodeScheme as String
     */
    public String getFormatCodeScheme() {
        return formatCodeScheme;
    }

    /**
     *
     * @param formatCodeScheme as String
     */
    public void setFormatCodeScheme(String formatCodeScheme) {
        this.formatCodeScheme = formatCodeScheme;
    }

    /**
     *
     * @return hash as String
     */
    public String getHash() {
        return hash;
    }

    /**
     *
     * @param hash as String
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     *
     * @return intendedRecipientOrganization as String
     */
    public String getIntendedRecipientOrganization() {
        return intendedRecipientOrganization;
    }

    /**
     *
     * @param intendedRecipientOrganization as String
     */
    public void setIntendedRecipientOrganization(String intendedRecipientOrganization) {
        this.intendedRecipientOrganization = intendedRecipientOrganization;
    }

    /**
     *
     * @return intendedRecipientPerson as String
     */
    public String getIntendedRecipientPerson() {
        return intendedRecipientPerson;
    }

    /**
     *
     * @param intendedRecipientPerson as String
     */
    public void setIntendedRecipientPerson(String intendedRecipientPerson) {
        this.intendedRecipientPerson = intendedRecipientPerson;
    }

    /**
     *
     * @return languageCode as String
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     *
     * @param languageCode as String
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     *
     * @return legalAuthenticator as String
     */
    public String getLegalAuthenticator() {
        return legalAuthenticator;
    }

    /**
     *
     * @param legalAuthenticator as String
     */
    public void setLegalAuthenticator(String legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }

    /**
     *
     * @return mimeType as String
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     *
     * @param mimeType as String
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     *
     * @return parentDocumentId as String
     */
    public String getParentDocumentId() {
        return parentDocumentId;
    }

    /**
     *
     * @param parentDocumentId as String
     */
    public void setParentDocumentId(String parentDocumentId) {
        this.parentDocumentId = parentDocumentId;
    }

    /**
     *
     * @return parentDocumentRelationship as String
     */
    public String getParentDocumentRelationship() {
        return parentDocumentRelationship;
    }

    /**
     *
     * @param parentDocumentRelationship as String
     */
    public void setParentDocumentRelationship(String parentDocumentRelationship) {
        this.parentDocumentRelationship = parentDocumentRelationship;
    }

    /**
     *
     * @return patientId as String
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId as String
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return pid11 as String
     */
    public String getPid11() {
        return pid11;
    }

    /**
     *
     * @param pid11 as String
     */
    public void setPid11(String pid11) {
        this.pid11 = pid11;
    }

    /**
     *
     * @return pid3 as String
     */
    public String getPid3() {
        return pid3;
    }

    /**
     *
     * @param pid3 as String
     */
    public void setPid3(String pid3) {
        this.pid3 = pid3;
    }

    /**
     *
     * @return pid5 as String
     */
    public String getPid5() {
        return pid5;
    }

    /**
     *
     * @param pid5 as String
     */
    public void setPid5(String pid5) {
        this.pid5 = pid5;
    }

    /**
     *
     * @return pid7 as String
     */
    public String getPid7() {
        return pid7;
    }

    /**
     *
     * @param pid7 as String
     */
    public void setPid7(String pid7) {
        this.pid7 = pid7;
    }

    /**
     *
     * @return pid8 as String
     */
    public String getPid8() {
        return pid8;
    }

    /**
     *
     * @param pid8 as String
     */
    public void setPid8(String pid8) {
        this.pid8 = pid8;
    }

    /**
     *
     * @return practiceSetting as String
     */
    public String getPracticeSetting() {
        return practiceSetting;
    }

    /**
     *
     * @param practiceSetting as String
     */
    public void setPracticeSetting(String practiceSetting) {
        this.practiceSetting = practiceSetting;
    }

    /**
     *
     * @return practiceSettingDisplayName as String
     */
    public String getPracticeSettingDisplayName() {
        return practiceSettingDisplayName;
    }

    /**
     *
     * @param practiceSettingDisplayName as String
     */
    public void setPracticeSettingDisplayName(String practiceSettingDisplayName) {
        this.practiceSettingDisplayName = practiceSettingDisplayName;
    }

    /**
     *
     * @return practiceSettingScheme as String
     */
    public String getPracticeSettingScheme() {
        return practiceSettingScheme;
    }

    /**
     *
     * @param practiceSettingScheme as String
     */
    public void setPracticeSettingScheme(String practiceSettingScheme) {
        this.practiceSettingScheme = practiceSettingScheme;
    }

    /**
     *
     * @return documentSize as Long
     */
    public Long getSize() {
        return documentSize;
    }

    /**
     *
     * @param size as Long
     */
    public void setSize(Long size) {
        this.documentSize = size;
    }

    /**
     *
     * @return sourcePatientId as String
     */
    public String getSourcePatientId() {
        return sourcePatientId;
    }

    /**
     *
     * @param sourcePatientId as String
     */
    public void setSourcePatientId(String sourcePatientId) {
        this.sourcePatientId = sourcePatientId;
    }

    /**
     *
     * @return creationTime as Date
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     *
     * @param creationTime as Date
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     *
     * @return serviceStartTime as Date
     */
    public Date getServiceStartTime() {
        return serviceStartTime;
    }

    /**
     *
     * @param serviceStartTime as Date
     */
    public void setServiceStartTime(Date serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    /**
     *
     * @return serviceStopTime as Date
     */
    public Date getServiceStopTime() {
        return serviceStopTime;
    }

    /**
     *
     * @param serviceStopTime as Date
     */
    public void setServiceStopTime(Date serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }

    /**
     *
     * @return status as String
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status as String
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return typeCode as String
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     *
     * @param typeCode as String
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     *
     * @return typeCodeDisplayName as String
     */
    public String getTypeCodeDisplayName() {
        return typeCodeDisplayName;
    }

    /**
     *
     * @param typeCodeDisplayName as String
     */
    public void setTypeCodeDisplayName(String typeCodeDisplayName) {
        this.typeCodeDisplayName = typeCodeDisplayName;
    }

    /**
     *
     * @return typeCodeScheme as String
     */
    public String getTypeCodeScheme() {
        return typeCodeScheme;
    }

    /**
     *
     * @param typeCodeScheme as String
     */
    public void setTypeCodeScheme(String typeCodeScheme) {
        this.typeCodeScheme = typeCodeScheme;
    }

    /**
     *
     * @return rawData as byte[]
     */
    public byte[] getRawData() {
        return rawData;
    }

    /**
     *
     * @param rawData as byte[]
     */
    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    /**
     *
     * @return perisitent as boolean
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     *
     * @param persistent as boolean
     */
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * Getter for a code representing the persistent state of the document.
     * This accessor is only to be used with the persistence layer. Use 
     * the isPersistent method to access this property.
     * 
     * @return Persistence layer code for persistent flag.
     */
    public int getPersistentCode() {
        return (persistent ? 1 : 0);
    }

    /**
     * Setter for the document's persistence code that is used by the 
     * persistence layer. Use the isPersistent method to look up this 
     * property.
     * 
     * @param persistentCode Persistence layer code for the persistence flag.
     */
    public void setPersistentCode(int persistentCode) {
        persistent = (persistentCode == 1);

    }

    /**
     *
     * @return repositoryId as String
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     *
     * @param repositoryId as String
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     *
     * @return eventCodes as Set<EventCode>
     */
    public Set<EventCode> getEventCodes() {
        return eventCodes;
    }

    /**
     *
     * @param eventCodes as Set<EventCode>
     */
    public void setEventCodes(Set<EventCode> eventCodes) {
        this.eventCodes = eventCodes;
    }

    /**
     *
     * @return extraSlots as Set<ExtraSlot>
     */
    public Set<ExtraSlot> getExtraSlots() {
        return extraSlots;
    }

    /**
     *
     * @param extraSlots as Set<ExtraSlot>
     */
    public void setExtraSlots(Set<ExtraSlot> extraSlots) {
        this.extraSlots = extraSlots;
    }
}
