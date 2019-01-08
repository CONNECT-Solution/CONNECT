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
package gov.hhs.fha.nhinc.adaptermpimanager;

import gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers.HL7Parser201301;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mflynn02
 */
public class PatientSaver {

    private static final Logger LOG = LoggerFactory.getLogger(PatientSaver.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String PROPERTY_NAME = "assigningAuthorityId";

    public static org.hl7.v3.MCCIIN000002UV01 SavePatient(final org.hl7.v3.PRPAIN201301UV02 message) {
        return PatientSaver.SaveAnnouncePatient(message, true, true, false);
    }

    private static org.hl7.v3.MCCIIN000002UV01 SaveAnnouncePatient(final PRPAIN201301UV02 message,
            final boolean AllowSearchByDemographics, final boolean UpdateDemographicsIfNeeded,
            final boolean ConfirmDemographicMatchPriorToUpdatingCorrelation) {

        LOG.info("in SaveAnnouncePatient (PRPAIN201301UV)");
        MCCIIN000002UV01 result;
        String senderOID = null;
        String receiverOID = null;
        String ackTypeCode = HL7AckTransforms.ACK_TYPE_CODE_ACCEPT;
        String msgText;
        String localDeviceId;

        // Set the senderOID in the Ack message
        if (NullChecker.isNotNullish(message.getReceiver()) && message.getReceiver().get(0).getDevice() != null
                && NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getId())
                && NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
            senderOID = message.getReceiver().get(0).getDevice().getId().get(0).getRoot();
        }

        // Set the receiverOID in the Ack Message
        if (message.getSender() != null && message.getSender().getDevice() != null
                && NullChecker.isNotNullish(message.getSender().getDevice().getId())
                && NullChecker.isNotNullish(message.getSender().getDevice().getId().get(0).getRoot())) {
            receiverOID = message.getSender().getDevice().getId().get(0).getRoot();
        }

        // Set the localDeviceId in the Ack Message
        try {
            localDeviceId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE, PROPERTY_NAME);
        } catch (final Exception e) {
            LOG.warn("Using default localDeviceId, could not read property: {}", e.getLocalizedMessage(), e);
            localDeviceId = HL7Constants.DEFAULT_LOCAL_DEVICE_ID;
        }

        HL7Parser201301.PrintMessageIdFromMessage(message);

        final PRPAMT201301UV02Patient patient = HL7Parser201301.ExtractHL7PatientFromMessage(message);
        final MCCIMT000100UV01Sender sender = message.getSender();

        if (patient == null) {
            LOG.warn(" no patient supplied");
            ackTypeCode = HL7AckTransforms.ACK_TYPE_CODE_ERROR;
            msgText = "Error: No Patient Supplied";
        } else if (sender == null) {
            LOG.warn(" no sender supplied");
            ackTypeCode = HL7AckTransforms.ACK_TYPE_CODE_ERROR;
            msgText = "Error: No Sender Supplied";
        } else {
            final Patient sourcePatient = HL7Parser201301.ExtractMpiPatientFromHL7Patient(patient);
            LOG.info("perform patient lookup in mpi");

            final Patients searchResults = MpiDataAccess.LookupPatients(sourcePatient, AllowSearchByDemographics);

            if (CommonChecks.isZeroSearchResult(searchResults)) {
                LOG.info("patient not found in MPI");
                LOG.info("creating patient");
                MpiDataAccess.SavePatient(sourcePatient);
                msgText = "Patient Successfully added to the MPI";
            } else if (CommonChecks.isMultipleSearchResult(searchResults)) {
                LOG.info("multiple patients found in MPI [searchResults.size()=" + searchResults.size() + "]");
                msgText = "Error: Multiple patients were found in the MPI";

            } else {
                LOG.info("patient found in MPI. Currently IGNORE record!");
                msgText = "Warning: Patient already found in MPI.";
            }
        }

        result = HL7AckTransforms.createAckMessage(localDeviceId, message.getId(), ackTypeCode, msgText, senderOID,
                receiverOID);

        return result;
    }
}
