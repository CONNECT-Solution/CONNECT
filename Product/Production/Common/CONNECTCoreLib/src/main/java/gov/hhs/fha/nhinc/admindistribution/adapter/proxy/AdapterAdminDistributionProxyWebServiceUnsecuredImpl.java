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

import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 *
 * @author dunnek
 */
public class AdapterAdminDistributionProxyWebServiceUnsecuredImpl implements AdapterAdminDistributionProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapteradmindistribution";
    private static final String SERVICE_LOCAL_PART = "Adapter_AdministrativeDistribution";
    private static final String PORT_LOCAL_PART = "Adapter_AdministrativeDistribution_PortType";
    private static final String WSDL_FILE = "AdapterAdminDist.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":SendAlertMessage_Message";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    public AdapterAdminDistributionProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        //adapterService = getWebService();
    }
    /*
     * protected AdapterAdministrativeDistribution getWebService() { return new
     * AdapterAdministrativeDistribution();
    }
     */

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        log.debug("Begin sendAlertMessage");
        String url = new AdminDistributionHelper().getAdapterUrl(NhincConstants.ADAPTER_ADMIN_DIST_SERVICE_NAME, ADAPTER_API_LEVEL.LEVEL_a0);

        if (NullChecker.isNotNullish(url)) {
            
            AdapterAdministrativeDistributionPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
            RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();

            message.setEDXLDistribution(body);
            message.setAssertion(assertion);
            try {
                oProxyHelper.invokePort(port, AdapterAdministrativeDistributionPortType.class, "sendAlertMessage", message);
            } catch (Exception ex) {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }
    }

    protected AdapterAdministrativeDistributionPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        AdapterAdministrativeDistributionPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                AdapterAdministrativeDistributionPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    protected AdminDistributionHelper getHelper() {
        return new AdminDistributionHelper();
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
