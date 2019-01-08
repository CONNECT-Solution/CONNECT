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

import gov.hhs.fha.nhinc.callback.SamlConstants;
import javax.xml.namespace.QName;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.dom.engine.WSSConfig;

/**
 * This interceptor will override how CXF normally handles the Signature element of the Security header by registering a
 * CONNECT security processor. That processor will inline all digest and signature values in the Signature element if
 * they are attached as a reference as otherwise CXF will erroneously consider them as invalid.
 *
 */
public class SecurityConfigInInterceptor extends AbstractPhaseInterceptor<Message> {

    /**
     * Constructor.
     */
    public SecurityConfigInInterceptor() {
        super(Phase.PRE_PROTOCOL);

        // this needs to run before the following interceptors that process security headers
        getBefore().add(WSS4JInInterceptor.class.getName());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(Message msg) throws Fault {
        WSSConfig config = (WSSConfig) msg.getContextualProperty(WSSConfig.class.getName());
        if (config == null) {
            config = WSSConfig.getNewInstance();
        }
        config.setProcessor(new QName(SamlConstants.XML_SIGNATURE_NS, SamlConstants.SIGNATURE_TAG),
            new CONNECTSignatureProcessor());
        msg.put(WSSConfig.class.getName(), config);
    }

}
