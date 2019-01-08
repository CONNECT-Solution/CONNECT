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
package gov.hhs.fha.nhinc.messaging.client.interceptor;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author akong
 *
 */
public class SoapResponseInInterceptor extends AbstractSoapInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(SoapResponseInInterceptor.class);

    public SoapResponseInInterceptor() {
        super(Phase.USER_PROTOCOL);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        String responseMessageId = null;

        List<Header> headers = message.getHeaders();

        if (headers != null) {
            for (Header header : headers) {
                if (header.getName().getLocalPart().equalsIgnoreCase(NhincConstants.HEADER_MESSAGEID)) {
                    Element element = (Element) header.getObject();
                    responseMessageId = element.getFirstChild().getNodeValue();
                }
            }
        }

        message.put(NhincConstants.RESPONSE_MESSAGE_ID_KEY, responseMessageId);
    }

    /**
     * Adds the response message id obtained by the port to the message context.
     *
     * @param port
     * @param currentMessage
     */
    @SuppressWarnings("unchecked")
    public static void addResponseMessageIdToContext(Object port, Message currentMessage) {
        try {
            Client clientProxy = ClientProxy.getClient(port);

            Map<String, Object> responseContext = clientProxy.getResponseContext();
            String responseMsgId = (String) responseContext.get(NhincConstants.RESPONSE_MESSAGE_ID_KEY);

            if (responseMsgId != null && currentMessage != null) {

                List<String> responseMsgIdList = (List<String>) currentMessage.getExchange()
                        .get(NhincConstants.RESPONSE_MESSAGE_ID_LIST_KEY);
                if (responseMsgIdList == null) {
                    responseMsgIdList = new ArrayList<>();
                }
                responseMsgIdList.add(responseMsgId);

                currentMessage.getExchange().put(NhincConstants.RESPONSE_MESSAGE_ID_LIST_KEY, responseMsgIdList);
            }
        } catch (Exception e) {
            // Do nothing, but write it to the log.
            LOG.warn("Exception ", e);
        }
    }
}
