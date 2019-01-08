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
package gov.hhs.fha.nhinc.docquery.model.builder.impl;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getDate;

import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResult;
import gov.hhs.fha.nhinc.docquery.model.builder.DocumentMetadataResultModelBuilder;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbAdhocQueryResponseHelper;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbAdhocQueryResponseHelperImpl;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.ClassificationScheme;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.IdentificationScheme;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.ResponseSlotName;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses the DQ query response.
 *
 * @author tjafri
 */
class DocumentMetadataResultModelBuilderImpl implements DocumentMetadataResultModelBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentMetadataResultModelBuilderImpl.class);

    /**
     * The result.
     */
    private DocumentMetadataResult result = null;

    /**
     * The extrinsic object.
     */
    private ExtrinsicObjectType extrinsicObject = null;

    /**
     * Instantiates a new document metadata result model builder impl.
     */
    DocumentMetadataResultModelBuilderImpl() {

    }

    @Override
    public void build() {
        result = new DocumentMetadataResult();
        XDSbAdhocQueryResponseHelper helper = new XDSbAdhocQueryResponseHelperImpl();

        String creationDate = helper.getSingleSlotValue(ResponseSlotName.creationTime, extrinsicObject);
        String serviceStartTime = helper.getSingleSlotValue(ResponseSlotName.serviceStartTime, extrinsicObject);
        String serviceStopTime = helper.getSingleSlotValue(ResponseSlotName.serviceStopTime, extrinsicObject);
        result.setLanguageCode(helper.getSingleSlotValue(ResponseSlotName.languageCode, extrinsicObject));
        result.setLegalAuthenticator(helper.getSingleSlotValue(ResponseSlotName.legalAuthenticator, extrinsicObject));
        result.setIntendedRecipient(helper.getSingleSlotValue(ResponseSlotName.intendedRecipient, extrinsicObject));
        try {
            String documentSize = helper.getSingleSlotValue(ResponseSlotName.size, extrinsicObject);
            int size = Integer.parseInt(documentSize);
            result.setSize(size);
        } catch (Exception e) {
            LOG.error("Failed to convert the String to int: {}", e.getLocalizedMessage(), e);
        }
        result.setSourcePatientId(helper.getSingleSlotValue(ResponseSlotName.sourcePatientId, extrinsicObject));
        result.setUri(helper.getSingleSlotValue(ResponseSlotName.URI, extrinsicObject));

        result.setHash(helper.getSingleSlotValue(ResponseSlotName.hash, extrinsicObject));
        result.setHome(extrinsicObject.getHome());

        result.setOpague(extrinsicObject.isIsOpaque());
        result.setId(extrinsicObject.getId());
        result.setMimeType(extrinsicObject.getMimeType());
        result.setObjectType(extrinsicObject.getObjectType());

        result.setStatus(extrinsicObject.getStatus());
        if (extrinsicObject.getName() != null && CollectionUtils.isNotEmpty(extrinsicObject.getName().getLocalizedString())) {
            result.setName(extrinsicObject.getName().getLocalizedString().get(0).getValue());
        }
        if (extrinsicObject.getDescription() != null && CollectionUtils.isNotEmpty(extrinsicObject.getDescription().getLocalizedString())) {
            result.setDescription(extrinsicObject.getDescription().getLocalizedString().get(0).getValue());
        }

        result.setCreationDate(getDate(creationDate));
        result.setServiceStartTime(getDate(serviceStartTime));
        result.setServiceStopTime(getDate(serviceStopTime));

        String repositoryId = helper.getSingleSlotValue(ResponseSlotName.repositoryUniqueId, extrinsicObject);
        result.setRepositoryId(repositoryId);

        // classifications
        String documentTypeCode = helper.getClassificationValue(ClassificationScheme.typeCode, extrinsicObject);
        result.setDocumentTypeCode(documentTypeCode);
        String documentClassCode = helper.getClassificationValue(ClassificationScheme.classCode, extrinsicObject);
        result.setDocumentClassCode(documentClassCode);
        String documentFormatCode = helper.getClassificationValue(ClassificationScheme.formatCode, extrinsicObject);
        result.setDocumentFormatCode(documentFormatCode);

        // author
        RegistryObjectType author = helper.getClassification(ClassificationScheme.Author, extrinsicObject);
        String authorPerson = helper.getSingleSlotValue("authorPerson", author);
        result.setAuthorPerson(authorPerson);
        String authorInstitution = helper.getSingleSlotValue("authorInstitution", author);
        result.setAuthorInstitution(authorInstitution);
        String authorRole = helper.getSingleSlotValue("authorRole", author);
        result.setAuthorRole(authorRole);
        String authorSpecialty = helper.getSingleSlotValue("authorSpecialty", author);
        result.setAuthorSpecialty(authorSpecialty);

        // external ids
        String documentId = helper.getExternalIdentifierValue(IdentificationScheme.uniqueId, extrinsicObject);
        result.setDocumentId(documentId);

        // extrinsic object
        String availabilityStatus = helper.getStatus(extrinsicObject);
        result.setAvailabilityStatus(availabilityStatus);
        String documentTitle = helper.getTitle(extrinsicObject);
        result.setDocumentTitle(documentTitle);
    }

    @Override
    public DocumentMetadataResult getDocumentMetadataResult() {
        return result;
    }

    @Override
    public void setRegistryObjectType(ExtrinsicObjectType extrinsicObject) {
        this.extrinsicObject = extrinsicObject;
    }

}
