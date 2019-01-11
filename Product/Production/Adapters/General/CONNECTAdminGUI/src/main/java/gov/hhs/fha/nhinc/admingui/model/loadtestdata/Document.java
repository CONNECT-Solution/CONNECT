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
package gov.hhs.fha.nhinc.admingui.model.loadtestdata;

import gov.hhs.fha.nhinc.common.loadtestdatamanagement.DocumentMetadataType;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.util.Date;

/**
 * @author ttang
 *
 */
public class Document extends DocumentMetadataType {

    public Document() {
    }

    public Document(DocumentMetadataType parent){
        setDocumentId(parent.getDocumentId());
        setDocumentUniqueId(parent.getDocumentUniqueId());
        setDocumentTitle(parent.getDocumentTitle());
        setAuthorPerson(parent.getAuthorPerson());
        setAuthorInstitution(parent.getAuthorInstitution());
        setAuthorRole(parent.getAuthorRole());
        setAuthorSpecialty(parent.getAuthorSpecialty());
        setAvailablityStatus(parent.getAvailablityStatus());
        setClassCode(parent.getClassCode())       ;
        setClassCodeScheme(parent.getClassCodeScheme());
        setClassCodeDisplayName(parent.getClassCodeDisplayName());
        setConfidentialityCode(parent.getConfidentialityCode());
        setConfidentialityCodeDisplayName(parent.getConfidentialityCodeDisplayName());
        setConfidentialityCodeScheme(parent.getConfidentialityCodeScheme());
        setCreationTime(parent.getCreationTime());
        setFormatCode(parent.getFormatCode());
        setFormatCodeScheme(parent.getFormatCodeScheme());
        setFormatCodeDisplayName(parent.getFormatCodeDisplayName());
        setPatientId(parent.getPatientId());
        setServiceStartTime(parent.getServiceStartTime());
        setServiceStopTime(parent.getServiceStopTime());
        setStatus(parent.getStatus());
        setComments(parent.getComments());
        setHash(parent.getHash());
        setFacilityCode(parent.getFacilityCode());
        setFacilityCodeDisplayName(parent.getFacilityCodeDisplayName());
        setFacilityCodeScheme(parent.getFacilityCodeScheme());
        setIntendedRecipientOrganization(parent.getIntendedRecipientOrganization());
        setIntendedRecipientPerson(parent.getIntendedRecipientPerson());
        setLanguageCode(parent.getLanguageCode());
        setLegalAuthenticator(parent.getLegalAuthenticator());
        setMimeType(parent.getMimeType());
        setParentDocumentId(parent.getParentDocumentId());
        setParentDocumentRelationship(parent.getParentDocumentRelationship());
        setPracticeSetting(parent.getPracticeSetting());
        setPracticeSettingScheme(parent.getPracticeSettingScheme());
        setPracticeSettingDisplayName(parent.getPracticeSettingDisplayName());
        setSize(parent.getSize());
        setSourcePatientId(parent.getSourcePatientId());
        setPid3(parent.getPid3());
        setPid5(parent.getPid5());
        setPid8(parent.getPid8());
        setPid7(parent.getPid7());
        setPid11(parent.getPid11());
        setTypeCode(parent.getTypeCode());
        setTypeCodeScheme(parent.getTypeCodeScheme());
        setTypeCodeDisplayName(parent.getTypeCodeDisplayName());
        setDocumentUri(parent.getDocumentUri());
        setOnDemand(parent.isOnDemand());
        setNewDocumentUniqueId(parent.getNewDocumentUniqueId());
        setNewRepositoryUniqueId(parent.getNewRepositoryUniqueId());
        setPersistent(parent.isPersistent());
        getEventCodeList().addAll(parent.getEventCodeList());
        setPatientRecordId(parent.getPatientRecordId());
        setPatientIdentifier(parent.getPatientIdentifier());
        setDocument(parent.getDocument());
        setStatusDisplay(parent.getStatusDisplay());
    }

    public Date getCreationTimeDate() {
        return CoreHelpUtils.getDate(getCreationTime());
    }

    public void setCreationTimeDate(Date date) {
        setCreationTime(CoreHelpUtils.getXMLGregorianCalendarFrom(date));
    }

    public Date getServiceStartTimeDate() {
        return CoreHelpUtils.getDate(getServiceStartTime());
    }

    public void setServiceStartTimeDate(Date date) {
        setServiceStartTime(CoreHelpUtils.getXMLGregorianCalendarFrom(date));
    }

    public Date getServiceStopTimeDate() {
        return CoreHelpUtils.getDate(getServiceStopTime());
    }

    public void setServiceStopTimeDate(Date date) {
        setServiceStopTime(CoreHelpUtils.getXMLGregorianCalendarFrom(date));
    }
}
