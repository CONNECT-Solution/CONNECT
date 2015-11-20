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
package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy.service.AdapterPIPRetrieveByDocIdServicePortDescriptor;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy.service.AdapterPIPRetrieveByPatientIdServicePortDescriptor;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy.service.AdapterPIPStoreConsentServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the concrete implementation for the Web Service based call to the AdapterPIP.
 *
 * @author Les Westberg
 */
public class AdapterPIPProxyWebServiceUnsecuredImpl implements AdapterPIPProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPIPProxyWebServiceUnsecuredImpl.class);
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPIPProxyWebServiceUnsecuredImpl() {
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected CONNECTClient<AdapterPIPPortType> getCONNECTClientUnsecured(
            ServicePortDescriptor<AdapterPIPPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request,
            AssertionType assertion) {
        LOG.trace("Begin AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtId");
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();
        String serviceName = NhincConstants.ADAPTER_PIP_SERVICE_NAME;

        try {
            LOG.debug("Before target system URL look up.");
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            LOG.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);

            if (NullChecker.isNotNullish(url)) {
                ServicePortDescriptor<AdapterPIPPortType> portDescriptor = new AdapterPIPRetrieveByPatientIdServicePortDescriptor();

                CONNECTClient<AdapterPIPPortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                oResponse = (RetrievePtConsentByPtIdResponseType) client.invokePort(AdapterPIPPortType.class,
                        "retrievePtConsentByPtId", request);
            } else {
                LOG.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtId.  Error: "
                    + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        LOG.trace("End AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtId");
        return oResponse;
    }

    /**
     * Retrieve the patient consent settings for the patient associated with the given document identifiers.
     *
     * @param request The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with the given document identifiers.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(
            RetrievePtConsentByPtDocIdRequestType request, AssertionType assertion) {
        LOG.trace("Begin AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtDocId");
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();
        String serviceName = NhincConstants.ADAPTER_PIP_SERVICE_NAME;

        try {
            LOG.debug("Before target system URL look up.");
            String url = oProxyHelper.getEndPointFromConnectionManagerByAdapterAPILevel(serviceName,
                    ADAPTER_API_LEVEL.LEVEL_a0);
            if (LOG.isDebugEnabled()) {
                LOG.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url)) {
                ServicePortDescriptor<AdapterPIPPortType> portDescriptor = new AdapterPIPRetrieveByDocIdServicePortDescriptor();

                CONNECTClient<AdapterPIPPortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                oResponse = (RetrievePtConsentByPtDocIdResponseType) client.invokePort(AdapterPIPPortType.class,
                        "retrievePtConsentByPtDocId", request);
            } else {
                LOG.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtDocId.  Error: "
                    + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        LOG.trace("End AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtDocId");
        return oResponse;
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param request The patient consent settings to be stored.
     * @return Status of the storage. Currently this is either "SUCCESS" or or the word "FAILED" followed by a ':'
     *         followed by the error information.
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request, AssertionType assertion) {
        LOG.trace("Begin AdapterPIPProxyWebServiceUnsecuredImpl.storePtConsent");
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();
        String serviceName = NhincConstants.ADAPTER_PIP_SERVICE_NAME;

        try {
            LOG.debug("Before target system URL look up.");
            String url = oProxyHelper.getAdapterEndPointFromConnectionManager(serviceName);
            LOG.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);

            if (NullChecker.isNotNullish(url)) {
                ServicePortDescriptor<AdapterPIPPortType> portDescriptor = new AdapterPIPStoreConsentServicePortDescriptor();

                CONNECTClient<AdapterPIPPortType> client = getCONNECTClientUnsecured(portDescriptor, url, assertion);

                oResponse = (StorePtConsentResponseType) client.invokePort(AdapterPIPPortType.class, "storePtConsent",
                        request);
            } else {
                LOG.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyWebServiceUnsecuredImpl.storePtConsent.  Error: "
                    + e.getMessage();
            oResponse.setStatus("FAILED: " + sErrorMessage);
            LOG.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        LOG.trace("End AdapterPIPProxyWebServiceUnsecuredImpl.storePtConsent");
        return oResponse;
    }
}
