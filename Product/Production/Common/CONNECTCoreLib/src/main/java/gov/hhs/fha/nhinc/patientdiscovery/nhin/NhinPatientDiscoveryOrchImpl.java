/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAdapterSender;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author westberg
 */
public class NhinPatientDiscoveryOrchImpl
{
    private static Log log = LogFactory.getLog(NhinPatientDiscoveryOrchImpl.class);

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, AssertionType assertion)
    {
        log.debug("Entering NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");

        // Audit the incoming Nhin 201305 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhin201305(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);


        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        ack = new AcknowledgementType();

        // Check if the Patient Discovery Service is enabled
        if (isServiceEnabled())
        {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode()))
            {
                PatientDiscovery201305Processor msgProcessor = new PatientDiscovery201305Processor();
                response = msgProcessor.process201305(body, assertion);
            }
            else
            {
                // Audit the outgoing Adapter 201305 Message
                ack = auditLogger.auditAdapter201305(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

                // Send the 201305 to the Adapter Interface
                PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();
                response = adapterSender.send201305ToAgency(body, assertion);

                // Audit the incoming Adapter 201306 Message  - response that came back from the adapter.
                ack = auditLogger.auditAdapter201306(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
            }
        }

        // Audit the responding 201306 Message - Response outbound to the NHIN
        ack = auditLogger.auditNhin201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        // Send response back to the initiating Gateway
        log.debug("Exiting NhinPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02");
        return response;
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Service is enabled.
     *
     * @return Returns true if the servicePatientDiscovery is enabled in the properties file.
     */
    protected boolean isServiceEnabled()
    {
        return NhinPatientDiscoveryUtils.isServiceEnabled(NhincConstants.NHINC_PATIENT_DISCOVERY_SERVICE_NAME);
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthrough property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode()
    {
        return NhinPatientDiscoveryUtils.isInPassThroughMode(NhincConstants.PATIENT_DISCOVERY_SERVICE_PASSTHRU_PROPERTY);
    }
}
