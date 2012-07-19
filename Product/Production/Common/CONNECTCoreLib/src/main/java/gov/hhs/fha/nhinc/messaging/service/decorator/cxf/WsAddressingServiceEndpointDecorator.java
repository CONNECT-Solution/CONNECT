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

package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.ServiceEndpointDecorator;
import gov.hhs.fha.nhinc.wsa.WSAHeaderHelper;

import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.RelatesToType;

/**
 * @author akong and young weezy
 * 
 */
public class WsAddressingServiceEndpointDecorator<T> extends ServiceEndpointDecorator<T> {

    private Log log = null;
    
    private BindingProvider bindingProviderPort;
    private AddressingPropertiesImpl maps;
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
        
        maps = new AddressingPropertiesImpl();
        
        AttributedURIType to = new AttributedURIType();
        log.debug("Setting wsa:To - " + url);
        to.setValue(url);
        maps.setTo(to);
        
        AttributedURIType action = new AttributedURIType();
        log.debug("Setting wsa:Action - " + wsAddressingAction);
        action.setValue(wsAddressingAction);
        maps.setAction(action);
        
        this.assertion = assertion;
    }

    @Override
    public void configure() {
        super.configure();

        String sRelatesTo = null;
        for (String s: assertion.getRelatesToList()) {
            sRelatesTo = s;
            break;
        }
        String sMessageId = assertion.getMessageId();
        
        WSAHeaderHelper wsaHelper = new WSAHeaderHelper();
        sRelatesTo = wsaHelper.addUrnUuid(sRelatesTo);
        sMessageId = wsaHelper.addUrnUuid(sMessageId);
        
        RelatesToType relatesTo = new RelatesToType();
        log.debug("Setting wsa:RelatesTo - " + sRelatesTo);
        relatesTo.setValue(sRelatesTo);
        maps.setRelatesTo(relatesTo);
        
        AttributedURIType messageId = new AttributedURIType();
        log.debug("Setting wsa:MessageId - " + sMessageId);
        messageId.setValue(sMessageId);
        maps.setMessageID(messageId);
        
        log.debug("Setting wsa attributes on the request context.");
        bindingProviderPort.getRequestContext().put(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES, maps);
    }
    
    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }
        
    
    

}
