/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.policy;

import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincComponentInternalPolicyEngineFacadeService", portName = "NhincComponentInternalPolicyEngineFacadePort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentinternalpolicyenginefacade.NhincComponentInternalPolicyEngineFacadePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:NhincComponentInternalPolicyEngineFacade", wsdlLocation = "META-INF/wsdl/GatewayPolicyEngineFacade/NhincComponentInternalPolicyEngineFacade.wsdl")
@Stateless
public class GatewayPolicyEngineFacade {

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubjectAdded(gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType checkPolicySubjectAddedRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicySubjectAdded(checkPolicySubjectAddedRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubjectRevised(gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType checkPolicySubjectRevisedRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicySubjectRevised(checkPolicySubjectRevisedRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubjectReidentification(gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType checkPolicySubjectReidentificationRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicySubjectReidentification(checkPolicySubjectReidentificationRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyAdhocQuery(gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType checkPolicyAdhocQueryRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicyAdhocQuery(checkPolicyAdhocQueryRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyDocRetrieve(gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType checkPolicyDocRetrieveRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicyDocRetrieve(checkPolicyDocRetrieveRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyFindAuditEvents(gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType checkPolicyFindAuditEventsRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicyFindAuditEvents(checkPolicyFindAuditEventsRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicySubscribe(gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType checkPolicySubscribeRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicySubscribe(checkPolicySubscribeRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyUnsubscribe(gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType checkPolicyUnsubscribeRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicyUnsubscribe(checkPolicyUnsubscribeRequest);
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType checkPolicyNotify(gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType checkPolicyNotifyRequest) {
        GatewayPolicyEngineFacadeImpl policyEngFacade = new GatewayPolicyEngineFacadeImpl();
        return policyEngFacade.checkPolicyNotify(checkPolicyNotifyRequest);
    }

}
