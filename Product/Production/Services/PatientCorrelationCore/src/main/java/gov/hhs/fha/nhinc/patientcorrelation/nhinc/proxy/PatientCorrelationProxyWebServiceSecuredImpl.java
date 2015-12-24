/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationSecuredAddServicePortDescriptor;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description
                            .PatientCorrelationSecuredRetrieveServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
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
 */
public class PatientCorrelationProxyWebServiceSecuredImpl implements PatientCorrelationProxy {
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
     * @param apiLevel Adapter apiLevel (this is a0,a1).
     * @return WS_ADDRESSING_ACTION_RETRIEVE.
     */
    public ServicePortDescriptor<PatientCorrelationSecuredPortType> getRetrieveServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_a0:
            return new PatientCorrelationSecuredRetrieveServicePortDescriptor();
        default:
            return new PatientCorrelationSecuredRetrieveServicePortDescriptor();
        }
    }

    /**
     * This method returns WS_ADDRESSING_ACTION_ADD for PatientCorrelationService.
     * @param apiLevel Adapter apiLevel (this is a0,a1).
     * @return WS_ADDRESSING_ACTION_ADD.
     */
    public ServicePortDescriptor<PatientCorrelationSecuredPortType> getAddServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_a0:
            return new PatientCorrelationSecuredAddServicePortDescriptor();
        default:
            return new PatientCorrelationSecuredAddServicePortDescriptor();
        }
    }

    /**
     * This method retrieves PatientCorrelation from the targeted community.
     * @param msg PRPAIN201309UV02 HL7 type of Request received.
     * @param assertion Assertion received.
     * @return PatientCorrelationresponse.
     */
    @Override
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 msg,
            AssertionType assertion) {
        LOG.debug("Begin retrievePatientCorrelations");
        RetrievePatientCorrelationsResponseType response = new RetrievePatientCorrelationsResponseType();
        RetrievePatientCorrelationsSecuredResponseType securedResp =
                new RetrievePatientCorrelationsSecuredResponseType();

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);

            if (msg == null) {
                LOG.error("Message was null");
            } else {
                RetrievePatientCorrelationsSecuredRequestType request =
                        new RetrievePatientCorrelationsSecuredRequestType();
                request.setPRPAIN201309UV02(msg);

                ServicePortDescriptor<PatientCorrelationSecuredPortType> portDescriptor =
                        getRetrieveServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                CONNECTClient<PatientCorrelationSecuredPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientSecured(portDescriptor, url, assertion);

                securedResp = (RetrievePatientCorrelationsSecuredResponseType) client
                        .invokePort(PatientCorrelationSecuredPortType.class,
                        "retrievePatientCorrelations", request);
                if (securedResp != null && securedResp.getPRPAIN201310UV02() != null) {
                    response.setPRPAIN201310UV02(securedResp.getPRPAIN201310UV02());
                }
            }
        } catch (Exception ex) {
            LOG.error("Error calling retrievePatientCorrelations: " + ex.getMessage(), ex);
        }

        LOG.debug("End retrievePatientCorrelations");
        return response;
    }

    /**
     * This method add PatientCorrelations to database.
     * @param msg PRPAIN201301UV02 HL7 type of Request received.
     * @param assertion Assertion received.
     * @return PatientCorrelationResponse.
     */
    @Override
    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 msg, AssertionType assertion) {
        LOG.debug("Begin addPatientCorrelation");
        AddPatientCorrelationResponseType response = new AddPatientCorrelationResponseType();
        AddPatientCorrelationSecuredResponseType securedResp = new AddPatientCorrelationSecuredResponseType();

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);

            if (msg == null) {
                LOG.error("Message was null");
            } else {
                AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
                request.setPRPAIN201301UV02(msg);

                ServicePortDescriptor<PatientCorrelationSecuredPortType> portDescriptor =
                        getAddServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                CONNECTClient<PatientCorrelationSecuredPortType> client = CONNECTClientFactory.getInstance()
                        .getCONNECTClientSecured(portDescriptor, url, assertion);

                securedResp = (AddPatientCorrelationSecuredResponseType) client
                        .invokePort(PatientCorrelationSecuredPortType.class,
                        "addPatientCorrelation", request);

                if (securedResp != null && securedResp.getMCCIIN000002UV01() != null) {
                    response.setMCCIIN000002UV01(securedResp.getMCCIIN000002UV01());
                }
            }
        } catch (Exception ex) {
            LOG.error("Error calling addPatientCorrelation: " + ex.getMessage(), ex);
        }

        LOG.debug("End addPatientCorrelation");
        return response;
    }
}
