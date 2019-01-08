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
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationAddPLQServicePortDescriptor;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationRetrieveServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import org.hl7.v3.AddPatientCorrelationPLQRequestType;
import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 *
 */
public class PatientCorrelationProxyWebServiceUnsecuredImpl implements PatientCorrelationProxy {

    private static final String UNABLE_TO_CALL = "Unable to call Patient Correlation Service";
    private static final String ASSERTION_MUST_BE_PROVIDED = "Assertion must be provided";
    private static final String REQUEST_MESSAGE_MUST_BE_PROVIDED = "Request Message must be provided";
    private static final Logger LOG = LoggerFactory.getLogger(PatientCorrelationProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    /**
     * Default Constructor.
     */
    public PatientCorrelationProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    /**
     * @return WebServiceProxyHelper Object.
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClientFactory getCONNECTClientFactory() {
        return CONNECTClientFactory.getInstance();
    }


    public ServicePortDescriptor<PatientCorrelationPortType> getRetrieveServicePortDescriptor() {
        return new PatientCorrelationRetrieveServicePortDescriptor();
    }

    /**
     * This method retrieves PatientCorrelation from the targeted community.
     *
     * @param msg PRPAIN201309UV02 HL7 type of Request received.
     * @param assertion Assertion received.
     * @return PatientCorrelationresponse.
     */
    @Override
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(final PRPAIN201309UV02 msg,
        final AssertionType assertion) {
        LOG.debug("Begin retrievePatientCorrelations");
        RetrievePatientCorrelationsResponseType response = null;

        try {
            final String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);

            if (msg == null) {
                throw new IllegalArgumentException(REQUEST_MESSAGE_MUST_BE_PROVIDED);
            } else if (assertion == null) {
                throw new IllegalArgumentException(ASSERTION_MUST_BE_PROVIDED);
            } else {
                final RetrievePatientCorrelationsRequestType request = new RetrievePatientCorrelationsRequestType();
                request.setPRPAIN201309UV02(msg);
                request.setAssertion(assertion);

                final ServicePortDescriptor<PatientCorrelationPortType> portDescriptor = getRetrieveServicePortDescriptor();

                final CONNECTClient<PatientCorrelationPortType> client = getCONNECTClientFactory()
                    .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                response = (RetrievePatientCorrelationsResponseType) client.invokePort(PatientCorrelationPortType.class,
                    "retrievePatientCorrelations", request);
            }
        } catch (final Exception ex) {
            throw new ErrorEventException(ex,UNABLE_TO_CALL);
        }

        LOG.debug("End retrievePatientCorrelations");
        return response;
    }

    /**
     * This method add PatientCorrelations to database.
     *
     * @param msg PRPAIN201301UV02 HL7 type of Request received.
     * @param assertion Assertion received.
     * @return PatientCorrelationResponse.
     */
    @Override
    public AddPatientCorrelationResponseType addPatientCorrelation(final PRPAIN201301UV02 msg,
        final AssertionType assertion) {
        LOG.debug("Begin addPatientCorrelation");
        AddPatientCorrelationResponseType response = null;

        try {
            final String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
            if (msg == null) {
                throw new IllegalArgumentException(REQUEST_MESSAGE_MUST_BE_PROVIDED);
            } else if (assertion == null) {
                throw new IllegalArgumentException(ASSERTION_MUST_BE_PROVIDED);
            } else {
                final AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
                request.setPRPAIN201301UV02(msg);
                request.setAssertion(assertion);

                final ServicePortDescriptor<PatientCorrelationPortType> portDescriptor = getRetrieveServicePortDescriptor();

                final CONNECTClient<PatientCorrelationPortType> client = getCONNECTClientFactory()
                    .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                response = (AddPatientCorrelationResponseType) client.invokePort(PatientCorrelationPortType.class,
                    "addPatientCorrelation", request);
            }
        } catch (final Exception ex) {
            throw new ErrorEventException(ex,UNABLE_TO_CALL);
        }

        LOG.debug("End addPatientCorrelation");
        return response;
    }
    @Override
    public void addPatientCorrelationPLQ(PatientLocationQueryResponseType plqRecords,
        AssertionType assertion) {
        AddPatientCorrelationPLQRequestType request = new AddPatientCorrelationPLQRequestType();
        request.setPatientLocationQueryResponse(plqRecords);
        request.setAssertion(assertion);

        try {
            final String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
            if (plqRecords == null) {
                throw new IllegalArgumentException(REQUEST_MESSAGE_MUST_BE_PROVIDED);
            } else if (assertion == null) {
                throw new IllegalArgumentException(ASSERTION_MUST_BE_PROVIDED);
            } else {

                final ServicePortDescriptor<PatientCorrelationPortType> portDescriptor = new PatientCorrelationAddPLQServicePortDescriptor();

                final CONNECTClient<PatientCorrelationPortType> client = getCONNECTClientFactory()
                    .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                client.invokePort(PatientCorrelationPortType.class,"addPatientCorrelationPLQ", request);
            }
        } catch (final Exception ex) {
            throw new ErrorEventException(ex,UNABLE_TO_CALL);
        }
        LOG.debug("End addPatientCorrelationPLQ");
    }
}
