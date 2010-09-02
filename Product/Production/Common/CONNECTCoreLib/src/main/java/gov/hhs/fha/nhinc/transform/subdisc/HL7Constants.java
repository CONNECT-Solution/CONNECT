/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

/**
 *
 * @author Jon Hoppesch
 */
public class HL7Constants {

    public static final String ITS_VERSION = "XML_1.0";
    public static final String SENDER_DETERMINER_CODE = "INSTANCE";
    public static final String RECEIVER_DETERMINER_CODE = "INSTANCE";
    public static final String DEFAULT_LOCAL_DEVICE_ID = "1.1";
    public static final String NULL_FLAVOR = "NA";
    public static final String INTERACTION_ID_ROOT = "2.16.840.1.113883.1.6";
    public static final String SSN_ID_ROOT = "2.16.840.1.113883.4.1";

    //ResponderBusy Error Constants
    public static final String QUERY_ACK_OK = "OK";
    public static final String QUERY_ACK_QE = "QE";
    public static final String DETECTED_ISSUE_CLASSCODE_ALRT = "ALRT";
    public static final String DETECTED_ISSUE_MOODCODE_EVN = "EVN";
    public static final String DETECTED_ISSUE_CODE_ADMINISTRATIVE = "ActAdministrativeDetectedIssueCode";
    public static final String DETECTED_ISSUE_CODESYSTEM_ERROR_CODE = "2.16.840.1.113883.5.4";
    public static final String DETECTED_ISSUE_MITIGATEDBY_TYPECODE_MITGT = "MITGT";
    public static final String DETECTEDISSUEMANAGEMENT_CLASSCODE = "ACT";
    public static final String DETECTEDISSUEMANAGEMENT_MOODCODE_RQO = "RQO";
    public static final String DETECTEDISSUEMANAGEMENT_CODE_RESPONDER_BUSY = "ResponderBusy";
    public static final String DETECTEDISSUEMANAGEMENT_CODESYSTEM = "1.3.6.1.4.1.19376.1.2.27.3";

    public static final String AGENT_CLASS_CODE="AGNT";
    public static final String ORG_CLASS_CODE="ORG";
    public static final String ASSIGNED_DEVICE_CLASS_CODE = "ASSIGNED";
}
