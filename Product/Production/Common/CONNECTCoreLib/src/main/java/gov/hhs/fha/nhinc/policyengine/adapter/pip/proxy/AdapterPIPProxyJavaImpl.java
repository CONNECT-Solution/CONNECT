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

import gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the Java based call to the
 * AdapterPIP.
 *
 * @author Les Westberg
 */
public class AdapterPIPProxyJavaImpl implements AdapterPIPProxy
{
    private Log log = null;

    public AdapterPIPProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request, AssertionType assertion)
    {
        log.debug("Begin AdapterPIPProxyJavaImpl.retrievePtConsentByPtId");
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();

        try
        {
            oResponse = oAdapterPIPImpl.retrievePtConsentByPtId(request);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyJavaImpl.retrievePtConsentByPtId.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPIPProxyJavaImpl.retrievePtConsentByPtId");
        return oResponse;
    }

    /**
     * Retrieve the patient consent settings for the patient associated with
     * the given document identifiers.
     *
     * @param request The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with
     *         the given document identifiers.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request, AssertionType assertion)
    {
        log.debug("Begin AdapterPIPProxyJavaImpl.retrievePtConsentByPtDocId");
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();

        try
        {
            oResponse = oAdapterPIPImpl.retrievePtConsentByPtDocId(request);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyJavaImpl.retrievePtConsentByPtDocId.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPIPProxyJavaImpl.retrievePtConsentByPtDocId");
        return oResponse;
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param request The patient consent settings to be stored.
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request, AssertionType assertion)
    {
        log.debug("Begin AdapterPIPProxyJavaImpl.storePtConsent");
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();

        try
        {
            oResponse = oAdapterPIPImpl.storePtConsent(request);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyJavaImpl.storePtConsent.  Error: " +
                                   e.getMessage();
            oResponse.setStatus("FAILED: " + sErrorMessage);
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPIPProxyJavaImpl.storePtConsent");
        return oResponse;
    }

}
