/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterPIPServiceImpl
{
    private Log log = null;

    public AdapterPIPServiceImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected AdapterPIPImpl getAdapterPIPImpl()
    {
        return new AdapterPIPImpl();
    }
    
    protected void loadAssertion(AssertionType assertion, WebServiceContext wsContext) throws Exception
    {
        // TODO: Extract message ID from the web service context for logging.
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param retrievePtConsentByPtIdRequest The patient ID for which the consent is being retrieved.
     * @param context Web service context
     * @return The patient consent information for that patient.
     * @throws AdapterPIPException This exception is thrown if the data cannot be retrieved.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType retrievePtConsentByPtIdRequest, WebServiceContext context) {
        log.debug("Begin retrievePtConsentByPtId");
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();

        AdapterPIPImpl oAdapterPIPImpl = getAdapterPIPImpl();

        try 
        {
            AssertionType assertion = retrievePtConsentByPtIdRequest.getAssertion();
            loadAssertion(assertion, context);

            oResponse = oAdapterPIPImpl.retrievePtConsentByPtId(retrievePtConsentByPtIdRequest);
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPImpl.retrievePtConsentByPtId.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End retrievePtConsentByPtId");
        return oResponse;
    }

    /**
     * Retrieve the patient consent settings for the patient associated with
     * the given document identifiers.
     *
     * @param retrievePtConsentByPtDocIdRequest The doucment identifiers of a document in the repository.
     * @param context Web service context
     * @return The patient consent settings for the patient associated with
     *         the given document identifiers.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType retrievePtConsentByPtDocIdRequest, WebServiceContext context) {
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();

        AdapterPIPImpl oAdapterPIPImpl = getAdapterPIPImpl();

        try {
            AssertionType assertion = retrievePtConsentByPtDocIdRequest.getAssertion();
            loadAssertion(assertion, context);

            oResponse = oAdapterPIPImpl.retrievePtConsentByPtDocId(retrievePtConsentByPtDocIdRequest);
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPImpl.retrievePtConsentByPtDocId.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param storePtConsentRequest The patient consent settings to be stored.
     * @param context Web service context
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType storePtConsent(gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType storePtConsentRequest, WebServiceContext context) {
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();

        AdapterPIPImpl oAdapterPIPImpl = getAdapterPIPImpl();

        try {
            AssertionType assertion = storePtConsentRequest.getAssertion();
            loadAssertion(assertion, context);

            oResponse = oAdapterPIPImpl.storePtConsent(storePtConsentRequest);
        } catch (Exception e) {
            String sErrorMessage = "Error occurred calling AdapterPIPImpl.storePtConsent.  Error: " +
                    e.getMessage();
            oResponse.setStatus("FAILED: " + sErrorMessage);
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

}
