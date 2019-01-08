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
package gov.hhs.fha.nhinc.util.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Format utility for patient identifiers.
 *
 * @author Neil Webb
 */
public class PatientIdFormatUtil {

    private static final Logger LOG = LoggerFactory.getLogger(PatientIdFormatUtil.class);

    /**
     * Parse an optionally HL7 encoded patient identifier. If the patient identifier is not HL7 encoded, the original id
     * will be returned. The format of an HL7 encoded patient id is "<id>^^^&<home coummunity id>&ISO"
     *
     * @param receivedPatientId Optionally HL7 encoded patient identifier
     * @return Parsed patient id
     */
    public static String parsePatientId(String receivedPatientId) {
        LOG.debug("Parsing patient id: " + receivedPatientId);
        String patientId = receivedPatientId;
        if (patientId != null && patientId.length() > 0) {
            patientId = stripQuotesFromPatientId(patientId);
            int componentIndex = patientId.indexOf("^");
            LOG.debug("Index: " + componentIndex);
            if (componentIndex != -1) {
                patientId = patientId.substring(0, componentIndex);
                LOG.debug("Parsed patient id: " + patientId);
            }
        }
        return patientId;
    }

    /**
     * stripQuotesFromPatientId - in some cases, we see pid wrapped in quotes; this function strips them to keep things
     * uniform during processing.
     *
     * @param patientId
     * @return patientId
     */
    public static String stripQuotesFromPatientId(String patientId) {
        LOG.debug("stripQuotesFromPatientId - Parsing patient id: {}", patientId);

        String sbPatientId = patientId;

        if (patientId != null && patientId.length() > 0) {
            if (patientId.startsWith("'") && patientId.length() > 1) {
                if (patientId.endsWith("'")) {
                    // strip off the ending quote
                    sbPatientId = sbPatientId.substring(0, sbPatientId.length() - 1);
                }

                // strip off the first char quote
                sbPatientId = sbPatientId.substring(1);
            }
        }

        LOG.debug("stripQuotesFromPatientId - Parsed patient id: {}", sbPatientId);
        return sbPatientId;
    }

    /**
     * Parse an optionally HL7 encoded community id. If the patient identifier is not HL7 encoded, null will be
     * returned. The format of an HL7 encoded patient id is "<id>^^^&<home coummunity id>&ISO"
     *
     * @param encodedPatientId Optionally HL7 encoded patient identifier
     * @return Parsed community id
     */
    public static String parseCommunityId(String encodedPatientId) {
        LOG.debug("Parsing community id: " + encodedPatientId);
        String communityId = null;
        if (encodedPatientId != null && encodedPatientId.length() > 0) {
            String workingCommunityId = encodedPatientId;
            workingCommunityId = stripQuotesFromPatientId(workingCommunityId);

            // First remove the first components
            int componentIndex = workingCommunityId.lastIndexOf("^");
            LOG.debug("Index: " + componentIndex);
            if (componentIndex != -1 && workingCommunityId.length() > componentIndex + 1) {
                workingCommunityId = workingCommunityId.substring(componentIndex + 1);
                LOG.debug("Working community id after first components removed: " + workingCommunityId);

                if (workingCommunityId.startsWith("&")) {
                    workingCommunityId = workingCommunityId.substring(1);
                }
                int subComponentIndex = workingCommunityId.indexOf("&");
                if (subComponentIndex != -1) {
                    workingCommunityId = workingCommunityId.substring(0, subComponentIndex);
                }
                communityId = workingCommunityId;
            }
        }
        return communityId;
    }

    /**
     * HL7 encode a patient identifier. The resulting format will be: "<id>^^^&<home coummunity id>&ISO"
     *
     * @param patientId Patient identifier
     * @param homeCommunityId Home community id
     * @return HL7 encoded patient id
     */
    public static String hl7EncodePatientId(String patientId, String homeCommunityId) {
        // Sometimes the homeCommunityId is prepended with "urn:oid:" for various reasons. We do not
        // want that included when putting together the Patient ID. If it is there, we need to
        // strip it off.
        // ---------------------------------------------------------------------------------------------
        String sLocalHomeCommunityId = homeCommunityId;
        if (homeCommunityId.startsWith("urn:oid:")) {
            sLocalHomeCommunityId = sLocalHomeCommunityId.substring("urn:oid:".length());
        }
        String encodedPatientId = null;
        LOG.debug("Creating HL7 encoded patient id for patient id: " + patientId + ", home community id: "
                + sLocalHomeCommunityId);
        if (patientId != null) {
            encodedPatientId = "'" + patientId + "^^^&" + sLocalHomeCommunityId + "&ISO" + "'";
            LOG.debug("HL7 encoded patient id: " + encodedPatientId);
        }
        return encodedPatientId;
    }
}
