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

import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.service.PassthruAdminDistributionG0SecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.service.PassthruAdminDistributionG1SecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistSecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionProxyWebServiceSecuredImpl implements PassthruAdminDistributionProxy {

    private final Log log = LogFactory.getLog(getClass());;

    /**
     * Default Constructor.
     */
    public PassthruAdminDistributionProxyWebServiceSecuredImpl() {
    }

    /**
     * @return a new AdminDistributionHelper() object
     */
    protected AdminDistributionHelper getAdminDistributionHelper() {
        return new AdminDistributionHelper();
    }

    public ServicePortDescriptor<NhincAdminDistSecuredPortType> getServicePortDescriptor(
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_g0:
            return new PassthruAdminDistributionG0SecuredServicePortDescriptor();
        default:
            return new PassthruAdminDistributionG1SecuredServicePortDescriptor();
        }
    }

    protected CONNECTClient<NhincAdminDistSecuredPortType> getCONNECTClientSecured(
            ServicePortDescriptor<NhincAdminDistSecuredPortType> portDescriptor, String url, AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxy#sendAlertMessage(oasis.names
     * .tc.emergency.edxl.de._1.EDXLDistribution, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType,
     * gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType,
     * gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL)
     */
    @Override
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        log.debug("begin sendAlertMessage");

        AdminDistributionHelper helper = getAdminDistributionHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME, apiLevel);

        if (NullChecker.isNotNullish(url)) {
            try {
                RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();
                message.setEDXLDistribution(body);
                message.setNhinTargetSystem(target);

                ServicePortDescriptor<NhincAdminDistSecuredPortType> portDescriptor = getServicePortDescriptor(apiLevel);

                CONNECTClient<NhincAdminDistSecuredPortType> client = getCONNECTClientSecured(portDescriptor, url,
                        assertion);

                client.invokePort(NhincAdminDistSecuredPortType.class, "sendAlertMessage", message);

            } catch (Exception ex) {
                log.error("Unable to send message: " + ex.getMessage(), ex);
            }
        } else {
            log.error("Failed to call the web service (" + NhincConstants.NHINC_ADMIN_DIST_SECURED_SERVICE_NAME
                    + ").  The URL is null.");
        }
    }
}
