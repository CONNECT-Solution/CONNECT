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
package gov.hhs.fha.nhinc.patientlocationquery.services;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQueryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQuerySecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterPatientLocationQuerySecuredResponseType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.model.RecordLocatorService;
import gov.hhs.fha.nhinc.patientlocationquery.dao.RecordLocationServiceDAO;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType.PatientLocationResponse;
import java.util.List;
import org.hl7.v3.II;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ptambellini
 *
 */
public class PatientLocationQueryImpl implements PatientLocationQuery {
    private static final Logger LOG = LoggerFactory.getLogger(PatientLocationQuery.class);
    private static PatientLocationQuery patientLocationQuery = new PatientLocationQueryImpl();

    private PatientLocationQueryImpl() {
        LOG.info("PatientLocationQueryImpl - Initialized");
    }

    public static PatientLocationQuery getPatientLocationQuery() {
        LOG.debug("getPatientLocationQuery()..");
        return patientLocationQuery;
    }

    @Override
    public List<RecordLocatorService> getAllPatientsBy(String requestedPatientId) {

        List<RecordLocatorService> requestedPatientIdList;

        requestedPatientIdList = RecordLocationServiceDAO.getAllPatientsBy(requestedPatientId);

        LOG.debug("RecordLocationService.findAllPatientsBy() - End");

        return requestedPatientIdList;
    }

    @Override
    public AdapterPatientLocationQueryResponseType getAdapterPLQResponse(AdapterPatientLocationQueryRequestType msg) {

        AdapterPatientLocationQueryResponseType response = new AdapterPatientLocationQueryResponseType();
        if (msg != null && msg.getPatientLocationQueryRequest() != null
            && msg.getPatientLocationQueryRequest().getRequestedPatientId() != null) {
            II requestedPatientId = msg.getPatientLocationQueryRequest().getRequestedPatientId();
            String requestedPatientid = msg.getPatientLocationQueryRequest().getRequestedPatientId().getExtension();

            List<RecordLocatorService> requestedPatientIdList = getAllPatientsBy(requestedPatientid);

            List<PatientLocationResponse> plqList = response.getPatientLocationQueryResponse()
                .getPatientLocationResponse();
            for (RecordLocatorService rec : requestedPatientIdList) {
                plqList.add(convertPatientLocationResponse(rec, requestedPatientId));
            }

        }
        return response;
    }

    @Override
    public AdapterPatientLocationQuerySecuredResponseType getAdapterPLQSecuredResponse(
        AdapterPatientLocationQuerySecuredRequestType msg) {

        AdapterPatientLocationQuerySecuredResponseType response = new AdapterPatientLocationQuerySecuredResponseType();
        response.setPatientLocationQueryResponse(new PatientLocationQueryResponseType());
        if (msg != null && msg.getPatientLocationQueryRequest() != null
            && msg.getPatientLocationQueryRequest().getRequestedPatientId() != null) {
            II requestPatientId = msg.getPatientLocationQueryRequest().getRequestedPatientId();
            String rlsid = msg.getPatientLocationQueryRequest().getRequestedPatientId().getExtension();

            List<RecordLocatorService> rlsList = getAllPatientsBy(rlsid);

            response.setPatientLocationQueryResponse(new PatientLocationQueryResponseType());
            List<PatientLocationResponse> plqList = response.getPatientLocationQueryResponse()
                .getPatientLocationResponse();
            for (RecordLocatorService rec : rlsList) {
                plqList.add(convertPatientLocationResponse(rec, requestPatientId));
            }
        }
        return response;
    }

    private static PatientLocationResponse convertPatientLocationResponse(RecordLocatorService rls,
        II requestPatientId) {
        PatientLocationResponse plr = new PatientLocationResponse();
        plr.setHomeCommunityId(rls.getAssigningAuthorityId());
        II corespondingPatientId = new II();
        corespondingPatientId.setRoot(rls.getAssigningAuthorityId());
        corespondingPatientId.setExtension(rls.getPatientId());
        plr.setCorrespondingPatientId(corespondingPatientId);
        plr.setRequestedPatientId(requestPatientId);
        return plr;
    }
}
