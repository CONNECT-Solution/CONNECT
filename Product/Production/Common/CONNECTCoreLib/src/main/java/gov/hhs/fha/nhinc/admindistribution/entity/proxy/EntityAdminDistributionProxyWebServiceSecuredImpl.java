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

package gov.hhs.fha.nhinc.admindistribution.entity.proxy;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionSecuredPortType;
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
public class EntityAdminDistributionProxyWebServiceSecuredImpl {
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entityadmindistribution";
    private static final String SERVICE_LOCAL_PART = "AdministrativeDistributionSecured_Service";
    private static final String PORT_LOCAL_PART = "AdministrativeDistributionSecured_PortType";
    private static final String WSDL_FILE = "EntityAdminDistSecured.wsdl";
    private static final String WSDL_FILE_G1 = "EntityAdminDistSecured_g1.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entityadmindistribution:SendAlertMessage_Message";

    private Log log = null;
    private static Service cachedService = null;
    private WebServiceProxyHelper proxyHelper = null;
    public EntityAdminDistributionProxyWebServiceSecuredImpl()
    {
        log = createLogger();
        proxyHelper =  getWebServiceProxyHelper();
    }
    private Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected AdministrativeDistributionSecuredPortType getPort(String url, String wsAddressingAction,
            AssertionType assertion, NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        AdministrativeDistributionSecuredPortType port = null;

        Service service = getService(apiLevel);
        if (service != null) {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdministrativeDistributionSecuredPortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetCommunitiesType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel)
    {
        log.debug("begin sendAlertMessage");
        AdminDistributionHelper helper = getHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            AdministrativeDistributionSecuredPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion, apiLevel);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            WebServiceProxyHelper webServiceHelper = new WebServiceProxyHelper();
            webServiceHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, NhincConstants.ADMIN_DIST_ACTION,
                    WS_ADDRESSING_ACTION, assertion);;

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();
            message.setEDXLDistribution(body);
            message.setNhinTargetCommunities(target);

            try
            {
                log.debug("invoke port");
                getWebServiceProxyHelper().invokePort(port, AdministrativeDistributionSecuredPortType.class, "sendAlertMessage", body);
            }
            catch(Exception ex)
            {
                log.error("Failed to call the web service (" + NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME + ").  An unexpected exception occurred.  " +
                        "Exception: " + ex.getMessage(), ex);
            }
        }
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    protected Service getService(NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        try {
            String wsdlFile = (apiLevel.equals(NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0)) ?
                    WSDL_FILE : WSDL_FILE_G1;
            return proxyHelper.createService(wsdlFile, NAMESPACE_URI, SERVICE_LOCAL_PART);
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
            return null;
        }
    } 
}
