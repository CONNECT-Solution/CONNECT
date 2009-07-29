/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquery.proxy;

import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhindocquery.proxy.NhinDocQueryProxy;
import gov.hhs.fha.nhinc.nhindocquery.proxy.NhinDocQueryProxyObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyDocQueryImpl {
    private static Log log = LogFactory.getLog(NhincProxyDocQueryImpl.class);

    /**
     * This method will perform an audit log query to a specified community on the Nhin Interface
     * and return a list of audit log records will be returned to the user.
     *
     * @param request The document query search criteria
     * @return A list of Documents that match the specified criteria
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request) {
        log.debug("Entering NhincProxyDocQueryImpl.respondingGatewayCrossGatewayQuery...");
        AdhocQueryResponse response = new AdhocQueryResponse();

        // Audit the Document Query Request Message sent on the Nhin Interface
        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        AcknowledgementType ack = auditLog.audit(request);

        NhinDocQueryProxyObjectFactory docQueryFactory = new NhinDocQueryProxyObjectFactory();
        NhinDocQueryProxy proxy = docQueryFactory.getNhinDocQueryProxy();

        response = proxy.respondingGatewayCrossGatewayQuery(request);

        // Audit the Document Query Response Message received on the Nhin Interface
        AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
        auditMsg.setAdhocQueryResponse(response);
        auditMsg.setAssertion(request.getAssertion());
        ack = auditLog.auditResponse(auditMsg, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        log.debug("Exiting NhincProxyDocQueryImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }
}
