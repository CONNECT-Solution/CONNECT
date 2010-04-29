/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.async.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryAsyncRespImpl {
    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02(PRPAIN201306UV02 body, WebServiceContext context) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setAsyncMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        // Audit the incoming Nhin 201306 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhin201306(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        resp = respondingGatewayPRPAIN201306UV02(body, assertion);
        
        // Audit the responding ack Message
        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02(PRPAIN201306UV02 body, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        return resp;
    }

}
