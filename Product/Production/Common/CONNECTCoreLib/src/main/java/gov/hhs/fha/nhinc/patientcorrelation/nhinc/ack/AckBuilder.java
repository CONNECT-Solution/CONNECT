/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.ack;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.*;
import org.hl7.v3.*;

/**
 *
 * @author rayj
 */
public class AckBuilder {

    private static final String AcceptAckCodeValue = "NE";
    private static final String ITSVersion = "XML_1.0";
    private static final String InteractionIdExtension = "MCCI_IN000002UV01";
    private static final String MoodCodeValue = "EVN";
    private static final String PatientClassCode = "PAT";
    private static final String PatientStatusCode = "active";
    private static final String ProcessingCodeValue = "P";
    private static final String ProcessingModeCode = "R";
    private static final String SubjectTypeCode = "SUBJ";

    public static MCCIIN000002UV01 BuildAck(PRPAIN201301UV02 originalMessage) {
        II receiverId = null;
        II senderId = null;
        String acknowledgementTypeCode = "CA";
        II originalMessageId = null;

        if (originalMessage != null) {
            if ((originalMessage.getSender() != null) && (originalMessage.getSender().getDevice() != null) && (originalMessage.getSender().getDevice().getId() != null) && (originalMessage.getSender().getDevice().getId().size() > 0)) {
                receiverId = originalMessage.getSender().getDevice().getId().get(0);
            }

            originalMessageId = originalMessage.getId();
        }

        senderId = IIHelper.IIFactory(Configuration.getMyCommunityId(), null);

        MCCIIN000002UV01 ack = BuildAck(receiverId, senderId, acknowledgementTypeCode, originalMessageId);
        return ack;

    }

    public static MCCIIN000002UV01 BuildAck(II receiverId, II senderId, String acknowledgementTypeCode, II originalMessageId) {
        MCCIIN000002UV01 message = new MCCIIN000002UV01();

        message.setITSVersion(ITSVersion);
        message.setId(UniqueIdHelper.createUniqueId());
        message.setCreationTime(CreationTimeHelper.getCreationTime());
        message.setInteractionId(InteractionIdHelper.createInteractionId(InteractionIdExtension));

        message.setProcessingCode(CSHelper.buildCS(ProcessingCodeValue));
        message.setProcessingModeCode(CSHelper.buildCS(ProcessingModeCode));
        message.setAcceptAckCode(CSHelper.buildCS(AcceptAckCodeValue));

        message.getReceiver().add(SenderReceiverHelperMCCIMT000200UV01.CreateReceiver(receiverId));
        message.setSender(SenderReceiverHelperMCCIMT000200UV01.CreateSender(senderId));

        MCCIMT000200UV01Acknowledgement acknowledgement = new MCCIMT000200UV01Acknowledgement();
        acknowledgement.setTypeCode(CSHelper.buildCS(acknowledgementTypeCode));
        MCCIMT000200UV01TargetMessage targetMessage = new MCCIMT000200UV01TargetMessage();
        targetMessage.setId(originalMessageId);
        acknowledgement.setTargetMessage(targetMessage);
        message.getAcknowledgement().add(acknowledgement);

        return message;
    }
}
