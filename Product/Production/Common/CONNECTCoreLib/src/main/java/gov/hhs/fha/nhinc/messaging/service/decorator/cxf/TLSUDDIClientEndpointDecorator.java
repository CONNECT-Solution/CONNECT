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
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import gov.hhs.fha.nhinc.cryptostore.StoreUtil;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mpnguyen
 *
 */
public class TLSUDDIClientEndpointDecorator<T> extends TLSClientServiceEndpointDecorator<T> {
    private static final Logger LOG = LoggerFactory.getLogger(TLSUDDIClientEndpointDecorator.class);

    /**
     * @param decoratoredEndpoint
     */
    public TLSUDDIClientEndpointDecorator(final ServiceEndpoint<T> decoratoredEndpoint, String exchangeName) {
        super(decoratoredEndpoint, StoreUtil.getGatewayCertificateAlias(exchangeName), exchangeName);
    }
    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.messaging.service.decorator.cxf.TLSClientServiceEndpointDecorator#configure()
     */
    @Override
    public void configure() {
        super.configure();
        final String protocol = getSecureProtocol();
        LOG.info("TLS support versions {}", protocol);
        TLSClientParameters tlsCP = getTlsClientFactory().getTLSClientParameters(protocol, certificateAlias,
            HomeCommunityMap.getSNIName(exchangeName));
        getHttpConduit().setTlsClientParameters(tlsCP);
    }

    /**
     * @return
     */
    private String getSecureProtocol() {
        String secureProtocol = null;
        try {
            secureProtocol = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.UDDI_TLS);
            LOG.debug("Retrieve UDDI {} from {} property",secureProtocol, NhincConstants.GATEWAY_PROPERTY_FILE);
        } catch (final PropertyAccessException e) {
            LOG.warn("Unable to retrieve {} {}",NhincConstants.UDDI_TLS, e.getLocalizedMessage(), e);
        }
        return StringUtils.isBlank(secureProtocol)? null : secureProtocol;
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

}
