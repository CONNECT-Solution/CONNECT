/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.nhin.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredRequestImpl {
    private static Log log = LogFactory.getLog(NhinDocQueryDeferredRequestImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        String ackMsg = null;
        DocQueryAcknowledgementType respAck = new DocQueryAcknowledgementType();

        // Audit the incoming NHIN Message
        DocQueryAuditLog auditLogger = new DocQueryAuditLog();
        AcknowledgementType ack = auditLogger.auditDQRequest(msg, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        // Check if the service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {

            }
            else {
                // Send the deferred request to the Adapter Interface
                respAck = sendToAgency(msg, assertion);
            }
        }
        else {
            // Send the error to the Adapter Error Interface
            ackMsg = "Document Query Deferred Request Service Not Enabled";
            log.error(ackMsg);
            respAck = sendToAgencyError(msg, assertion, ackMsg);
        }

        return respAck;
    }

    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;

        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

    private DocQueryAcknowledgementType sendToAgencyError(AdhocQueryRequest request, AssertionType assertion, String errMsg) {
        log.debug("Sending Request to Adapter Error Interface");
        return new DocQueryAcknowledgementType();
    }

    private DocQueryAcknowledgementType sendToAgency(AdhocQueryRequest request, AssertionType assertion) {
        log.debug("Sending Request to Adapter Interface");
        return new DocQueryAcknowledgementType();
    }

}
