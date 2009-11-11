/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.gateway.policyenginetransformation;

import gov.hhs.fha.nhinc.transform.policy.PolicyEngineTransformer;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincComponentInternalPolicyEngineTransformService", portName = "NhincInternalComponentPolicyEngineTransformPort", endpointInterface = "gov.hhs.fha.nhinc.nhincinternalcomponentpolicyenginetransform.NhincInternalComponentPolicyEngineTransformPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincinternalcomponentpolicyenginetransform", wsdlLocation = "META-INF/wsdl/PolicyEngineTransformationService/NhincComponentInternalPolicyEngineTransform.wsdl")
@Stateless
public class PolicyEngineTransformationService {

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectAddedToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectAddedToCheckPolicy(transformSubjectAddedToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectRevisedToCheckPolicy(transformSubjectRevisedToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType transformSubjectReidentificationToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubjectReidentificationToCheckPolicy(transformSubjectReidentificationToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubscribeToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType transformSubscribeToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformSubscribeToCheckPolicy(transformSubscribeToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformUnsubscribeToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType transformUnsubscribeToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformUnsubscribeToCheckPolicy(transformUnsubscribeToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformNotifyToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType transformNotifyToCheckPolicyRequest) {
        PolicyEngineTransformer policyTransformer = new PolicyEngineTransformer();
        return policyTransformer.transformNotifyToCheckPolicy(transformNotifyToCheckPolicyRequest);
    }

}
