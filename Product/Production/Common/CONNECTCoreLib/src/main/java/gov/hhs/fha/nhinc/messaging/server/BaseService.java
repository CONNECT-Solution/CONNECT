/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

/**
 * @author bhumphrey
 *
 */
public abstract class BaseService {

    private AsyncMessageIdExtractor extractor = new AsyncMessageIdExtractor();

    protected AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SAML2AssertionExtractor.getInstance().extractSamlAssertion(context);
        } else {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(extractor.getOrCreateAsyncMessageId(context));
        }

        // Extract the relates-to value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            List<String> relatesToList = extractor.getAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
                assertion.getRelatesToList().add(extractor.getAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
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
        if (context != null && context.getMessageContext() != null && context.getMessageContext().get(AbstractHTTPDestination.HTTP_REQUEST) != null) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) context.getMessageContext().get(AbstractHTTPDestination.HTTP_REQUEST);
            remoteAddress = httpServletRequest.getRemoteAddr();
        }
        return remoteAddress;
    }

    /**
     * Returns the local client host address
     *
     * @param context
     * @return
     */
    protected String getLocalAddress(WebServiceContext context) {
        String remoteAddress = null;
        if (context != null && context.getMessageContext() != null && context.getMessageContext().get(AbstractHTTPDestination.HTTP_REQUEST) != null) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) context.getMessageContext().get(AbstractHTTPDestination.HTTP_REQUEST);
            remoteAddress = httpServletRequest.getLocalAddr();
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

    /**
     * Returns a Web Service Context properties object with the following properties: 1. Web Service Request URL 2.
     * Remote Host Address
     *
     * @param context
     * @return
     */
    public Properties getWebContextProperties(WebServiceContext context) {
        Properties webContextProperties = new Properties();
        if (context != null && context.getMessageContext() != null) {
            //add Web Service Request URL
            webContextProperties.put(NhincConstants.WEB_SERVICE_REQUEST_URL, getWebServiceRequestUrl(context));
            //add Remote Server address or Host
            webContextProperties.put(NhincConstants.REMOTE_HOST_ADDRESS, getRemoteAddress(context));
            //add Local Server address or Host
            webContextProperties.put(NhincConstants.LOCAL_HOST_ADDRESS, getLocalAddress(context));

        }
        return webContextProperties;
    }
}
