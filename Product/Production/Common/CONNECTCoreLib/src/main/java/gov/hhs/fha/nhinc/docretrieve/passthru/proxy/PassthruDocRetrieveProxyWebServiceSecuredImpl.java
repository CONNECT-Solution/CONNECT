/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class PassthruDocRetrieveProxyWebServiceSecuredImpl implements PassthruDocRetrieveProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievesecured";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocRetrieveSecured";
    private static final String PORT_LOCAL_PART = "NhincProxyDocRetrieveSecuredPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocRetrieveSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocretrievesecured:RespondingGateway_CrossGatewayRetrieveRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public PassthruDocRetrieveProxyWebServiceSecuredImpl()
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

    protected NhincProxyDocRetrieveSecuredPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        NhincProxyDocRetrieveSecuredPortType port = null;

        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocRetrieveSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, NhincConstants.DOC_RETRIEVE_ACTION, wsAddressingAction, assertion);
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

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem)
    {
        log.debug("Begin PassthruDocRetrieveProxyWebServiceSecuredImpl.respondingGatewayCrossGatewayRetrieve");
        RetrieveDocumentSetResponseType response = null;
        String serviceName = NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SECURED_SERVICE_NAME;

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
                RespondingGatewayCrossGatewayRetrieveSecuredRequestType securedRequest = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                if(request != null)
                {
                    securedRequest.setRetrieveDocumentSetRequest(request);
                    securedRequest.setNhinTargetSystem(targetSystem);
                }
                NhincProxyDocRetrieveSecuredPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                response = (RetrieveDocumentSetResponseType)oProxyHelper.invokePort(port, NhincProxyDocRetrieveSecuredPortType.class, "respondingGatewayCrossGatewayRetrieve", securedRequest);
            }
            else
            {
                log.error("Failed to call the web service (" + serviceName + ").  The URL is null.");
            }
        }
        catch (Exception ex)
        {
            log.error("Error: Failed to retrieve url for service: " + serviceName + " for local home community");
            log.error(ex.getMessage(), ex);
        }

        log.debug("End PassthruDocRetrieveProxyWebServiceSecuredImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

}
