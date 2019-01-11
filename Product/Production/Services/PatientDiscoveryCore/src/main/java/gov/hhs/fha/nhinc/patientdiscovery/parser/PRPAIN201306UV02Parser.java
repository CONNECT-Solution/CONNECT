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
package gov.hhs.fha.nhinc.patientdiscovery.parser;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a utility class that parses the PRPAIN201306UV02 message and extract patientId and participantObjectId from
 * it.
 *
 * @author tjafri
 */
public class PRPAIN201306UV02Parser {

    private static final Logger LOG = LoggerFactory.getLogger(PRPAIN201306UV02Parser.class);

    public static String getQueryId(PRPAIN201306UV02 response) {
        String oid = null;

        if (response != null
            && response.getControlActProcess() != null
            && response.getControlActProcess().getQueryByParameter() != null
            && response.getControlActProcess().getQueryByParameter().getValue() != null
            && response.getControlActProcess().getQueryByParameter().getValue().getQueryId() != null) {

            oid = response.getControlActProcess().getQueryByParameter().getValue().getQueryId().getExtension();
        }

        return oid;
    }

    public static List<II> getPatientIds(PRPAIN201306UV02 response) {
        List<II> oIIs = null;
        if (response != null && response.getControlActProcess() != null
            && response.getControlActProcess().getSubject() != null) {
            oIIs = new ArrayList<>();
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : response.getControlActProcess().getSubject()) {
                if (subject.getRegistrationEvent() != null && subject.getRegistrationEvent().getSubject1() != null
                    && subject.getRegistrationEvent().getSubject1().getPatient() != null
                    && subject.getRegistrationEvent().getSubject1().getPatient().getId() != null) {

                    oIIs.addAll(subject.getRegistrationEvent().getSubject1().getPatient().getId());
                }
            }
        } else {
            LOG.error("PatientId doesn't exist in the received PRPAIN201306UV02 message");
        }
        return oIIs;
    }

    public static String getSenderHcid(PRPAIN201306UV02 response) {
        String id = null;
        if (response != null && response.getSender() != null
            && response.getSender().getDevice() != null && response.getSender().getDevice().getAsAgent() != null
            && response.getSender().getDevice().getAsAgent().getValue() != null
            && response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().
            getId() != null
            && !response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().isEmpty()
            && response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0) != null
            && response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0).getRoot() != null
            && !response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
            .getId().get(0).getRoot().isEmpty()) {
            id = response.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot();
        }
        //If representedOrganization Id root is null get id from device
        if (NullChecker.isNullish(id) && response != null && response.getSender() != null
            && response.getSender().getDevice() != null && response.getSender().getDevice().getId() != null
            && response.getSender().getDevice().getId().get(0) != null
            && StringUtils.isNotEmpty(response.getSender().getDevice().getId().get(0).getRoot())) {
            id = response.getSender().getDevice().getId().get(0).getRoot();
        }
        return id;
    }

    public static String getReceiverHCID(PRPAIN201306UV02 response) {
        String id = null;
        if (response != null && response.getReceiver() != null && response.getReceiver().get(0) != null
            && response.getReceiver().get(0).getDevice() != null && response.getReceiver().get(0).getDevice().
            getAsAgent() != null
            && response.getReceiver().get(0).getDevice().getAsAgent().getValue() != null
            && response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null
            && response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue().getId() != null
            && !response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue().getId().isEmpty()
            && response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue().getId().get(0) != null
            && response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue().getId().get(0).getRoot() != null
            && !response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
            .getValue().getId().get(0).getRoot().isEmpty()) {
            id = response.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization()
                .getValue().getId().get(0).getRoot();
        }
        //If representedOrganization Id root is null get id from device
        if (NullChecker.isNullish(id) && response != null && response.getReceiver() != null
            && response.getReceiver().get(0) != null
            && response.getReceiver().get(0).getDevice() != null
            && response.getReceiver().get(0).getDevice().getId() != null
            && response.getReceiver().get(0).getDevice().getId().get(0) != null
            && StringUtils.isNotEmpty(response.getSender().getDevice().getId().get(0).getRoot())) {
            id = response.getReceiver().get(0).getDevice().getId().get(0).getRoot();
        }
        return id;
    }
}
