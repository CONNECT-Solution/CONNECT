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
package gov.hhs.fha.nhinc.callback.cxf.wss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import javax.xml.namespace.QName;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.dom.engine.WSSConfig;
import org.apache.wss4j.dom.processor.Processor;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class SecurityConfigInInterceptorTest {

    @Test
    public void phaseConfiguration() {
        SecurityConfigInInterceptor interceptor = new SecurityConfigInInterceptor();

        assertEquals(Phase.PRE_PROTOCOL, interceptor.getPhase());
        assertTrue(interceptor.getBefore().contains(WSS4JInInterceptor.class.getName()));
    }

    @Test
    public void verifySecurityConfig() throws WSSecurityException {
        Message msg = new MessageImpl();

        SecurityConfigInInterceptor interceptor = new SecurityConfigInInterceptor();
        interceptor.handleMessage(msg);

        assertSecurityProcessorIsRegistered(msg);
    }

    @Test
    public void verifySecurityConfigWithPresetWSSConfig() throws WSSecurityException {
        Message msg = new MessageImpl();
        //msg.setContextualProperty(WSSConfig.class.getName(), WSSConfig.getNewInstance());
        msg.put(WSSConfig.class.getName(), WSSConfig.getNewInstance());
        SecurityConfigInInterceptor interceptor = new SecurityConfigInInterceptor();
        interceptor.handleMessage(msg);

        assertSecurityProcessorIsRegistered(msg);
    }

    private void assertSecurityProcessorIsRegistered(Message msg) throws WSSecurityException {
        WSSConfig config = (WSSConfig) msg.getContextualProperty(WSSConfig.class.getName());
        Processor securityProcessor = config
                .getProcessor(new QName(SamlConstants.XML_SIGNATURE_NS, SamlConstants.SIGNATURE_TAG));

        assertTrue(securityProcessor instanceof CONNECTSignatureProcessor);
    }
}
