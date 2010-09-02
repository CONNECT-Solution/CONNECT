/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docrepository.adapter.model;

import java.util.Date;
import java.util.Set;

/**
 * Data class for a document.
 * 
 * @author Neil Webb
 */
public class Document
{
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
    private boolean persistent;
    private Set<EventCode> eventCodes;

    public Document()
    {
        persistent = false;
    }

    public String getAuthorInstitution()
    {
        return authorInstitution;
    }

    public void setAuthorInstitution(String authorInstitution)
    {
        this.authorInstitution = authorInstitution;
    }

    public String getAuthorPerson()
    {
        return authorPerson;
    }

    public void setAuthorPerson(String authorPerson)
    {
        this.authorPerson = authorPerson;
    }

    public String getAuthorRole()
    {
        return authorRole;
    }

    public void setAuthorRole(String authorRole)
    {
        this.authorRole = authorRole;
    }

    public String getAuthorSpecialty()
    {
        return authorSpecialty;
    }

    public void setAuthorSpecialty(String authorSpecialty)
    {
        this.authorSpecialty = authorSpecialty;
    }

    public String getAvailablityStatus()
    {
        return availablityStatus;
    }

    public void setAvailablityStatus(String availablityStatus)
    {
        this.availablityStatus = availablityStatus;
    }

