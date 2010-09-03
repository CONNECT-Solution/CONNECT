/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is a "NoOp" implementation of the AdapterPIPProxy interface.
 * It will return OptIn as true for all responses.
 *
 * @author Les Westberg
 */
public class AdapterPIPProxyOptInNoOpImpl implements AdapterPIPProxy
{
    private Log log = null;

    public AdapterPIPProxyOptInNoOpImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * NO-OP implementation of the RetrievePtConsentByPtId operation.
     * It will return a message with the same assigning authority
     * and patient Id that was passed and return an OptIn of false.
     *
     * @param request The assigning authority and patient ID of the patient.
     * @return A response containing the given assigning authority and patient
     *         ID along with OptIn set to false.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request, AssertionType assertion)
    {
        log.debug("Begin AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtId");
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();
        PatientPreferencesType oPref = new PatientPreferencesType();
        oResponse.setPatientPreferences(oPref);

        if ((request != null) &&
            (request.getAssigningAuthority() != null))
        {
            oPref.setAssigningAuthority(request.getAssigningAuthority());
        }
        else
        {
            oPref.setAssigningAuthority("");
        }

        if ((request != null) &&
            (request.getPatientId() != null))
        {
            oPref.setPatientId(request.getPatientId());
        }
        else
        {
            oPref.setPatientId("");
        }

        oPref.setOptIn(true);

        log.debug("End AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtId");
        return oResponse;
    }

    /**
     * NO-Op implementation of the RetrievePtConsentByDocId operation.
     * It will return an empty message with the OptIn set to false.
     *
     * @param request The patient doc ID information for the patient.
     * @return An empty message with the OptIn set to false.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request, AssertionType assertion)
    {
        log.debug("Begin AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtDocId");
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();
        PatientPreferencesType oPref = new PatientPreferencesType();
        oResponse.setPatientPreferences(oPref);
        oPref.setAssigningAuthority("");
        oPref.setPatientId("");
        oPref.setOptIn(true);
        log.debug("End AdapterPIPProxyOptInNoOpImpl.retrievePtConsentByPtDocId");
        return oResponse;
    }


    /**
     * NOOP implememtation of the storePtConsent operation.  This does nothing
     * but still returns "SUCCESS".
     *
     * @param request Patient consent preferenes to be stored.
     * @return Always returns "SUCCESS".
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request, AssertionType assertion)
    {
        log.debug("Begin AdapterPIPProxyOptInNoOpImpl.storePtConsent");
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();
        oResponse.setStatus("SUCCESS");
        log.debug("End AdapterPIPProxyOptInNoOpImpl.storePtConsent");
        return oResponse;
    }

}
