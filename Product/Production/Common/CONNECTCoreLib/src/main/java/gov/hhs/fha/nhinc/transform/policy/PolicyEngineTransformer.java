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
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDREventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseEventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDSEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

/**
 *
 * @author rayj
 */
public class PolicyEngineTransformer {

    private PatientDiscoveryPolicyTransformHelper pdPolicyTransformerHelepr
        = new PatientDiscoveryPolicyTransformHelper();

    public CheckPolicyRequestType transformPatDiscReqToCheckPolicy(
        PatDiscReqEventType transformPatDiscReqToCheckPolicyRequest) {
        return pdPolicyTransformerHelepr
            .transformPatientDiscoveryNhincToCheckPolicy(transformPatDiscReqToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformAdhocQueryToCheckPolicy(
        AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest) {
        return AdhocQueryTransformHelper.transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(
        AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest) {
        return AdhocQueryTransformHelper
            .transformAdhocQueryResponseToCheckPolicy(transformAdhocQueryResultToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformDocRetrieveToCheckPolicy(
        DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest) {
        return DocRetrieveTransformHelper.transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(
        DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(
        FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest) {
        return FindAuditEventsTransformHelper
            .transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformXDRRequestToCheckPolicy(XDREventType request) {
        return new XDRPolicyTransformHelper().transformXDRToCheckPolicy(request);
    }

    public CheckPolicyRequestType transformXDRResponseInputToCheckPolicy(XDRResponseEventType request) {
        return new XDRPolicyTransformHelper().transformXDRResponseToCheckPolicy(request);
    }

    public CheckPolicyRequestType transformXDSRequestToCheckPolicy(XDSEventType request) {
        return new XDSPolicyTransformHelper().transformXDSToCheckPolicy(request);
    }

}
