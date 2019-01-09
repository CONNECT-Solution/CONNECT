/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionUtils;
import gov.hhs.fha.nhinc.admindistribution.aspect.EDXLDistributionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.service.NhinAdminDistributionG0ServicePortDescriptor;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.service.NhinAdminDistributionG1ServicePortDescriptor;
import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class NhinAdminDistributionProxyWebServiceSecuredImpl implements NhinAdminDistributionProxy {

    private static final Logger LOG = LoggerFactory.getLogger(NhinAdminDistributionProxyWebServiceSecuredImpl.class);
    private AdminDistributionAuditLogger adLogger = null;

    /**
     * @return AdminDistributionUtils instance.
     */
    protected AdminDistributionUtils getAdminDistributionUtils() {
        return AdminDistributionUtils.getInstance();
    }

    protected AdminDistributionHelper getHelper() {
        return new AdminDistributionHelper();
    }

    /**
     * @return WebServiceProxyHelper instance.
     */
    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method returns ServicePortDescriptor for AdminDist based on gateway apiLevel.
     *
     * @param apiLevel gateway apiLevel received (g0/g1).
     * @return NhinAdminDistributionPortDescriptor based on g0/g1 impl
     */
    public ServicePortDescriptor<RespondingGatewayAdministrativeDistributionPortType> getServicePortDescriptor(
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {

        switch (apiLevel) {
        case LEVEL_g0:
            return new NhinAdminDistributionG0ServicePortDescriptor();
        default:
            return new NhinAdminDistributionG1ServicePortDescriptor();
        }
    }

    /**
     * This method returns CXFClient to implement AdminDist Secured Service.
     *
     * @param portDescriptor Comprises of
     * @param url target community url to send the message.
     * @param assertion Assertion received.
     * @param target
     * @param serviceName
     * @return CXFClient to implement Secured Service.
     */
    protected CONNECTClient<RespondingGatewayAdministrativeDistributionPortType> getCONNECTClientSecured(
            ServicePortDescriptor<RespondingGatewayAdministrativeDistributionPortType> portDescriptor, String url,
            AssertionType assertion, NhinTargetSystemType target, String serviceName) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, assertion, url, target,
                serviceName);
    }

    /**
     * This method implements sendAlertMessage for AdminDist.
     *
     * @param body Emergency Message Distribution Element transaction messgae body.
     * @param assertion Assertion received.
     * @param target NhinTargetCommunity received.
     * @param apiLevel gateway apiLevel (g0/g1).
     */
    @Override
    @NwhinInvocationEvent(beforeBuilder = EDXLDistributionEventDescriptionBuilder.class, afterReturningBuilder =
    EDXLDistributionEventDescriptionBuilder.class, serviceType = "Admin Distribution", version = "")
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {

        LOG.debug("begin sendAlertMessage");
        auditMessage(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, target);

        String url = getHelper().getUrl(target, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME, apiLevel);
        try {
            if (NullChecker.isNotNullish(url)) {
                getAdminDistributionUtils().convertFileLocationToDataIfEnabled(body);

                ServicePortDescriptor<RespondingGatewayAdministrativeDistributionPortType> portDescriptor =
                    getServicePortDescriptor(apiLevel);

                CONNECTClient<RespondingGatewayAdministrativeDistributionPortType> client = getCONNECTClientSecured(
                        portDescriptor, url, assertion, target, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);

                if (apiLevel == GATEWAY_API_LEVEL.LEVEL_g1) {
                    client.enableMtom();
                }

                client.invokePort(RespondingGatewayAdministrativeDistributionPortType.class, "sendAlertMessage", body);
            } else {
                throw new WebServiceException("Could not determine URL for Nhin Admin Distribution endpoint");
            }
        } catch (Exception ex) {
            throw new ErrorEventException(ex, "Unable to call Nhin Admin Distribution");
        }
    }

    /**
     * This method audits the AdminDist Service at Nhin interface.
     *
     * @param message Emergency Message Distribution Element transaction message.
     * @param assertion Assertion received.
     * @param direction The direction can be eigther outbound or inbound.
     * @param target
     */
    protected void auditMessage(EDXLDistribution message, AssertionType assertion, String direction,
            NhinTargetSystemType target) {

        AcknowledgementType ack = getLogger().auditNhinAdminDist(message, assertion, direction, target,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        if (ack != null) {
            LOG.debug("ack: " + ack.getMessage());
        }
    }

    /**
     * @return Nhin AdminDist audit logger.
     */
    protected AdminDistributionAuditLogger getLogger() {
        return adLogger != null ? adLogger : new AdminDistributionAuditLogger();
    }

}
