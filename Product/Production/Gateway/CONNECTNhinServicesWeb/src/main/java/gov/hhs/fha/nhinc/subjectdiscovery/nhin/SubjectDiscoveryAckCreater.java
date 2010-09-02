/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.subjectdiscovery.nhin;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201310Transforms;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author Jon Hoppesch
 */
public class SubjectDiscoveryAckCreater {

    public MCCIIN000002UV01 createAck(PIXConsumerPRPAIN201301UVRequestType request, String ackMsgText) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null &&
                request.getPRPAIN201301UV02() != null) {
            // Extract the message id
            if (request.getPRPAIN201301UV02().getId() != null) {
                msgId = request.getPRPAIN201301UV02().getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver()) &&
                    request.getPRPAIN201301UV02().getReceiver().get(0) != null &&
                    request.getPRPAIN201301UV02().getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId()) &&
                    request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                senderOID = request.getPRPAIN201301UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getPRPAIN201301UV02().getSender() != null &&
                    request.getPRPAIN201301UV02().getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getSender().getDevice().getId()) &&
                    request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0).getRoot())) {
                receiverOID = request.getPRPAIN201301UV02().getSender().getDevice().getId().get(0).getRoot();
            }

            // Create the ack message
            ack = HL7AckTransforms.createAckMessage(null, msgId, ackMsgText, senderOID, receiverOID);
        }

        return ack;
    }

    public MCCIIN000002UV01 createAck(PIXConsumerPRPAIN201302UVRequestType request, String ackMsgText) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        II msgId = new II();
        String senderOID = null;
        String receiverOID = null;

        if (request != null &&
                request.getPRPAIN201302UV02() != null) {
            // Extract the message id
            if (request.getPRPAIN201302UV02().getId() != null) {
                msgId = request.getPRPAIN201302UV02().getId();
            }

            // Set the sender OID to the receiver OID from the original message
            if (NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver()) &&
                    request.getPRPAIN201302UV02().getReceiver().get(0) != null &&
                    request.getPRPAIN201302UV02().getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId()) &&
                    request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                senderOID = request.getPRPAIN201302UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getPRPAIN201302UV02().getSender() != null &&
                    request.getPRPAIN201302UV02().getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getSender().getDevice().getId()) &&
                    request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0).getRoot())) {
                receiverOID = request.getPRPAIN201302UV02().getSender().getDevice().getId().get(0).getRoot();
            }

            ack = HL7AckTransforms.createAckMessage(null, msgId, ackMsgText, senderOID, receiverOID);
        }
        return ack;
    }

    public PRPAIN201310UV02 createFault201310(PIXConsumerPRPAIN201309UVRequestType request, String ackMsgText) {
        PRPAIN201310UV02 result = new PRPAIN201310UV02();
        String senderOID = null;
        String receiverOID = null;

        if (request != null &&
                request.getPRPAIN201309UV02() != null) {
            // Set the sender OID to the receiver OID from the original message
            if (NullChecker.isNotNullish(request.getPRPAIN201309UV02().getReceiver()) &&
                    request.getPRPAIN201309UV02().getReceiver().get(0) != null &&
                    request.getPRPAIN201309UV02().getReceiver().get(0).getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201309UV02().getReceiver().get(0).getDevice().getId()) &&
                    request.getPRPAIN201309UV02().getReceiver().get(0).getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201309UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
                senderOID = request.getPRPAIN201309UV02().getReceiver().get(0).getDevice().getId().get(0).getRoot();
            }

            // Set the receiver OID to the sender OID from the original message
            if (request.getPRPAIN201309UV02().getSender() != null &&
                    request.getPRPAIN201309UV02().getSender().getDevice() != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201309UV02().getSender().getDevice().getId()) &&
                    request.getPRPAIN201309UV02().getSender().getDevice().getId().get(0) != null &&
                    NullChecker.isNotNullish(request.getPRPAIN201309UV02().getSender().getDevice().getId().get(0).getRoot())) {
                receiverOID = request.getPRPAIN201309UV02().getSender().getDevice().getId().get(0).getRoot();
            }

            // Create the 201310 message
            result = HL7PRPA201310Transforms.createFaultPRPA201310(senderOID, receiverOID);
        }
        return result;
    }
}
