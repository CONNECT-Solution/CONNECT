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
package gov.hhs.fha.nhinc.corex12.ds.genericbatch.request.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;

/**
 *
 * @author svalluripalli
 */
public abstract class OutboundX12BatchRequestOrchestrationContextBuilder implements OrchestrationContextBuilder {

    private AssertionType assertionType;
    private OutboundDelegate nhinDelegate;
    private NhinTargetSystemType target;
    private COREEnvelopeBatchSubmission request;

    @Override
    public abstract OrchestrationContext build();

    /**
     *
     * @param message
     */
    public void init(OutboundOrchestratable message) {
        setAssertionType(message.getAssertion());
        setRequest(((OutboundX12BatchRequestOrchestratable) message).getRequest());
        setTarget(((OutboundX12BatchRequestOrchestratable) message).getTarget());
        setNhinDelegate(((OutboundX12BatchRequestOrchestratable) message).getNhinDelegate());
    }

    /**
     *
     * @return AssertionType
     */
    public AssertionType getAssertionType() {
        return assertionType;
    }

    /**
     *
     * @param assertionType
     */
    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    /**
     *
     * @return OutboundDelegate
     */
    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    /**
     *
     * @param nhinDelegate
     */
    public void setNhinDelegate(OutboundDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
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
}
