/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.gateway.policyenginetransformation;

import javax.jws.WebService;
import gov.hhs.fha.nhinc.transform.policy.PolicyEngineTransformer;
import javax.xml.ws.BindingType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentInternalPolicyEngineTransformService", portName = "NhincInternalComponentPolicyEngineTransformPort", endpointInterface = "gov.hhs.fha.nhinc.nhincinternalcomponentpolicyenginetransform.NhincInternalComponentPolicyEngineTransformPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincinternalcomponentpolicyenginetransform", wsdlLocation = "WEB-INF/wsdl/PolicyEngineTransformationService/NhincComponentInternalPolicyEngineTransform.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PolicyEngineTransformationService
{

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectAddedToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubjectAddedToCheckPolicy(transformSubjectAddedToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubjectRevisedToCheckPolicy(transformSubjectRevisedToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest)
    {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType transformSubjectReidentificationToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubjectReidentificationToCheckPolicy(transformSubjectReidentificationToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformSubscribeToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType transformSubscribeToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformSubscribeToCheckPolicy(transformSubscribeToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformUnsubscribeToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType transformUnsubscribeToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformUnsubscribeToCheckPolicy(transformUnsubscribeToCheckPolicyRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transformNotifyToCheckPolicy(gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType transformNotifyToCheckPolicyRequest)
    {
        return new PolicyEngineTransformer().transformNotifyToCheckPolicy(transformNotifyToCheckPolicyRequest);
    }

}
