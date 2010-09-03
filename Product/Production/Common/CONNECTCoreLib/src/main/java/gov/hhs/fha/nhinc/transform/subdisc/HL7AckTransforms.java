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
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.hl7.v3.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7AckTransforms {

    private static Log log = LogFactory.getLog(HL7AckTransforms.class);

    public static MCCIIN000002UV01 createAckFrom201305(PRPAIN201305UV02 request, String ackMsgText) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null) {
            // Extract the message id
            if (request.getId() != null) {
                msgId = request.getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (NullChecker.isNotNullish(request.getReceiver()) &&
                    request.getReceiver().get(0) != null &&
                    request.getReceiver().get(0).getDevice() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                senderOID = request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    request.getSender().getDevice().getAsAgent() != null &&
                    request.getSender().getDevice().getAsAgent().getValue() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                receiverOID = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Create the ack message
            ack = HL7AckTransforms.createAckMessage(null, msgId, ackMsgText, senderOID, receiverOID);
        }

        return ack;
    }

    public static MCCIIN000002UV01 createAckFrom201306(PRPAIN201306UV02 request, String ackMsgText) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null) {
            // Extract the message id
            if (request.getId() != null) {
                msgId = request.getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (NullChecker.isNotNullish(request.getReceiver()) &&
                    request.getReceiver().get(0) != null &&
                    request.getReceiver().get(0).getDevice() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                senderOID = request.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getSender() != null &&
                    request.getSender().getDevice() != null &&
                    request.getSender().getDevice().getAsAgent() != null &&
                    request.getSender().getDevice().getAsAgent().getValue() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                    request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
                receiverOID = request.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
            }

            // Create the ack message
            ack = HL7AckTransforms.createAckMessage(null, msgId, ackMsgText, senderOID, receiverOID);
        }

        return ack;
    }

    public static MCCIIN000002UV01 createAckMessage(String localDeviceId, II origMsgId, String msgText, String senderOID, String receiverOID) {
        MCCIIN000002UV01 ackMsg = new MCCIIN000002UV01();

        // Validate input parameters
        if (NullChecker.isNullish(senderOID)) {
            log.error("Failed to specify a sender OID");
            return null;
        }

        if (NullChecker.isNullish(receiverOID)) {
            log.error("Failed to specify a receiver OID");
            return null;
        }

        // Create the Ack message header fields
        ackMsg.setITSVersion(HL7Constants.ITS_VERSION);
        ackMsg.setId(HL7MessageIdGenerator.GenerateHL7MessageId(localDeviceId));
        ackMsg.setCreationTime(HL7DataTransformHelper.CreationTimeFactory());
        ackMsg.setInteractionId(HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "MCCIIN000002UV01"));
        ackMsg.setProcessingCode(HL7DataTransformHelper.CSFactory("T"));
        ackMsg.setProcessingModeCode(HL7DataTransformHelper.CSFactory("T"));
        ackMsg.setAcceptAckCode(HL7DataTransformHelper.CSFactory("NE"));

        // Create the Sender
        ackMsg.setSender(HL7SenderTransforms.createMCCIMT000200UV01Sender(senderOID));

        // Create the Receiver
        ackMsg.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000200UV01Receiver(receiverOID));

        // Create Acknowledgement section if an original message id or message text was specified
        if (NullChecker.isNotNullish(msgText) ||
                (origMsgId != null && NullChecker.isNotNullish(origMsgId.getRoot()) && NullChecker.isNotNullish(origMsgId.getExtension()))) {
            log.debug("Adding Acknowledgement Section");
            ackMsg.getAcknowledgement().add(createAcknowledgement(origMsgId, msgText));
        }

        return ackMsg;
    }

    public static MCCIMT000200UV01Acknowledgement createAcknowledgement(II msgId, String msgText) {
        MCCIMT000200UV01Acknowledgement ack = new MCCIMT000200UV01Acknowledgement();

        ack.setTypeCode(HL7DataTransformHelper.CSFactory("CA"));

        if (msgId != null) {
            ack.setTargetMessage(createTargetMessage(msgId));
        }

        if (msgText != null) {
            ack.getAcknowledgementDetail().add(createAckDetail(msgText));
        }

        return ack;
    }

    public static MCCIMT000200UV01TargetMessage createTargetMessage(II msgId) {
        MCCIMT000200UV01TargetMessage targetMsg = new MCCIMT000200UV01TargetMessage();

        if (msgId != null) {
            log.debug("Setting original message id, root: " + msgId.getRoot() + ", extension: " + msgId.getExtension());
            targetMsg.setId(msgId);
        }

        return targetMsg;
    }

    public static MCCIMT000200UV01AcknowledgementDetail createAckDetail(String msgText) {
        MCCIMT000200UV01AcknowledgementDetail ackDetail = new MCCIMT000200UV01AcknowledgementDetail();


        if (NullChecker.isNotNullish(msgText)) {
            // Set the acknowledge message text
            EDExplicit msg = new EDExplicit();

            log.debug("Setting ack message text: " + msgText);
            msg.getContent().add(msgText);
            ackDetail.setText(msg);
        }
        return ackDetail;
    }


}
