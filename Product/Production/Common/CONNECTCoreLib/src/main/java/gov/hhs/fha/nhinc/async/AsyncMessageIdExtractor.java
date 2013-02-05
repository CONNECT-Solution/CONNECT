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
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Element;

/**
 * 
 * @author JHOPPESC
 */
public class AsyncMessageIdExtractor {

    protected Element getSoapHeaderElement(WebServiceContext context, String headerName) {
        if (context == null) {
            return null;
        }

        MessageContext mContext = context.getMessageContext();
        if (mContext == null) {
            return null;
        }

        return getSoapHeaderFromContext(headerName, mContext);
    }

    private Element getSoapHeaderFromContext(String headerName, MessageContext mContext) {
        @SuppressWarnings("unchecked")
        List<Header> headers = (List<Header>) mContext.get(Header.HEADER_LIST);
        if (CollectionUtils.isEmpty(headers)) {
            return null;
        }

        for (Header header : headers) {
            if (header.getName().getLocalPart().equalsIgnoreCase(headerName)) {
                return (Element) ((SoapHeader) header).getObject();
            }
        }

        return null;
    }

    public String getMessageId(WebServiceContext context) {
        String messageId = null;

        Element element = getSoapHeaderElement(context, NhincConstants.HEADER_MESSAGEID);
        if (element != null) {
            messageId = element.getFirstChild().getNodeValue();
        }

        return messageId;
    }

    public String getOrCreateAsyncMessageId(WebServiceContext context) {
        String messageId = getMessageId(context);

        if (messageId == null) {
            messageId = AddressingHeaderCreator.generateMessageId();
        }
        return messageId;
    }

    public List<String> getAsyncRelatesTo(WebServiceContext context) {
        List<String> relatesToId = new ArrayList<String>();

        Element element = getSoapHeaderElement(context, NhincConstants.HEADER_RELATESTO);
        if (element != null) {
            relatesToId.add(element.getFirstChild().getNodeValue());
        }

        return relatesToId;
    }

    public String getAction(WebServiceContext context) {
        String action = null;

        Element element = getSoapHeaderElement(context, NhincConstants.WS_SOAP_HEADER_ACTION);
        if (element != null) {
            action = element.getFirstChild().getNodeValue();
        }

        return action;
    }
}
