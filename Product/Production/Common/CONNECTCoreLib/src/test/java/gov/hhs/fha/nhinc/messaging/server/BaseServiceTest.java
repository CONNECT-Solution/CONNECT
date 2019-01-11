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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CONNECTCustomHttpHeadersType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author jassmit
 */
public class BaseServiceTest {
    
    private static final String HEADER_NAME_1 = "headerName1";
    private static final String HEADER_NAME_2 = "headerName2";
    private static final String HEADER_VALUE_1 = "headerValue1";
    private static final String HEADER_VALUE_2 = "headerValue2";
    private static final String HEADER_NAME_CONNECTION = "connection";
    private static final String HEADER_VALUE_CONNECTION = "keep-alive";
    private Map<String, List<String>> happyHeaders; 
    private Map<String, List<String>> filterHeaders;
    
   
    public BaseServiceTest() {   
    }
    
    @Before
    public void setUp() {
        happyHeaders = new HashMap<>();
        List<String> values1 = new ArrayList<>();
        values1.add(HEADER_VALUE_1);
        
        List<String> values2 = new ArrayList<>();
        values2.add(HEADER_VALUE_2);
        
        List<String> connectionValues = new ArrayList<>();
        connectionValues.add(HEADER_VALUE_CONNECTION);
        
        happyHeaders.put(HEADER_NAME_1, values1);
        happyHeaders.put(HEADER_NAME_2, values2);
        
        filterHeaders = new HashMap<>();
        filterHeaders.put(HEADER_NAME_1, values1);
        filterHeaders.put(HEADER_NAME_2, values2);
        filterHeaders.put(HEADER_NAME_CONNECTION, connectionValues);
    }
    
    @After
    public void tearDown() {
        happyHeaders = filterHeaders = null;
    }
    
    @Test
    public void testReadHttpHeaders_ReadOff() throws PropertyAccessException {
        final PropertyAccessor mockPropAccessor = mock(PropertyAccessor.class);
        
        TestService testService = new TestService() {
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockPropAccessor;
            }
        };
        
        AssertionType resultAssertion = new AssertionType();
        
        when(mockPropAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.READ_HTTP_HEADERS)).thenReturn(Boolean.FALSE);
        
        testService.getAssertion(resultAssertion);
        
        List<CONNECTCustomHttpHeadersType> resultHeaders = resultAssertion.getCONNECTCustomHttpHeaders();
        
        assert(resultHeaders.isEmpty());
    }
    
    @Test
    public void testReadHttpHeaders_ReadsTwoValues() throws PropertyAccessException {
        final PropertyAccessor mockPropAccessor = mock(PropertyAccessor.class);
        final MessageImpl mockMessage = mock(MessageImpl.class);
        final WrappedMessageContext messageContext = new WrappedMessageContext(mockMessage);
        
        TestService testService = new TestService() {
            
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockPropAccessor;
            }
            
            @Override
            protected MessageContext getMessageContext(WebServiceContext context) {
                return messageContext;
            }
        };

        AssertionType resultAssertion = new AssertionType();
        
        when(mockPropAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.READ_HTTP_HEADERS)).thenReturn(Boolean.TRUE);
        when(mockMessage.get(Message.PROTOCOL_HEADERS)).thenReturn(happyHeaders);
        
        testService.getAssertion(resultAssertion);
        
        List<CONNECTCustomHttpHeadersType> resultHeaders = resultAssertion.getCONNECTCustomHttpHeaders();
        assertNotNull(resultHeaders);
        assertEquals(resultAssertion.getCONNECTCustomHttpHeaders().size(), happyHeaders.size());
        assert(happyHeaders.keySet().contains(resultHeaders.get(0).getHeaderName()));
        assert(happyHeaders.keySet().contains(resultHeaders.get(1).getHeaderName()));        
    }
    
    @Test
    public void testReadHttpHeaders_FilterDefaultHeader() throws PropertyAccessException {
        final PropertyAccessor mockPropAccessor = mock(PropertyAccessor.class);
        final MessageImpl mockMessage = mock(MessageImpl.class);
        final WrappedMessageContext messageContext = new WrappedMessageContext(mockMessage);
        
        TestService testService = new TestService() {
            
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockPropAccessor;
            }
            
            @Override
            protected MessageContext getMessageContext(WebServiceContext context) {
                return messageContext;
            }
        };

        AssertionType resultAssertion = new AssertionType();
        
        when(mockPropAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.READ_HTTP_HEADERS)).thenReturn(Boolean.TRUE);
        when(mockMessage.get(Message.PROTOCOL_HEADERS)).thenReturn(filterHeaders);
        
        testService.getAssertion(resultAssertion);
        
        List<CONNECTCustomHttpHeadersType> resultHeaders = resultAssertion.getCONNECTCustomHttpHeaders();
        assertNotNull(resultHeaders);
        assertEquals(resultAssertion.getCONNECTCustomHttpHeaders().size(), filterHeaders.size() - 1);
        
        boolean containsDefaultHeader = false;
        for(CONNECTCustomHttpHeadersType header : resultHeaders) {
            if(header.getHeaderName().equals(HEADER_NAME_CONNECTION)) {
                containsDefaultHeader = true;
                break;
            }
        }
        
        assertFalse(containsDefaultHeader);
    }
    
    
    class TestService extends BaseService{
    
        public AssertionType getAssertion(AssertionType assertion) {
            return getAssertion(null, assertion);
        }
        
    }

}
