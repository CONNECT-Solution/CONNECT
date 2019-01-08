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
package gov.hhs.fha.nhinc.corex12.ds.realtime._10.nhin;

import gov.hhs.fha.nhinc.corex12.ds.realtime.inbound.InboundX12RealTime;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;
import org.caqh.soap.wsdl.CORETransactions;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 *
 * @author svalluripalli, cmay
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class NhinX12RealTime implements CORETransactions {

    private WebServiceContext context;
    private InboundX12RealTime inboundCORE_X12DSRealTime;

    /**
     * TODO: Unused?
     *
     * @return InboundX12RealTime
     */
    public InboundX12RealTime getInboundCORE_X12DSRealTime() {
        return inboundCORE_X12DSRealTime;
    }

    /**
     *
     * @param inboundCORE_X12DSRealTime
     */
    public void setInboundCORE_X12DSRealTime(InboundX12RealTime inboundCORE_X12DSRealTime) {
        this.inboundCORE_X12DSRealTime = inboundCORE_X12DSRealTime;
    }

    /**
     *
     * @param context
     */
    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    @Override
    public COREEnvelopeRealTimeResponse realTimeTransaction(COREEnvelopeRealTimeRequest body) {
        return new NhinX12RealTimeImpl(inboundCORE_X12DSRealTime).realTimeTransaction(body, context);
    }
}
