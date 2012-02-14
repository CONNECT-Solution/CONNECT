/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.gateway.executorservice;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;

import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements the AdhocQueryResponse (DQClient Response) Aggregation Strategy Each response returned from a
 * CallableRequest comes to processResponse where it is aggregated into the cumulativeResponse
 * 
 * No additional processing is required for DQProcessor other than response aggregation
 * 
 * @author paul.eftis
 */
public class DQProcessor<Target extends QualifiedSubjectIdentifierType, Request extends AdhocQueryRequest, Response extends AdhocQueryResponse, CumulativeResponse extends AdhocQueryResponse>
        extends ResponseProcessor<Target, Request, Response, CumulativeResponse> {

    private Log log = LogFactory.getLog(getClass());

    private AdhocQueryResponse cumulativeResponse = null;

    private static final QName ExtrinsicObjectQname = new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0",
            "ExtrinsicObject");
    private static final String XDS_RESPONSE_STATUS_SUCCESS = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    private static final String XDS_RESPONSE_STATUS_FAILURE = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";

    public DQProcessor() {
        super();
        // create new cumulativeResponse object
        cumulativeResponse = new AdhocQueryResponse();
        cumulativeResponse.setStatus(XDS_RESPONSE_STATUS_SUCCESS);
        cumulativeResponse.setStartIndex(BigInteger.ZERO);
        cumulativeResponse.setTotalResultCount(BigInteger.ZERO);

        RegistryObjectListType regObj = new RegistryObjectListType();
        cumulativeResponse.setRegistryObjectList(regObj);
        SlotListType slots = new SlotListType();
        cumulativeResponse.setResponseSlotList(slots);
        RegistryErrorList regErr = new RegistryErrorList();
        cumulativeResponse.setRegistryErrorList(regErr);
        log.debug("DQProcessor constructed initial cumulativeResponse");
    }

    @Override
    public CumulativeResponse getCumulativeResponse() {
        return (CumulativeResponse) cumulativeResponse;
    }

    /**
     * Synchronization is covered by blocking queue implemented by the ExecutorCompletionService (i.e. we do not need to
     * synchronize anything in processResponse or combineResponses)
     * 
     * In TaskExecutor we have the following: Future<Result> fut = executorCompletionService.take(); which will block
     * until a result is available. Once result is available it will be processed here and complete processing before
     * the next future is retrieved and processed, so processor does not have to be concerned with synchronization.
     * 
     * @param request is not used/needed for DQ processing
     * @param individual is the AdhocQueryResponse response returned from the CallableRequest
     * @param Target t is not used in DQProcessor
     */
    @Override
    public void processResponse(Request request, Response individual, Target t) {
        try {
            log.debug("DQProcessor::processResponse");
            processDQResponse(individual);
        } catch (Exception e) {
            // add error response for exception to cumulativeResponse
            RegistryError regErr = new RegistryError();
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setCodeContext(e.getMessage());
            regErr.setValue("Error from target aaId=" + t.getAssigningAuthorityIdentifier()
                    + " and target subjectIdentifier=" + t.getSubjectIdentifier());
            regErr.setSeverity("Error");
            cumulativeResponse.getRegistryErrorList().getRegistryError().add(regErr);
            cumulativeResponse.setTotalResultCount(cumulativeResponse.getTotalResultCount().add(BigInteger.ONE));
        }
    }

    /**
     * Called from CallableRequest for any exception from the WebServiceClient Generate a new AdhocQueryResponse with
     * the error and the source of the error
     * 
     * @param String error (exception message)
     * @param Request is not used/needed for DQ processing
     * @param Target t that generated the exception/error
     * @return Response AdhocQueryResponse object with the error
     */
    @Override
    public Response processError(String error, Request r, Target t) {
        log.debug("DQProcessor::processError error=" + error);
        AdhocQueryResponse adhocresponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        adhocresponse.setStatus(XDS_RESPONSE_STATUS_FAILURE);
        RegistryError regErr = new RegistryError();
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setCodeContext(error);
        regErr.setValue("Error from target aaId=" + t.getAssigningAuthorityIdentifier()
                + " and target subjectIdentifier=" + t.getSubjectIdentifier());
        regErr.setSeverity("Error");
        regErrList.getRegistryError().add(regErr);
        adhocresponse.setRegistryErrorList(regErrList);
        return (Response) adhocresponse;
    }

    /**
     * DQ response processing only requires aggregation of the responses onto the cumulativeResponse
     * 
     * @param current is the AdhocQueryResponse returned from the CallableRequest WebServiceClient
     */
    @SuppressWarnings("static-access")
    private void processDQResponse(AdhocQueryResponse current) throws Exception {
        if (current == null) {
            throw new Exception("AdhocQueryResponse received was null.");
        }
        try {
            // add the responses from registry object list
            if (current.getRegistryObjectList() != null) {
                List<JAXBElement<? extends IdentifiableType>> IdentifiableList = current.getRegistryObjectList()
                        .getIdentifiable();
                if (IdentifiableList != null) {
                    for (JAXBElement<? extends IdentifiableType> identifiable : IdentifiableList) {
                        ExtrinsicObjectType currentExtrinsicObject = cast(identifiable, ExtrinsicObjectType.class);
                        JAXBElement<ExtrinsicObjectType> identifiableObj = new JAXBElement<ExtrinsicObjectType>(
                                ExtrinsicObjectQname, ExtrinsicObjectType.class, currentExtrinsicObject);
                        cumulativeResponse.getRegistryObjectList().getIdentifiable().add(identifiableObj);
                    }
                }
            }

            // add any registry errors
            if (current.getRegistryErrorList() != null && current.getRegistryErrorList().getRegistryError() != null
                    && current.getRegistryErrorList().getRegistryError().size() > 0) {
                for (RegistryError re : current.getRegistryErrorList().getRegistryError()) {
                    cumulativeResponse.getRegistryErrorList().getRegistryError().add(re);
                }
            }

            // add any slotlist response data
            if (current.getResponseSlotList() != null && current.getResponseSlotList().getSlot() != null
                    && current.getResponseSlotList().getSlot().size() > 0) {
                for (SlotType1 slot : current.getResponseSlotList().getSlot()) {
                    cumulativeResponse.getResponseSlotList().getSlot().add(slot);
                }
            }
            cumulativeResponse.setTotalResultCount(cumulativeResponse.getTotalResultCount().add(BigInteger.ONE));
            log.debug("DQProcessor::processDQResponse combine next response done cumulativeResponse count="
                    + cumulativeResponse.getTotalResultCount().toString());
        } catch (Exception ex) {
            ExecutorServiceHelper.getInstance().outputCompleteException(ex);
            throw ex;
        }
    }

    private <T extends IdentifiableType> T cast(JAXBElement<? extends IdentifiableType> identifiable, Class<T> type) {
        if ((identifiable.getDeclaredType() == type) || identifiable.getValue().getClass() == type) {
            return type.cast(identifiable.getValue());
        }
        return null;
    }

}
