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
package gov.hhs.fha.nhinc.perfrepo;

import java.util.Set;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;

/**
 * @author zmelnick
 * 
 */
public class PerformanceLogHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public void close(MessageContext message) {
    }

    @Override
    public Set getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext message) {
        if (isOutboundMessage(message) == true) {
            return handleResponse(message);
        } else {
            return handleRequest(message);
        }
    }

    private boolean handleResponse(SOAPMessageContext message) {
        String messageType = this.getMessageType(message);
        if (messageType != null) {
            PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(
                    message.get(MessageContext.WSDL_SERVICE).toString(), getMessageType(message),
                    NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        }
        return true;

    }

    private boolean handleRequest(SOAPMessageContext message) {
        String messageType = this.getMessageType(message);
        if (messageType != null) {
            PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(
                    message.get(MessageContext.WSDL_SERVICE).toString(), getMessageType(message),
                    NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, HomeCommunityMap.getLocalHomeCommunityId());
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext message) {
        return false;
    }

    private boolean isOutboundMessage(SOAPMessageContext message) {
        return (Boolean) message.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY);
    }

    private String getMessageType(SOAPMessageContext message) {
        String messagetype = message.get(SOAPMessageContext.WSDL_INTERFACE).toString();

        if (messagetype.contains(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE)) {
            return NhincConstants.AUDIT_LOG_ENTITY_INTERFACE;
        } else if (messagetype.contains(NhincConstants.AUDIT_LOG_NHIN_INTERFACE)) {
            return NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        } else if (messagetype.contains(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE)) {
            return NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE;
        }
        return null;

    }

}
