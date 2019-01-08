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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.ServiceEndpointDecorator;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.wsa.WSAHeaderHelper;
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.Names;
import org.apache.cxf.ws.addressing.RelatesToType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akong
 *
 */
public class WsAddressingServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    private static final Logger LOG = LoggerFactory.getLogger(WsAddressingServiceEndpointDecorator.class);

    private BindingProvider bindingProviderPort;
    private AddressingProperties maps;
    private AssertionType assertion;

    public WsAddressingServiceEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint, String wsAddressingTo,
        String wsAddressingAction, AssertionType assertion) {
        super(decoratoredEndpoint);

        this.bindingProviderPort = (BindingProvider) decoratedEndpoint.getPort();
        if (assertion != null) {
            this.assertion = assertion;
        }

        maps = new AddressingProperties();

        AttributedURIType to = new AttributedURIType();
        to.setValue(wsAddressingTo);
        maps.setTo(to);

        AttributedURIType action = new AttributedURIType();
        action.setValue(wsAddressingAction);
        maps.getMustUnderstand().add(Names.WSA_ACTION_QNAME);
        maps.setAction(action);

        EndpointReferenceType replyTo = new EndpointReferenceType();
        AttributedURIType replyToAddress = new AttributedURIType();
        replyToAddress.setValue(NhincConstants.WSA_REPLY_TO);
        replyTo.setAddress(replyToAddress);
        maps.getMustUnderstand().add(Names.WSA_REPLYTO_QNAME);
        maps.setReplyTo(replyTo);

        setContentTypeInHTTPHeader();
    }

    @Override
    public void configure() {
        super.configure();

        String sRelatesTo = null;
        if (assertion != null) {
            for (String s : assertion.getRelatesToList()) {
                sRelatesTo = s;
                break;
            }
        }
        String sMessageId = getMessageId();

        if (!StringUtils.isBlank(sRelatesTo)) {
            RelatesToType relatesTo = new RelatesToType();
            relatesTo.setValue(sRelatesTo);
            maps.setRelatesTo(relatesTo);
        }

        AttributedURIType messageId = new AttributedURIType();
        messageId.setValue(sMessageId);
        maps.setMessageID(messageId);

        bindingProviderPort.getRequestContext().put(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES, maps);
    }

    /**
     * Sets the content type in the HTTP header. It will create one if needed as leaving it empty will cause CXF to
     * automatically generates it with an action parameter that will cause problems. As the action parameter is optional
     * for Soap 1.2, we will ensure that it is not included.
     */
    private void setContentTypeInHTTPHeader() {
        HTTPClientPolicy httpClientPolicy = getHTTPClientPolicy();

        String contentType = httpClientPolicy.getContentType();
        if (NullChecker.isNullish(contentType)) {
            contentType = "application/soap+xml; charset=UTF-8";
        } else {
            contentType = removeActionFromContentType(contentType);
        }

        httpClientPolicy.setContentType(contentType);
    }

    private static String removeActionFromContentType(String contentType) {
        String[] contentTypeValues = contentType.split("(?<=;)");

        String newContentType = "";
        for (int i = 0; i < contentTypeValues.length; i++) {
            if (!contentTypeValues[i].trim().startsWith("version")) {
                newContentType = newContentType.concat(contentTypeValues[i]);
            }
        }

        return newContentType;
    }

    /**
     * This method retrieves the message identifier stored in the assertion If the message ID is null or empty, this
     * method will generate a new UUID to use for the message ID.
     *
     * @return The message identifier
     */
    private String getMessageId() {
        WSAHeaderHelper wsaHelper = new WSAHeaderHelper();

        String messageId = null;
        if (assertion != null) {
            messageId = assertion.getMessageId();
        }

        if (NullChecker.isNullish(messageId)) {
            messageId = wsaHelper.generateMessageID();
            LOG.warn("Assertion did not contain a message ID.  Generating one now...  Message ID = {}", messageId);
        } else {
            messageId = wsaHelper.fixMessageIDPrefix(messageId);
        }

        return messageId;
    }

}
