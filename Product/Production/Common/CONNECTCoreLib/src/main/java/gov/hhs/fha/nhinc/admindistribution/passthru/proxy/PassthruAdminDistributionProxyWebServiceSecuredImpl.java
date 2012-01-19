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

package gov.hhs.fha.nhinc.admindistribution.passthru.proxy;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionProxyWebServiceSecuredImpl implements PassthruAdminDistributionProxy {
    private Log log = null;
    private static Service cachedService = null;
    private WebServiceProxyHelper proxyHelper = null;

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincadmindistribution";
    private static final String SERVICE_LOCAL_PART = "NhincAdminDistSecuredService";
    private static final String PORT_LOCAL_PART = "NhincAdminDistSecured_PortType";
    private static final String WSDL_FILE = "NhincAdminDistSecured.wsdl";
    private static final String WSDL_FILE_G1 = "NhincAdminDistSecured_g1.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincadmindistribution:SendAlertMessageSecured_Message";

    public PassthruAdminDistributionProxyWebServiceSecuredImpl()
    {
        log = createLogger();
        //service = getWebService();
        proxyHelper =  getWebServiceProxyHelper();        
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    /*protected NhincAdminDistSecuredService getWebService()
    {
        return new NhincAdminDistSecuredService();
    }*/
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    protected Service getService(String wsdl, String uri, String service)
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = proxyHelper.createService(wsdl, uri, service);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
    protected NhincAdminDistSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction,
            AssertionType assertion, NhincConstants.GATEWAY_API_LEVEL apiLevel)
    {
        NhincAdminDistSecuredPortType port = null;
        String wsdlFile = (apiLevel.equals(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0))
                ? WSDL_FILE : WSDL_FILE_G1;
        Service service = getService(wsdlFile,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincAdminDistSecuredPortType.class);

            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel)
    {
        log.debug("begin sendAlertMessage");
        
        AdminDistributionHelper helper = getHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            NhincAdminDistSecuredPortType port = getPort(url, NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME, WS_ADDRESSING_ACTION,
                    assertion, apiLevel);
            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();

            message.setEDXLDistribution(body);
            message.setNhinTargetSystem(target);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            try
            {
                WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
                oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);
                ((BindingProvider) port).getRequestContext().putAll(requestContext);

                proxyHelper.invokePort(port, RespondingGatewaySendAlertMessageSecuredType.class, "sendAlertMessage", message);
            }
            catch(Exception ex)
            {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }
    }

}
