/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPIP", portName = "AdapterPIPPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpip", wsdlLocation = "WEB-INF/wsdl/AdapterPIPService/AdapterPIP.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterPIPService
{
    @Resource
    private WebServiceContext context;

    protected AdapterPIPServiceImpl getAdapterPIPServiceImpl()
    {
        return new AdapterPIPServiceImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param retrievePtConsentByPtIdRequest The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     * @throws AdapterPIPException This exception is thrown if the data cannot be retrieved.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType retrievePtConsentByPtIdRequest) 
    {
        return getAdapterPIPServiceImpl().retrievePtConsentByPtId(retrievePtConsentByPtIdRequest, getWebServiceContext());
    }

    /**
     * Retrieve the patient consent settings for the patient associated with
     * the given document identifiers.
     *
     * @param retrievePtConsentByPtDocIdRequest The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with
     *         the given document identifiers.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType retrievePtConsentByPtDocIdRequest)
    {
        return getAdapterPIPServiceImpl().retrievePtConsentByPtDocId(retrievePtConsentByPtDocIdRequest, getWebServiceContext());
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param storePtConsentRequest The patient consent settings to be stored.
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType storePtConsent(gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType storePtConsentRequest) 
    {
        return getAdapterPIPServiceImpl().storePtConsent(storePtConsentRequest, getWebServiceContext());
    }
}
