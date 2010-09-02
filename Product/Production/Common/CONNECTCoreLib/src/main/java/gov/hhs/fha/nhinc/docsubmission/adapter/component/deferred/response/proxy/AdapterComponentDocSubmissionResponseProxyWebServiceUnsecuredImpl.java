/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.adaptercomponentxdrresponse.AdapterComponentXDRResponsePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author dunnek, Les Westberg
 */
public class AdapterComponentDocSubmissionResponseProxyWebServiceUnsecuredImpl implements AdapterComponentDocSubmissionResponseProxy
{
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptercomponentxdrresponse";
    private static final String SERVICE_LOCAL_PART = "AdapterComponentXDRResponse_Service";
    private static final String PORT_LOCAL_PART = "AdapterComponentXDRResponse_Port";
    private static final String WSDL_FILE = "AdapterComponentXDRResponse.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":" + "XDRResponseInputMessage";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public AdapterComponentDocSubmissionResponseProxyWebServiceUnsecuredImpl()
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
     * Receive document deferred document submission response.
     *
     * @param body The doc submission response message.
     * @param assertion The assertion information.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, AssertionType assertion)
    {
        String endpointUrl = null;
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        String sServiceName = NhincConstants.ADAPTER_COMPONENT_XDR_RESPONSE_SERVICE_NAME;

        try
        {
            if (body != null)
            {
                log.debug("Before target system URL look up.");
                endpointUrl = oProxyHelper.getUrlLocalHomeCommunity(sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + endpointUrl);

                if (NullChecker.isNotNullish(endpointUrl))
                {
                    AdapterRegistryResponseType adaptResponse = new AdapterRegistryResponseType();
                    adaptResponse.setAssertion(assertion);
                    adaptResponse.setRegistryResponse(body);

                    AdapterComponentXDRResponsePortType port = getPort(endpointUrl, NhincConstants.ADAPTER_XDRRESPONSE_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (XDRAcknowledgementType) oProxyHelper.invokePort(port, AdapterComponentXDRResponsePortType.class, "provideAndRegisterDocumentSetBResponse", adaptResponse);
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
    protected AdapterComponentXDRResponsePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        AdapterComponentXDRResponsePortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterComponentXDRResponsePortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
