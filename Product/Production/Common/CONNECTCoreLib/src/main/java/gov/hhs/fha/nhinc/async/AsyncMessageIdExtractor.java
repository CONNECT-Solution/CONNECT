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

import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.JAXWSProperties;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.xml.ws.WebServiceContext;

/**
 * 
 * @author JHOPPESC
 */
public class AsyncMessageIdExtractor {
	private static String UUID_TAG = "urn:uuid:";

    public static String GetAsyncMessageId(WebServiceContext context) {
        String messageId = null;

        if (context != null && context.getMessageContext() != null) {
            Object oList = context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
            if (oList instanceof HeaderList) {
                HeaderList hlist = (HeaderList) oList;
                Header header = hlist.get(NhincConstants.NS_ADDRESSING_2005, NhincConstants.HEADER_MESSAGEID, false);
                if (header != null) {
                    messageId = header.getStringContent();
                }
            }
        }

        if (messageId == null) {
            messageId = UUID_TAG + UUID.randomUUID().toString();
        }
        return messageId;
    }

    public static List<String> GetAsyncRelatesTo(WebServiceContext context) {
        List<String> relatesToId = new ArrayList<String>();

        if (context != null && context.getMessageContext() != null) {
            Object oList = context.getMessageContext().get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY);
            if (oList instanceof HeaderList) {
                HeaderList hlist = (HeaderList) oList;
                Iterator<Header> allRelatesToHeaders = hlist.getHeaders(NhincConstants.NS_ADDRESSING_2005,
                        NhincConstants.HEADER_RELATESTO, false);
                while (allRelatesToHeaders.hasNext()) {
                    Header header = allRelatesToHeaders.next();

                    if (header != null) {
                        relatesToId.add(header.getStringContent());
                    }
                }
            }
        }
        return relatesToId;
    }
}
