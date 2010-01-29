package gov.hhs.fha.nhinc.policy;

import javax.jws.WebService;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentInternalPolicyEngineFacadeService", portName = "NhincComponentInternalPolicyEngineFacadePort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentinternalpolicyenginefacade.NhincComponentInternalPolicyEngineFacadePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:NhincComponentInternalPolicyEngineFacade", wsdlLocation = "WEB-INF/wsdl/GatewayPolicyEngineFacade/NhincComponentInternalPolicyEngineFacade.wsdl")
public class GatewayPolicyEngineFacade
{

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubjectAdded(gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType checkPolicySubjectAddedRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicySubjectAdded(checkPolicySubjectAddedRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubjectRevised(gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType checkPolicySubjectRevisedRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicySubjectRevised(checkPolicySubjectRevisedRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubjectReidentification(gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType checkPolicySubjectReidentificationRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicySubjectReidentification(checkPolicySubjectReidentificationRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyAdhocQuery(gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType checkPolicyAdhocQueryRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicyAdhocQuery(checkPolicyAdhocQueryRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyDocRetrieve(gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType checkPolicyDocRetrieveRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicyDocRetrieve(checkPolicyDocRetrieveRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyFindAuditEvents(gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType checkPolicyFindAuditEventsRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicyFindAuditEvents(checkPolicyFindAuditEventsRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubscribe(gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType checkPolicySubscribeRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicySubscribe(checkPolicySubscribeRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyUnsubscribe(gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType checkPolicyUnsubscribeRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicyUnsubscribe(checkPolicyUnsubscribeRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyNotify(gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType checkPolicyNotifyRequest)
    {
        return new GatewayPolicyEngineFacadeImpl().checkPolicyNotify(checkPolicyNotifyRequest);
    }

}
