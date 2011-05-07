/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adaptermpimanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;
import gov.hhs.fha.nhinc.mpilib.*;
import gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers.*;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;

/**
 *
 * @author mflynn02
 */
public class PatientSaver {

    private static Log log = LogFactory.getLog(PatientSaver.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String PROPERTY_NAME = "assigningAuthorityId";


    public static org.hl7.v3.MCCIIN000002UV01 SavePatient(org.hl7.v3.PRPAIN201301UV02 message) {
        MCCIIN000002UV01 result = new MCCIIN000002UV01();
        result = PatientSaver.SaveAnnouncePatient(message, true, true, true, false);

        return result;
    }

    private static org.hl7.v3.MCCIIN000002UV01 SaveAnnouncePatient(PRPAIN201301UV02 message, boolean AllowSearchByDemographics, boolean CreatePatientIfDoesNotExist, boolean UpdateDemographicsIfNeeded, boolean ConfirmDemographicMatchPriorToUpdatingCorrelation) {
        log.info("in SaveAnnouncePatient (PRPAIN201301UV)");
        MCCIIN000002UV01 result = new MCCIIN000002UV01();
        String senderOID = null;
        String receiverOID = null;
        String ackTypeCode = HL7AckTransforms.ACK_TYPE_CODE_ACCEPT;
        String msgText = null;
        String localDeviceId = HL7Constants.DEFAULT_LOCAL_DEVICE_ID;

        // Set the senderOID in the Ack message
        if (NullChecker.isNotNullish(message.getReceiver()) &&
                message.getReceiver().get(0).getDevice() != null &&
                NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getId()) &&
                NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
            senderOID = message.getReceiver().get(0).getDevice().getId().get(0).getRoot();
        }

        // Set the receiverOID in the Ack Message
        if (message.getSender() != null &&
                message.getSender().getDevice() != null &&
                NullChecker.isNotNullish(message.getSender().getDevice().getId()) &&
                NullChecker.isNotNullish(message.getSender().getDevice().getId().get(0).getRoot())) {
            receiverOID = message.getSender().getDevice().getId().get(0).getRoot();
        }

        // Set the localDeviceId in the Ack Message
        try {
           localDeviceId = PropertyAccessor.getProperty(PROPERTY_FILE, PROPERTY_NAME);
        }
        catch (Exception e) {
            localDeviceId = HL7Constants.DEFAULT_LOCAL_DEVICE_ID;
        }

        HL7Parser201301.PrintMessageIdFromMessage(message);

        PRPAMT201301UV02Patient patient = HL7Parser201301.ExtractHL7PatientFromMessage(message);
        MCCIMT000100UV01Sender sender = message.getSender();

        if (patient == null) {
            log.warn(" no patient supplied");
            ackTypeCode = HL7AckTransforms.ACK_TYPE_CODE_ERROR;
            msgText = "Error: No Patient Supplied";
        } else if (sender == null) {
            log.warn(" no sender supplied");
            ackTypeCode = HL7AckTransforms.ACK_TYPE_CODE_ERROR;
            msgText = "Error: No Sender Supplied";
        } else {
            Patient sourcePatient = HL7Parser201301.ExtractMpiPatientFromHL7Patient(patient);
            log.info("perform patient lookup in mpi");

            log.info("source patient check 1 [" + sourcePatient.getName().getLastName() + "]");
            Patients searchResults = MpiDataAccess.LookupPatients(sourcePatient, AllowSearchByDemographics);
            log.info("source patient check 2 [" + sourcePatient.getName().getLastName() + "]");

            if (CommonChecks.isZeroSearchResult(searchResults)) {
                log.info("patient not found in MPI");
                if (CreatePatientIfDoesNotExist) {
                    log.info("creating patient");
                    MpiDataAccess.SavePatient(sourcePatient);
                    msgText = "Patient Successfully added to the MPI";
                } else {
                    log.info("patient not found in MPI - ignore");
                }
            } else if (CommonChecks.isMultipleSearchResult(searchResults)) {
                log.info("multiple patients found in MPI [searchResults.size()=" + searchResults.size() + "]");
                msgText = "Error: Multiple patients were found in the MPI";

                result = null;
            } else {
                log.info("patient found in MPI. Currently IGNORE record!");
                msgText = "Warning: Patient already found in MPI.";
            }
        }

        result = HL7AckTransforms.createAckMessage(localDeviceId, message.getId(), ackTypeCode, msgText, senderOID, receiverOID);

        return result;
    }
}
