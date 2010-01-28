package gov.hhs.fha.nhinc.policyengine.adapterpipservice;

import gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType;
import javax.jws.WebService;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;

import gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterPIP", portName = "AdapterPIPPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpip", wsdlLocation = "WEB-INF/wsdl/AdapterPIPService/AdapterPIP.wsdl")
public class AdapterPIPService {

    private static Log log = LogFactory.getLog(AdapterPIPService.class);

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param retrievePtConsentByPtIdRequest The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     * @throws AdapterPIPException This exception is thrown if the data cannot be retrieved.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType retrievePtConsentByPtIdRequest)
    {
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();

        try
        {
            oResponse = oAdapterPIPImpl.retrievePtConsentByPtId(retrievePtConsentByPtIdRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPImpl.retrievePtConsentByPtId.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
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
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();

        try
        {
            oResponse = oAdapterPIPImpl.retrievePtConsentByPtDocId(retrievePtConsentByPtDocIdRequest);
        }
        catch (Exception e)
        {
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
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType storePtConsent(gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType storePtConsentRequest)
    {
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();

        AdapterPIPImpl oAdapterPIPImpl = new AdapterPIPImpl();

        try
        {
            oResponse = oAdapterPIPImpl.storePtConsent(storePtConsentRequest);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPImpl.storePtConsent.  Error: " +
                                   e.getMessage();
            oResponse.setStatus("FAILED: " + sErrorMessage);
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }
}
