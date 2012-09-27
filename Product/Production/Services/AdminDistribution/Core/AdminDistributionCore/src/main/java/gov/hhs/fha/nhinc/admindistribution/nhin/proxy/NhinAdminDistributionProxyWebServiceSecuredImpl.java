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
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.service.NhinAdminDistributionG0ServicePortDescriptor;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.service.NhinAdminDistributionG1ServicePortDescriptor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import javax.xml.ws.BindingProvider;

import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class NhinAdminDistributionProxyWebServiceSecuredImpl implements NhinAdminDistributionProxy {
    private Log log = null;
    private AdminDistributionAuditLogger adLogger = null;


    public NhinAdminDistributionProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    private Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    private AdminDistributionHelper getHelper() {
        return new AdminDistributionHelper();
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public ServicePortDescriptor<RespondingGatewayAdministrativeDistributionPortType> getServicePortDescriptor(
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_g0:
            return new NhinAdminDistributionG0ServicePortDescriptor();
        default:
            return new NhinAdminDistributionG1ServicePortDescriptor();
        }
    }

    protected CONNECTClient<RespondingGatewayAdministrativeDistributionPortType> getCONNECTClientSecured(
            ServicePortDescriptor<RespondingGatewayAdministrativeDistributionPortType> portDescriptor, String url,
            AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

    @Override
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("begin sendAlertMessage");
        AdminDistributionHelper helper = getHelper();
        String url = helper.getUrl(target, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME, apiLevel);

        if (NullChecker.isNotNullish(url)) {
            
            
            auditMessage(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
            
            try {
                ServicePortDescriptor<RespondingGatewayAdministrativeDistributionPortType> portDescriptor =
                        getServicePortDescriptor(apiLevel);

                CONNECTClient<RespondingGatewayAdministrativeDistributionPortType> client = getCONNECTClientSecured(
                        portDescriptor, url, assertion);

                WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
                wsHelper.addTargetCommunity((BindingProvider) client.getPort(), target);
                wsHelper.addTargetApiLevel((BindingProvider) client.getPort(), apiLevel);
                wsHelper.addServiceName((BindingProvider) client.getPort(), 
                        NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);

                client.invokePort(RespondingGatewayAdministrativeDistributionPortType.class, "sendAlertMessage", body);
            } catch (Exception ex) {
                log.error("Failed to call the web service (" + NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME
                        + ").  An unexpected exception occurred.  " + "Exception: " + ex.getMessage(), ex);
            }
        } else {
            log.error("Failed to call the web service (" + NhincConstants.ADAPTER_ADMIN_DIST_SERVICE_NAME
                    + ").  The URL is null.");
        }
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
