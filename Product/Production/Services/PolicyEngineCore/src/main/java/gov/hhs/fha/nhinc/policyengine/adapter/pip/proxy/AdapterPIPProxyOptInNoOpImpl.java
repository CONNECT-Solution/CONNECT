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
package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a "NoOp" implementation of the AdapterPIPProxy interface. It will return OptIn as true for all responses.
 *
 * @author Les Westberg
 */
public class AdapterPIPProxyOptInNoOpImpl implements AdapterPIPProxy {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPIPProxyOptInNoOpImpl.class);

    /**
     * NO-OP implementation of the RetrievePtConsentByPtId operation. It will return a message with the same assigning
     * authority and patient Id that was passed and return an OptIn of false.
     *
     * @param request The assigning authority and patient ID of the patient.
     * @return A response containing the given assigning authority and patient ID along with OptIn set to false.
     */
    @Override
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request,
            AssertionType assertion) {
        LOG.trace("Begin AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtId");
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();
        PatientPreferencesType oPref = new PatientPreferencesType();
        oResponse.setPatientPreferences(oPref);

        if ((request != null) && (request.getAssigningAuthority() != null)) {
            oPref.setAssigningAuthority(request.getAssigningAuthority());
        } else {
            oPref.setAssigningAuthority("");
        }

        if ((request != null) && (request.getPatientId() != null)) {
            oPref.setPatientId(request.getPatientId());
        } else {
            oPref.setPatientId("");
        }

        oPref.setOptIn(true);

        LOG.trace("End AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtId");
        return oResponse;
    }

    /**
     * NO-Op implementation of the RetrievePtConsentByDocId operation. It will return an empty message with the OptIn
     * set to false.
     *
     * @param request The patient doc ID information for the patient.
     * @return An empty message with the OptIn set to false.
     */
    @Override
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(
            RetrievePtConsentByPtDocIdRequestType request, AssertionType assertion) {
        LOG.trace("Begin AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtDocId");
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();
        PatientPreferencesType oPref = new PatientPreferencesType();
        oResponse.setPatientPreferences(oPref);
        oPref.setAssigningAuthority("");
        oPref.setPatientId("");
        oPref.setOptIn(true);
        LOG.trace("End AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtDocId");
        return oResponse;
    }

    /**
     * NOOP implememtation of the storePtConsent operation. This does nothing but still returns "SUCCESS".
     *
     * @param request Patient consent preferenes to be stored.
     * @return Always returns "SUCCESS".
     */
    @Override
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request, AssertionType assertion) {
        LOG.trace("Begin AdapterPIPProxyOptInNoOpImpl.storePtConsent");
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();
        oResponse.setStatus("SUCCESS");
        LOG.trace("End AdapterPIPProxyOptInNoOpImpl.storePtConsent");
        return oResponse;
    }

}
