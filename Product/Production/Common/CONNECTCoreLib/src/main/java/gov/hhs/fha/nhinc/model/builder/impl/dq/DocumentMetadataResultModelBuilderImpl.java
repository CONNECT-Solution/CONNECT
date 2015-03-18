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
package gov.hhs.fha.nhinc.model.builder.impl.dq;

import gov.hhs.fha.nhinc.xdsb.helper.XDSbAdhocQueryResponseHelper;
import gov.hhs.fha.nhinc.xdsb.helper.XDSbAdhocQueryResponseHelperImpl;
import gov.hhs.fha.nhinc.xdsb.helper.XDSbConstants.ClassificationScheme;
import gov.hhs.fha.nhinc.xdsb.helper.XDSbConstants.IdentificationScheme;
import gov.hhs.fha.nhinc.xdsb.helper.XDSbConstants.ResponseSlotName;
import gov.hhs.fha.nhinc.model.DocumentMetadataResult;
import gov.hhs.fha.nhinc.model.builder.DocumentMetadataResultModelBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import org.apache.commons.lang.StringUtils;

/**
 * This class parses the DQ query response.
 *
 * @author tjafri
 */
class DocumentMetadataResultModelBuilderImpl implements DocumentMetadataResultModelBuilder {

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

        // slot values
        String creationDate = helper.getSingleSlotValue(ResponseSlotName.creationTime, extrinsicObject);
        String serviceStartTime = helper.getSingleSlotValue(ResponseSlotName.serviceStartTime, extrinsicObject);
        String serviceStopTime = helper.getSingleSlotValue(ResponseSlotName.serviceStopTime, extrinsicObject);
        try {
            result.setCreationDate(formatDatePretty(creationDate));
            result.setServiceStartTime(formatDateTimePretty(serviceStartTime));
            result.setServiceStopTime(formatDateTimePretty(serviceStopTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    /**
     * Format date time pretty.
     *
     * @param date the date
     * @return the string
     * @throws ParseException the parse exception
     */
    private String formatDateTimePretty(String date) throws ParseException {
        String prettyDateTime = null;
        if (!StringUtils.isBlank(date)) {
            Date dDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
            prettyDateTime = new SimpleDateFormat("M dd, yyyy, HH:mm Z").format(dDate);
        }
        return prettyDateTime;
    }

    /**
     * Format date pretty.
     *
     * @param date the date
     * @return the string
     * @throws ParseException the parse exception
     */
    private String formatDatePretty(String date) throws ParseException {
        String prettyDate = null;
        if (!StringUtils.isBlank(date)) {
            Date dDate = new SimpleDateFormat("yyyyMMdd").parse(date);
            prettyDate = new SimpleDateFormat("dd/MMMM/yyyy").format(dDate);
        }
        return prettyDate;
    }
}
