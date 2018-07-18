/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientlocationquery.outbound;

import org.hl7.v3.AddPatientCorrelationPLQRequestType;
import org.hl7.v3.SimplePatientCorrelationSecuredResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.patientlocationquery.descriptor.PatientCorrelationSecuredAddPLQServicePortDescriptor;
import gov.hhs.fha.nhinc.patientlocationquery.services.PatientLocationQuery;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

public class PatientLocationQueryProxyWebServiceSecuredImpl implements PatientLocationQueryProxy {

    private static final Logger LOG = LoggerFactory.getLogger(PatientLocationQueryProxyWebServiceSecuredImpl.class);
    private WebServiceProxyHelper proxyHelper = null;

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        if (proxyHelper == null) {
            proxyHelper = new WebServiceProxyHelper();
        }
        return proxyHelper;
    }

     @Override
    public SimplePatientCorrelationSecuredResponseType processPatientLocationQueryPLQ(AddPatientCorrelationPLQRequestType request,
        AssertionType assertion) {
        LOG.debug("Begin processPatientLocationQuery");
        SimplePatientCorrelationSecuredResponseType response = new SimplePatientCorrelationSecuredResponseType();

        try {
            String url = getWebServiceProxyHelper().getUrlLocalHomeCommunity(NhincConstants.PATIENT_CORRELATION_SERVICE_NAME);

            ServicePortDescriptor<PatientCorrelationSecuredPortType> portDescriptor = new PatientCorrelationSecuredAddPLQServicePortDescriptor();

            CONNECTClient<PatientCorrelationSecuredPortType> client = CONNECTClientFactory.getInstance().getCONNECTClientSecured(
                portDescriptor, url, assertion);

            response = (SimplePatientCorrelationSecuredResponseType) client.invokePort(PatientCorrelationSecuredPortType.class,
                "addPatientCorrelationPLQ", request);

        } catch (Exception ex) {
            LOG.error("Error calling processPatientLocationQuery: {}", ex.getMessage(), ex);
            throw new IllegalStateException("Error calling NHIN processPatientLocationQuery", ex);
        }

        LOG.debug("End processPatientLocationQueryPLQ");
        return response;
    }

}
