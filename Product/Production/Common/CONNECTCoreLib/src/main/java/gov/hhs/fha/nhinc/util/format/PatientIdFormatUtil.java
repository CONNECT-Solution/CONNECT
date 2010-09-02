/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.util.format;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Format utility for patient identifiers.
 * 
 * @author Neil Webb
 */
public class PatientIdFormatUtil
{
    private static Log log = LogFactory.getLog(PatientIdFormatUtil.class);

    /**
     * Parse an optionally HL7 encoded patient identifier. If the patient 
     * identifier is not HL7 encoded, the original id will be returned.
     * The format of an HL7 encoded patient id is 
     * "<id>^^^&<home coummunity id>&ISO"
     * 
     * @param receivedPatientId Optionally HL7 encoded patient identifier
     * @return Parsed patient id
     */
    public static String parsePatientId(String receivedPatientId)
    {
        log.debug("Parsing patient id: " + receivedPatientId);
        String patientId = receivedPatientId;
        if ((patientId != null) && (patientId.length() > 0))
        {
            // In some cases we see a quote - in others we do not.  So lets strip them off if we see them.
            //---------------------------------------------------------------------------------------------
            if ((patientId.startsWith("'")) && (patientId.length() > 1))
            {
                StringBuffer sbPatientId = new StringBuffer(patientId);
                if (patientId.endsWith("'"))
                {
                    sbPatientId.deleteCharAt(sbPatientId.length() - 1);     // strip off the ending quote
                }
                sbPatientId.deleteCharAt(0);        // strip off hte first char quote
                
                patientId = sbPatientId.toString();
            }

            int componentIndex = patientId.indexOf("^");
            log.debug("Index: " + componentIndex);
            if (componentIndex != -1)
            {
                patientId = patientId.substring(0, componentIndex);
                log.debug("Parsed patient id: " + patientId);
            }
        }
        return patientId;
    }
    
    /**
     * Parse an optionally HL7 encoded community id. If the patient 
     * identifier is not HL7 encoded, null will be returned.
     * The format of an HL7 encoded patient id is 
     * "<id>^^^&<home coummunity id>&ISO"
     * 
     * @param encodedPatientId Optionally HL7 encoded patient identifier
     * @return Parsed community id
     */
    public static String parseCommunityId(String encodedPatientId)
    {
        log.debug("Parsing community id: " + encodedPatientId);
        String communityId = null;
        if ((encodedPatientId != null) && (encodedPatientId.length() > 0))
        {
            String workingCommunityId = encodedPatientId;
            // In some cases we see a quote - in others we do not.  So lets strip them off if we see them.
            //---------------------------------------------------------------------------------------------
            if ((workingCommunityId.startsWith("'")) && (workingCommunityId.length() > 1))
            {
                StringBuffer sbCommunityId = new StringBuffer(workingCommunityId);
                if (workingCommunityId.endsWith("'"))
                {
                    sbCommunityId.deleteCharAt(sbCommunityId.length() - 1);     // strip off the ending quote
                }
                sbCommunityId.deleteCharAt(0);        // strip off hte first char quote
                
                workingCommunityId = sbCommunityId.toString();
            }

            // First remove the first components
            int componentIndex = workingCommunityId.lastIndexOf("^");
            log.debug("Index: " + componentIndex);
            if ((componentIndex != -1) && (workingCommunityId.length() > (componentIndex + 1)))
            {
                workingCommunityId = workingCommunityId.substring(componentIndex + 1);
                log.debug("Working community id after first components removed: " + workingCommunityId);

                if(workingCommunityId.startsWith("&"))
                {
                   workingCommunityId = workingCommunityId.substring(1);
                }
                int subComponentIndex = workingCommunityId.indexOf("&");
                if(subComponentIndex != -1)
                {
                    workingCommunityId = workingCommunityId.substring(0, subComponentIndex);
                }
                communityId = workingCommunityId;
            }
        }
        return communityId;
    }
    
    /**
     * HL7 encode a patient identifier. The resulting format will be:
     * "<id>^^^&<home coummunity id>&ISO"
     * 
     * @param patientId Patient identifier
     * @param homeCommunityId Home community id
     * @return HL7 encoded patient id
     */
    public static String hl7EncodePatientId(String patientId, String homeCommunityId)
    {
        // Sometimes the homeCommunityId is prepended with "urn:oid:" for various reasons.  We do not
        // want that included when putting together the Patient ID.  If it is there, we need to 
        // strip it off.
        //---------------------------------------------------------------------------------------------
        String sLocalHomeCommunityId = homeCommunityId;
        if (homeCommunityId.startsWith("urn:oid:"))
        {
            sLocalHomeCommunityId = sLocalHomeCommunityId.substring("urn:oid:".length());
        }
        String encodedPatientId = null;
        log.debug("Creating HL7 encoded patient id for patient id: " + patientId + ", home community id: " + sLocalHomeCommunityId);
        if (patientId != null)
        {
            encodedPatientId = "'" + patientId + "^^^&" + sLocalHomeCommunityId + "&ISO" + "'";
            log.debug("HL7 encoded patient id: " + encodedPatientId);
        }
        return encodedPatientId;
    }
}
