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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.common.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;

/**
 *
 * @author cmay, svalluripalli
 */
public abstract class OutboundX12BatchOrchestratable implements OutboundOrchestratable {

    protected NhinTargetSystemType target = null;
    private AssertionType assertion = null;
    private OutboundDelegate nhinDelegate = null;
    private COREEnvelopeBatchSubmission request = null;
    private COREEnvelopeBatchSubmissionResponse response = null;

    /**
     *
     * @param delegate
     */
    public OutboundX12BatchOrchestratable(OutboundDelegate delegate) {
        this.nhinDelegate = delegate;
    }

    /**
     *
     * @param delegate
     * @param request
     * @param target
     * @param assertion
     */
    public OutboundX12BatchOrchestratable(OutboundDelegate delegate,
        COREEnvelopeBatchSubmission request, NhinTargetSystemType target, AssertionType assertion) {

        this(delegate);
        this.assertion = assertion;
        this.request = request;
        this.target = target;
    }

    @Override
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     *
     * @param assertion
     */
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     *
     * @return NhinTargetSystemType
     */
    public NhinTargetSystemType getTarget() {
        return target;
    }

    /**
     *
     * @param target
     */
    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    /**
     *
     * @return COREEnvelopeBatchSubmission
     */
    public COREEnvelopeBatchSubmission getRequest() {
        return request;
    }

    /**
     *
     * @param request
     */
    public void setRequest(COREEnvelopeBatchSubmission request) {
        this.request = request;
    }

    /**
     *
     * @return COREEnvelopeBatchSubmissionResponse
     */
    public COREEnvelopeBatchSubmissionResponse getResponse() {
        return response;
    }

    /**
     *
     * @param response
     */
    public void setResponse(COREEnvelopeBatchSubmissionResponse response) {
        this.response = response;
    }

    @Override
    public OutboundDelegate getDelegate() {
        return getNhinDelegate();
    }

    /**
     * TODO: Do we need both getDelegate and getNhinDelegate?
     *
     * @return OutboundDelegate
     */
    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    @Override
    public boolean isPassthru() {
        return true;
    }

    @Override
    public NhinAggregator getAggregator() {
        // TODO: Implement (or customize) the exception
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PolicyTransformer getPolicyTransformer() {
        // TODO: Implement (or customize) the exception
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
