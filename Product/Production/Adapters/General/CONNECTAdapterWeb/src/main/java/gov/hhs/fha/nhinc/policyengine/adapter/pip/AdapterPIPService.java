/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.SOAPBinding;

/**
 *
 * @author Sai Valluripalli
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPIPService implements gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType {

    @Resource
    private WebServiceContext context;

    protected AdapterPIPServiceImpl getAdapterPIPServiceImpl() {
        return new AdapterPIPServiceImpl();
    }

    protected WebServiceContext getWebServiceContext() {
        return context;
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param retrievePtConsentByPtIdRequest The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     * @throws AdapterPIPException This exception is thrown if the data cannot be retrieved.
     */
    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(
            gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType retrievePtConsentByPtIdRequest) {
        return getAdapterPIPServiceImpl().retrievePtConsentByPtId(retrievePtConsentByPtIdRequest,
                getWebServiceContext());
    }

    /**
     * Retrieve the patient consent settings for the patient associated with the given document identifiers.
     *
     * @param retrievePtConsentByPtDocIdRequest The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with the given document identifiers.
     */
    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(
            gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType retrievePtConsentByPtDocIdRequest) {
        return getAdapterPIPServiceImpl().retrievePtConsentByPtDocId(retrievePtConsentByPtDocIdRequest,
                getWebServiceContext());
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param storePtConsentRequest The patient consent settings to be stored.
     * @return Status of the storage. Currently this is either "SUCCESS" or or the word "FAILED" followed by a ':'
     *         followed by the error information.
     */
    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType storePtConsent(
            gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType storePtConsentRequest) {
        return getAdapterPIPServiceImpl().storePtConsent(storePtConsentRequest, getWebServiceContext());
    }
}
