/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoverySecuredAsyncRespImpl {

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02SecuredRequestType request, WebServiceContext context) {
        RespondingGatewayPRPAIN201306UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201306UV02RequestType();
        unsecureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        unsecureRequest.setPRPAIN201306UV02(request.getPRPAIN201306UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                unsecureRequest.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            unsecureRequest.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        // Audit the Patient Discovery Request Message sent on the Entity Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ackMsg = auditLog.auditEntity201306(unsecureRequest, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);


        MCCIIN000002UV01 ack = processPatientDiscoveryAsyncResp(unsecureRequest);

        ackMsg = auditLog.auditAck(ack, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        return ack;
    }

}
