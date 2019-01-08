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
package gov.hhs.fha.nhinc.docquery.builder.impl;

import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbAdhocQueryRequestHelper;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbAdhocQueryRequestHelperImpl;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.RegistryStoredQueryParameter;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.XDSQueryStatus;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.XDSbStoredQuery;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author tjafri
 */
public class FindDocumentsAdhocQueryRequestBuilder extends AbstractAdhocQueryRequestBuilder {

    private final String loincSchemaCode = "2.16.840.1.113883.6.1";

    /**
     * The helper.
     */
    private XDSbAdhocQueryRequestHelper helper = null;

    /**
     * The request.
     */
    private AdhocQueryRequest request = null;

    /**
     * The query id.
     */
    private XDSbStoredQuery queryId = XDSbStoredQuery.FindDocuments;

    /**
     * The patient id.
     */
    private String patientId = null;

    private String patientIdRoot = null;

    /**
     * The status.
     */
    private XDSQueryStatus status = XDSQueryStatus.APPROVED;

    /**
     * The creation time from.
     */
    private Date creationTimeFrom = null;

    /**
     * The creation time to.
     */
    private Date creationTimeTo = null;

    /**
     * The document type code.
     */
    private List<String> documentTypeCode = null;

    /**
     * Instantiates a new find documents adhoc query request builder.
     */
    public FindDocumentsAdhocQueryRequestBuilder() {
        this.helper = new XDSbAdhocQueryRequestHelperImpl();
    }

    /**
     * Instantiates a new find documents adhoc query request builder.
     *
     * @param helper the helper
     */
    FindDocumentsAdhocQueryRequestBuilder(XDSbAdhocQueryRequestHelper helper) {
        this.helper = helper;
    }

    @Override
    public void build() {
        if (StringUtils.isBlank(patientId)) {
            throw new IllegalArgumentException("Patient Id is a required value.");
        }

        super.build();
        request = super.getMessage();

        // required parameters
        StringBuilder builder = new StringBuilder();
        builder.append(patientId);
        builder.append("^^^&");
        builder.append(patientIdRoot);
        builder.append("&ISO");
        String qualifiedPatientId = builder.toString();
        String delimitedPatientId = helper.createSingleQuoteDelimitedValue(qualifiedPatientId);
        helper.createOrReplaceSlotValue(RegistryStoredQueryParameter.$XDSDocumentEntryPatientId, delimitedPatientId,
            request);
        String delimitedStatuses = helper.createSingleQuoteDelimitedListValue(Collections.singletonList(status
            .toString()));
        helper.createOrReplaceSlotValue(RegistryStoredQueryParameter.$XDSDocumentEntryStatus, delimitedStatuses,
            request);

        // optional parameters
        if (creationTimeFrom != null) {
            helper.createOrReplaceSlotValue(RegistryStoredQueryParameter.$XDSDocumentEntryCreationTimeFrom,
                helper.formatXDSbDate(creationTimeFrom), request);
        }
        if (creationTimeTo != null) {
            helper.createOrReplaceSlotValue(RegistryStoredQueryParameter.$XDSDocumentEntryCreationTimeTo,
                helper.formatXDSbDate(creationTimeTo), request);
        }
        if (!documentTypeCode.isEmpty()) {
            helper.createOrReplaceSlotValue(RegistryStoredQueryParameter.$XDSDocumentEntryClassCode,
                helper.createCodeSchemeValue(documentTypeCode, loincSchemaCode), request);
        }

    }

    @Override
    public AdhocQueryRequest getMessage() {
        return request;
    }

    /**
     * Sets the patient id.
     *
     * @param patientId the new patient id
     */
    @Override
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Sets the query status.
     *
     * @param status the new query status
     */
    public void setQueryStatus(XDSbConstants.XDSQueryStatus status) {
        this.status = status;
    }

    /**
     * Sets the creation time from.
     *
     * @param creationTimeFrom the new creation time from
     */
    @Override
    public void setCreationTimeFrom(Date creationTimeFrom) {
        this.creationTimeFrom = creationTimeFrom;
    }

    /**
     * Sets the creation time to.
     *
     * @param creationTimeTo the new creation time to
     */
    @Override
    public void setCreationTimeTo(Date creationTimeTo) {
        this.creationTimeTo = creationTimeTo;
    }

    /**
     * Sets the document type code.
     *
     * @param documentTypeCode the new document type code
     */
    @Override
    public void setDocumentTypeCode(List<String> documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    @Override
    public XDSbStoredQuery getQueryId() {
        return queryId;
    }

    @Override
    public void setPatientIdRoot(String patientIdRoot) {
        this.patientIdRoot = patientIdRoot;
    }
}
