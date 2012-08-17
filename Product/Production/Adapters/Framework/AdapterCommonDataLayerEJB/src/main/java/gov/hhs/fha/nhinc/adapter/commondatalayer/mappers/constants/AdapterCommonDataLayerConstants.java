/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants;

import java.util.Properties;
import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.properties.PropertyLoader;

/**
 *
 * @author A22387
 */
public class AdapterCommonDataLayerConstants
{
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

    static
    {
        String sEMULATOR_DATA_LOCATION = null;
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

        try
        {
            Properties oProps = PropertyLoader.getProperties("gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.properties", "adapter_common_datalayer");


            sEMULATOR_DATA_LOCATION = oProps.getProperty("emulator_data");
            sEMULATOR_ALLERGIES_TAG = oProps.getProperty("allergies_tag");
            sEMULATOR_PROBLEMS_TAG = oProps.getProperty("problems_tag");
            sEMULATOR_MEDS_TAG = oProps.getProperty("meds_tag");
            sEMULATOR_PATIENT_INFO_TAG = oProps.getProperty("patient_info_tag");
            sEMULATOR_FIND_PATIENTS_TAG = oProps.getProperty("find_patients_tag");
            sEMULATOR_FIND_PROVIDERS_TAG = oProps.getProperty("find_providers_tag");
            sEMULATOR_FIND_DOCUMENT_TAG = oProps.getProperty("find_document_tag");
            sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG = oProps.getProperty("find_document_with_content_tag");
            sEMULATOR_ALLERGIES_RESPONSE_TYPE = oProps.getProperty("allergies_response_type");
            sEMULATOR_PROBLEMS_RESPONSE_TYPE = oProps.getProperty("problems_response_type");
            sEMULATOR_MEDS_RESPONSE_TYPE = oProps.getProperty("meds_response_type");
            sEMULATOR_PATIENT_INFO_RESPONSE_TYPE = oProps.getProperty("patient_info_response_type");
            sEMULATOR_FIND_PATIENTS_RESPONSE_TYPE = oProps.getProperty("find_patients_response_type");
            sEMULATOR_FIND_PROVIDERS_RESPONSE_TYPE = oProps.getProperty("find_providers_response_type");
            sEMULATOR_FIND_DOCUMENT_RESPONSE_TYPE = oProps.getProperty("find_document_response_type");
            sEMULATOR_FIND_DOCUMENT_WITH_CONTENT_RESPONSE_TYPE = oProps.getProperty("find_document_with_content_response_type");
            sEMULATOR_NO_PATIENT_ID_LABEL = oProps.getProperty("unknown_patientID");
            sEMULATOR_NO_LAST_NAME_LABEL = oProps.getProperty("unknown_lastName");
            sEMULATOR_NO_FIRST_NAME_LABEL = oProps.getProperty("unknown_firstName");
            sEMULATOR_NO_GENDER_LABEL = oProps.getProperty("unknown_gender");
            sEMULATOR_NO_DOB_LABEL = oProps.getProperty("unknown_dob");
            sEMULATOR_NO_PROVIDER_ID_LABEL = oProps.getProperty("unknown_providerID");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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