    public String getClassCode()
    {
        return classCode;
    }

    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }

    public String getClassCodeDisplayName()
    {
        return classCodeDisplayName;
    }

    public void setClassCodeDisplayName(String classCodeDisplayName)
    {
        this.classCodeDisplayName = classCodeDisplayName;
    }

    public String getClassCodeScheme()
    {
        return classCodeScheme;
    }

    public void setClassCodeScheme(String classCodeScheme)
    {
        this.classCodeScheme = classCodeScheme;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public String getConfidentialityCode()
    {
        return confidentialityCode;
    }

    public void setConfidentialityCode(String confidentialityCode)
    {
        this.confidentialityCode = confidentialityCode;
    }

    public String getConfidentialityCodeDisplayName()
    {
        return confidentialityCodeDisplayName;
    }

    public void setConfidentialityCodeDisplayName(String confidentialityCodeDisplayName)
    {
        this.confidentialityCodeDisplayName = confidentialityCodeDisplayName;
    }

    public String getConfidentialityCodeScheme()
    {
        return confidentialityCodeScheme;
    }

    public void setConfidentialityCodeScheme(String confidentialityCodeScheme)
    {
        this.confidentialityCodeScheme = confidentialityCodeScheme;
    }

    public String getDocumentTitle()
    {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle)
    {
        this.documentTitle = documentTitle;
    }

    public String getDocumentUniqueId()
    {
        return documentUniqueId;
    }

    public void setDocumentUniqueId(String documentUniqueId)
    {
        this.documentUniqueId = documentUniqueId;
    }

    public String getDocumentUri()
    {
        return documentUri;
    }

    public void setDocumentUri(String documentUri)
    {
        this.documentUri = documentUri;
    }

    public Long getDocumentid()
    {
        return documentid;
    }

    public void setDocumentid(Long documentid)
    {
        this.documentid = documentid;
    }

    public String getFacilityCode()
    {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode)
    {
        this.facilityCode = facilityCode;
    }

    public String getFacilityCodeDisplayName()
    {
        return facilityCodeDisplayName;
    }

    public void setFacilityCodeDisplayName(String facilityCodeDisplayName)
    {
        this.facilityCodeDisplayName = facilityCodeDisplayName;
    }

    public String getFacilityCodeScheme()
    {
        return facilityCodeScheme;
    }

    public void setFacilityCodeScheme(String facilityCodeScheme)
    {
        this.facilityCodeScheme = facilityCodeScheme;
    }

    public String getFormatCode()
    {
        return formatCode;
    }

    public void setFormatCode(String formatCode)
    {
        this.formatCode = formatCode;
    }

    public String getFormatCodeDisplayName()
    {
        return formatCodeDisplayName;
    }

    public void setFormatCodeDisplayName(String formatCodeDisplayName)
    {
        this.formatCodeDisplayName = formatCodeDisplayName;
    }

    public String getFormatCodeScheme()
    {
        return formatCodeScheme;
    }

    public void setFormatCodeScheme(String formatCodeScheme)
    {
        this.formatCodeScheme = formatCodeScheme;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public String getIntendedRecipientOrganization()
    {
        return intendedRecipientOrganization;
    }

    public void setIntendedRecipientOrganization(String intendedRecipientOrganization)
    {
        this.intendedRecipientOrganization = intendedRecipientOrganization;
    }

    public String getIntendedRecipientPerson()
    {
        return intendedRecipientPerson;
    }

    public void setIntendedRecipientPerson(String intendedRecipientPerson)
    {
        this.intendedRecipientPerson = intendedRecipientPerson;
    }

    public String getLanguageCode()
    {
        return languageCode;
    }

    public void setLanguageCode(String languageCode)
    {
        this.languageCode = languageCode;
    }

    public String getLegalAuthenticator()
    {
        return legalAuthenticator;
    }

    public void setLegalAuthenticator(String legalAuthenticator)
    {
        this.legalAuthenticator = legalAuthenticator;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public String getParentDocumentId()
    {
        return parentDocumentId;
    }

    public void setParentDocumentId(String parentDocumentId)
    {
        this.parentDocumentId = parentDocumentId;
    }

    public String getParentDocumentRelationship()
    {
        return parentDocumentRelationship;
    }

    public void setParentDocumentRelationship(String parentDocumentRelationship)
    {
        this.parentDocumentRelationship = parentDocumentRelationship;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getPid11()
    {
        return pid11;
    }

    public void setPid11(String pid11)
    {
        this.pid11 = pid11;
    }

    public String getPid3()
    {
        return pid3;
    }

    public void setPid3(String pid3)
    {
        this.pid3 = pid3;
    }

    public String getPid5()
    {
        return pid5;
    }

    public void setPid5(String pid5)
    {
        this.pid5 = pid5;
    }

    public String getPid7()
    {
        return pid7;
    }

    public void setPid7(String pid7)
    {
        this.pid7 = pid7;
    }

    public String getPid8()
    {
        return pid8;
    }

    public void setPid8(String pid8)
    {
        this.pid8 = pid8;
    }

    public String getPracticeSetting()
    {
        return practiceSetting;
    }

    public void setPracticeSetting(String practiceSetting)
    {
        this.practiceSetting = practiceSetting;
    }

    public String getPracticeSettingDisplayName()
    {
        return practiceSettingDisplayName;
    }

    public void setPracticeSettingDisplayName(String practiceSettingDisplayName)
    {
        this.practiceSettingDisplayName = practiceSettingDisplayName;
    }

    public String getPracticeSettingScheme()
    {
        return practiceSettingScheme;
    }

    public void setPracticeSettingScheme(String practiceSettingScheme)
    {
        this.practiceSettingScheme = practiceSettingScheme;
    }

    public Integer getSize()
    {
        return size;
    }

    public void setSize(Integer size)
    {
        this.size = size;
    }

    public String getSourcePatientId()
    {
        return sourcePatientId;
    }

    public void setSourcePatientId(String sourcePatientId)
    {
        this.sourcePatientId = sourcePatientId;
    }

    public Date getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    public Date getServiceStartTime()
    {
        return serviceStartTime;
    }

    public void setServiceStartTime(Date serviceStartTime)
    {
        this.serviceStartTime = serviceStartTime;
    }

    public Date getServiceStopTime()
    {
        return serviceStopTime;
    }

    public void setServiceStopTime(Date serviceStopTime)
    {
        this.serviceStopTime = serviceStopTime;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTypeCode()
    {
        return typeCode;
    }

    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }

    public String getTypeCodeDisplayName()
    {
        return typeCodeDisplayName;
    }

    public void setTypeCodeDisplayName(String typeCodeDisplayName)
    {
        this.typeCodeDisplayName = typeCodeDisplayName;
    }

    public String getTypeCodeScheme()
    {
        return typeCodeScheme;
    }

    public void setTypeCodeScheme(String typeCodeScheme)
    {
        this.typeCodeScheme = typeCodeScheme;
    }

    public byte[] getRawData()
    {
        return rawData;
    }

    public void setRawData(byte[] rawData)
    {
        this.rawData = rawData;
    }

    public boolean isPersistent()
    {
        return persistent;
    }

    public void setPersistent(boolean persistent)
    {
        this.persistent = persistent;
    }

    /**
     * Getter for a code representing the persistent state of the document.
     * This accessor is only to be used with the persistence layer. Use 
     * the isPersistent method to access this property.
     * 
     * @return Persistence layer code for persistent flag.
     */
    public int getPersistentCode()
    {
        return (persistent ? 1 : 0);
    }

    /**
     * Setter for the document's persistence code that is used by the 
     * persistence layer. Use the isPersistent method to look up this 
     * property.
     * 
     * @param persistentCode Persistence layer code for the persistence flag.
     */
    public void setPersistentCode(int persistentCode)
    {
        persistent = (persistentCode == 1);

    }

    public Set<EventCode> getEventCodes()
    {
        return eventCodes;
    }

    public void setEventCodes(Set<EventCode> eventCodes)
    {
        this.eventCodes = eventCodes;
    }
}
