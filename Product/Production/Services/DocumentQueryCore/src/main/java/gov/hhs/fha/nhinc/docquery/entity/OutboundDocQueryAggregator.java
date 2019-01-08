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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.docquery.DQMessageGeneratorUtils;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import java.math.BigInteger;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author bhumphrey
 *
 */
public class OutboundDocQueryAggregator implements NhinAggregator {

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateRegistryErrors(AdhocQueryResponse aggregatedResponse, AdhocQueryResponse individualResponse) {
        RegistryErrorList registryErrorList = individualResponse.getRegistryErrorList();
        if (registryErrorList != null && CollectionUtils.isNotEmpty(registryErrorList.getRegistryError())) {

            if (aggregatedResponse.getRegistryErrorList() == null) {
                aggregatedResponse.setRegistryErrorList(new RegistryErrorList());
            }

            aggregatedResponse.getRegistryErrorList().getRegistryError().addAll(registryErrorList.getRegistryError());
        }
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateSlotlistResponse(AdhocQueryResponse aggregatedResponse,
        AdhocQueryResponse individualResponse) {
        SlotListType individualResponseSlotList = individualResponse.getResponseSlotList();
        if (individualResponseSlotList != null && CollectionUtils.isNotEmpty(individualResponseSlotList.getSlot())) {
            aggregatedResponse.getResponseSlotList().getSlot().addAll(individualResponseSlotList.getSlot());
        }
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void collectRegistryObjectResponses(AdhocQueryResponse aggregatedResponse,
        RegistryObjectListType singleRegistryObjectList) {
        RegistryObjectListType registryObjectList = aggregatedResponse.getRegistryObjectList();
        registryObjectList.getIdentifiable().addAll(singleRegistryObjectList.getIdentifiable());
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateRegistryObjectList(AdhocQueryResponse aggregatedResponse,
        AdhocQueryResponse individualResponse) {
        RegistryObjectListType registryObjectList = individualResponse.getRegistryObjectList();
        if (registryObjectList != null) {
            collectRegistryObjectResponses(aggregatedResponse, registryObjectList);
        }
    }

    /**
     * @param aggregateStatus
     * @param individualStatus
     * @return
     */
    protected boolean isEitherParitalSuccess(final String aggregateStatus, final String individualStatus) {
        return aggregateStatus.equalsIgnoreCase(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS)
            || individualStatus.equalsIgnoreCase(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS);
    }

    /**
     * @param individualStatus
     * @param aggregateStatus
     * @return
     */
    protected boolean areTheStatusesDifferent(final String aggregateStatus, final String individualStatus) {
        return !aggregateStatus.equalsIgnoreCase(individualStatus);
    }

    /**
     * @param aggregateStatus
     * @param individualStatus
     * @return
     */
    protected String determineAggregateStatus(final String aggregateStatus, final String individualStatus) {
        if (aggregateStatus == null) {
            return individualStatus;
        } else {
            if (isEitherParitalSuccess(aggregateStatus, individualStatus)) {
                return DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS;
            } else {
                if (areTheStatusesDifferent(aggregateStatus, individualStatus)) {
                    return DocumentConstants.XDS_QUERY_RESPONSE_STATUS_PARTIAL_SUCCESS;
                }
            }
        }
        return aggregateStatus;
    }

    /**
     * @param individualResponse
     * @param aggregatedResponse
     */
    protected void aggregateStatus(AdhocQueryResponse aggregatedResponse, AdhocQueryResponse individualResponse) {
        aggregatedResponse.setStatus(determineAggregateStatus(aggregatedResponse.getStatus(),
            individualResponse.getStatus()));
    }

    /**
     * @param aggregate
     */
    public void increaseCount(AdhocQueryResponse aggregate) {
        aggregate.setTotalResultCount(aggregate.getTotalResultCount().add(BigInteger.ONE));
    }

    /**
     * @param single
     * @param aggregate
     * @throws Exception
     */
    protected void aggregate(AdhocQueryResponse aggregate, AdhocQueryResponse single) {

        aggregateStatus(aggregate, single);

        aggregateRegistryObjectList(aggregate, single);

        aggregateRegistryErrors(aggregate, single);

        aggregateSlotlistResponse(aggregate, single);

        increaseCount(aggregate);
    }

    public void aggregate(OutboundDocQueryOrchestratable to, OutboundDocQueryOrchestratable from) {
        initializeResponse(to);
        AdhocQueryResponse singleResponse = from.getResponse();
        if (singleResponse == null) {
            singleResponse = DQMessageGeneratorUtils.getInstance().createAdhocQueryErrorResponse("Null response.",
                DocumentConstants.XDS_ERRORCODE_REPOSITORY_ERROR,
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
        }
        aggregate(to.getResponse(), singleResponse);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.orchestration.NhinAggregator#aggregate(gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable,
     * gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable)
     */
    @Override
    public void aggregate(OutboundOrchestratable to, OutboundOrchestratable from) {
        if (to instanceof OutboundDocQueryOrchestratable) {
            if (from instanceof OutboundDocQueryOrchestratable) {
                aggregate((OutboundDocQueryOrchestratable) to, (OutboundDocQueryOrchestratable) from);
            }
        }
    }

    private void initializeResponse(OutboundDocQueryOrchestratable aggregatedOrchestrable) {
        AdhocQueryResponse aggreatedResponse = aggregatedOrchestrable.getResponse();
        if (aggreatedResponse == null) {
            aggreatedResponse = new AdhocQueryResponse();
            aggreatedResponse.setStartIndex(BigInteger.ZERO);
            aggreatedResponse.setTotalResultCount(BigInteger.ZERO);
            aggregatedOrchestrable.setResponse(aggreatedResponse);
        }

        RegistryObjectListType registryObjectList = aggreatedResponse.getRegistryObjectList();

        if (registryObjectList == null) {
            registryObjectList = new RegistryObjectListType();
            aggreatedResponse.setRegistryObjectList(registryObjectList);
        }

        if (aggreatedResponse.getResponseSlotList() == null) {
            aggreatedResponse.setResponseSlotList(new SlotListType());
        }

    }

}
