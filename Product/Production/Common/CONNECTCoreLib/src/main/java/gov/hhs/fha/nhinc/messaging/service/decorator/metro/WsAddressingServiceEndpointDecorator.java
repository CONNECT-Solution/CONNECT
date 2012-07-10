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

package gov.hhs.fha.nhinc.messaging.service.decorator.metro;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;

import gov.hhs.fha.nhinc.async.AddressingHeaderCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.ServiceEndpointDecorator;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 * @author akong
 * 
 */
public class WsAddressingServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    private static final String UUID_TAG = "urn:uuid:";
    private Log log = null;
    
    private BindingProvider bindingProviderPort;
    private String url;
    private String wsAddressingAction;
    private AssertionType assertion;

    /**
     * 
     * @param decoratoredEndpoint
     * @param url
     * @param wsAddressingAction
     * @param assertion
     */
    public WsAddressingServiceEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint, String url,
            String wsAddressingAction, AssertionType assertion) {
        super(decoratoredEndpoint);
        log = createLogger();
        
        this.bindingProviderPort = (BindingProvider) decoratedEndpoint.getPort();
        this.url = url;
        this.wsAddressingAction = wsAddressingAction;
        this.assertion = assertion;
    }

    @Override
    public void configure() {
        super.configure();

        if (wsAddressingAction != null) {
            setOutboundHeaders();
        } else {
            if (hasInvalidMessageID()) {
                fixMessageIDPrefix();
            }
            log.warn("WS-Addressing information is unavailable, relying on wsdl policy");
        }        
    }
    
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }
        
    private void setOutboundHeaders() {
        List<Header> createdHeaders = createWSAddressingHeaders();
                
        ((WSBindingProvider) bindingProviderPort).setOutboundHeaders(createdHeaders);
    }
        
    /**
     * This method get the WS-Addressing headers to be initialized on the port
     * 
     * @return The list of WS-Addressing headers
     */
    private List<Header> createWSAddressingHeaders() {

        String messageId = getMessageId();
        List<String> allRelatesTo = getRelatesTo();

        AddressingHeaderCreator hdrCreator = new AddressingHeaderCreator(url, wsAddressingAction, messageId,
                allRelatesTo);

        List<Header> createdHeaders = hdrCreator.build();

        return createdHeaders;
    }
        
    /**
     * This method retrieves the message identifier stored in the assertion If the message ID is null or empty, this
     * method will generate a new UUID to use for the message ID.
     * 
     * @return The message identifier
     */
    private String getMessageId() {
        if ((assertion != null) && (NullChecker.isNotNullish(assertion.getMessageId()))) {
            if (hasProperMessageIDPrefix(assertion.getMessageId()) == false) {
                fixMessageIDPrefix();
            }
            return assertion.getMessageId();
        } else {
            UUID oUuid = UUID.randomUUID();
            String sUuid = UUID_TAG + oUuid.toString();
            log.warn("Assertion did not contain a message ID.  Generating one now...  Message ID = " + sUuid);
            if (assertion != null) {
                assertion.setMessageId(sUuid);
            }
            return sUuid;
        }
    }
    
    /**
     * @param messageId
     * @return
     */
    private boolean hasProperMessageIDPrefix(String messageId) {
        return messageId.trim().startsWith("urn:uuid:");
    }
    
    /**
     * @param assertion
     */
    private void fixMessageIDPrefix() {
        String messageId = assertion.getMessageId();
        if (illegalUUID(messageId, "uuid:")) {
            assertion.setMessageId("urn:" + messageId);
        } else {
            assertion.setMessageId("urn:uuid:" + messageId);
        }
    }
    
    /**
     * @param messageId
     * @param string
     * @return
     */
    private boolean illegalUUID(String messageId, String illegalPrefix) {
        return messageId.trim().startsWith(illegalPrefix);
    }

    /**
     * This method retrieves the list of relatesTo identifiers stored in the assertion.
     * 
     * @return The list of relatesTo identifiers
     */
    private List<String> getRelatesTo() {
        List<String> allRelatesTo = new ArrayList<String>();
        if (assertion != null && NullChecker.isNotNullish(assertion.getRelatesToList())) {
            allRelatesTo.addAll(assertion.getRelatesToList());
        }
        return allRelatesTo;
    }
    
    private boolean hasInvalidMessageID() {
        if (assertion != null && (NullChecker.isNotNullish(assertion.getMessageId()))
                && !hasProperMessageIDPrefix(assertion.getMessageId())) {
            return true;
        }
        return false;
    }
    

}
