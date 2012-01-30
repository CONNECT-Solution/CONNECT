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
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageSecuredType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
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
public class AdapterAdminDistributionProxyWebServiceSecuredImpl implements AdapterAdminDistributionProxy {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapteradmindistribution";
    private static final String SERVICE_LOCAL_PART = "Adapter_AdministrativeDistributionSecured";
    private static final String PORT_LOCAL_PART = "Adapter_AdministrativeDistributionSecured_PortType";
    private static final String WSDL_FILE = "AdapterAdminDistSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":SendAlertMessageSecured_Message";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();
    private static Service cachedService = null;

    public AdapterAdminDistributionProxyWebServiceSecuredImpl() {
        log = createLogger();
    }
    /*
     * protected AdapterAdministrativeDistributionSecured getWebService() {
     * return new AdapterAdministrativeDistributionSecured();
    }
     */

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected AdminDistributionHelper getHelper() {
        return new AdminDistributionHelper();
    }

    protected AdapterAdministrativeDistributionSecuredPortType getPort(String url, AssertionType assertion) {

        AdapterAdministrativeDistributionSecuredPortType port = null;
        Service cacheService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null) {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterAdministrativeDistributionSecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME,
                    WS_ADDRESSING_ACTION, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        log.debug("Begin sendAlertMessage");
        String url = new AdminDistributionHelper().getAdapterUrl(
                NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME, ADAPTER_API_LEVEL.LEVEL_a0);

        if (NullChecker.isNotNullish(url)) {
            AdapterAdministrativeDistributionSecuredPortType port = getPort(url, assertion);
            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();

            message.setEDXLDistribution(body);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            try {
                getWebServiceProxyHelper().invokePort(port, AdapterAdministrativeDistributionSecuredPortType.class, "sendAlertMessage", message);
            } catch (Exception ex) {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(wsdl, uri, service);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}