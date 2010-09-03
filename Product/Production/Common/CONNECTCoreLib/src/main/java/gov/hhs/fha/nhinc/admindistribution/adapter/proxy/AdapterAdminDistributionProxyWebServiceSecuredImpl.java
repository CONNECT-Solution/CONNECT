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

package gov.hhs.fha.nhinc.admindistribution.adapter.proxy;
import gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionSecuredPortType;
import gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionSecured;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageSecuredType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.ws.Service;
import javax.xml.namespace.QName;
/**
 *
 * @author dunnek
 */
public class AdapterAdminDistributionProxyWebServiceSecuredImpl implements AdapterAdminDistributionProxy{
   private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapteradmindistribution";
    private static final String SERVICE_LOCAL_PART = "Adapter_AdministrativeDistributionSecured";
    private static final String PORT_LOCAL_PART = "Adapter_AdministrativeDistributionSecured_PortType";
    private static final String WSDL_FILE = "AdapterAdminDistSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":SendAlertMessageSecured_Message";

    private Log log = null;
    private static AdapterAdministrativeDistributionSecured adapterService = null;
    private static Service cachedService = null;

    public AdapterAdminDistributionProxyWebServiceSecuredImpl()
    {
        log = createLogger();
        adapterService = getWebService();
    }
    protected AdapterAdministrativeDistributionSecured getWebService()
    {
        return new AdapterAdministrativeDistributionSecured();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    protected WebServiceProxyHelper getWebserviceProxy()
    {
        return new WebServiceProxyHelper();
    }
    protected AdapterAdministrativeDistributionSecuredPortType getPort(String url, AssertionType assertion)
    {
        WebServiceProxyHelper proxyHelper = getWebserviceProxy();

        AdapterAdministrativeDistributionSecuredPortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterAdministrativeDistributionSecuredPortType.class);
            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url,NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME, WS_ADDRESSING_ACTION, assertion);
         }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion)
    {
        log.debug("Begin sendAlertMessage");
        String url = null;
        AdminDistributionHelper helper = getHelper();
        String target = helper.getLocalCommunityId();

        url = helper.getUrl(target, NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            AdapterAdministrativeDistributionSecuredPortType port = getPort(url, assertion);
            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();

            message.setEDXLDistribution(body);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);


            WebServiceProxyHelper oHelper = new WebServiceProxyHelper();
            oHelper.initializePort((javax.xml.ws.BindingProvider) port, url);


            try
            {
                getWebServiceProxyHelper().invokePort(port, AdapterAdministrativeDistributionSecuredPortType.class, "sendAlertMessage", message);
            }
            catch(Exception ex)
            {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }

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
