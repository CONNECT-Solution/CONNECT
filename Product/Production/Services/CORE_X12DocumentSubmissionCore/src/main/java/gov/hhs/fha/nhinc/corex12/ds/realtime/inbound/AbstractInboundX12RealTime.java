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
package gov.hhs.fha.nhinc.corex12.ds.realtime.inbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.ds.audit.X12RealTimeAuditLogger;
import gov.hhs.fha.nhinc.corex12.ds.realtime.adapter.proxy.AdapterX12RealTimeProxy;
import gov.hhs.fha.nhinc.corex12.ds.realtime.adapter.proxy.AdapterX12RealTimeProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Properties;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 *
 * @author cmay
 */
public abstract class AbstractInboundX12RealTime implements InboundX12RealTime {

    private AdapterX12RealTimeProxyObjectFactory adapterFactory = null;
    private X12RealTimeAuditLogger auditLogger = null;

    /**
     *
     * @param adapterFactory
     * @param auditLogger
     */
    public AbstractInboundX12RealTime(AdapterX12RealTimeProxyObjectFactory adapterFactory,
        X12RealTimeAuditLogger auditLogger) {

        this.adapterFactory = adapterFactory;
        this.auditLogger = auditLogger;
    }

    /**
     *
     * @param body
     * @param assertion
     * @return COREEnvelopeRealTimeResponse
     */
    abstract COREEnvelopeRealTimeResponse processCORE_X12DocSubmission(COREEnvelopeRealTimeRequest body,
        AssertionType assertion);

    /**
     *
     * @param msg
     * @param assertion
     * @param webContextProperties
     * @return
     */
    @Override
    public COREEnvelopeRealTimeResponse realTimeTransaction(COREEnvelopeRealTimeRequest msg, AssertionType assertion,
        Properties webContextProperties) {

        COREEnvelopeRealTimeResponse response = processCORE_X12DocSubmission(msg, assertion);
        auditResponseToNhin(msg, response, assertion, webContextProperties);
        return response;
    }

    /**
     *
     * @param request
     * @param assertion
     * @return COREEnvelopeRealTimeResponse
     */
    protected COREEnvelopeRealTimeResponse sendToAdapter(COREEnvelopeRealTimeRequest request,
        AssertionType assertion) {

        AdapterX12RealTimeProxy proxy = adapterFactory.getAdapterCOREX12DocSubmissionProxy();
        return proxy.realTimeTransaction(request, assertion);
    }

    protected void auditResponseToNhin(COREEnvelopeRealTimeRequest request, COREEnvelopeRealTimeResponse response,
        AssertionType assertion, Properties webContextProperties) {

        getAuditLogger().auditResponseMessage(request, response, assertion, null,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.FALSE,
            webContextProperties, NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME);
    }

    protected X12RealTimeAuditLogger getAuditLogger() {
        if (auditLogger == null) {
            auditLogger = new X12RealTimeAuditLogger();
        }
        return auditLogger;
    }
}
