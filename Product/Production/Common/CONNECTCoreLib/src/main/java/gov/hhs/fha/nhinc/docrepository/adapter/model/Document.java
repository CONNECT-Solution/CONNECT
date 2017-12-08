/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docrepository.adapter.model;

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
    private Integer size;
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
    private boolean onDemand;
    private String NewDocumentUniqueId;
    private String NewRepositoryUniqueId;
    private boolean persistent;
    private Set<EventCode> eventCodes;
    private Long patientRecordId;
    private String patientIdentifier;

    public Document() {
        persistent = false;
    }

    public String getAuthorInstitution() {
        return authorInstitution;
    }

    public void setAuthorInstitution(String authorInstitution) {
        this.authorInstitution = authorInstitution;
    }

    public String getAuthorPerson() {
        return authorPerson;
    }

    public void setAuthorPerson(String authorPerson) {
        this.authorPerson = authorPerson;
    }

    public String getAuthorRole() {
        return authorRole;
    }

    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    public String getAuthorSpecialty() {
        return authorSpecialty;
    }

    public void setAuthorSpecialty(String authorSpecialty) {
        this.authorSpecialty = authorSpecialty;
    }

    public String getAvailablityStatus() {
        return availablityStatus;
    }

    public void setAvailablityStatus(String availablityStatus) {
        this.availablityStatus = availablityStatus;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassCodeDisplayName() {
        return classCodeDisplayName;
    }

    public void setClassCodeDisplayName(String classCodeDisplayName) {
        this.classCodeDisplayName = classCodeDisplayName;
    }

    public String getClassCodeScheme() {
        return classCodeScheme;
    }

    public void setClassCodeScheme(String classCodeScheme) {
        this.classCodeScheme = classCodeScheme;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getConfidentialityCode() {
        return confidentialityCode;
    }

    public void setConfidentialityCode(String confidentialityCode) {
        this.confidentialityCode = confidentialityCode;
    }

    public String getConfidentialityCodeDisplayName() {
        return confidentialityCodeDisplayName;
    }

    public void setConfidentialityCodeDisplayName(String confidentialityCodeDisplayName) {
        this.confidentialityCodeDisplayName = confidentialityCodeDisplayName;
    }

    public String getConfidentialityCodeScheme() {
        return confidentialityCodeScheme;
    }

    public void setConfidentialityCodeScheme(String confidentialityCodeScheme) {
        this.confidentialityCodeScheme = confidentialityCodeScheme;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentUniqueId() {
        return documentUniqueId;
    }

    public void setDocumentUniqueId(String documentUniqueId) {
        this.documentUniqueId = documentUniqueId;
    }

    public String getDocumentUri() {
        return documentUri;
    }

    public void setDocumentUri(String documentUri) {
        this.documentUri = documentUri;
    }

    public Long getDocumentid() {
        return documentid;
    }

    public void setDocumentid(Long documentid) {
        this.documentid = documentid;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getFacilityCodeDisplayName() {
        return facilityCodeDisplayName;
    }

    public void setFacilityCodeDisplayName(String facilityCodeDisplayName) {
        this.facilityCodeDisplayName = facilityCodeDisplayName;
    }

    public String getFacilityCodeScheme() {
        return facilityCodeScheme;
    }

    public void setFacilityCodeScheme(String facilityCodeScheme) {
        this.facilityCodeScheme = facilityCodeScheme;
    }

    public String getFormatCode() {
        return formatCode;
    }

    public void setFormatCode(String formatCode) {
        this.formatCode = formatCode;
    }

    public String getFormatCodeDisplayName() {
        return formatCodeDisplayName;
    }

    public void setFormatCodeDisplayName(String formatCodeDisplayName) {
        this.formatCodeDisplayName = formatCodeDisplayName;
    }

    public String getFormatCodeScheme() {
        return formatCodeScheme;
    }

    public void setFormatCodeScheme(String formatCodeScheme) {
        this.formatCodeScheme = formatCodeScheme;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getIntendedRecipientOrganization() {
        return intendedRecipientOrganization;
    }

    public void setIntendedRecipientOrganization(String intendedRecipientOrganization) {
        this.intendedRecipientOrganization = intendedRecipientOrganization;
    }

    public String getIntendedRecipientPerson() {
        return intendedRecipientPerson;
    }

    public void setIntendedRecipientPerson(String intendedRecipientPerson) {
        this.intendedRecipientPerson = intendedRecipientPerson;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLegalAuthenticator() {
        return legalAuthenticator;
    }

    public void setLegalAuthenticator(String legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getParentDocumentId() {
        return parentDocumentId;
    }

    public void setParentDocumentId(String parentDocumentId) {
        this.parentDocumentId = parentDocumentId;
    }

    public String getParentDocumentRelationship() {
        return parentDocumentRelationship;
    }

    public void setParentDocumentRelationship(String parentDocumentRelationship) {
        this.parentDocumentRelationship = parentDocumentRelationship;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPid11() {
        return pid11;
    }

    public void setPid11(String pid11) {
        this.pid11 = pid11;
    }

    public String getPid3() {
        return pid3;
    }

    public void setPid3(String pid3) {
        this.pid3 = pid3;
    }

    public String getPid5() {
        return pid5;
    }

    public void setPid5(String pid5) {
        this.pid5 = pid5;
    }

    public String getPid7() {
        return pid7;
    }

    public void setPid7(String pid7) {
        this.pid7 = pid7;
    }

    public String getPid8() {
        return pid8;
    }

    public void setPid8(String pid8) {
        this.pid8 = pid8;
    }

    public String getPracticeSetting() {
        return practiceSetting;
    }

    public void setPracticeSetting(String practiceSetting) {
        this.practiceSetting = practiceSetting;
    }

    public String getPracticeSettingDisplayName() {
        return practiceSettingDisplayName;
    }

    public void setPracticeSettingDisplayName(String practiceSettingDisplayName) {
        this.practiceSettingDisplayName = practiceSettingDisplayName;
    }

    public String getPracticeSettingScheme() {
        return practiceSettingScheme;
    }

    public void setPracticeSettingScheme(String practiceSettingScheme) {
        this.practiceSettingScheme = practiceSettingScheme;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSourcePatientId() {
        return sourcePatientId;
    }

    public void setSourcePatientId(String sourcePatientId) {
        this.sourcePatientId = sourcePatientId;
    }

    public Date getCreationTime() {
        if (creationTime == null) {
            return null;
        } else {
            return (Date) creationTime.clone();
        }
    }

    public void setCreationTime(Date creationTime) {
        if (creationTime != null) {
            this.creationTime = (Date) creationTime.clone();
        } else {
            this.creationTime = null;
        }
    }

    public Date getServiceStartTime() {
        if (serviceStartTime == null) {
            return null;
        } else {
            return (Date) serviceStartTime.clone();
        }
    }

    public void setServiceStartTime(Date serviceStartTime) {
        if (serviceStartTime != null) {
            this.serviceStartTime = (Date) serviceStartTime.clone();
        } else {
            this.serviceStartTime = null;
        }
    }

    public Date getServiceStopTime() {
        if (serviceStopTime == null) {
            return null;
        } else {
            return (Date) serviceStopTime.clone();
        }
    }

    public void setServiceStopTime(Date serviceStopTime) {
        if (serviceStopTime != null) {
            this.serviceStopTime = (Date) serviceStopTime.clone();
        } else {
            this.serviceStopTime = null;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeCodeDisplayName() {
        return typeCodeDisplayName;
    }

    public void setTypeCodeDisplayName(String typeCodeDisplayName) {
        this.typeCodeDisplayName = typeCodeDisplayName;
    }

    public String getTypeCodeScheme() {
        return typeCodeScheme;
    }

    public void setTypeCodeScheme(String typeCodeScheme) {
        this.typeCodeScheme = typeCodeScheme;
    }

    public byte[] getRawData() {
        if (rawData == null) {
            return null;
        } else {
            return rawData.clone();
        }
    }

    public void setRawData(byte[] rawData) {
        if (rawData != null) {
            this.rawData = rawData.clone();
        } else {
            this.rawData = null;
        }
    }

    public boolean isOnDemand() {
        return onDemand;
    }

    public void setOnDemand(boolean onDemand) {
        this.onDemand = onDemand;
    }

    public String getNewDocumentUniqueId() {
        return NewDocumentUniqueId;
    }

    public void setNewDocumentUniqueId(String NewDocumentUniqueId) {
        this.NewDocumentUniqueId = NewDocumentUniqueId;
    }

    public String getNewRepositoryUniqueId() {
        return NewRepositoryUniqueId;
    }

    public void setNewRepositoryUniqueId(String NewRepositoryUniqueId) {
        this.NewRepositoryUniqueId = NewRepositoryUniqueId;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * Getter for a code representing the persistent state of the document. This accessor is only to be used with the
     * persistence layer. Use the isPersistent method to access this property.
     *
     * @return Persistence layer code for persistent flag.
     */
    public int getPersistentCode() {
        return persistent ? 1 : 0;
    }

    /**
     * Setter for the document's persistence code that is used by the persistence layer. Use the isPersistent method to
     * look up this property.
     *
     * @param persistentCode Persistence layer code for the persistence flag.
     */
    public void setPersistentCode(int persistentCode) {
        persistent = persistentCode == 1;

    }

    public Set<EventCode> getEventCodes() {
        return eventCodes;
    }

    public void setEventCodes(Set<EventCode> eventCodes) {
        this.eventCodes = eventCodes;
    }

    public Long getPatientRecordId() {
        return patientRecordId;
    }

    public void setPatientRecordId(Long patientRecordId) {
        this.patientRecordId = patientRecordId;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public String getStatusDisplay() {
        if (status != null) {
            String[] aStatus = status.split(":");
            return aStatus[aStatus.length - 1];
        }
        return null;
    }


    public Document cloneDocument() {

        Document obj = new Document();

        obj.setDocumentUniqueId(documentUniqueId);
        obj.setDocumentTitle(documentTitle);
        obj.setAuthorPerson(authorPerson);
        obj.setAuthorRole(authorRole);
        obj.setAuthorInstitution(authorInstitution);
        obj.setAuthorSpecialty(authorSpecialty);
        obj.setAvailablityStatus(availablityStatus);
        obj.setClassCode(classCode);
        obj.setClassCodeDisplayName(classCodeDisplayName);
        obj.setClassCodeScheme(classCodeScheme);
        obj.setConfidentialityCode(confidentialityCode);
        obj.setConfidentialityCodeScheme(confidentialityCodeScheme);
        obj.setConfidentialityCodeDisplayName(confidentialityCodeDisplayName);
        obj.setCreationTime(creationTime);
        obj.setFormatCode(formatCode);
        obj.setFormatCodeScheme(formatCodeScheme);
        obj.setFormatCodeDisplayName(formatCodeDisplayName);
        obj.setPatientId(patientId);
        obj.setServiceStartTime(serviceStartTime);
        obj.setServiceStopTime(serviceStopTime);
        obj.setStatus(status);
        obj.setComments(comments);
        obj.setHash(hash);
        obj.setFacilityCode(facilityCode);
        obj.setFacilityCodeScheme(facilityCodeScheme);
        obj.setFacilityCodeDisplayName(facilityCodeDisplayName);
        obj.setIntendedRecipientOrganization(intendedRecipientOrganization);
        obj.setIntendedRecipientPerson(intendedRecipientPerson);
        obj.setLanguageCode(languageCode);
        obj.setLegalAuthenticator(legalAuthenticator);
        obj.setMimeType(mimeType);
        obj.setParentDocumentId(parentDocumentId);
        obj.setParentDocumentRelationship(parentDocumentRelationship);
        obj.setPracticeSetting(practiceSetting);
        obj.setPracticeSettingScheme(practiceSettingScheme);
        obj.setPracticeSettingDisplayName(practiceSettingDisplayName);
        obj.setSize(size);
        obj.setSourcePatientId(sourcePatientId);
        obj.setPid3(pid3);
        obj.setPid5(pid5);
        obj.setPid7(pid7);
        obj.setPid8(pid8);
        obj.setPid11(pid11);
        obj.setTypeCode(typeCode);
        obj.setTypeCodeScheme(typeCodeScheme);
        obj.setTypeCodeDisplayName(typeCodeDisplayName);
        obj.setDocumentUri(documentUri);
        obj.setRawData(rawData);
        obj.setOnDemand(onDemand);
        obj.setNewDocumentUniqueId(NewDocumentUniqueId);
        obj.setNewRepositoryUniqueId(NewRepositoryUniqueId);
        obj.setPersistent(persistent);
        obj.setPatientRecordId(patientRecordId);
        obj.setPatientIdentifier(patientIdentifier);

        obj.setDocumentid(null);
        obj.setEventCodes(null);

        return obj;
    }

}
