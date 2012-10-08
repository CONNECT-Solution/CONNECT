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
package gov.hhs.fha.nhinc.gateway.aggregator.model;

import gov.hhs.fha.nhinc.gateway.aggregator.AggregatorException;

/**
 * This represents the fields that make up the message key that identifies a single document query record in the
 * aggregator message results table.
 * 
 * @author Les Westberg
 */
public class DocQueryMessageKey {
    private static final String DOC_QUERY_TAG = "DocQuery";
    private static final String DOC_QUERY_TAG_START = "<" + DOC_QUERY_TAG + ">";
    private static final String DOC_QUERY_TAG_END = "</" + DOC_QUERY_TAG + ">";
    private static final String HOME_COMMUNITY_ID_TAG = "HomeCommunityId";
    private static final String HOME_COMMUNITY_ID_TAG_START = "<" + HOME_COMMUNITY_ID_TAG + ">";
    private static final String HOME_COMMUNITY_ID_TAG_END = "</" + HOME_COMMUNITY_ID_TAG + ">";
    private static final String ASSIGNING_AUTHORITY_TAG = "AssigningAuthority";
    private static final String ASSIGNING_AUTHORITY_TAG_START = "<" + ASSIGNING_AUTHORITY_TAG + ">";
    private static final String ASSIGNING_AUTHORITY_TAG_END = "</" + ASSIGNING_AUTHORITY_TAG + ">";
    private static final String PATIENT_ID_TAG = "PatientId";
    private static final String PATIENT_ID_TAG_START = "<" + PATIENT_ID_TAG + ">";
    private static final String PATIENT_ID_TAG_END = "</" + PATIENT_ID_TAG + ">";

    // Private member variables
    // -------------------------
    private String homeCommunityId;
    private String assigningAuthority;
    private String patientId;

    /**
     * Default constructor.
     */
    public DocQueryMessageKey() {
        clear();
    }

    /**
     * This method takes the information in a formatted message key and creates an object with that information. This
     * key should be one that was created by this class. (or at least exactly formatted that way).
     * 
     * @param sMessageKey The message key as formatted by calling the creatingXMLMessageKey.
     * @throws AggregatorException This is thrown if the format of the XML message is not correct.
     */
    public DocQueryMessageKey(String sMessageKey) throws AggregatorException {
        // Since this is a simple XML that is in a controlled format -
        // It is easiest to just pull out the fields by hand-parsing...
        // -------------------------------------------------------------
        parseXMLMessageKey(sMessageKey);
    }

    /**
     * Clear the ocntents of this object
     */
    public void clear() {
        homeCommunityId = "";
        assigningAuthority = "";
        patientId = "";
    }

    /**
     * Return the assigning authority.
     * 
     * @return The assigning authority.
     */
    public String getAssigningAuthority() {
        return assigningAuthority;
    }

    /**
     * Sets the assigning authority.
     * 
     * @param assigningAuthority The assigning authority.
     */
    public void setAssigningAuthority(String assigningAuthority) {
        this.assigningAuthority = assigningAuthority;
    }

    /**
     * Return the home community ID. Note if this has not been set, then a look up will be done to retrieve it based on
     * assigning authority.
     * 
     * @return The home community ID.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Sets the home community ID. Note if this has not been set, then a look up will be done to retrieve it based on
     * assigning authority.
     * 
     * @param homeCommunityId The home community ID.
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Returns the patient Id.
     * 
     * @return The patient Id.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient Id.
     * 
     * @param patientId The patient Id.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * This method creates the XML Message Key that will be stored in the MessageKey field of the
     * AGGREGATOR.AGG_MESSAGE_RESULTS table.
     * 
     * @return The XML key that is created based on the fields in this object.
     */
    public String createXMLMessageKey() {
        String sAssigningAuthority = "";
        String sHomeCommunityId = "";
        String sPatientId = "";

        if (assigningAuthority != null) {
            sAssigningAuthority = assigningAuthority.trim();
        }

        if (homeCommunityId != null) {
            // Right now the assigning authority and home community are the same.
            // When this work gets done, we will need to
            // -------------------------------------------------------------------
            if (homeCommunityId.trim().length() <= 0) {
                sHomeCommunityId = sAssigningAuthority;
            } else {
                sHomeCommunityId = homeCommunityId.trim();
            }
        }

        if (patientId != null) {
            sPatientId = patientId.trim();
        }

        String sKey = DOC_QUERY_TAG_START + HOME_COMMUNITY_ID_TAG_START + sHomeCommunityId + HOME_COMMUNITY_ID_TAG_END
                + ASSIGNING_AUTHORITY_TAG_START + sAssigningAuthority + ASSIGNING_AUTHORITY_TAG_END
                + PATIENT_ID_TAG_START + sPatientId + PATIENT_ID_TAG_END + DOC_QUERY_TAG_END;

        return sKey;
    }

    /**
     * Since this is a simple XML that is in a controlled format - It is easiest to just pull out the fields by
     * hand-parsing...
     * 
     * @param sMessageKey The XML string key containing the data.
     * @throws AggregatorException This is thrown if the format of the XML message is not correct.
     */
    public void parseXMLMessageKey(String sMessageKey) throws AggregatorException {
        int iStartIdx = 0;
        int iEndIdx = 0;

        // Get the Home community
        // ------------------------
        iStartIdx = sMessageKey.indexOf(HOME_COMMUNITY_ID_TAG_START);
        if (iStartIdx >= 0) {
            iStartIdx += HOME_COMMUNITY_ID_TAG_START.length();
        } else {
            throw new AggregatorException("Format of DocQueryMessageKey was invalid.  MessageKey = '" + sMessageKey
                    + "'");
        }

        iEndIdx = sMessageKey.indexOf(HOME_COMMUNITY_ID_TAG_END);
        if (iEndIdx > 0) {
            homeCommunityId = sMessageKey.substring(iStartIdx, iEndIdx);
        } else {
            throw new AggregatorException("Format of DocQueryMessageKey was invalid.  MessageKey = '" + sMessageKey
                    + "'");
        }

        // Get the Assigning Authority
        // -----------------------------
        iStartIdx = sMessageKey.indexOf(ASSIGNING_AUTHORITY_TAG_START);
        if (iStartIdx >= 0) {
            iStartIdx += ASSIGNING_AUTHORITY_TAG_START.length();
        } else {
            throw new AggregatorException("Format of DocQueryMessageKey was invalid.  MessageKey = '" + sMessageKey
                    + "'");
        }

        iEndIdx = sMessageKey.indexOf(ASSIGNING_AUTHORITY_TAG_END);
        if (iEndIdx > 0) {
            assigningAuthority = sMessageKey.substring(iStartIdx, iEndIdx);
        } else {
            throw new AggregatorException("Format of DocQueryMessageKey was invalid.  MessageKey = '" + sMessageKey
                    + "'");
        }

        // Get the Patient Id
        // -------------------
        iStartIdx = sMessageKey.indexOf(PATIENT_ID_TAG_START);
        if (iStartIdx >= 0) {
            iStartIdx += PATIENT_ID_TAG_START.length();
        } else {
            throw new AggregatorException("Format of DocQueryMessageKey was invalid.  MessageKey = '" + sMessageKey
                    + "'");
        }

        iEndIdx = sMessageKey.indexOf(PATIENT_ID_TAG_END);
        if (iEndIdx > 0) {
            patientId = sMessageKey.substring(iStartIdx, iEndIdx);
        } else {
            throw new AggregatorException("Format of DocQueryMessageKey was invalid.  MessageKey = '" + sMessageKey
                    + "'");
        }
    }

}
