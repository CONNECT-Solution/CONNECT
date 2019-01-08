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

import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a utility class that parses the PRPAIN201305UV02 message and extract patientId and participantObjectId from
 * it.
 *
 * @author tjafri
 */
public class PRPAIN201305UV02Parser {

    private static final Logger LOG = LoggerFactory.getLogger(PRPAIN201305UV02Parser.class);

    public static II getPatientId(PRPAIN201305UV02 request) {
        II livingSubjectId = null;

        if (request != null && request.getControlActProcess() != null
            && request.getControlActProcess().getQueryByParameter() != null
            && request.getControlActProcess().getQueryByParameter().getValue() != null
            && request.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null
            && request.getControlActProcess().getQueryByParameter().getValue().getParameterList()
            .getLivingSubjectId() != null) {

            List<PRPAMT201306UV02LivingSubjectId> ids = request.getControlActProcess().getQueryByParameter().getValue()
                .getParameterList().getLivingSubjectId();
            if (ids != null && !ids.isEmpty()) {
                if (ids.size() == 1) {
                    livingSubjectId = getLivingSubjectId(ids.get(0));
                } else if (ids.size() > 1) {
                    livingSubjectId = getLivingSubjectIdFromAuthorOrPerformerValue(request, ids);
                }
            }
        } else {
            LOG.error("PatientId doesn't exist in the received PRPAIN201305UV02 message");
        }

        return livingSubjectId;
    }

    public static String getQueryId(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null
            && request.getControlActProcess() != null
            && request.getControlActProcess().getQueryByParameter() != null
            && request.getControlActProcess().getQueryByParameter().getValue() != null
            && request.getControlActProcess().getQueryByParameter().getValue().getQueryId() != null) {

            oid = request.getControlActProcess().getQueryByParameter().getValue().getQueryId().getExtension();
        }
        return oid;
    }

    private static II getLivingSubjectIdFromAuthorOrPerformerValue(PRPAIN201305UV02 request,
        List<PRPAMT201306UV02LivingSubjectId> ids) {

        II livingSubjectId = null;

        // Get assignedDevice root
        if (request.getControlActProcess().getAuthorOrPerformer() != null
            && !request.getControlActProcess().getAuthorOrPerformer().isEmpty()
            && request.getControlActProcess().getAuthorOrPerformer().get(0) != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue()
            .getId() != null
            && !request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()
            .isEmpty()
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()
            .get(0) != null
            && request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()
            .get(0).getRoot() != null) {

            String root = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice()
                .getValue().getId().get(0).getRoot();

            // Compare assignedDevice root to each livingSubjectId root
            for (PRPAMT201306UV02LivingSubjectId id : ids) {
                II oII = getLivingSubjectId(id);

                if (oII != null && oII.getRoot() != null && oII.getRoot().equals(root)) {
                    livingSubjectId = oII;
                    break;
                }
            }
        } else {
            livingSubjectId = getLivingSubjectId(ids.get(0));
        }

        return livingSubjectId;
    }

    private static II getLivingSubjectId(PRPAMT201306UV02LivingSubjectId id) {
        II oII = null;

        if (id != null && id.getValue() != null && !id.getValue().isEmpty()) {
            oII = id.getValue().get(0);
        }

        return oII;
    }
}
