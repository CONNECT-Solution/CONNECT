/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved.
 *
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
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
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * 
 * @author A22387
 */
public class AdapterCommonDataLayerConstants {

    public static final String ADAPTER_PROPERTIES_FILENAME = "adapter_common_datalayer";
    // static data switches
    public static final String ALLERGIES_TEST;
    public static final String PROBLEMS_TEST;
    public static final String PATIENT_INFO_TEST;
    public static final String MEDICATIONS_TEST;
    public static final String FIND_PATIENTS_TEST;
    public static final String FDWC_TEST;
    //Emulator Specific Constants
    public static final String EMULATOR_DATA_LOCATION;
    public static final String EMULATOR_ALLERGIES_TAG;
    public static final String EMULATOR_PROBLEMS_TAG;
    public static final String EMULATOR_MEDS_TAG;
    public static final String EMULATOR_PATIENT_INFO_TAG;
    public static final String EMULATOR_FIND_PATIENTS_TAG;
    public static final String EMULATOR_FIND_PROVIDERS_TAG;
    public static final String EMULATOR_FIND_DOCUMENT_TAG;
    public static final String EMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG;
    public static final String EMULATOR_ALLERGIES_RESPONSE_TYPE;
    public static final String EMULATOR_PROBLEMS_RESPONSE_TYPE;
    public static final String EMULATOR_MEDS_RESPONSE_TYPE;
    public static final String EMULATOR_PATIENT_INFO_RESPONSE_TYPE;
    public static final String EMULATOR_FIND_PATIENTS_RESPONSE_TYPE;
    public static final String EMULATOR_FIND_PROVIDERS_RESPONSE_TYPE;
    public static final String EMULATOR_FIND_DOCUMENT_RESPONSE_TYPE;
    public static final String EMULATOR_FIND_DOCUMENT_WITH_CONTENT_RESPONSE_TYPE;
    public static final String EMULATOR_NO_PATIENT_ID_LABEL;
    public static final String EMULATOR_NO_LAST_NAME_LABEL;
    public static final String EMULATOR_NO_FIRST_NAME_LABEL;
    public static final String EMULATOR_NO_GENDER_LABEL;
    public static final String EMULATOR_NO_DOB_LABEL;
    public static final String EMULATOR_NO_PROVIDER_ID_LABEL;

