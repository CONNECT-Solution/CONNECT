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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationAddServicePortDescriptor;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.description.PatientCorrelationRetrieveServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.apache.log4j.Logger;
import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 * 
 * @author jhoppesc
 */
public class PatientCorrelationProxyWebServiceUnsecuredImpl implements PatientCorrelationProxy {
    private static final Logger LOG = Logger.getLogger(PatientCorrelationProxyWebServiceUnsecuredImpl.class);
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

    /**
     * This method returns WS_ADDRESSING_ACTION_RETRIEVE.
     * @param apiLevel Adapter apiLevel (this is a0,a1).
     * @return WS_ADDRESSING_ACTION_RETRIEVE.
     */
    public ServicePortDescriptor<PatientCorrelationPortType> getRetrieveServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_a0:
            return new PatientCorrelationRetrieveServicePortDescriptor();
        default:
            return new PatientCorrelationRetrieveServicePortDescriptor();
        }
    }

    /**
     * returns WS_ADDRESSING_ACTION_ADD.
     * @param apiLevel Adapter apiLevel (this is a0,a1).
     * @return WS_ADDRESSING_ACTION_ADD.
     */
    public ServicePortDescriptor<PatientCorrelationPortType> getAddServicePortDescriptor(
            NhincConstants.ADAPTER_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_a0:
            return new PatientCorrelationAddServicePortDescriptor();
        default:
            return new PatientCorrelationAddServicePortDescriptor();
        }
    }

    /**
     * This method retrieves PatientCorrelation from the targeted community. 
     * @param msg PRPAIN201309UV02 HL7 type of Request received.
     * @param assertion Assertion received.
     * @return PatientCorrelationresponse.
     */
    public RetrievePatientCorrelationsResponseType retrievePatientCorrelations(PRPAIN201309UV02 msg,
            AssertionType assertion) {
        LOG.debug("Begin retrievePatientCorrelations");
        RetrievePatientCorrelationsResponseType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);

            if (msg == null) {
                LOG.error("Message was null");
            } else if (assertion == null) {
                LOG.error("assertion was null");
            } else {
                RetrievePatientCorrelationsRequestType request = new RetrievePatientCorrelationsRequestType();
                request.setPRPAIN201309UV02(msg);
                request.setAssertion(assertion);

                ServicePortDescriptor<PatientCorrelationPortType> portDescriptor = 
                        getRetrieveServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                CONNECTClient<PatientCorrelationPortType> client = getCONNECTClientFactory()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                response = (RetrievePatientCorrelationsResponseType) client
                        .invokePort(PatientCorrelationPortType.class, "retrievePatientCorrelations", request);
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
    public AddPatientCorrelationResponseType addPatientCorrelation(PRPAIN201301UV02 msg, AssertionType assertion) {
        LOG.debug("Begin addPatientCorrelation");
        AddPatientCorrelationResponseType response = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);
            if (msg == null) {
                LOG.error("Message was null");
            } else if (assertion == null) {
                LOG.error("assertion was null");
            } else {
                AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
                request.setPRPAIN201301UV02(msg);
                request.setAssertion(assertion);

                ServicePortDescriptor<PatientCorrelationPortType> portDescriptor = 
                        getRetrieveServicePortDescriptor(NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);

                CONNECTClient<PatientCorrelationPortType> client = getCONNECTClientFactory()
                        .getCONNECTClientUnsecured(portDescriptor, url, assertion);

                response = (AddPatientCorrelationResponseType) client
                        .invokePort(PatientCorrelationPortType.class, "addPatientCorrelation", request);
            }
        } catch (Exception ex) {
            LOG.error("Error calling addPatientCorrelation: " + ex.getMessage(), ex);
        }

        LOG.debug("End addPatientCorrelation");
        return response;
    }
}
