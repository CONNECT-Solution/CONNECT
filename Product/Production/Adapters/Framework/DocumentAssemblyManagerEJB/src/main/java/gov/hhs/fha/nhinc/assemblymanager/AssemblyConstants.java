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
package gov.hhs.fha.nhinc.assemblymanager;

/**
 * 
 * @author kim
 */
public class AssemblyConstants {

    public final static String DAS_DATASERVICE_ENDPOINT = "das.dataservice.endpoint";
    public final static String DAS_PU_VALUE = "docassemblyPU";
    public final static String TEMPLATE_MANAGER_PU = "TemplateManagerPU";
    public final static String ORGANIZATION_OID = "ORGANIZATION_OID";
    public final static String ORGANIZATION_NAME = "ORGANIZATION_NAME";
    public final static String ORGANIZATION_SYSTEM = "ORGANIZATION_SYS";
    public final static String C32_DOCUMENT = "34133-9";
    public final static String DEF_CONFIDENTIAL_CODE = "DEF_CONFIDENTIAL_CODE";
    public final static String DEF_CONFIDENTIAL_CODE_DESCR = "DEF_CONFIDENTIAL_CODE_DESCR";
    public final static String DEF_CONFIDENTIAL_CODE_SYS_OID = "DEF_CONFIDENTIAL_CODE_SYS_OID";
    public final static String DEF_LANGUAGE = "DEF_LANGUAGE";
    public final static String HCFT_CODE = "HCFT_CODE";
    public final static String HCFT_CODE_DESCR = "HCFT_CODE_DESCR";
    public final static String HCFT_CODE_SYS_OID = "HCFT_CODE_SYS_OID";
    public final static String PRACTICE_SETTING_CODE = "PRACTICE_SETTING_CODE";
    public final static String PRACTICE_SETTING_CODE_DESCR = "PRACTICE_SETTING_CODE_DESCR";
    public final static String PRACTICE_SETTING_CODE_SYS_OID = "PRACTICE_SETTING_CODE_SYS_OID";
    public final static String LOINC_CODE_NAME = "LOINC";
    public final static String CARE_RECORD_QUERY_INTERACTION_ID = "QUPC_IN043100UV";
    public final static String CARE_RECORD_QUERY_TRIGGER = "QUPC_TE043100UV01";
    public final static String CDL_SERVICE = "Common Data Layer Service";
    public final static String ADAS_SERVICE = "Adapter Document Assembly Service";
    public final static String TEMPLATES_PRE25_YN = "das.hitsp.pre25.templates";
    public final static String DAS_C32_STYLESHEET = "das.c32.stylesheet";

    private static String pre25TemplatesSupport = "N"; // default to HITSP 2.5
                                                       // templates

    public static boolean usePre25Templates() {
        pre25TemplatesSupport = AssemblyManager.getProperty(AssemblyConstants.TEMPLATES_PRE25_YN);

        // default to HITSP 2.5 templates
        if (pre25TemplatesSupport == null || pre25TemplatesSupport.length() == 0) {
            return false;
        }

        if (pre25TemplatesSupport.equalsIgnoreCase("Y")) {
            return true;
        }

        return false;
    }
}
