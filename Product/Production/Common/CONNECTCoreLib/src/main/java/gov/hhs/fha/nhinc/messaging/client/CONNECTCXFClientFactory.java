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
package gov.hhs.fha.nhinc.messaging.client;

import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import javax.xml.ws.WebServiceException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author akong
 *
 */
public class CONNECTCXFClientFactory extends CONNECTClientFactory {

    private static final String ERROR = "Could not resolve URL for CONNECT Webservice Client";
    private static final Logger LOG = LoggerFactory.getLogger(CONNECTCXFClientFactory.class);
    /**
     * Returns a CONNECTClient configured for secured invocation.
     */
    @Override
    public <T> CONNECTClient<T> getCONNECTClientSecured(ServicePortDescriptor<T> portDescriptor, String url, String exchangeName,
        AssertionType assertion) {

        if (StringUtils.isEmpty(url)) {
            throw new WebServiceException(ERROR);
        }

        return new CONNECTCXFClientSecured<>(portDescriptor, url, exchangeName, assertion);
    }

    /**
     * Returns a CONNECTClient configured for secured invocation. This method allows the target hcid and service name to
     * be passed to be used for purpose of/purpose for logic.
     */
    @Override
    public <T> CONNECTClient<T> getCONNECTClientSecured(ServicePortDescriptor<T> portDescriptor,
        AssertionType assertion, String url, NhinTargetSystemType target, String serviceName) {

        if (StringUtils.isEmpty(url)) {
            throw new WebServiceException(ERROR);
        }
        if (StringUtils.isBlank(target.getExchangeName())) {
            LOG.debug("Set default NHIN default exchange ");
            target.setExchangeName(ExchangeManager.getInstance().getDefaultExchange());
        }
        return new CONNECTCXFClientSecured<>(portDescriptor, assertion, url, target, serviceName);
    }

    /**
     * Returns a CONNECTClient configured for unsecured invocation.
     */
    @Override
    public <T> CONNECTClient<T> getCONNECTClientUnsecured(ServicePortDescriptor<T> portDescriptor, String url, String exchangeName,
        AssertionType assertion) {

        if (StringUtils.isEmpty(url)) {
            throw new WebServiceException(ERROR);
        }

        return new CONNECTCXFClientUnsecured<>(portDescriptor, url, exchangeName, assertion);
    }

}
