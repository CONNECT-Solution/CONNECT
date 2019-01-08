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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jassmit
 */
public class HttpHeaderRequestOutInterceptorTest {
    
    public HttpHeaderRequestOutInterceptorTest() {
    }
    
    @Test
    public void testHandleMessage() {
        Message message = new MessageImpl();
        
        final HashMap<String, String> customHttpHeaders = new HashMap<>();
        String headerName = "headerName";
        String headerValue = "headerValue";
        customHttpHeaders.put(headerName, headerValue);
        
        Map<String, List> protocolHeaders = new HashMap<>();
        List<String> connectionList = new ArrayList<>();
        String connectionHeader = "connection";
        String keepAliveValue = "keep-alive";
        
        connectionList.add(keepAliveValue);
        protocolHeaders.put(connectionHeader, connectionList);
        message.put(Message.PROTOCOL_HEADERS, protocolHeaders);
        
        HttpHeaderRequestOutInterceptor httpInterceptor = new HttpHeaderRequestOutInterceptor() {
            @Override
            protected Map<String,String> getCustomHttpHeaders(Message message) {
                return customHttpHeaders;
            }
        };
        httpInterceptor.handleMessage(message);
        
        Map<String, List> protocolHeadersResult = (Map<String, List>) message.get(Message.PROTOCOL_HEADERS);
        
        assertEquals(protocolHeadersResult.size(), 2);
        assert(protocolHeadersResult.containsKey(headerName));
        assertEquals(protocolHeadersResult.get(headerName).get(0), headerValue);
    }

}
