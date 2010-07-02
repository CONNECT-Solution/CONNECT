package gov.hhs.fha.nhinc.policyengine.adapterpip.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;

import gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.adapterpip.AdapterPIP;
import gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType;


/**
 * This is the concrete implementation for the Web Service based call to the
 * AdapterPIP.
 *
 * @author Les Westberg
 */
public class AdapterPIPWebServiceProxy implements AdapterPIPProxy
{
    private static Log log = LogFactory.getLog(AdapterPIPWebServiceProxy.class);
    private static AdapterPIP oAdapterPIPService = null;
    private static String ADAPTER_PIP_SERVICE_NAME = "adapterpip";
    private static String ADAPTER_PIP_DEFAULT_SERVICE_URL = "http://localhost:8080/NhinConnect/AdapterPIP";

    /**
     * Return a handle to the AdapterPIP web service.
     *
     * @return The handle to the Adapter PIP port web service.
     */
    private AdapterPIPPortType getAdapterPIPPort()
        throws AdapterPIPException
    {
        AdapterPIPPortType oAdapterPIPPort = null;

        try
        {
            if (oAdapterPIPService == null)
            {
                oAdapterPIPService = new AdapterPIP();
            }

            oAdapterPIPPort = oAdapterPIPService.getAdapterPIPPortSoap11();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_PIP_SERVICE_NAME);

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = ADAPTER_PIP_DEFAULT_SERVICE_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       ADAPTER_PIP_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) oAdapterPIPPort,sEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the Adapter PIP web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oAdapterPIPPort;
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request)
    {
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();

        try
        {
            AdapterPIPPortType oAdapterPIPPort = getAdapterPIPPort();
            oResponse = oAdapterPIPPort.retrievePtConsentByPtId(request);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPWebServiceProxy.retrievePtConsentByPtId.  Error: " +
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
     * @param request The doucment identifiers of a document in the repository.
     * @return The patient consent settings for the patient associated with
     *         the given document identifiers.
     */
    public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request)
    {
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();

        try
        {
            AdapterPIPPortType oAdapterPIPPort = getAdapterPIPPort();
            oResponse = oAdapterPIPPort.retrievePtConsentByPtDocId(request);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPWebServiceProxy.retrievePtConsentByPtDocId.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Store the patient consent information into the repository.
     *
     * @param request The patient consent settings to be stored.
     * @return Status of the storage.  Currently this is either "SUCCESS" or
     *         or the word "FAILED" followed by a ':' followed by the error information.
     */
    public StorePtConsentResponseType storePtConsent(StorePtConsentRequestType request)
    {
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();

        try
        {
            AdapterPIPPortType oAdapterPIPPort = getAdapterPIPPort();
            oResponse = oAdapterPIPPort.storePtConsent(request);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPWebServiceProxy.storePtConsent.  Error: " +
                                   e.getMessage();
            oResponse.setStatus("FAILED: " + sErrorMessage);
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        return oResponse;
    }

}
