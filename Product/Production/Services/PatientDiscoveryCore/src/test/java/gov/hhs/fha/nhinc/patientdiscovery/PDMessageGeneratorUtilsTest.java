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
package gov.hhs.fha.nhinc.patientdiscovery;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201310UV02Patient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;


public class PDMessageGeneratorUtilsTest {

    private PDMessageGeneratorUtils msgUtils = PDMessageGeneratorUtils.getInstance();

    @Test
    public void extractPatientIdFromSubject() {
        assertNull(msgUtils.extractPatientIdFromSubject(null));

        PRPAIN201306UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();
        assertNull(msgUtils.extractPatientIdFromSubject(subject));

        subject.setRegistrationEvent(new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent());
        assertNull(msgUtils.extractPatientIdFromSubject(subject));

        subject.getRegistrationEvent().setSubject1(new PRPAIN201306UV02MFMIMT700711UV01Subject2());
        assertNull(msgUtils.extractPatientIdFromSubject(subject));

        subject.getRegistrationEvent().getSubject1().setPatient(new PRPAMT201310UV02Patient());
        assertNull(msgUtils.extractPatientIdFromSubject(subject));

        subject.getRegistrationEvent().getSubject1().getPatient().getId().add(new II());
        assertNull(msgUtils.extractPatientIdFromSubject(subject));

        subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).setExtension("extension");
        assertNull(msgUtils.extractPatientIdFromSubject(subject));

        subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).setRoot("root");
        II id = msgUtils.extractPatientIdFromSubject(subject);

        assertEquals("extension", id.getExtension());
        assertEquals("root", id.getRoot());
    }

    @Test
    public void createCommunityPRPAIN201306UV02ResponseType() {
        CommunityPRPAIN201306UV02ResponseType response = msgUtils.createCommunityPRPAIN201306UV02ResponseType("1.1");

        assertEquals("1.1", response.getNhinTargetCommunity().getHomeCommunity().getHomeCommunityId());
        assertNull(response.getPRPAIN201306UV02());
    }
}