    static {

        String sEMULATOR_DATA_LOCATION = null;

        // static data switches
        String sALLERGIES_TEST = null;
        String sPROBLEMS_TEST = null;
        String sPATIENT_INFO_TEST = null;
        String sMEDICATIONS_TEST = null;
        String sFIND_PATIENTS_TEST = null;
        String sFDWC_TEST = null;

        String sEMULATOR_ALLERGIES_TAG = null;
        String sEMULATOR_PROBLEMS_TAG = null;
        String sEMULATOR_MEDS_TAG = null;
        String sEMULATOR_PATIENT_INFO_TAG = null;
        String sEMULATOR_FIND_PATIENTS_TAG = null;
        String sEMULATOR_FIND_PROVIDERS_TAG = null;
        String sEMULATOR_FIND_DOCUMENT_TAG = null;
        String sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG = null;
        String sEMULATOR_ALLERGIES_RESPONSE_TYPE = null;
        String sEMULATOR_PROBLEMS_RESPONSE_TYPE = null;
        String sEMULATOR_MEDS_RESPONSE_TYPE = null;
        String sEMULATOR_PATIENT_INFO_RESPONSE_TYPE = null;
        String sEMULATOR_FIND_PATIENTS_RESPONSE_TYPE = null;
        String sEMULATOR_FIND_PROVIDERS_RESPONSE_TYPE = null;
        String sEMULATOR_FIND_DOCUMENT_RESPONSE_TYPE = null;
        String sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_RESPONSE_TYPE = null;
        String sEMULATOR_NO_PATIENT_ID_LABEL = null;
        String sEMULATOR_NO_LAST_NAME_LABEL = null;
        String sEMULATOR_NO_FIRST_NAME_LABEL = null;
        String sEMULATOR_NO_GENDER_LABEL = null;
        String sEMULATOR_NO_DOB_LABEL = null;
        String sEMULATOR_NO_PROVIDER_ID_LABEL = null;

        try {

            PropertyAccessor oProps = PropertyAccessor.getInstance();

            // static data switches
            sALLERGIES_TEST = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "allergies_test");
            sPROBLEMS_TEST = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "problems_test");
            sMEDICATIONS_TEST = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "medications_test");
            sPATIENT_INFO_TEST = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "patient_info_test");
            sFIND_PATIENTS_TEST = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_patients_test");

            sFDWC_TEST = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "fdwc_test");

            sEMULATOR_DATA_LOCATION = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "emulator_data");
            sEMULATOR_ALLERGIES_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "allergies_tag");
            sEMULATOR_PROBLEMS_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "problems_tag");
            sEMULATOR_MEDS_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "meds_tag");
            sEMULATOR_PATIENT_INFO_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "patient_info_tag");
            sEMULATOR_FIND_PATIENTS_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_patients_tag");
            sEMULATOR_FIND_PROVIDERS_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_providers_tag");
            sEMULATOR_FIND_DOCUMENT_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_document_tag");
            sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_document_with_content_tag");
            sEMULATOR_ALLERGIES_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "allergies_response_type");
            sEMULATOR_PROBLEMS_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "problems_response_type");
            sEMULATOR_MEDS_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "meds_response_type");
            sEMULATOR_PATIENT_INFO_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "patient_info_response_type");
            sEMULATOR_FIND_PATIENTS_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_patients_response_type");
            sEMULATOR_FIND_PROVIDERS_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_providers_response_type");
            sEMULATOR_FIND_DOCUMENT_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_document_response_type");
            sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_RESPONSE_TYPE = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "find_document_with_content_response_type");
            sEMULATOR_NO_PATIENT_ID_LABEL = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "unknown_patientID");
            sEMULATOR_NO_LAST_NAME_LABEL = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "unknown_lastName");
            sEMULATOR_NO_FIRST_NAME_LABEL = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "unknown_firstName");
            sEMULATOR_NO_GENDER_LABEL = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "unknown_gender");
            sEMULATOR_NO_DOB_LABEL = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "unknown_dob");
            sEMULATOR_NO_PROVIDER_ID_LABEL = oProps.getProperty(ADAPTER_PROPERTIES_FILENAME, "unknown_providerID");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // static data tests
        ALLERGIES_TEST = sALLERGIES_TEST;
        PROBLEMS_TEST = sPROBLEMS_TEST;
        MEDICATIONS_TEST = sMEDICATIONS_TEST;
        PATIENT_INFO_TEST = sPATIENT_INFO_TEST;
        FIND_PATIENTS_TEST = sFIND_PATIENTS_TEST;
        FDWC_TEST = sFDWC_TEST;

        EMULATOR_DATA_LOCATION = sEMULATOR_DATA_LOCATION;
        EMULATOR_ALLERGIES_TAG = sEMULATOR_ALLERGIES_TAG;
        EMULATOR_PROBLEMS_TAG = sEMULATOR_PROBLEMS_TAG;
        EMULATOR_MEDS_TAG = sEMULATOR_MEDS_TAG;
        EMULATOR_PATIENT_INFO_TAG = sEMULATOR_PATIENT_INFO_TAG;
        EMULATOR_FIND_PATIENTS_TAG = sEMULATOR_FIND_PATIENTS_TAG;
        EMULATOR_FIND_PROVIDERS_TAG = sEMULATOR_FIND_PROVIDERS_TAG;
        EMULATOR_FIND_DOCUMENT_TAG = sEMULATOR_FIND_DOCUMENT_TAG;
        EMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG = sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG;
        EMULATOR_ALLERGIES_RESPONSE_TYPE = sEMULATOR_ALLERGIES_RESPONSE_TYPE;
        EMULATOR_PROBLEMS_RESPONSE_TYPE = sEMULATOR_PROBLEMS_RESPONSE_TYPE;
        EMULATOR_MEDS_RESPONSE_TYPE = sEMULATOR_MEDS_RESPONSE_TYPE;
        EMULATOR_PATIENT_INFO_RESPONSE_TYPE = sEMULATOR_PATIENT_INFO_RESPONSE_TYPE;
        EMULATOR_FIND_PATIENTS_RESPONSE_TYPE = sEMULATOR_FIND_PATIENTS_RESPONSE_TYPE;
        EMULATOR_FIND_PROVIDERS_RESPONSE_TYPE = sEMULATOR_FIND_PROVIDERS_RESPONSE_TYPE;
        EMULATOR_FIND_DOCUMENT_RESPONSE_TYPE = sEMULATOR_FIND_DOCUMENT_RESPONSE_TYPE;
        EMULATOR_FIND_DOCUMENT_WITH_CONTENT_RESPONSE_TYPE = sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_RESPONSE_TYPE;
        EMULATOR_NO_PATIENT_ID_LABEL = sEMULATOR_NO_PATIENT_ID_LABEL;
        EMULATOR_NO_LAST_NAME_LABEL = sEMULATOR_NO_LAST_NAME_LABEL;
        EMULATOR_NO_FIRST_NAME_LABEL = sEMULATOR_NO_FIRST_NAME_LABEL;
        EMULATOR_NO_GENDER_LABEL = sEMULATOR_NO_GENDER_LABEL;
        EMULATOR_NO_DOB_LABEL = sEMULATOR_NO_DOB_LABEL;
        EMULATOR_NO_PROVIDER_ID_LABEL = sEMULATOR_NO_PROVIDER_ID_LABEL;
    }
}
