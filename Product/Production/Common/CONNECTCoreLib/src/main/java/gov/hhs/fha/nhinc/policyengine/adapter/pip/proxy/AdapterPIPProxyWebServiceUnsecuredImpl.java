/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.adapterpip.AdapterPIPPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * This is the concrete implementation for the Web Service based call to the
 * AdapterPIP.
 *
 * @author Les Westberg
 */
public class AdapterPIPProxyWebServiceUnsecuredImpl implements AdapterPIPProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterpip";
    private static final String SERVICE_LOCAL_PART = "AdapterPIP";
    private static final String PORT_LOCAL_PART = "AdapterPIPPortSoap";
    private static final String WSDL_FILE = "AdapterPIP.wsdl";
    private static final String WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTID = "urn:gov:hhs:fha:nhinc:adapterpip:RetrievePtConsentByPtIdRequest";
    private static final String WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTDOCID = "urn:gov:hhs:fha:nhinc:adapterpip:RetrievePtConsentByPtDocIdRequest";
    private static final String WS_ADDRESSING_ACTION_STOREPTCONSENT = "urn:gov:hhs:fha:nhinc:adapterpip:StorePtConsentRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPIPProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AdapterPIPPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterPIPPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterPIPPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    /**
     * Retrieve the patient consent settings for the given patient ID.
     *
     * @param request The patient ID for which the consent is being retrieved.
     * @return The patient consent information for that patient.
     */
    public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request, AssertionType assertion)
    {
        log.debug("Begin AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtId");
        RetrievePtConsentByPtIdResponseType oResponse = new RetrievePtConsentByPtIdResponseType();
        String serviceName = NhincConstants.ADAPTER_PIP_SERVICE_NAME;

        try
        {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if(log.isDebugEnabled())
            {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url))
            {
                AdapterPIPPortType port = getPort(url, WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTID, assertion);
                oResponse = (RetrievePtConsentByPtIdResponseType) oProxyHelper.invokePort(port, AdapterPIPPortType.class, "retrievePtConsentByPtId", request);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtId.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtId");
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
        log.debug("Begin AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtDocId");
        RetrievePtConsentByPtDocIdResponseType oResponse = new RetrievePtConsentByPtDocIdResponseType();
        String serviceName = NhincConstants.ADAPTER_PIP_SERVICE_NAME;

        try
        {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if(log.isDebugEnabled())
            {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url))
            {
                AdapterPIPPortType port = getPort(url, WS_ADDRESSING_ACTION_RETRIEVEPTCONSENTBYPTDOCID, assertion);
                oResponse = (RetrievePtConsentByPtDocIdResponseType) oProxyHelper.invokePort(port, AdapterPIPPortType.class, "retrievePtConsentByPtDocId", request);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtDocId.  Error: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPIPProxyWebServiceUnsecuredImpl.retrievePtConsentByPtDocId");
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
        log.debug("Begin AdapterPIPProxyWebServiceUnsecuredImpl.storePtConsent");
        StorePtConsentResponseType oResponse = new StorePtConsentResponseType();
        String serviceName = NhincConstants.ADAPTER_PIP_SERVICE_NAME;

        try
        {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if(log.isDebugEnabled())
            {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url))
            {
                AdapterPIPPortType port = getPort(url, WS_ADDRESSING_ACTION_STOREPTCONSENT, assertion);
                oResponse = (StorePtConsentResponseType) oProxyHelper.invokePort(port, AdapterPIPPortType.class, "storePtConsent", request);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling AdapterPIPProxyWebServiceUnsecuredImpl.storePtConsent.  Error: " +
                    e.getMessage();
            oResponse.setStatus("FAILED: " + sErrorMessage);
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }

        log.debug("End AdapterPIPProxyWebServiceUnsecuredImpl.storePtConsent");
        return oResponse;
    }
}
