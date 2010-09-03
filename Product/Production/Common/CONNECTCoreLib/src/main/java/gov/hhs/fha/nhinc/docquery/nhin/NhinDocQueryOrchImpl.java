/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.DocQueryPolicyChecker;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.adapter.proxy.AdapterDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryOrchImpl {
    private Log log = null;

    public NhinDocQueryOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, AssertionType assertion) {
        RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
        AdhocQueryResponse resp = new AdhocQueryResponse();
        crossGatewayQueryRequest.setAdhocQueryRequest(body);
        crossGatewayQueryRequest.setAssertion(assertion);

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
    private AcknowledgementType auditAdhocQueryRequest(RespondingGatewayCrossGatewayQueryRequestType msg, String direction, String _interface)
    {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQRequest(msg.getAdhocQueryRequest(), msg.getAssertion(), direction, _interface);

        return ack;
    }

    /**
     * Creates an audit log for an AdhocQueryResponse.
     * @param crossGatewayDocQueryResponse AdhocQueryResponse message to log
     * @param direction Indicates whether the message is going out or comming in
     * @param _interface Indicates which interface component is being logged??
     * @return Returns an acknowledgement object indicating whether the audit was successfully completed.
     */
    private AcknowledgementType auditAdhocQueryResponse(AdhocQueryResponse msg, String direction, String _interface, AssertionType assertion)
    {
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQResponse(msg, assertion, direction, _interface);

        return ack;
    }

    /**
     * Checks to see if the security policy will permit the query to be executed.
     * @param message The AdhocQuery request message.
     * @return Returns true if the security policy permits the query; false if denied.
     */
    private boolean checkPolicy(RespondingGatewayCrossGatewayQueryRequestType message) {
        boolean policyIsValid = new DocQueryPolicyChecker().checkIncomingPolicy(message.getAdhocQueryRequest(), message.getAssertion());

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
        resp = adapterProxy.respondingGatewayCrossGatewayQuery(adhocQueryRequestMsg.getAdhocQueryRequest(), adhocQueryRequestMsg.getAssertion());

        return resp;
    }


    private AdhocQueryResponse queryInternalDocRegistry(RespondingGatewayCrossGatewayQueryRequestType adhocQueryRequestMsg)
    {
        AdhocQueryResponse resp = new AdhocQueryResponse();
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
