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
package gov.hhs.fha.nhinc.patientdiscovery._10.testhelper;

import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.MCCIMT000200UV01AcknowledgementDetail;
import org.hl7.v3.MCCIMT000200UV01TargetMessage;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 * @author JHOPPESC
 */
public class TestHelper {
    public static void assertAckMsgEquals(String msgText, MCCIIN000002UV01 message) {
        assertNotNull(message);
        assertNotNull(message.getAcknowledgement());
        assertNotNull(message.getAcknowledgement().get(0));
        assertAckMsgEquals(msgText, message.getAcknowledgement().get(0));
    }

    public static void assertAckMsgEquals(String msgText, MCCIMT000200UV01Acknowledgement message) {
        assertNotNull(message);
        assertNotNull(message.getAcknowledgementDetail());
        assertNotNull(message.getAcknowledgementDetail().get(0));

        assertAckMsgEquals(msgText, message.getAcknowledgementDetail().get(0));
    }

    public static void assertAckMsgEquals(String msgText, MCCIMT000200UV01AcknowledgementDetail message) {
        assertNotNull(message);
        assertNotNull(message.getText());
        assertNotNull(message.getText().getContent());

        assertEquals(msgText, message.getText().getContent().get(0).toString());
    }

    public static void assertSenderEquals(String oid, MCCIIN000002UV01 message) {
        assertNotNull(message);
        assertNotNull(message.getSender());
        assertNotNull(message.getSender().getDevice());
        assertNotNull(message.getSender().getDevice().getAsAgent());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0));
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot());
        assertEquals(oid, message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot());
    }

    public static void assertSenderEquals(String oid, PRPAIN201305UV02 message) {
        assertNotNull(message);
        assertNotNull(message.getSender());
        assertNotNull(message.getSender().getDevice());
        assertNotNull(message.getSender().getDevice().getAsAgent());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue());
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0));
        assertNotNull(message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot());
        assertEquals(oid, message.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot());
    }

    public static void assertReceiverEquals(String oid, MCCIIN000002UV01 message) {
        assertNotNull(message);
        assertNotNull(message.getReceiver());
        assertNotNull(message.getReceiver().get(0));
        assertNotNull(message.getReceiver().get(0).getDevice());
        assertNotNull(message.getReceiver().get(0).getDevice().getId());
        assertNotNull(message.getReceiver().get(0).getDevice().getId().get(0));
        assertNotNull(message.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        assertEquals(oid, message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot());
    }

    public static void assertReceiverEquals(String oid, PRPAIN201305UV02 message) {
        assertNotNull(message);
        assertNotNull(message.getReceiver());
        assertNotNull(message.getReceiver().get(0));
        assertNotNull(message.getReceiver().get(0).getDevice());
        assertNotNull(message.getReceiver().get(0).getDevice().getId());
        assertNotNull(message.getReceiver().get(0).getDevice().getId().get(0));
        assertNotNull(message.getReceiver().get(0).getDevice().getId().get(0).getRoot());
        assertEquals(oid, message.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot());
    }

    public static void assertAckMsgIdEquals(II origMsgId, MCCIIN000002UV01 message) {
        assertNotNull(message);
        assertNotNull(message.getAcknowledgement());
        assertNotNull(message.getAcknowledgement().get(0));

        assertAckMsgIdEquals(origMsgId, message.getAcknowledgement().get(0));
    }

    public static void assertAckMsgIdEquals(II origMsgId, MCCIMT000200UV01Acknowledgement message) {
        assertNotNull(message);
        assertNotNull(message.getTargetMessage());

        assertAckMsgIdEquals(origMsgId, message.getTargetMessage());
    }

    public static void assertAckMsgIdEquals(II origMsgId, MCCIMT000200UV01TargetMessage message) {
        assertNotNull(message);
        assertNotNull(message.getId());
        assertNotNull(message.getId().getExtension());
        assertNotNull(message.getId().getRoot());

        assertEquals(origMsgId.getExtension(), message.getId().getExtension());
        assertEquals(origMsgId.getRoot(), message.getId().getRoot());
    }

    public static void assertEmpty(MCCIIN000002UV01 message) {
        assertNotNull(message);
        assertNull(message.getReceiver());
        assertNull(message.getSender());
    }

    public static void assertPatientFound(PRPAIN201306UV02 message) {
        assertNotNull(message);
        assertNotNull(message.getControlActProcess());
        assertNotNull(message.getControlActProcess().getSubject());
        assertNotNull(message.getControlActProcess().getSubject().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0));
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getRoot());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getId().get(0).getExtension());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson().getValue());
        assertNotNull(message.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                .getPatient().getPatientPerson().getValue().getName());
    }

    public static void assertPatientNotFound(PRPAIN201306UV02 message) {
        assertNotNull(message);
        assertNotNull(message.getControlActProcess());
        assertNotNull(message.getControlActProcess().getSubject());
        assertEquals(0, message.getControlActProcess().getSubject().size());
    }

    public static void assertSSNEquals(String ssn, PRPAIN201305UV02 message) {
        assertNotNull(message);
        assertNotNull(message.getControlActProcess());
        assertNotNull(message.getControlActProcess().getQueryByParameter());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList());
        assertNotNull(message.getControlActProcess().getQueryByParameter().getValue().getParameterList()
                .getLivingSubjectId());

        boolean match = false;

        for (PRPAMT201306UV02LivingSubjectId livingSubId : message.getControlActProcess().getQueryByParameter()
                .getValue().getParameterList().getLivingSubjectId()) {
            for (II subId : livingSubId.getValue()) {
                if (subId.getRoot().equalsIgnoreCase("2.16.840.1.113883.4.1")
                        && subId.getExtension().equalsIgnoreCase(ssn)) {
                    match = true;
                }
            }
        }

        assertEquals(true, match);
    }

}
