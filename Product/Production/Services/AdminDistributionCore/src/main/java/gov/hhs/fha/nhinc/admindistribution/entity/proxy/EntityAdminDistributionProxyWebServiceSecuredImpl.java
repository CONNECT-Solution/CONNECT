/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
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
package gov.hhs.fha.nhinc.admindistribution.entity.proxy;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.admindistribution.entity.proxy.service.EntityAdminDistributionG0SecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.admindistribution.entity.proxy.service.EntityAdminDistributionG1SecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionSecuredPortType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class EntityAdminDistributionProxyWebServiceSecuredImpl {
    private static final Logger LOG = LoggerFactory.getLogger(EntityAdminDistributionProxyWebServiceSecuredImpl.class);

    /**
     * @return instance of AdminDistributionHelper.
     */
    protected AdminDistributionHelper getHelper() {
        return new AdminDistributionHelper();
    }

    /**
     * @return instance of WebServiceProxyHelper.
     */
    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method returns EntityAdminDistributionSecuredServicePortDescriptor based on gateway apiLevel.
     * 
     * @param apiLevel gateway apiLevel received (g0/g1).
     * @return instance of EntityAdminDistributionSecuredServicePortDescriptor based on gateway apiLevel.
     */
    public ServicePortDescriptor<AdministrativeDistributionSecuredPortType> getServicePortDescriptor(
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        switch (apiLevel) {
        case LEVEL_g0:
            return new EntityAdminDistributionG0SecuredServicePortDescriptor();
        default:
            return new EntityAdminDistributionG1SecuredServicePortDescriptor();
        }
    }

    /**
     * This method returns CXFClient to implement AdminDist Secured Service.
     * 
     * @param portDescriptor comprises of NameSpaceUri, WSDL File, Port, ServiceName and WS_ADDRESSING_ACTION.
     * @param url target community url .
     * @param assertion Assertion received.
     * @return CXFClient to implement AdminDist Secured Service.
     */
    protected CONNECTClient<AdministrativeDistributionSecuredPortType> getCONNECTClientSecured(
            ServicePortDescriptor<AdministrativeDistributionSecuredPortType> portDescriptor, String url,
            AssertionType assertion) {

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientSecured(portDescriptor, url, assertion);
    }

    /**
     * This method implements sendAlertMessage from initiater to responder.
     * 
     * @param body Emergency Message Distribution Element transaction message body received.
     * @param assertion Assertion received.
     * @param target NhinTargetCommunity receievd.
     * @param apiLevel gateway apiLevel received (g0/g1).
     */
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetCommunitiesType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel) {
        LOG.debug("begin sendAlertMessage");
        AdminDistributionHelper helper = getHelper();
        String hcid = helper.getLocalCommunityId();
        String url = helper.getUrl(hcid, NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME, apiLevel);

        if (NullChecker.isNotNullish(url)) {
            try {
                RespondingGatewaySendAlertMessageSecuredType message = new RespondingGatewaySendAlertMessageSecuredType();
                message.setEDXLDistribution(body);
                message.setNhinTargetCommunities(target);

                ServicePortDescriptor<AdministrativeDistributionSecuredPortType> portDescriptor = getServicePortDescriptor(
                        apiLevel);

                CONNECTClient<AdministrativeDistributionSecuredPortType> client = getCONNECTClientSecured(
                        portDescriptor, url, assertion);

                client.invokePort(AdministrativeDistributionSecuredPortType.class, "sendAlertMessage", message);
            } catch (Exception ex) {
                LOG.error("Failed to call the web service (" + NhincConstants.ENTITY_ADMIN_DIST_SECURED_SERVICE_NAME
                        + ").  An unexpected exception occurred.  " + "Exception: " + ex.getMessage(), ex);
            }
        } else {
            LOG.error("Failed to call the web service (" + NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME
                    + ").  The URL is null.");
        }
    }
}
