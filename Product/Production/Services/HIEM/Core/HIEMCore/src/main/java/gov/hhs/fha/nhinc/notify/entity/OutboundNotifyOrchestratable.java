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
package gov.hhs.fha.nhinc.notify.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.consumerreference.SoapMessageElements;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

import org.oasis_open.docs.wsn.b_2.Notify;

public class OutboundNotifyOrchestratable implements OutboundOrchestratable {

    protected NhinTargetSystemType target = null;
    private AssertionType assertion = null;
    private OutboundDelegate nhinDelegate = null;
    private SoapMessageElements referenceParameters = null;
    private Notify request = null;

    /**
     * A basic constructor.
     * @param delegate the delegate to be used
     */
    public OutboundNotifyOrchestratable(OutboundDelegate delegate) {
        this.nhinDelegate = delegate;
    }

    /**
     * A constructor to populate a OutboundNotifyOrchestratable.
     * @param delegate the delegate to be used
     * @param request the request to be used
     * @param referenceParameters the reference parameters to be used
     * @param target the target to be used
     * @param assertion the assertion to be used
     */
    public OutboundNotifyOrchestratable(OutboundDelegate delegate,
    		Notify request, SoapMessageElements referenceParameters,
    		NhinTargetSystemType target, AssertionType assertion) {
        this(delegate);
        this.assertion = assertion;
        this.request = request;
        this.referenceParameters = referenceParameters;
        this.target = target;
    }

    /**
     * Getter for the delegate object.
     * @return the delegate
     */
    @Override
    public OutboundDelegate getDelegate() {
        return getNhinDelegate();
    }

    /**
     * Getter for the delegate object.
     * @return the delegate
     */
    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    /**
     * Getter for an aggregator object.
     * @return the aggregator
     */
    @Override
    public NhinAggregator getAggregator() {
        throw new UnsupportedOperationException("Notify does not support aggregation.");
    }

    /**
     * Getter for the assertion.
     * @return the assertion
     */
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * The setter for the assertion to be used.
     * @param assertion the assertion
     */
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     * Getter for the request message.
     * @return the request
     */
    public Notify getRequest() {
        return request;
    }

    /**
     * Setter for the request message.
     * @param request the request message to be used
     */
    public void setRequest(Notify request) {
        this.request = request;
    }

    /**
     * Getter for the target system.
     * @return the target system
     */
    public NhinTargetSystemType getTarget() {
        return target;
    }

    /**
     * Setter for the target system.
     * @param target the NhinTargetSystemType to be used
     */
    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    /**
     * Getter for reference parameters.
     * @return reference parameters
     */
    public SoapMessageElements getReferenceParameters() {
		return referenceParameters;
	}

    /**
     * Setter for reference parameters.
     * @param referenceParameters the ReferenceParametersElements to be used
     */
	public void setReferenceParameters(SoapMessageElements referenceParameters) {
		this.referenceParameters = referenceParameters;
	}

	/**
	 * Return this service name.
	 * @return the name
	 */
	public String getServiceName() {
        return NhincConstants.HIEM_NOTIFY_SERVICE_NAME;
    }

	/**
     * Check if pass thru mode is enabled.
     * @return a boolean true if in passthru
     */
    public boolean isPassthru() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Getter for the audit transformer.
     * @return the transformer
     */
    public AuditTransformer getAuditTransformer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Getter for the policy transformer.
     * @return the transformer
     */
    public PolicyTransformer getPolicyTransformer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
