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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveOrchestratable implements OutboundOrchestratable {
    protected RetrieveDocumentSetRequestType request = null;
    protected RetrieveDocumentSetResponseType response = null;
    protected NhinTargetSystemType target = null;
    private AssertionType _assertion = null;
    private final String serviceName = NhincConstants.DOC_RETRIEVE_SERVICE_NAME;
    private PolicyTransformer _policyTransformer = null;
    private AuditTransformer _auditTransformer = null;
    private OutboundDelegate _nhinDelegate = null;
    private NhinAggregator _nhinAggregator = null;

    public NhinTargetSystemType getTarget() {
        return target;
    }

    public void setTarget(NhinTargetSystemType target) {
        this.target = target;
    }

    public void setAssertion(AssertionType _assertion) {
        this._assertion = _assertion;
    }

    public OutboundDocRetrieveOrchestratable() {

    }

    public OutboundDocRetrieveOrchestratable(PolicyTransformer pt, AuditTransformer at, OutboundDelegate ad,
            NhinAggregator na) {
        _policyTransformer = pt;
        _auditTransformer = at;
        _nhinDelegate = ad;
        _nhinAggregator = na;
    }

    public RetrieveDocumentSetRequestType getRequest() {
        return request;
    }

    public void setRequest(RetrieveDocumentSetRequestType request) {
        this.request = request;
    }

    public OutboundDelegate getNhinDelegate() {
        return _nhinDelegate;
    }

    public boolean isEnabled() {
        boolean result = false;
        try {
            result = PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_KEY);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(OutboundDocRetrieveOrchestratable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean isPassthru() {
        boolean result = false;
        try {
            result = PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.NHINC_DOCUMENT_RETRIEVE_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(OutboundDocRetrieveOrchestratable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public AuditTransformer getAuditTransformer() {
        return _auditTransformer;
    }

    public PolicyTransformer getPolicyTransformer() {
        return _policyTransformer;
    }

    public AssertionType getAssertion() {
        return _assertion;
    }

    public String getServiceName() {
        return serviceName;
    }

    public NhinAggregator getAggregator() {
        return _nhinAggregator;
    }

    @Override
    public OutboundDelegate getDelegate() {
        return getNhinDelegate();
    }
    
    public RetrieveDocumentSetResponseType getResponse() {
        return response;
    }

    public void setResponse(RetrieveDocumentSetResponseType response) {
        this.response = response;
    }

}
