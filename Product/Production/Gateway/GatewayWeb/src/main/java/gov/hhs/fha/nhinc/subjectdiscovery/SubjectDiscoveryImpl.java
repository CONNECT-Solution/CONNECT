/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscoveryImpl {

    private static Log log = LogFactory.getLog(SubjectDiscoveryImpl.class);

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PRPAIN201301UV02 message, WebServiceContext context) {
        log.debug("Entering SubjectDiscoveryImpl.pixConsumerPRPAIN201301UV");

        MCCIIN000002UV01 ackMsg = new MCCIIN000002UV01();
        PIXConsumerPRPAIN201301UVRequestType pix201301Request = new PIXConsumerPRPAIN201301UVRequestType();
        SubjectDiscoveryAckCreater ackCreater = new SubjectDiscoveryAckCreater();
        String errMsg = null;

        pix201301Request.setPRPAIN201301UV02(message);
        pix201301Request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Audit the incoming 201301 message
        SubjectDiscoveryAuditLogger auditLogger = new SubjectDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.audit201301(pix201301Request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        // Check if the Subject Discovery Service is in passthrough mode
        if (!(isInPassThroughMode())) {
            // Check if the Subject Discovery Service is enabled
            if (isServiceEnabled()) {
                // Service is enabled so process the subject discovery request
                SubjectDiscovery201301Processor processor = new SubjectDiscovery201301Processor();
                ackMsg = processor.process201301(pix201301Request);
            } else {
                log.error(NhincConstants.SUBJECT_DISCOVERY_DISABLED_ACK_MSG);
                ackMsg = ackCreater.createAck(pix201301Request, NhincConstants.SUBJECT_DISCOVERY_DISABLED_ACK_MSG);
            }
        } else {
            // Just forward the request to the Agency
            SubjectDiscoveryAdapterSender sdAdapterSender = new SubjectDiscoveryAdapterSender();
            ackMsg = sdAdapterSender.send201301ToAgency(pix201301Request);
        }

        // Audit the outgoing ack message
        ack = auditLogger.auditNhinAck(ackMsg, pix201301Request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        log.debug("Exiting SubjectDiscoveryImpl.pixConsumerPRPAIN201301UV");
        return ackMsg;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PRPAIN201302UV02 message, WebServiceContext context) {
        log.debug("Entering SubjectDiscoveryImpl.pixConsumerPRPAIN201302UV");

        MCCIIN000002UV01 ackMsg = new MCCIIN000002UV01();
        PIXConsumerPRPAIN201302UVRequestType pix201302Request = new PIXConsumerPRPAIN201302UVRequestType();
        SubjectDiscoveryAckCreater ackCreater = new SubjectDiscoveryAckCreater();
        String errMsg = null;

        pix201302Request.setPRPAIN201302UV02(message);
        pix201302Request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Audit the incoming 201302 message
        SubjectDiscoveryAuditLogger auditLogger = new SubjectDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.audit201302(pix201302Request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        // Check if the Subject Discovery Service is in passthrough mode
        if (!(isInPassThroughMode())) {
            // Check if the Subject Discovery Service is enabled
            if (isServiceEnabled()) {
                // Check to make sure the policy is valid - Need to add a call to the MPI here first
                SubjectDiscoveryPolicyChecker policyChecker = new SubjectDiscoveryPolicyChecker();
                if (policyChecker.check201302Policy(pix201302Request)) {
                    // Policy is valid continue with process the subject discovery request
                    SubjectDiscovery201302Processor processor = new SubjectDiscovery201302Processor();
                    ackMsg = processor.process201302(pix201302Request);
                } else {
                    errMsg = "Policy Check failed for the 201302 request";
                    log.error(errMsg);
                    ackMsg = ackCreater.createAck(pix201302Request, errMsg);
                }
            } else {
                log.error(NhincConstants.SUBJECT_DISCOVERY_DISABLED_ACK_MSG);
                ackMsg = ackCreater.createAck(pix201302Request, NhincConstants.SUBJECT_DISCOVERY_DISABLED_ACK_MSG);
            }
        } else {
            // Just forward the request to the Agency
            SubjectDiscoveryAdapterSender sdAdapterSender = new SubjectDiscoveryAdapterSender();
            ackMsg = sdAdapterSender.send201302ToAgency(pix201302Request);
        }

        // Audit the outgoing ack message
        ack = auditLogger.auditNhinAck(ackMsg, pix201302Request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        log.debug("Exiting SubjectDiscoveryImpl.pixConsumerPRPAIN201302UV");
        return ackMsg;
    }       

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PRPAIN201304UV02 message, WebServiceContext context) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public PRPAIN201310UV02 pixConsumerPRPAIN201309UV(PRPAIN201309UV02 message, WebServiceContext context) {
        log.debug("Entering SubjectDiscoveryImpl.pixConsumerPRPAIN201309UV");

        PRPAIN201310UV02 ret310 = new PRPAIN201310UV02();
        PIXConsumerPRPAIN201309UVRequestType pix201309Request = new PIXConsumerPRPAIN201309UVRequestType();
        SubjectDiscoveryAckCreater ackCreater = new SubjectDiscoveryAckCreater();
        String errMsg = null;

        pix201309Request.setPRPAIN201309UV02(message);
        pix201309Request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Audit the incoming 201309 message
        SubjectDiscoveryAuditLogger auditLogger = new SubjectDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.audit201309(pix201309Request, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        // Check if the Subject Discovery Service is in passthrough mode
        if (!(isInPassThroughMode())) {
            // Check if the Subject Discovery Service is enabled
            if (isServiceEnabled()) {
                // Check to make sure the policy is valid - Need to add a call to the MPI here first
                SubjectDiscoveryPolicyChecker policyChecker = new SubjectDiscoveryPolicyChecker();
                if (policyChecker.check201309Policy(pix201309Request)) {
                    // Policy is valid continue with process the subject discovery request
                    SubjectDiscovery201309Processor processor = new SubjectDiscovery201309Processor();
                    ret310 = processor.process201309(pix201309Request);
                } else {
                    errMsg = "Policy Check failed for the 201309 request";
                    log.error(errMsg);
                    ret310 = ackCreater.createFault201310(pix201309Request, errMsg);
                }
            } else {
                log.error(NhincConstants.SUBJECT_DISCOVERY_DISABLED_ACK_MSG);
                ret310 = ackCreater.createFault201310(pix201309Request, NhincConstants.SUBJECT_DISCOVERY_DISABLED_ACK_MSG);
            }
        } else {
            // Just forward the request to the Agency
            SubjectDiscoveryAdapterSender sdAdapterSender = new SubjectDiscoveryAdapterSender();
            ret310 = sdAdapterSender.send201309ToAgency(pix201309Request);
        }

        // Audit the outgoing 201310 message
        ack = auditLogger.audit201310(ret310, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, pix201309Request.getAssertion());

        log.debug("Exiting SubjectDiscoveryImpl.pixConsumerPRPAIN201309UV");
        return ret310;
    }

    /**
     * Checks the gateway.properties file to see if the Subject Discovery is enabled.
     *
     * @return Returns true if the SUBJECT_DISCOVERY_SERVICE_NAME is enabled in the properties file.
     */
    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_SUBJECT_DISCOVERY_SERVICE_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_SUBJECT_DISCOVERY_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     * @return Returns true if the documentQueryPassthrough property of the gateway.properties file is true.
     */
    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.SUBJECT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.SUBJECT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }
}
