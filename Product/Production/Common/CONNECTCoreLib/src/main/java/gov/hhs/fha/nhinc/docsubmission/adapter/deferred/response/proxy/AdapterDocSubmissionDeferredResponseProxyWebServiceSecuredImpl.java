/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Visu Patlolla
 */
public class AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl implements AdapterDocSubmissionDeferredResponseProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterxdrresponsesecured";
    private static final String SERVICE_LOCAL_PART = "AdapterXDRResponseSecured_Service";
    private static final String PORT_LOCAL_PART = "AdapterXDRResponseSecured_Port_Soap";
    private static final String WSDL_FILE = "AdapterXDRResponseSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "tns:XDRResponseInput";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl()
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

    protected AdapterXDRResponseSecuredPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterXDRResponseSecuredPortType port = null;

        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterXDRResponseSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, NhincConstants.XDR_RESPONSE_ACTION, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType regResponse, AssertionType assertion)
    {
        log.debug("Begin AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetBResponse");
        XDRAcknowledgementType response = null;
        String serviceName = NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME;

        try
        {
            log.debug("Before target system URL look up.");
            String url = oProxyHelper.getUrlLocalHomeCommunity(serviceName);
            if (log.isDebugEnabled())
            {
                log.debug("After target system URL look up. URL for service: " + serviceName + " is: " + url);
            }

            if (NullChecker.isNotNullish(url))
            {
                AdapterXDRResponseSecuredPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                response = (XDRAcknowledgementType) oProxyHelper.invokePort(port, AdapterXDRResponseSecuredPortType.class, "provideAndRegisterDocumentSetBResponse", regResponse);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error in provideAndRegisterDocumentSetBResponse: " + ex.getMessage());
            ex.printStackTrace();
            log.error("Error: Failed to retrieve url for service: " + serviceName + " for local home community");
            log.error(ex.getMessage(), ex);
        }

        log.debug("End AdapterDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetBResponse");
        return response;
    }
}
