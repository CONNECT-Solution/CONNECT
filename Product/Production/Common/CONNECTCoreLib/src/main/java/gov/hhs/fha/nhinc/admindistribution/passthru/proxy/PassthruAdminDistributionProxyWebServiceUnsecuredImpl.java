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
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewaySendAlertMessageType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionProxyWebServiceUnsecuredImpl implements PassthruAdminDistributionProxy{
    private Log log = null;
    private static Service cachedService = null;
    private WebServiceProxyHelper proxyHelper = null;

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincadmindistribution";
    private static final String SERVICE_LOCAL_PART = "NhincAdminDistService";
    private static final String PORT_LOCAL_PART = "NhincAdminDist_PortType";
    private static final String WSDL_FILE_G0 = "NhincAdminDist.wsdl";
    private static final String WSDL_FILE_G1 = "NhincAdminDist_g1.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincadmindistribution:SendAlertMessage_Message";

    public PassthruAdminDistributionProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        proxyHelper =  getWebServiceProxyHelper();
    }
    protected AdminDistributionHelper getHelper()
    {
        return new AdminDistributionHelper();
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
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
    protected NhincAdminDistPortType getPort(String url, String wsAddressingAction, AssertionType assertion,
            NhincConstants.GATEWAY_API_LEVEL apiLevel)
    {
        NhincAdminDistPortType port = null;
        Service service;
        switch (apiLevel) {
            case LEVEL_g0: service = getService(WSDL_FILE_G0, NAMESPACE_URI, SERVICE_LOCAL_PART);
                break;
            case LEVEL_g1: service = getService(WSDL_FILE_G0, NAMESPACE_URI, SERVICE_LOCAL_PART);
                break;
            default: service = null;
        }
        if (service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincAdminDistPortType.class);
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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
        String url = helper.getUrl(hcid, NhincConstants.NHINC_ADMIN_DIST_SERVICE_NAME);

        if (NullChecker.isNotNullish(url))
        {
            NhincAdminDistPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion, apiLevel);
            RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
            message.setAssertion(assertion);
            message.setEDXLDistribution(body);
            message.setNhinTargetSystem(target);
            try
            {
                proxyHelper.invokePort(port, NhincAdminDistPortType.class, "sendAlertMessage", message);
            }
            catch(Exception ex)
            {
                log.error("Unable to send message: " + ex.getMessage());
            }
        }
    }
}
