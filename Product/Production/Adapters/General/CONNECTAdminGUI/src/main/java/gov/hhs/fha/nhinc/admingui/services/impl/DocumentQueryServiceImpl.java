/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.services.DocumentQueryService;
import gov.hhs.fha.nhinc.admingui.services.exception.DocumentMetadataException;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.builder.AdhocQueryRequestBuilder;
import gov.hhs.fha.nhinc.docquery.entity.proxy.EntityDocQueryProxyWebServiceUnsecuredImpl;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResults;
import gov.hhs.fha.nhinc.docquery.model.builder.DocumentMetadataResultsModelBuilder;
import gov.hhs.fha.nhinc.messaging.builder.impl.NhinTargetCommunitiesBuilderImpl;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.model.PatientSearchResults;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DocumentQueryServiceImpl construct an AdhocQueryRequest object with assertion and nhinTargetCommunities object, makes
 * the call using EntityDocQueryProxyWebServiceUnsecuredImpl and process the response.
 *
 * @see EntityDocQueryProxyWebServiceUnsecuredImpl
 *
 * @author tjafri
 */
public class DocumentQueryServiceImpl implements DocumentQueryService {

    private static Logger LOG = LoggerFactory.getLogger(DocumentQueryServiceImpl.class);
    private AdhocQueryRequestBuilder requestBuilder;
    private DocumentMetadataResultsModelBuilder responseBuilder;

    /**
     * Instantiates a new DocumentQueryServiceImpl.
     */
    public DocumentQueryServiceImpl() {

    }

    public DocumentQueryServiceImpl(AdhocQueryRequestBuilder requestBuilder,
            DocumentMetadataResultsModelBuilder responseBuilder) {
        this.requestBuilder = requestBuilder;
        this.responseBuilder = responseBuilder;
    }

    /**
     * Returns a DocumentMetadataResults object
     * <p>
     * Usage: DocumentMetadata d = new DocumentMetadata(); d.setPatientId("D123401"); d.setPatientIdRoot("1.1");
     * d.setOrganization("urn:oid:2.2"); queryForDocuments(d);
     *
     * @param query Patient object populated with patient search criteria.
     * @return PatientSearchResults
     * @see Patient
     * @see PatientSearchResults
     */
    @Override
    public DocumentMetadataResults queryForDocuments(DocumentMetadata query, AssertionType assertion) throws DocumentMetadataException {
        AdhocQueryRequest adhocQueryRequest = createAdhocQueryRequest(query);
        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);
        request.setAssertion(assertion);
        NhinTargetCommunitiesBuilderImpl targetCommunity = new NhinTargetCommunitiesBuilderImpl();
        if (NullChecker.isNullish(query.getOrganization())) {
            throw new DocumentMetadataException("Organization is a required field");
        }
        List<String> targets = new ArrayList<>();
        targets.add(HomeCommunityMap.formatHomeCommunityId(query.getOrganization()));
        targetCommunity.setTargets(targets);
        targetCommunity.build();
        EntityDocQueryProxyWebServiceUnsecuredImpl instance = new EntityDocQueryProxyWebServiceUnsecuredImpl();

        AdhocQueryResponse response = instance.respondingGatewayCrossGatewayQuery(request.getAdhocQueryRequest(),
                request.getAssertion(), targetCommunity.getNhinTargetCommunities());

        return createDocumentMetadataResult(response);
    }

    /**
     * Creates the document metadata result.
     *
     * @param response the response
     * @return the document metadata results
     */
    private DocumentMetadataResults createDocumentMetadataResult(AdhocQueryResponse response) {
        responseBuilder.setAdhocQueryResponse(response);
        responseBuilder.build();
        return responseBuilder.getResults();
    }

    /**
     * Creates the adhoc query request.
     *
     * @param query the query
     * @return the adhoc query request
     */
    private AdhocQueryRequest createAdhocQueryRequest(DocumentMetadata query) {
        if (!StringUtils.isBlank(query.getPatientId())) {
            requestBuilder.setPatientId(query.getPatientId());
        }
        if (!StringUtils.isBlank(query.getPatientIdRoot())) {
            requestBuilder.setPatientIdRoot(query.getPatientIdRoot());
        }
        requestBuilder.setDocumentTypeCode(query.getDocumentType());

        requestBuilder.setCreationTimeFrom(query.getStartTime());

        requestBuilder.setCreationTimeTo(query.getEndTime());

        requestBuilder.build();
        return requestBuilder.getMessage();
    }
}
