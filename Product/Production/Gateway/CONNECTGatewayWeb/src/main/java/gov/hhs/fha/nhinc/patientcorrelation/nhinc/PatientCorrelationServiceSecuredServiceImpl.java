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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author svalluripalli
 */
public class PatientCorrelationServiceSecuredServiceImpl
        implements
        PatientCorrelationService<RetrievePatientCorrelationsSecuredRequestType, RetrievePatientCorrelationsSecuredResponseType, AddPatientCorrelationSecuredRequestType, AddPatientCorrelationSecuredResponseType>

{

    private PatientCorrelationOrch orchestration;

    private static final Logger LOG = LoggerFactory.getLogger(PatientCorrelationServiceSecuredServiceImpl.class);

    PatientCorrelationServiceSecuredServiceImpl(PatientCorrelationOrch orchestration) {
        this.orchestration = orchestration;
    }

    @Override
    public RetrievePatientCorrelationsSecuredResponseType retrievePatientCorrelations(
            RetrievePatientCorrelationsSecuredRequestType request, AssertionType assertion) {
        RetrievePatientCorrelationsSecuredResponseType response = new RetrievePatientCorrelationsSecuredResponseType();

        LOG.info("Calling the Patient Correlation Retrieve Correlations Orch Impl");
        RetrievePatientCorrelationsResponseType unsecureResp = orchestration.retrievePatientCorrelations(
                request.getPRPAIN201309UV02(), assertion);

        if (unsecureResp != null && unsecureResp.getPRPAIN201310UV02() != null) {
            response.setPRPAIN201310UV02(unsecureResp.getPRPAIN201310UV02());
        }
        return response;
    }

    @Override
    public AddPatientCorrelationSecuredResponseType addPatientCorrelation(
            AddPatientCorrelationSecuredRequestType request, AssertionType assertion) {
        AddPatientCorrelationSecuredResponseType response = new AddPatientCorrelationSecuredResponseType();

        LOG.info("Calling the Patient Correlation Add Correlations Orch Impl");
        AddPatientCorrelationResponseType unsecureResp = orchestration.addPatientCorrelation(
                request.getPRPAIN201301UV02(), assertion);

        if (unsecureResp != null && unsecureResp.getMCCIIN000002UV01() != null) {
            response.setMCCIIN000002UV01(unsecureResp.getMCCIIN000002UV01());
        }

        return response;
    }

}
