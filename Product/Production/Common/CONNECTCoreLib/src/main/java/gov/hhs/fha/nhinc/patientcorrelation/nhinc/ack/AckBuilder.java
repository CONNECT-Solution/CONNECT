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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.ack;

import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.CSHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.Configuration;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.CreationTimeHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.IIHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.InteractionIdHelper;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.SenderReceiverHelperMCCIMT000200UV01;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers.UniqueIdHelper;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.MCCIMT000200UV01TargetMessage;
import org.hl7.v3.PRPAIN201301UV02;

/**
 *
 * @author rayj
 */
public class AckBuilder {

    private static final String AcceptAckCodeValue = "NE";
    private static final String ITSVersion = "XML_1.0";
    private static final String InteractionIdExtension = "MCCI_IN000002UV01";
    private static final String ProcessingCodeValue = "P";
    private static final String ProcessingModeCode = "R";

    public static MCCIIN000002UV01 buildAck(PRPAIN201301UV02 originalMessage) {
        II receiverId = null;
        II senderId;
        String acknowledgementTypeCode = "CA";
        II originalMessageId = null;

        if (originalMessage != null) {
            if (originalMessage.getSender() != null && originalMessage.getSender().getDevice() != null
                && CollectionUtils.isNotEmpty(originalMessage.getSender().getDevice().getId())) {
                receiverId = originalMessage.getSender().getDevice().getId().get(0);
            }

            originalMessageId = originalMessage.getId();
        }

        senderId = IIHelper.IIFactory(Configuration.getMyCommunityId(), null);

        return buildAck(receiverId, senderId, acknowledgementTypeCode, originalMessageId);

    }

    public static MCCIIN000002UV01 buildAck(II receiverId, II senderId, String acknowledgementTypeCode,
        II originalMessageId) {
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
