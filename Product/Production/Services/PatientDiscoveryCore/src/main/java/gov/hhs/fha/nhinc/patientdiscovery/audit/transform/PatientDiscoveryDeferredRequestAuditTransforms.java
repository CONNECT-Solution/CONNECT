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
package gov.hhs.fha.nhinc.patientdiscovery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.parser.PRPAIN201305UV02Parser;
import java.io.ByteArrayOutputStream;
import javax.xml.bind.JAXBException;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class PatientDiscoveryDeferredRequestAuditTransforms extends AbstractPatientDiscoveryAuditTransforms<
    PRPAIN201305UV02, MCCIIN000002UV01> {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDiscoveryDeferredRequestAuditTransforms.class);

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(PRPAIN201305UV02 request,
        AssertionType assertion, AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentificationForRequest(request, auditMsg);
        try {
            auditMsg = getQueryParamsParticipantObjectIdentificationForRequest(request, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }

        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(PRPAIN201305UV02 request,
        MCCIIN000002UV01 response, AssertionType assertion, AuditMessageType auditMsg) {

        try {
            auditMsg = getQueryParamsParticipantObjectIdentificationForResponse(request, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }

        return auditMsg;
    }

    private AuditMessageType getQueryParamsParticipantObjectIdentificationForResponse(PRPAIN201305UV02 request,
        AuditMessageType auditMsg) throws JAXBException {

        ParticipantObjectIdentificationType participantObject = buildBaseParticipantObjectIdentificationType(
            PRPAIN201305UV02Parser.getQueryId(request));
        // For PDDeferred request we are populating queryByParameter from request as we don't have queryByParameter
        // segment in Ack.
        participantObject.setParticipantObjectQuery(getParticipantObjectQueryForAck(request));
        auditMsg.getParticipantObjectIdentification().add(participantObject);
        return auditMsg;
    }

    private byte[] getParticipantObjectQueryForAck(PRPAIN201305UV02 request) throws JAXBException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (request.getControlActProcess() != null && request.getControlActProcess().getQueryByParameter() != null) {
            getMarshaller().marshal(request.getControlActProcess().getQueryByParameter(), baos);
        }
        return baos.toByteArray();
    }
}
