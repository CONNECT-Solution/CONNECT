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
package gov.hhs.fha.nhinc.messaging.server.interceptor;


import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jassmit
 */
public class HSTSResponseOutInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(HSTSResponseOutInterceptor.class);
    private static final String DEFAULT_MAX_AGE = "31536000";
    
    public HSTSResponseOutInterceptor() {
        super(Phase.PRE_LOGICAL);
    }

    @Override
    public void handleMessage(Message message) {
        LOG.trace("Begin HSTSResponseOutInterceptor");
        Map<String, List> headers = (Map<String, List>) message.get(Message.PROTOCOL_HEADERS);
        
        if(headers == null) {
            headers = new HashMap<>();
        }
        
        String maxAge = null;
        
        try {
            maxAge = getMaxAgeValue();
        } catch (PropertyAccessException ex) {
            LOG.warn(ex.getLocalizedMessage(), ex);
        }
        
        if(NullChecker.isNullish(maxAge)) {
            maxAge = DEFAULT_MAX_AGE; 
        }
        
        final String hstsHeaderKey = "Strict-Transport-Security";
        final String hstsHeaderValue = "max-age=" + maxAge + "; includeSubDomains";
        
        headers.put(hstsHeaderKey, Collections.singletonList(hstsHeaderValue));
        
        message.put(Message.PROTOCOL_HEADERS, headers);
    }
    
    private String getMaxAgeValue() throws PropertyAccessException {
        return getPropAccessor().getProperty("gateway", "hstsMaxAge");
    }
    
    protected PropertyAccessor getPropAccessor() {
        return PropertyAccessor.getInstance();
    }

}
