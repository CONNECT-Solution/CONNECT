/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author vvickers
 */
class DocQueryImpl {
    
    private static Log log = LogFactory.getLog(DocQueryImpl.class);

    AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, WebServiceContext context) {
        log.debug("Entering DocQueryImpl.respondingGatewayCrossGatewayQuery");
        AdhocQueryResponse resp = new AdhocQueryResponse();
        RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
        crossGatewayQueryRequest.setAdhocQueryRequest(body);
        crossGatewayQueryRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Audit the incomming query
        AcknowledgementType ack = auditAdhocQueryRequest(crossGatewayQueryRequest, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        //AssignProcessFlag: 'true' = $GetPropertyOut.GetPropertyResponse/propacc:propertyValue
        // Check if the AdhocQuery Service is enabled
        if (isServiceEnabled())
        {
            // Check to see if in adapter pass through mode for this service
            if (isInPassThroughMode())
            {
                log.info("Passthrough mode is enabled, sending message to the Adapter");
                resp = forwardToAgency(crossGatewayQueryRequest);
            } 
            else
            {
                resp = queryInternalDocRegistry(crossGatewayQueryRequest);
            }
        }
        else
        {
            log.warn("Document Query Service is disabled");

            //AssignEmptyResponse
            resp.setTotalResultCount(NhincConstants.NHINC_ADHOC_QUERY_NO_RESULT_COUNT);
            resp.setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
        }
        
        //create an audit record for the response
        ack = auditAdhocQueryResponse(resp, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, crossGatewayQueryRequest.getAssertion());

        log.debug("Exiting DocQueryImpl.respondingGatewayCrossGatewayQuery");
        return resp;
    }

    /**
     * Creates an audit log for an AdhocQueryRequest.
     * @param crossGatewayDocQueryRequest AdhocQueryRequest message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    private AcknowledgementType auditAdhocQueryRequest(RespondingGatewayCrossGatewayQueryRequestType crossGatewayDocQueryRequest, String direction, String _interface)
    {
        AcknowledgementType ack = new AcknowledgementType();

        // Need to resolve the namespace issues here because this type is defined in multiple schemas
        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryMessageType();
        message.setAssertion(crossGatewayDocQueryRequest.getAssertion());
        message.setAdhocQueryRequest(crossGatewayDocQueryRequest.getAdhocQueryRequest());
        
        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQuery(message, direction, _interface);

        if (auditLogMsg != null) {
           ack = audit(auditLogMsg, crossGatewayDocQueryRequest.getAssertion());
        }

        return ack;
    }

    /**
     * Creates an audit log for an AdhocQueryResponse.
     * @param crossGatewayDocQueryResponse AdhocQueryResponse message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    private AcknowledgementType auditAdhocQueryResponse(AdhocQueryResponse crossGatewayDocQueryResponse, String direction, String _interface, AssertionType assertion)
    {
        AcknowledgementType ack = new AcknowledgementType();

        // Need to resolve the namespace issues here because this type is defined in multiple schemas
        gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType message = new gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType();
        // message.setAssertion(crossGatewayDocQueryResponse.getAssertion());
        message.setAdhocQueryResponse(crossGatewayDocQueryResponse);
        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logAdhocQueryResult(message, direction, _interface);

        if (auditLogMsg != null) {
           ack = audit(auditLogMsg, assertion);
        }
       
        return ack;
    }

    /**
     * Creates an audit log for an AdhocQueryRequest or AdhocQueryResponse
     * @param auditLogMsg AdhocQueryRequest or AdhocQueryResponse message to log.
     * @return Returns an AcknowledgementType object indicating whether the audit message was successfully stored.
     */
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion)
    {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

    /**
     * Checks to see if the security policy will permit the query to be executed.
     * @param message The AdhocQuery request message.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    private boolean checkPolicy(RespondingGatewayCrossGatewayQueryRequestType message) {
        boolean policyIsValid = false;

        //convert the request message to an object recognized by the policy engine
        AdhocQueryRequestEventType policyCheckReq = new AdhocQueryRequestEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestMessageType();
        request.setAssertion(message.getAssertion());
        request.setAdhocQueryRequest(message.getAdhocQueryRequest());
        policyCheckReq.setMessage(request);

        //call the policy engine to check the permission on the request
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyAdhocQuery(policyCheckReq);
        policyReq.setAssertion(message.getAssertion());
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);

        //check the policy engine's response, return true if response = permit
        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT)
        {
            policyIsValid = true;
        }

        return policyIsValid;
    }

    /**
     * Checks the gateway.properties file to see if the DOCUMENT_QUERY_SERVICE is enabled.
     *
     * Replaces the BPEL logic:
     *            AssignServiceDocQueryPropInput
     *            InvokeDocQueryEnabledProp
     * @return Returns true if the DOCUMENT_QUERY_SERVICE is enabled in the properties file.
     */
    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;
        try
        {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_SERVICE_NAME);
        }
        catch (PropertyAccessException ex)
        {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     * @return Returns true if the documentQueryPassthrough property of the gateway.properties file is true.
     */
    private boolean isInPassThroughMode()
    {
        boolean passThroughModeEnabled = false;
        try
        {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_SERVICE_PASSTHRU_PROPERTY);
        }
        catch (PropertyAccessException ex)
        {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

    /**
     * Forwards the AdhocQueryRequest to an agency's adapter doc query service
     * @param adhocQueryRequestMsg
     * @return
     */
    private AdhocQueryResponse forwardToAgency(RespondingGatewayCrossGatewayQueryRequestType adhocQueryRequestMsg)
    {
        AdhocQueryResponse resp = new AdhocQueryResponse();

        // Audit the Audit Log Query Request Message received on the Nhin Interface
        AcknowledgementType ack = auditAdhocQueryRequest(adhocQueryRequestMsg, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryEventsRequest = new gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType();
        crossGatewayQueryEventsRequest.setAssertion(adhocQueryRequestMsg.getAssertion());
        crossGatewayQueryEventsRequest.setAdhocQueryRequest(adhocQueryRequestMsg.getAdhocQueryRequest());

        AdapterDocQueryProxyObjectFactory adapterFactory = new AdapterDocQueryProxyObjectFactory();
        AdapterDocQueryProxy adapterProxy = adapterFactory.getAdapterDocQueryProxy();
        resp = adapterProxy.respondingGatewayCrossGatewayQuery(crossGatewayQueryEventsRequest);

        return resp;
    }


    private AdhocQueryResponse queryInternalDocRegistry(RespondingGatewayCrossGatewayQueryRequestType adhocQueryRequestMsg)
    {
        AdhocQueryResponse resp = new AdhocQueryResponse();
//      FindCommunitiesAndAuditEventsResponseType result = new FindCommunitiesAndAuditEventsResponseType();
        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAssertion(adhocQueryRequestMsg.getAssertion());
        request.setAdhocQueryRequest(adhocQueryRequestMsg.getAdhocQueryRequest());
//      Check the policy engine to make sure processing can proceed
        if (checkPolicy(adhocQueryRequestMsg))
        {
            resp = forwardToAgency(adhocQueryRequestMsg);
        }
        return resp;
    }
}
