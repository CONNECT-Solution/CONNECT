/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policy;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policydteproxy.IPolicyDteProxy;
import gov.hhs.fha.nhinc.policydteproxy.PolicyDteProxyFactory;
import gov.hhs.fha.nhinc.policyengineproxy.IPolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengineproxy.PolicyEngineProxyFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class GatewayPolicyEngineFacadeImpl {

    private static Log log = LogFactory.getLog(GatewayPolicyEngineFacadeImpl.class);

    public CheckPolicyResponseType checkPolicySubjectAdded(SubjectAddedEventType checkPolicySubjectAddedRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicySubjectAdded...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformSubjectAddedToCheckPolicy(checkPolicySubjectAddedRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicySubjectAdded...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicySubjectRevised(SubjectRevisedEventType checkPolicySubjectRevisedRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicySubjectRevised...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformSubjectRevisedToCheckPolicy(checkPolicySubjectRevisedRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicySubjectRevised...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicySubjectReidentification(SubjectReidentificationEventType checkPolicySubjectReidentificationRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicySubjectReidentification...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformSubjectReidentificationToCheckPolicy(checkPolicySubjectReidentificationRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicySubjectReidentification...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicyAdhocQuery(AdhocQueryRequestEventType checkPolicyAdhocQueryRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicyAdhocQuery...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformAdhocQueryToCheckPolicy(checkPolicyAdhocQueryRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicyAdhocQuery...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicyDocRetrieve(DocRetrieveEventType checkPolicyDocRetrieveRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicyDocRetrieve...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformDocRetrieveToCheckPolicy(checkPolicyDocRetrieveRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicyDocRetrieve...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicyFindAuditEvents(FindAuditEventsEventType checkPolicyFindAuditEventsRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicyFindAuditEvents...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformFindAuditEventsToCheckPolicy(checkPolicyFindAuditEventsRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicyFindAuditEvents...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicySubscribe(SubscribeEventType checkPolicySubscribeRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicySubscribe...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformSubscribeToCheckPolicy(checkPolicySubscribeRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicySubscribe...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicyUnsubscribe(UnsubscribeEventType checkPolicyUnsubscribeRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicyUnsubscribe...");

        IPolicyDteProxy proxy = PolicyDteProxyFactory.getPolicyDteProxy();

        CheckPolicyRequestType policyReq = proxy.transformUnsubscribeToCheckPolicy(checkPolicyUnsubscribeRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicyUnsubscribe...");
        return checkPolicy(policyReq);
    }

    public CheckPolicyResponseType checkPolicyNotify(NotifyEventType checkPolicyNotifyRequest) {
        log.debug("Entering GatewayPolicyEngineFacadeImpl.checkPolicyNotify...");

        IPolicyDteProxy dteProxy = PolicyDteProxyFactory.getPolicyDteProxy();
        CheckPolicyRequestType policyReq = dteProxy.transformNotifyToCheckPolicy(checkPolicyNotifyRequest);

        log.debug("Exiting GatewayPolicyEngineFacadeImpl.checkPolicyNotify...");
        return checkPolicy(policyReq);
    }

    private CheckPolicyResponseType checkPolicy (CheckPolicyRequestType checkPolicyRequest) {
        IPolicyEngineProxy policyEngineProxy = PolicyEngineProxyFactory.getPolicyEngineProxy();
        CheckPolicyResponseType response = policyEngineProxy.checkPolicy(checkPolicyRequest);

        return response;
    }

}
