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
package gov.hhs.fha.nhinc.transform.subdisc;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.COCTMT090300UV01AssignedDevice;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.QUQIMT021001UV01AuthorOrPerformer;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.XParticipationAuthorPerformer;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7PRPA201305Transforms {

    private static HL7MessageIdGenerator idGenerator = new HL7MessageIdGenerator();

    public static PRPAIN201305UV02 createPRPA201305(PRPAMT201301UV02Patient patient, String senderOID,
            String receiverOID, String localDeviceId) {
        PRPAIN201305UV02 result = new PRPAIN201305UV02();

        // Create the 201305 message header fields
        result.setITSVersion(HL7Constants.ITS_VERSION);
        result.setId(idGenerator.generateHL7MessageId(localDeviceId));
        result.setCreationTime(HL7DataTransformHelper.creationTimeFactory());
        result.setInteractionId(
                HL7DataTransformHelper.IIFactory(HL7Constants.INTERACTION_ID_ROOT, "PRPA_IN201305UV02"));
        result.setProcessingCode(HL7DataTransformHelper.CSFactory("P"));
        result.setProcessingModeCode(HL7DataTransformHelper.CSFactory("R"));
        result.setAcceptAckCode(HL7DataTransformHelper.CSFactory("AL"));

        // Create the Sender
        result.setSender(HL7SenderTransforms.createMCCIMT000100UV01Sender(senderOID));

        // Create the Receiver
        result.getReceiver().add(HL7ReceiverTransforms.createMCCIMT000100UV01Receiver(receiverOID));

        result.setControlActProcess(createQUQIMT021001UV01ControlActProcess(patient, localDeviceId));

        return result;
    }

    public static PRPAIN201305UV02QUQIMT021001UV01ControlActProcess createQUQIMT021001UV01ControlActProcess(
            PRPAMT201301UV02Patient patient, String localDeviceId) {
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        controlActProcess
                .setCode(HL7DataTransformHelper.CDFactory("PRPA_TE201305UV", HL7Constants.INTERACTION_ID_ROOT));

        controlActProcess.setQueryByParameter(HL7QueryParamsTransforms.createQueryParams(patient, localDeviceId));

        QUQIMT021001UV01AuthorOrPerformer authorOrPerformer = new QUQIMT021001UV01AuthorOrPerformer();
        authorOrPerformer.setTypeCode(XParticipationAuthorPerformer.AUT);

        COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
        II id = new II();
        id.setRoot(localDeviceId);

        assignedDevice.getId().add(id);

        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "assignedDevice");
        JAXBElement<COCTMT090300UV01AssignedDevice> assignedDeviceJAXBElement = new JAXBElement<>(xmlqname,
                COCTMT090300UV01AssignedDevice.class, assignedDevice);

        authorOrPerformer.setAssignedDevice(assignedDeviceJAXBElement);

        controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);

        return controlActProcess;
    }

}
