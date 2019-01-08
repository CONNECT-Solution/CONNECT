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
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationSecuredAddPLQServicePortDescriptor;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationSecuredAddServicePortDescriptor;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationSecuredRetrieveServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import org.hl7.v3.AddPatientCorrelationPLQSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 *
 */
public class PatientCorrelationProxyWebServiceSecuredImpl implements PatientCorrelationProxy {

    private static final String REQUEST_MESSAGE_MUST_BE_PROVIDED = "Request Message must be provided";
    private static final String UNABLE_TO_CALL = "Unable to call Patient Correlation Service";
    private static final Logger LOG = LoggerFactory.getLogger(PatientCorrelationProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    /**
     * Default Constructor.
     */
    public PatientCorrelationProxyWebServiceSecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }


    /**
     * @return WebServiceProxyHelper Object.
     */
    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves WS_ADDRESSING_ACTION_RETRIEVE for PatientCorrelationService.
     *
     * @param apiLevel Adapter apiLevel (this is a0,a1).
     * @return WS_ADDRESSING_ACTION_RETRIEVE.
     */
    public ServicePortDescriptor<PatientCorrelationSecuredPortType> getRetrieveServicePortDescriptor(
        final NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new PatientCorrelationSecuredRetrieveServicePortDescriptor();
    }

    /**
     * This method returns WS_ADDRESSING_ACTION_ADD for PatientCorrelationService.
     *
     * @param apiLevel Adapter apiLevel (this is a0,a1).
     * @return WS_ADDRESSING_ACTION_ADD.
     */
    public ServicePortDescriptor<PatientCorrelationSecuredPortType> getAddServicePortDescriptor(
        final NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        return new PatientCorrelationSecuredAddServicePortDescriptor();
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
        final RetrievePatientCorrelationsResponseType response = new RetrievePatientCorrelationsResponseType();
        RetrievePatientCorrelationsSecuredResponseType securedResp = new RetrievePatientCorrelationsSecuredResponseType();

        try {
            final String url = oProxyHelper
                .getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);

            if (msg == null) {
                throw new IllegalArgumentException(REQUEST_MESSAGE_MUST_BE_PROVIDED);
            } else {
                final RetrievePatientCorrelationsSecuredRequestType request = new RetrievePatientCorrelationsSecuredRequestType();
                request.setPRPAIN201309UV02(msg);

                final ServicePortDescriptor<PatientCorrelationSecuredPortType> portDescriptor = getRetrieveServicePortDescriptor(
                    NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                final CONNECTClient<PatientCorrelationSecuredPortType> client = CONNECTClientFactory.getInstance()
                    .getCONNECTClientSecured(portDescriptor, url, assertion);

                securedResp = (RetrievePatientCorrelationsSecuredResponseType) client
                    .invokePort(PatientCorrelationSecuredPortType.class, "retrievePatientCorrelations", request);
                if (securedResp != null && securedResp.getPRPAIN201310UV02() != null) {
                    response.setPRPAIN201310UV02(securedResp.getPRPAIN201310UV02());
                }
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
        final AddPatientCorrelationResponseType response = new AddPatientCorrelationResponseType();
        AddPatientCorrelationSecuredResponseType securedResp = new AddPatientCorrelationSecuredResponseType();

        try {
            final String url = oProxyHelper
                .getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);

            if (msg == null) {
                throw new IllegalArgumentException(REQUEST_MESSAGE_MUST_BE_PROVIDED);
            } else {
                final AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
                request.setPRPAIN201301UV02(msg);

                final ServicePortDescriptor<PatientCorrelationSecuredPortType> portDescriptor = getAddServicePortDescriptor(
                    NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                final CONNECTClient<PatientCorrelationSecuredPortType> client = CONNECTClientFactory.getInstance()
                    .getCONNECTClientSecured(portDescriptor, url, assertion);

                securedResp = (AddPatientCorrelationSecuredResponseType) client
                    .invokePort(PatientCorrelationSecuredPortType.class, "addPatientCorrelation", request);

                if (securedResp != null && securedResp.getMCCIIN000002UV01() != null) {
                    response.setMCCIIN000002UV01(securedResp.getMCCIIN000002UV01());
                }
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
        AddPatientCorrelationPLQSecuredRequestType request = new AddPatientCorrelationPLQSecuredRequestType();
        request.setPatientLocationQueryResponse(plqRecords);

        try {
            final String url = oProxyHelper
                .getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);

            if (plqRecords == null) {
                throw new IllegalArgumentException(REQUEST_MESSAGE_MUST_BE_PROVIDED);
            } else {

                final ServicePortDescriptor<PatientCorrelationSecuredPortType> portDescriptor = new PatientCorrelationSecuredAddPLQServicePortDescriptor();

                final CONNECTClient<PatientCorrelationSecuredPortType> client = CONNECTClientFactory.getInstance()
                    .getCONNECTClientSecured(portDescriptor, url, assertion);

                client.invokePort(PatientCorrelationSecuredPortType.class,"addPatientCorrelationPLQ", request);
            }
        }
        catch (final Exception ex) {
            throw new ErrorEventException(ex,UNABLE_TO_CALL);
        }

        LOG.debug("End addPatientCorrelationPLQ");
    }
}

