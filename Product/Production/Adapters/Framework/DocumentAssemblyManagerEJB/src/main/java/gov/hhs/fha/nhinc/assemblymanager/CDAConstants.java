/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.assemblymanager;

/**
 * 
 * This class is used to hold the CDA constants that are used when creating the
 * 
 * CDA document.
 * 
 * 
 * 
 * @author Kim
 */
public class CDAConstants {

    public static final String TYPE_ID_EXTENSION_POCD_HD000040 = "POCD_HD000040";
    public static final String TITLE_TAG = "title";
    public static final String TEXT_TAG = "text";
    public static final String SUBSTANCE_ADMINISTRATION_CLASS_CODE = "SBADM";
    public static final String TYPE_CODE_REFR = "REFR";
    public static final String TYPE_CODE_SUBJ = "SUBJ";
    public static final String TYPE_CODE_COMP = "COMP";
    public static final String TYPE_CODE_CSM = "CSM";
    public static final String TYPE_CODE_MFST = "MFST";
    public static final String CLASS_CODE_MMAT = "MMAT";
    public static final String CLASS_CODE_MANU = "MANU";
    public static final String CLASS_CODE_OBS = "OBS";
    public static final String UNKNOWN_CODE = "UNK";
    public static final String STATUS_CODE_COMPLETED = "completed";
    public final static String MED_SECTION_TITLE = "Medications";
    public final static String ALLERGIES_SECTION_TITLE = "Allergies and Adverse Reactions";
    public final static String PROBLEMS_SECTION_TITLE = "Problems";
    /* TEMPLATE and TYPE IDS */
    public static final String PROBLEM_ACT_TEMPLATE_ID = "2.16.840.1.113883.10.20.1.27";
    public static final String TYPE_ID_ROOT = "2.16.840.1.113883.1.3";
    public static final String TEMPLATE_ID_ROOT_MEDICAL_DOCUMENTS = "1.3.6.1.4.1.19376.1.5.3.1.1.1";
    /* SNOMED Constants */
    public static final String SNOMED_CT_CODE_SYS_OID = "2.16.840.1.113883.6.96";
    /* LOINC Constants */
    public final static String LOINC_MED_CODE = "10160-0";
    public final static String LOINC_ALLERGY_CODE = "48765-2";
    public final static String LOINC_PROBLEM_CODE = "11450-4";
    public final static String LOINC_CODE_SYS_OID = "2.16.840.1.113883.6.1";
    public static final String LOINC_CODE_SYS_NAME = "LOINC";
    public static final String LOINC_STATUS_CODE = "33999-4";
    public static final String LOINC_SYS_NAME = "LOINC";

    /* ACT Constants */
    public final static String ACT_CODE_SEVERITY = "SEV";
    public final static String ACT_CODE_SEVERITY_LABEL = "Severity";
    public final static String ACT_CODE_SYS_NAME = "ActCode";
    public final static String ACT_CODE_SYS_OID = "2.16.840.1.113883.5.4";

    /* ICD9 Constants */
    public static final String ICD9_CODE_SYS_OID = "2.16.840.1.113883.6.42";

    /* NCD Constants */
    public static final String NDC_CODE_SYSTEM_OID = "2.16.840.1.113883.6.69";

    /* RXNorm Constants */
    public static final String RXNORM_CODE_SYS_OID = "2.16.840.1.113883.6.88";

    /* Other OIDS */
    public static final String CONFIDENTIAL_CODE_SYS_OID = "2.16.840.1.113883.5.25";
    public static final String FORMAT_CODE_OID = "1.3.6.1.4.1.19376.1.2.3";
    public static final String HCFT_CODE_SYS_OID = "2.16.840.1.113883.6.96";
    public static final String PRACTICE_SETTING_CODE_SYS_OID = "2.16.840.1.113883.6.96";
}
