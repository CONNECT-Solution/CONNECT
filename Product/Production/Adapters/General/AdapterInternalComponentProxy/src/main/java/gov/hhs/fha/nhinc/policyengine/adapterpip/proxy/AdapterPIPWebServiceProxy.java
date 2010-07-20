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
import java.util.StringTokenizer;


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
            		
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    oResponse = oAdapterPIPPort.retrievePtConsentByPtId(request);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterPIP call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterPIP call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterPIP Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterPIP Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            oResponse = oAdapterPIPPort.retrievePtConsentByPtId(request);
        }
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
            
					int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
        int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        if (retryCount > 0 && retryDelay > 0) {
            int i = 1;
            javax.xml.ws.WebServiceException catchExp = null;
            while (i <= retryCount) {
                try {
                    oResponse = oAdapterPIPPort.retrievePtConsentByPtDocId(request);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    log.warn("Exception calling ... web service: " + e.getMessage());
                    System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                    log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                    i++;
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException iEx) {
                        log.error("Thread Got Interrupted while waiting on AdapterPIP call :" + iEx);
                    } catch (IllegalArgumentException iaEx) {
                        log.error("Thread Got Interrupted while waiting on AdapterPIP call :" + iaEx);
                    }
                    retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                }
            }

            if (retryCount == 0 && catchExp != null) {
                log.error("Unable to call AdapterPIP Webservice due to  : " + catchExp);
                throw catchExp;
            }
        } else {
            oResponse = oAdapterPIPPort.retrievePtConsentByPtDocId(request);
        }
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
            
					int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
        int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        if (retryCount > 0 && retryDelay > 0) {
            int i = 1;
            javax.xml.ws.WebServiceException catchExp = null;
            while (i <= retryCount) {
                try {
                    oResponse = oAdapterPIPPort.storePtConsent(request);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    log.warn("Exception calling ... web service: " + e.getMessage());
                    System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                    log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                    i++;
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException iEx) {
                        log.error("Thread Got Interrupted while waiting on AdapterPIP call :" + iEx);
                    } catch (IllegalArgumentException iaEx) {
                        log.error("Thread Got Interrupted while waiting on AdapterPIP call :" + iaEx);
                    }
                    retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                }
            }

            if (retryCount == 0 && catchExp != null) {
                log.error("Unable to call AdapterPIP Webservice due to  : " + catchExp);
                throw catchExp;
            }
        } else {
            oResponse = oAdapterPIPPort.storePtConsent(request);
        }
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
