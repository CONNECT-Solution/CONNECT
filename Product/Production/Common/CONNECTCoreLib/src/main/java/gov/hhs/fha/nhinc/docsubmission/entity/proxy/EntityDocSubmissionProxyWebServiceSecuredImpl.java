/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.EntityXDRSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EntityDocSubmissionProxyWebServiceSecuredImpl implements EntityDocSubmissionProxy
{

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured";
    private static final String SERVICE_LOCAL_PART = "EntityXDRSecured_Service";
    private static final String PORT_LOCAL_PART = "EntityXDRSecured_Port";
    private static final String WSDL_FILE = "EntityXDRSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured:ProvideAndRegisterDocumentSet-b";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityDocSubmissionProxyWebServiceSecuredImpl()
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
     * @return The port object for the web service.
     */
    protected EntityXDRSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        EntityXDRSecuredPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityXDRSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else
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
            } catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType message,
            AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo)
    {
        log.debug("Begin EntityDocSubmissionProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetB");
        RegistryResponseType response = new RegistryResponseType();

        try
        {
                String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ENTITY_XDR_SECURED_SERVICE_NAME);
                EntityXDRSecuredPortType port = getPort(url, NhincConstants.XDR_ACTION, WS_ADDRESSING_ACTION, assertion);

                if (port != null)
                {
                    RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType securedRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
                    securedRequest.setNhinTargetCommunities(targets);
                    securedRequest.setProvideAndRegisterDocumentSetRequest(message);
                    securedRequest.setUrl(urlInfo);
                    response = (RegistryResponseType) oProxyHelper.invokePort(port, EntityXDRSecuredPortType.class, "provideAndRegisterDocumentSetB", securedRequest);
                } else
                {
                    log.error("EntityXDRSecuredPortType is null");
                }
        } catch (Exception ex)
        {
            log.error("Error calling provideAndRegisterDocumentSetB: " + ex.getMessage(), ex);
        }

        log.debug("End EntityDocSubmissionProxyWebServiceSecuredImpl.provideAndRegisterDocumentSetB");
        return response;
    }
}
