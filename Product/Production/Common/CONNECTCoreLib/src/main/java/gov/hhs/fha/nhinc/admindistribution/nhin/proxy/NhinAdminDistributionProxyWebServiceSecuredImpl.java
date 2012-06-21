/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.admindistribution.nhin.proxy;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.HashMap;
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
public class NhinAdminDistributionProxyWebServiceSecuredImpl implements NhinAdminDistributionProxy {
    private Log log = null;
    private AdminDistributionAuditLogger adLogger = null;

    private static HashMap<String, Service> cachedServiceMap = new HashMap<String, Service>();
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhinadmindistribution";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_AdministrativeDistribution";
    private static final String PORT_LOCAL_PART = "RespondingGateway_AdministrativeDistribution_PortType";
    private static final String WSDL_FILE_G0 = "NhinAdminDist.wsdl";
    private static final String WSDL_FILE_G1 = "NhinAdminDist_g1.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:oasis:names:tc:emergency:EDXL:DE:1.0:SendAlertMessage";

    public NhinAdminDistributionProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    private Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    private AdminDistributionHelper getHelper() {
        return new AdminDistributionHelper();
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("begin sendAlertMessage");
        AdminDistributionHelper helper = getHelper();
        String url = helper.getUrl(target, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME, apiLevel);

        if (NullChecker.isNotNullish(url)) {
            RespondingGatewayAdministrativeDistributionPortType port = getPort(url, assertion, apiLevel);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            auditMessage(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

            try {
                log.debug("invoke port");
                getWebServiceProxyHelper().invokePort(port, RespondingGatewayAdministrativeDistributionPortType.class,
                        "sendAlertMessage", body);
            } catch (Exception ex) {
                log.error("Failed to call the web service (" + NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME
                        + ").  An unexpected exception occurred.  " + "Exception: " + ex.getMessage(), ex);
            }
        } else {
            log.error("Failed to call the web service (" + NhincConstants.ADAPTER_ADMIN_DIST_SERVICE_NAME
                    + ").  The URL is null.");
        }
    }

    protected RespondingGatewayAdministrativeDistributionPortType getPort(String url, AssertionType assertion,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        WebServiceProxyHelper proxyHelper = getWebServiceProxyHelper();

        RespondingGatewayAdministrativeDistributionPortType port = null;
        Service service;
        switch (apiLevel) {
        case LEVEL_g0:
            service = getService(WSDL_FILE_G0, NAMESPACE_URI, SERVICE_LOCAL_PART);
            break;
        case LEVEL_g1:
            service = getService(WSDL_FILE_G1, NAMESPACE_URI, SERVICE_LOCAL_PART);
            break;
        default:
            service = null;
        }
        if (service != null) {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                    RespondingGatewayAdministrativeDistributionPortType.class);
            proxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url,
                    NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME, WS_ADDRESSING_ACTION, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected Service getService(String wsdl, String uri, String service) {
        Service cachedService = cachedServiceMap.get(wsdl);
        if (cachedService == null) {
            try {
                WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
                cachedService = proxyHelper.createService(wsdl, uri, service);
                cachedServiceMap.put(wsdl, cachedService);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    protected void auditMessage(EDXLDistribution message, AssertionType assertion, String direction) {
        AcknowledgementType ack = getLogger().auditNhinAdminDist(message, assertion, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        if (ack != null) {
            log.debug("ack: " + ack.getMessage());
        }
    }

    protected AdminDistributionAuditLogger getLogger() {
        return (adLogger != null) ? adLogger : new AdminDistributionAuditLogger();
    }


}
