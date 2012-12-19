/**
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContext;
import gov.hhs.fha.nhinc.orchestration.OrchestrationContextBuilder;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * 
 * @author akong
 */
public abstract class OutboundDocSubmissionDeferredResponseOrchestrationContextBuilder implements
        OrchestrationContextBuilder {

	private AssertionType assertionType;
    private OutboundDelegate nhinDelegate;
    private RegistryResponseType request;
    private NhinTargetSystemType target;

    public abstract OrchestrationContext build();

    public void init(OutboundOrchestratable message) {
        setAssertionType(message.getAssertion());
        setRequest(((OutboundDocSubmissionDeferredResponseOrchestratable) message).getRequest());
        setTarget(((OutboundDocSubmissionDeferredResponseOrchestratable) message).getTarget());
        setNhinDelegate(((OutboundDocSubmissionDeferredResponseOrchestratable) message).getNhinDelegate());
    }

    public AssertionType getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(AssertionType assertionType) {
        this.assertionType = assertionType;
    }

    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    public void setNhinDelegate(OutboundDelegate nhinDelegate) {
        this.nhinDelegate = nhinDelegate;
    }

    public RegistryResponseType getRequest() {
        return request;
    }

    public void setRequest(RegistryResponseType request) {
        this.request = request;
    }

    public NhinTargetSystemType getTarget() {
        return target;
    }

    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }
}
