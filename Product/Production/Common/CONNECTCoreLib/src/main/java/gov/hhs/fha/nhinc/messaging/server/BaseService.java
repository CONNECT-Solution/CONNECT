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
package gov.hhs.fha.nhinc.messaging.server;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CONNECTCustomHttpHeadersType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import gov.hhs.fha.nhinc.wsa.WSAHeaderHelper;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.commons.collections.MapUtils;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey
 *
 */
public abstract class BaseService {

    private final AsyncMessageIdExtractor extractor = new AsyncMessageIdExtractor();
    private static final Logger LOG = LoggerFactory.getLogger(BaseService.class);
    private static HttpHeaderHelper headerHelper = new HttpHeaderHelper();

    protected AssertionType getAssertion(WebServiceContext context) {
        return getAssertion(context, null);
    }

    protected AssertionType getAssertion(WebServiceContext context, AssertionType assertionIn) {
        AssertionType assertion;
        WSAHeaderHelper wsaHelper = new WSAHeaderHelper();

        if (assertionIn == null) {
            assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
        } else {
            assertion = assertionIn;
        }

        if (assertion != null) {
            handleMessageId(assertion, context, wsaHelper);
            handleRelatesTo(context, assertion);
            handleHttpHeaders(assertion, context);
        }
        
        return assertion;
    }

    //Extract custom http headers from message context.
    private void handleHttpHeaders(AssertionType assertion, WebServiceContext context) {
        try {
            if (getPropertyAccessor().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.READ_HTTP_HEADERS)) {
                MessageContext messageContext = getMessageContext(context);

                if (messageContext instanceof WrappedMessageContext) {
                    readHttpHeaders(messageContext, assertion);
                }
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to access property for reading HTTP headers.", ex);
        }
    }

    // Extract the relates-to value from the WS-Addressing Header and place it in the Assertion Class
    private void handleRelatesTo(WebServiceContext context, AssertionType assertion) {
        List<String> relatesToList = extractor.getAsyncRelatesTo(context);
        if (NullChecker.isNotNullish(relatesToList)) {
            assertion.getRelatesToList().add(relatesToList.get(0));
        }
    }

    // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
    private void handleMessageId(AssertionType assertion, WebServiceContext context, WSAHeaderHelper wsaHelper) {
        if (NullChecker.isNullish(assertion.getMessageId())) {
            assertion.setMessageId(extractor.getOrCreateAsyncMessageId(context));
        } else {
            // Message ID prefix should be added here to the assertion's original messageID if it is missing.
            assertion.setMessageId(wsaHelper.fixMessageIDPrefix(assertion.getMessageId()));
        }
    }

    protected String getLocalHomeCommunityId() {
        return HomeCommunityMap.getLocalHomeCommunityId();
    }

    protected AssertionType extractAssertion(WebServiceContext context) {
        return SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
    }

    /**
     * Returns the Remote client host address
     *
     * @param context
     * @return
     */
    protected String getRemoteAddress(WebServiceContext context) {
        String remoteAddress = null;

        if (context != null && context.getMessageContext() != null
                && context.getMessageContext().get(AbstractHTTPDestination.HTTP_REQUEST) != null) {

            HttpServletRequest httpServletRequest = (HttpServletRequest) context.getMessageContext()
                    .get(AbstractHTTPDestination.HTTP_REQUEST);
            remoteAddress = httpServletRequest.getRemoteAddr();
        }

        return remoteAddress;
    }

    /**
     * Returns the called Web Service URL
     *
     * @param context
     * @return
     */
    protected String getWebServiceRequestUrl(WebServiceContext context) {
        String requestWebServiceUrl = null;

        if (context != null && context.getMessageContext() != null) {
            requestWebServiceUrl = (String) context.getMessageContext().get(org.apache.cxf.message.Message.REQUEST_URL);
        }

        return requestWebServiceUrl;
    }

    private String getInboundReplyToHeader(WebServiceContext context) {
        MessageContext messageContext = context.getMessageContext();

        if (messageContext == null || !(messageContext instanceof WrappedMessageContext)) {
            return null;
        }

        Message message = ((WrappedMessageContext) messageContext).getWrappedMessage();
        AddressingProperties maps = (AddressingProperties) message.get(NhincConstants.INBOUND_REPLY_TO_HEADER);
        return maps.getReplyTo().getAddress().getValue();
    }

    /**
     * Returns a Web Service Context properties object with the following
     * properties: 1. Web Service Request URL 2. Remote Host Address
     *
     * @param context
     * @return
     */
    public Properties getWebContextProperties(WebServiceContext context) {
        Properties webContextProperties = new Properties();

        if (context != null && context.getMessageContext() != null) {
            // Add Web Service Request URL
            webContextProperties.put(NhincConstants.WEB_SERVICE_REQUEST_URL, getWebServiceRequestUrl(context));

            // Add Remote Server address or Host
            webContextProperties.put(NhincConstants.REMOTE_HOST_ADDRESS, getRemoteAddress(context));

            // Get Inbound Message ReplyTo Header
            webContextProperties.put(NhincConstants.INBOUND_REPLY_TO, getInboundReplyToHeader(context));
        }

        return webContextProperties;
    }

    private static void readHttpHeaders(MessageContext messageContext, AssertionType assertion) {
        Message message = ((WrappedMessageContext) messageContext).getWrappedMessage();
        if (message != null && message.get(Message.PROTOCOL_HEADERS) != null) {
            Map<String, List<String>> headers = CastUtils.cast((Map) message.get(Message.PROTOCOL_HEADERS));
            if (MapUtils.isNotEmpty(headers)) {
                addHeadersToAssertion(headers, assertion);
            }
        }
    }

    private static void addHeadersToAssertion(Map<String, List<String>> headers, AssertionType assertion) {
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            if (!headerHelper.isStandardHeader(entry.getKey()) && !headers.get(entry.getKey()).isEmpty()) {
                CONNECTCustomHttpHeadersType assertionHeader = new CONNECTCustomHttpHeadersType();
                assertionHeader.setHeaderName(entry.getKey());
                assertionHeader.setHeaderValue(entry.getValue().get(0));
                assertion.getCONNECTCustomHttpHeaders().add(assertionHeader);
            }
        }
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    protected MessageContext getMessageContext(WebServiceContext context) {
        return context.getMessageContext();
    }

}
