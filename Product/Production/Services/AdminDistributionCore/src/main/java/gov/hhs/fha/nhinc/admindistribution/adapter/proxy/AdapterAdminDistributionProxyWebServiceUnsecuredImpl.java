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
package gov.hhs.fha.nhinc.admindistribution.adapter.proxy;

import gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.service.AdapterAdminDistributionUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.admindistribution.aspect.EDXLDistributionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import javax.xml.ws.WebServiceException;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class AdapterAdminDistributionProxyWebServiceUnsecuredImpl implements AdapterAdminDistributionProxy {

    private static final Logger LOG = LoggerFactory
        .getLogger(AdapterAdminDistributionProxyWebServiceUnsecuredImpl.class);
    private AdminDistributionHelper adminDistributionHelper;

    /**
     * Constructor.
     */
    public AdapterAdminDistributionProxyWebServiceUnsecuredImpl() {
        adminDistributionHelper = getHelper();
    }

    /**
     * @return an instance of AdminDistributionHelper.
     */
    protected AdminDistributionHelper getHelper() {
        return new AdminDistributionHelper();
    }

    /**
     * This method returns CXFClient to implement AdpaterAdmin Dist Unsecured Service.
     *
     * @param portDescriptor comprises of NameSpaceUri, WSDLFile to read,Port, ServiceName and WS_ADDRESSING_ACTION.
     * @param url targetCommunity Url received.
     * @param assertion Assertion received.
     * @return CXFClient for AdapterAdminDist Unsecured Service.
     */
    protected CONNECTClient<AdapterAdministrativeDistributionPortType> getCONNECTClientUnsecured(
        ServicePortDescriptor<AdapterAdministrativeDistributionPortType> portDescriptor, String url,
        AssertionType assertion) {

        return CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, assertion);
    }

    protected String getUrl() {
        return adminDistributionHelper.getAdapterUrl(NhincConstants.ADAPTER_ADMIN_DIST_SERVICE_NAME,
            ADAPTER_API_LEVEL.LEVEL_a0);
    }

    /**
     * This method implements SendAlertMessage for AdminDist.
     *
     * @param body Emergency Message Distribution Element transaction message body received.
     * @param assertion Assertion received.
     */
    @AdapterDelegationEvent(beforeBuilder = EDXLDistributionEventDescriptionBuilder.class,
        afterReturningBuilder = EDXLDistributionEventDescriptionBuilder.class, serviceType = "Admin Distribution",
        version = "LEVEL_a0")
    @Override
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion) {
        LOG.debug("Begin sendAlertMessage");
        String url = getUrl();

        try {
            if (StringUtils.isBlank(url)) {
                throw new WebServiceException("Could not determine URL for Admin Distribution Adapter endpoint");
            } else {
                RespondingGatewaySendAlertMessageType message = new RespondingGatewaySendAlertMessageType();
                message.setEDXLDistribution(body);
                message.setAssertion(assertion);

                ServicePortDescriptor<AdapterAdministrativeDistributionPortType> portDescriptor = new AdapterAdminDistributionUnsecuredServicePortDescriptor();

                CONNECTClient<AdapterAdministrativeDistributionPortType> client = getCONNECTClientUnsecured(
                    portDescriptor, url, assertion);
                client.enableMtom();

                client.invokePort(AdapterAdministrativeDistributionPortType.class, "sendAlertMessage", message);
            }
        } catch (Exception ex) {
            throw new ErrorEventException(ex, "Unable to call Admin Distribution Adapter");
        }
    }

}
