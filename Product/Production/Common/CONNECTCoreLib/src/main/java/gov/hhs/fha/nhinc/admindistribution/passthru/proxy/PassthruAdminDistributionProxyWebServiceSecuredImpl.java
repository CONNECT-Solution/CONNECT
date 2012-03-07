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
package gov.hhs.fha.nhinc.admindistribution.passthru.proxy;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.admindistribution.PassthruAdminDistributionHelper;
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

    private Log log = LogFactory.getLog(getClass());;
    private WebServiceProxyHelper proxyHelper;
    private PassthruAdminDistributionHelper adminDistributionHelper;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincadmindistribution";
    private static final String SERVICE_LOCAL_PART = "NhincAdminDistSecuredService";
    private static final String PORT_LOCAL_PART = "NhincAdminDistSecured_PortType";
    private static final String WSDL_FILE_G0 = "NhincAdminDistSecured.wsdl";
    private static final String WSDL_FILE_G1 = "NhincAdminDistSecured_g1.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincadmindistribution:SendAlertMessageSecured_Message";

    public PassthruAdminDistributionProxyWebServiceSecuredImpl() {
        proxyHelper = new WebServiceProxyHelper();
        adminDistributionHelper = new PassthruAdminDistributionHelper(proxyHelper, WSDL_FILE_G0, WSDL_FILE_G1,
                NAMESPACE_URI, SERVICE_LOCAL_PART, PORT_LOCAL_PART, WS_ADDRESSING_ACTION);
    }

    public PassthruAdminDistributionProxyWebServiceSecuredImpl(WebServiceProxyHelper proxyHelper,
            PassthruAdminDistributionHelper adminDistributionHelper) {
        this.proxyHelper = proxyHelper;
        this.adminDistributionHelper = adminDistributionHelper;
    }

    protected AdminDistributionHelper getHelper() {
        return adminDistributionHelper;
    }

    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("begin sendAlertMessage");

        String hcid = adminDistributionHelper.getLocalCommunityId();
        String url = adminDistributionHelper.getUrl(hcid, NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME,
                apiLevel);

        if (NullChecker.isNotNullish(url)) {
            NhincAdminDistSecuredPortType port = adminDistributionHelper.getSecuredPort(url,
                    NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME, WS_ADDRESSING_ACTION, assertion, apiLevel);
            RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();

            message.setEDXLDistribution(body);
            message.setNhinTargetSystem(target);
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADMIN_DIST_ACTION);

            try {
                ((BindingProvider) port).getRequestContext().putAll(requestContext);
                proxyHelper.invokePort(port, RespondingGatewaySendAlertMessageSecuredType.class, "sendAlertMessage",
                        message);
            } catch (Exception ex) {
                log.error("Unable to send message: " + ex.getMessage());
            }
        } else {
            log.error("Failed to call the web service (" + NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME
                    + ").  The URL is null.");
        }
    }
}
