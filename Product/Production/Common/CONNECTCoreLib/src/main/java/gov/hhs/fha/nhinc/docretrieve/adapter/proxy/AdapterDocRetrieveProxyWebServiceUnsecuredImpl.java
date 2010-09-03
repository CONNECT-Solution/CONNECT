/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.Service;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This is the unsecured web service implementation of the Adapter Doc Retrieve
 * component proxy.
 *
 * @author Neil Webb, Les Westberg
 */
public class AdapterDocRetrieveProxyWebServiceUnsecuredImpl implements AdapterDocRetrieveProxy
{
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocretrieve";
    private static final String SERVICE_LOCAL_PART = "AdapterDocRetrieve";
    private static final String PORT_LOCAL_PART = "AdapterDocRetrievePortSoap";
    private static final String WSDL_FILE = "AdapterDocRetrieve.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":" + "RespondingGateway_CrossGatewayRetrieveRequestMessage";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public AdapterDocRetrieveProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Retrieve the document(s)
     *
     * @param request The identifiers for the document(s) to be retrieved.
     * @param assertion The assertion data.
     * @return The document(s) that were retrieved.
     */
    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request, AssertionType assertion)
    {
        String url = null;
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        String sServiceName = NhincConstants.ADAPTER_DOC_RETRIEVE_SERVICE_NAME;

        try
        {
            if (request != null)
            {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getUrlLocalHomeCommunity(sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url))
                {
                    RespondingGatewayCrossGatewayRetrieveRequestType oUnsecuredRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();

                    // Note that the adapter has a combined set of data.  We need to combine them before sending to the adapter.
                    //-----------------------------------------------------------------------------------------------------------
                    oUnsecuredRequest.setRetrieveDocumentSetRequest(request);
                    oUnsecuredRequest.setAssertion(assertion);
                    AdapterDocRetrievePortType port = getPort(url, NhincConstants.DOC_RETRIEVE_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (RetrieveDocumentSetResponseType) oProxyHelper.invokePort(port, AdapterDocRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", oUnsecuredRequest);
                }
                else
                {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            }
            else
            {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        }
        catch (Exception e)
        {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        return response;
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
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected AdapterDocRetrievePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        AdapterDocRetrievePortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterDocRetrievePortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
