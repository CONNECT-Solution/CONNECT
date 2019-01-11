/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.patientdiscovery.inbound;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.logInfoServiceProcess;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditLogger;
import java.util.Properties;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 * @author akong
 *
 */
public class StandardInboundPatientDiscovery extends AbstractInboundPatientDiscovery {

    private final PatientDiscovery201305Processor patientDiscoveryProcessor;
    private final PatientDiscoveryAuditLogger auditLogger;

    /**
     * Constructor.
     */
    public StandardInboundPatientDiscovery() {
        patientDiscoveryProcessor = new PatientDiscovery201305Processor();
        auditLogger = new PatientDiscoveryAuditLogger();
    }

    /**
     * Constructor.
     *
     * @param patientDiscoveryProcessor
     * @param auditLogger
     */
    public StandardInboundPatientDiscovery(PatientDiscovery201305Processor patientDiscoveryProcessor,
        PatientDiscoveryAuditLogger auditLogger) {
        this.patientDiscoveryProcessor = patientDiscoveryProcessor;
        this.auditLogger = auditLogger;
    }

    @Override
    @InboundProcessingEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class, afterReturningBuilder = PRPAIN201306UV02EventDescriptionBuilder.class, serviceType = "Patient Discovery", version = "1.0")
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body, AssertionType assertion, Properties webContextProperties)
        throws PatientDiscoveryException {

        PRPAIN201306UV02 response = process(body, assertion, webContextProperties);

        auditResponse(body, response, assertion, webContextProperties);

        return response;
    }

    @Override
    public PRPAIN201306UV02 process(PRPAIN201305UV02 body, AssertionType assertion, Properties webContextProperties)
        throws PatientDiscoveryException {
        logInfoServiceProcess(this.getClass());

        PRPAIN201306UV02 response = patientDiscoveryProcessor.process201305(body, assertion);
        return response;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.patientdiscovery.inbound.AbstractInboundPatientDiscovery#getAuditLogger()
     */
    @Override
    public PatientDiscoveryAuditLogger getAuditLogger() {
        return auditLogger;
    }

}
