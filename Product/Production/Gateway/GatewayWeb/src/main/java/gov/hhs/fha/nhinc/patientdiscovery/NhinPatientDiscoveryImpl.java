/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryImpl {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryImpl.class);

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, WebServiceContext context) {
        log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Audit the incoming Nhin 201305 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhin201305(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        // Check if the Patient Discovery Service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {
                PatientDiscovery201305Processor msgProcessor = new PatientDiscovery201305Processor();
                response = msgProcessor.process201305(body, assertion);
            }
            else {
                // Audit the outgoing Adapter 201305 Message
                ack = auditLogger.auditAdapter201305(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

                // Send the 201305 to the Adapter Interface
                PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();
                response = adapterSender.send201301ToAgency(body, assertion);

                // Audit the incoming Adapter 201306 Message
                ack = auditLogger.auditAdapter201306(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
            }
        }

        // Audit the responding 201306 Message
        ack = auditLogger.auditNhin201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        
        // Send response back to the initiating Gateway
        log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;
    }

     /**
     * Checks the gateway.properties file to see if the Subject Discovery is enabled.
     *
     * @return Returns true if the servicePatientDiscovery is enabled in the properties file.
     */
    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.NHINC_PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.NHINC_PATIENT_DISCOVERY_SERVICE_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return serviceEnabled;
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     * @return Returns true if the patientDiscoveryPassthrough property of the gateway.properties file is true.
     */
    private boolean isInPassThroughMode() {
        boolean passThroughModeEnabled = false;
        try {
            passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.PATIENT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.PATIENT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return passThroughModeEnabled;
    }

}
