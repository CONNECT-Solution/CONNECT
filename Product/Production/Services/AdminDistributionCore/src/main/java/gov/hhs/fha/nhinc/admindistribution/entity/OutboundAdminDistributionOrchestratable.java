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
package gov.hhs.fha.nhinc.admindistribution.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;

/**
 *
 * @author nnguyen
 */
public class OutboundAdminDistributionOrchestratable implements OutboundOrchestratable {

    private NhinTargetSystemType target = null;
    private AssertionType assertion = null;
    private OutboundDelegate nhinDelegate = null;
    private RespondingGatewaySendAlertMessageType request = null;
    private boolean isPassthru = false;

    /**
     * Constructor.
     *
     * @param delegate OutboundDelegate delegate received.
     */
    public OutboundAdminDistributionOrchestratable(OutboundDelegate delegate) {
        nhinDelegate = delegate;
    }

    /**
     * Constructor.
     *
     * @param delegate OutboundDelegate delegate received.
     * @param request SendAlertMessage received.
     * @param targetSystem NhinTargetSystem targetSystem received.
     * @param assertion Assertion received.
     */
    public OutboundAdminDistributionOrchestratable(OutboundDelegate delegate,
            RespondingGatewaySendAlertMessageType request, NhinTargetSystemType targetSystem, AssertionType assertion) {
        this(delegate);
        setRequest(request);
        setAssertion(assertion);
        setTarget(targetSystem);
    }

    /**
     * @return request SendAlertMessage request.
     */
    public RespondingGatewaySendAlertMessageType getRequest() {
        return request;
    }

    /**
     * @param request SendAlertMessage request.
     */
    public void setRequest(RespondingGatewaySendAlertMessageType request) {
        this.request = request;
    }

    /**
     * @return target NhinTargetSystem target.
     */
    public NhinTargetSystemType getTarget() {
        return target;
    }

    /**
     * @param target NhinTargetSystem target received.
     */
    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    /**
     * @param assertion Assertion assertion received.
     */
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    /**
     * @return nhinDelegate.
     */
    public OutboundDelegate getNhinDelegate() {
        return nhinDelegate;
    }

    /**
     * @return assertion Assertion assertion received.
     */
    @Override
    public AssertionType getAssertion() {
        return assertion;
    }

    /**
     * @return serviceName Administrative_Distribution.
     */
    @Override
    public String getServiceName() {
        return NhincConstants.ADMIN_DIST_SERVICE_NAME;
    }

    @Override
    public OutboundDelegate getDelegate() {
        return getNhinDelegate();
    }

    /**
     * @return boolean true if AdminDist in Passthru mode.
     */
    @Override
    public boolean isPassthru() {
        return isPassthru;
    }

    public void setPassthru(boolean isPassthru) {
        this.isPassthru = isPassthru;
    }

    /**
     * @return throws RunTimeException when called.
     */
    @Override
    public PolicyTransformer getPolicyTransformer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return throws RunTimeException when called.
     */
    @Override
    public NhinAggregator getAggregator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
