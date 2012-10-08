/*
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
package gov.hhs.fha.nhinc.unsubscribe.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;

public class OutboundUnsubscribeOrchestratable implements OutboundOrchestratable {

    protected NhinTargetSystemType target = null;
    private AssertionType assertion = null;
    private OutboundDelegate nhinDelegate = null;
    private Unsubscribe request = null;
    private SoapMessageElements referenceParameters = null;
    private UnsubscribeResponse response = null;
    private String subscriptionId;

    public OutboundUnsubscribeOrchestratable(OutboundDelegate delegate) {
        this.nhinDelegate = delegate;
    }

    public OutboundUnsubscribeOrchestratable(OutboundDelegate delegate,
    		Unsubscribe request, SoapMessageElements referenceParameters, NhinTargetSystemType target, AssertionType assertion,
    		String subscriptionId) {
        this(delegate);
        this.assertion = assertion;
        this.request = request;
        this.referenceParameters = referenceParameters;
        this.target = target;
        this.subscriptionId = subscriptionId;
    }

    @Override
    public OutboundDelegate getDelegate() {
        return getNhinDelegate();
    }

    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    @Override
    public NhinAggregator getAggregator() {
        throw new UnsupportedOperationException("Document Submission does not support aggregation.");
    }

    public AssertionType getAssertion() {
        return assertion;
    }

    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    public Unsubscribe getRequest() {
        return request;
    }
    
    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setRequest(Unsubscribe request) {
        this.request = request;
    }

    public SoapMessageElements getReferenceParameters() {
		return referenceParameters;
	}

	public void setReferenceParameters(SoapMessageElements referenceParameters) {
		this.referenceParameters = referenceParameters;
	}

	public UnsubscribeResponse getResponse() {
        return response;
    }

    public void setResponse(UnsubscribeResponse response) {
        this.response = response;
    }

    public NhinTargetSystemType getTarget() {
        return target;
    }

    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }
    
    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getServiceName() {
        return NhincConstants.NHINC_XDR_SERVICE_NAME;
    }

    public boolean isPassthru() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AuditTransformer getAuditTransformer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PolicyTransformer getPolicyTransformer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
