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

package gov.hhs.fha.nhinc.admindistribution.nhin.proxy;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class NhinAdminDistributionProxyWebServiceSecuredImpl implements NhinAdminDistributionProxy{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinadmindistribution";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_AdministrativeDistribution";
    private static final String PORT_LOCAL_PART = "RespondingGateway_AdministrativeDistribution_PortType";
    private static final String WSDL_FILE_G0 = "NhinAdminDist.wsdl";
    private static final String WSDL_FILE_G1 = "NhinAdminDist_g1.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:oasis:names:tc:emergency:EDXL:DE:1.0:SendAlertMessage";

    //static RespondingGatewayAdministrativeDistribution nhinService = new RespondingGatewayAdministrativeDistribution();
    public NhinAdminDistributionProxyWebServiceSecuredImpl()
    {
        log = createLogger();
    }
    private Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    private AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel)
    {
        log.debug("begin sendAlertMessage");
        AdminDistributionHelper helper = getHelper();
        String url = helper.getUrl(target, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME, apiLevel);

        if (NullChecker.isNotNullish(url))
        {
            RespondingGatewayAdministrativeDistributionPortType port = getPort(url, assertion, apiLevel);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            try
            {
                log.debug("invoke port");
                getWebServiceProxyHelper().invokePort(port, RespondingGatewayAdministrativeDistributionPortType.class, "sendAlertMessage", body);
            }
            catch(Exception ex)
            {
                log.error("Failed to call the web service (" + NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                        "Exception: " + ex.getMessage(), ex);
            }
        }


    }

    protected RespondingGatewayAdministrativeDistributionPortType getPort(String url, AssertionType assertion,
            NhincConstants.GATEWAY_API_LEVEL apiLevel)
    {
        WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();

        RespondingGatewayAdministrativeDistributionPortType port = null;
        Service service;
        switch (apiLevel) {
            case LEVEL_g0: service = getService(WSDL_FILE_G0, NAMESPACE_URI, SERVICE_LOCAL_PART);
                break;
            case LEVEL_g1: service = getService(WSDL_FILE_G1, NAMESPACE_URI, SERVICE_LOCAL_PART);
                break;
            default: service = null;
        }
        if (service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayAdministrativeDistributionPortType.class);
            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url,NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME, WS_ADDRESSING_ACTION, assertion);
         }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    
    protected Service getService(String wsdl, String uri, String service)
    {
        if (cachedService == null)
        {
            try
            {
                WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
                cachedService = proxyHelper.createService(wsdl, uri, service);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
