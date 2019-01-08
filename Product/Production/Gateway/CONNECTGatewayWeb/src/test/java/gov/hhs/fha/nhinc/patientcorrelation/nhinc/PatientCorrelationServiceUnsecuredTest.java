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
import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.*;
import org.junit.Test;

public class PatientCorrelationServiceUnsecuredTest {

    Mockery context = new JUnit4Mockery();

    @SuppressWarnings("unchecked")
    final PatientCorrelationServiceFactory<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> mockFactory = (PatientCorrelationServiceFactory<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType>) context
            .mock(PatientCorrelationServiceFactory.class);
    @SuppressWarnings("unchecked")
    final PatientCorrelationService<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType> mockService = (PatientCorrelationService<RetrievePatientCorrelationsRequestType, RetrievePatientCorrelationsResponseType, AddPatientCorrelationRequestType, AddPatientCorrelationResponseType>) context
            .mock(PatientCorrelationService.class);

    @Test
    public void defaultConstructor() {
        assertNotNull(new PatientCorrelationServiceUnsecured());
    }

    @Test
    public void addPatientCorrelation() {
        final AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
        final AddPatientCorrelationResponseType expectedResponse = new AddPatientCorrelationResponseType();
        context.checking(new Expectations() {
            {

                oneOf(mockFactory).createPatientCorrelationService();
                will(returnValue(mockService));

                oneOf(mockService).addPatientCorrelation(with(same(request)), with(aNull(AssertionType.class)));
                will(returnValue(expectedResponse));

            }
        });

        PatientCorrelationServiceUnsecured serviceUnderTest = new PatientCorrelationServiceUnsecured(mockFactory);
        AddPatientCorrelationResponseType actualResponse = serviceUnderTest.addPatientCorrelation(request);
        assertSame(expectedResponse, actualResponse);

    }

    @Test
    public void retrievePatientCorrelation() {
        final RetrievePatientCorrelationsRequestType request = new RetrievePatientCorrelationsRequestType();
        final RetrievePatientCorrelationsResponseType expectedResponse = new RetrievePatientCorrelationsResponseType();
        context.checking(new Expectations() {
            {

                oneOf(mockFactory).createPatientCorrelationService();
                will(returnValue(mockService));

                oneOf(mockService).retrievePatientCorrelations(with(same(request)), with(aNull(AssertionType.class)));
                will(returnValue(expectedResponse));

            }
        });

        PatientCorrelationServiceUnsecured serviceUnderTest = new PatientCorrelationServiceUnsecured(mockFactory);
        RetrievePatientCorrelationsResponseType actualResponse = serviceUnderTest.retrievePatientCorrelations(request);
        assertSame(expectedResponse, actualResponse);

    }

}
