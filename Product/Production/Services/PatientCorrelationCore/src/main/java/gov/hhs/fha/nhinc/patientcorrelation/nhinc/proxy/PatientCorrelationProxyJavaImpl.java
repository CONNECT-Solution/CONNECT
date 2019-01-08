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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.PatientCorrelationOrch;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.PatientCorrelationOrchImpl;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.CorrelatedIdentifiersDaoImpl;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientCorrelationProxyJavaImpl implements PatientCorrelationProxy {

    private PatientCorrelationOrch orchestration = new PatientCorrelationOrchImpl(new CorrelatedIdentifiersDaoImpl());


    /**
     * This method retrieves PatientCorrelation from the targeted community.
     * @param request PRPAIN201309UV02 HL7 type of Request received.
     * @param assertion Assertion received.
     * @return PatientCorrelationresponse.
     */
    @Override
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 request,
        AssertionType assertion) {
        return orchestration.retrievePatientCorrelations(request, assertion);
    }

    /**
     * This method add PatientCorrelations to database.
     * @param request PRPAIN201301UV02 HL7 type of Request received.
     * @param assertion Assertion received.
     * @return PatientCorrelationResponse.
     */
    @Override
    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 request, AssertionType assertion) {
        return orchestration.addPatientCorrelation(request, assertion);
    }

    /**
     * This method add PatientCorrelations to database with record locator service id.
     * @param response PatientLocationQueryResponse HL7 type of response received.
     * @param assertion Assertion received.
     */
    @Override
    public void addPatientCorrelationPLQ(PatientLocationQueryResponseType plqRecords, AssertionType assertion) {
        orchestration.addPatientCorrelationPLQ(plqRecords, assertion);
    }
}
