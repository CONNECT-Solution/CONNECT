/*
 * Copyright (c) 2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime._10.entity;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionAckResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayBatchSubmissionResponseMessageRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRealTimeResponseType;
import gov.hhs.fha.nhinc.corex12.docsubmission.realtime.outbound.OutboundCORE_X12DSRealTime;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author svalluripalli
 */
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class CORE_EntityX12DSRealTimeUnsecured implements gov.hhs.fha.nhinc.nhincentitycore.EntityCORETransactionPortType {

    private WebServiceContext context;
    private OutboundCORE_X12DSRealTime outboundCORE_X12DSRealTime;
    
    /**
     * 
     * @param body
     * @return RespondingGatewayCrossGatewayBatchSubmissionAckResponseType
     */
    @Override
    public RespondingGatewayCrossGatewayBatchSubmissionAckResponseType batchSubmitTransactionDeferredResponse(RespondingGatewayCrossGatewayBatchSubmissionResponseMessageRequestType body) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param body
     * @return RespondingGatewayCrossGatewayBatchSubmissionAckResponseType
     */
    @Override
    public RespondingGatewayCrossGatewayBatchSubmissionAckResponseType batchSubmitTransactionDeferredRequest(RespondingGatewayCrossGatewayBatchSubmissionRequestType body) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @param body
     * @return RespondingGatewayCrossGatewayRealTimeResponseType
     */
    @Override
    public RespondingGatewayCrossGatewayRealTimeResponseType realTimeTransaction(RespondingGatewayCrossGatewayRealTimeRequestType body) {
        return new CORE_EntityX12DSRealTimeImpl(outboundCORE_X12DSRealTime).realTimeTransaction(body, context);
    }
    
    /**
     * 
     * @param context 
     */
    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }
    
    /**
     * 
     * @param outboundCORE_X12DSRealTime 
     */
    public void setOutboundCORE_X12DSRealTime(OutboundCORE_X12DSRealTime outboundCORE_X12DSRealTime)
    {
        this.outboundCORE_X12DSRealTime = outboundCORE_X12DSRealTime;
    }
}
