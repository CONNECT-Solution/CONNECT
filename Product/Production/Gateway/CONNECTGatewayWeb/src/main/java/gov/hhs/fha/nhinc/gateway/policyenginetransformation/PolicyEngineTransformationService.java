/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.gateway.policyenginetransformation;

import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.transform.policy.PolicyEngineTransformer;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

/**
 *
 * @author Neil Webb
 */
@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
public class PolicyEngineTransformationService implements gov.hhs.fha.nhinc.nhincinternalcomponentpolicyenginetransform.NhincInternalComponentPolicyEngineTransformPortType {

    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryToCheckPolicy(
        gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest) {
        return new PolicyEngineTransformer().transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(
        gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest
    ) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(
        gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveToCheckPolicy(
        gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest) {
        return new PolicyEngineTransformer()
            .transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);
    }

    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(
        gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(
        gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest) {
        return new PolicyEngineTransformer()
            .transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformNotifyToCheckPolicy(
        gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType transformNotifyToCheckPolicyRequest) {
        return new PolicyEngineTransformer().transformNotifyToCheckPolicy(transformNotifyToCheckPolicyRequest);
    }

    public CheckPolicyRequestType transformSubjectAddedToCheckPolicy(
        SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest) {
        throw new UnsupportedOperationException("Not implemented.");
    }

}
