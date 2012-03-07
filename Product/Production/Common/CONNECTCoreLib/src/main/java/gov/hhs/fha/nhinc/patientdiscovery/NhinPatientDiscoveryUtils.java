/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;

/**
 * 
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryUtils {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryUtils.class);

    /**
     * Extracts first patient id from response message
     * 
     * @param msg
     * @return <code>II</code> with extracted patient id
     */
    public static II extractPatientIdFrom201306(PRPAIN201306UV02 msg) {
        II id = new II();

        if (msg != null
                && msg.getControlActProcess() != null
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject())
                && msg.getControlActProcess().getSubject().get(0) != null
                && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
                && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
                && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient().getId())
                && msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                        .getId().get(0) != null
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient().getId().get(0).getExtension())
                && NullChecker.isNotNullish(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient().getId().get(0).getRoot())) {
            id.setExtension(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                    .getPatient().getId().get(0).getExtension());
            id.setRoot(msg.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                    .getId().get(0).getRoot());
        } else {
            id = null;
        }

        return id;
    }

    /**
     * Extracts the patient id from response message subject element
     * 
     * @param msg
     * @return <code>II</code> with extracted patient id
     */
    public static II extractPatientIdFromSubject(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        II id = new II();

        if (subject != null
                && subject.getRegistrationEvent() != null
                && subject.getRegistrationEvent().getSubject1() != null
                && subject.getRegistrationEvent().getSubject1().getPatient() != null
                && NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId())
                && subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null
                && NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0)
                        .getExtension())
                && NullChecker.isNotNullish(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0)
                        .getRoot())) {
            id.setExtension(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            id.setRoot(subject.getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        } else {
            id = null;
        }

        return id;
    }
}
