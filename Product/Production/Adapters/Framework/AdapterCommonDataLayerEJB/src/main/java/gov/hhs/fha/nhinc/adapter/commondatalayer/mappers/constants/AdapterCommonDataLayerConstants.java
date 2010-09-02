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

package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants;

import java.util.Properties;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author A22387
 */
public class AdapterCommonDataLayerConstants
{
    private static Log log = LogFactory.getLog(AdapterCommonDataLayerConstants.class);

    public static final String ADAPTER_PROPERTIES_FILENAME = "adapter_common_datalayer";

    //static data switches
    public static final String ALLERGIES_TEST;
    public static final String PROBLEMS_TEST;
    public static final String PATIENT_INFO_TEST;
    public static final String MEDICATIONS_TEST;

        // Added by FHA for successful build
    public static final String EMULATOR_ALLERGIES_TAG = "ALLERGIES";
    public static final String EMULATOR_MEDS_TAG = "MEDS";
    public static final String EMULATOR_PATIENT_INFO_TAG = "PATIENT_INFO";
    public static final String EMULATOR_PROBLEMS_TAG = "PROBLEMS";
    public static final String EMULATOR_ALLERGIES_RESPONSE_TYPE = "CareRecordQUPCIN043200UV01Response";
    public static final String EMULATOR_MEDS_RESPONSE_TYPE = "CareRecordQUPCIN043200UV01Response";
    public static final String EMULATOR_PATIENT_INFO_RESPONSE_TYPE = "PatientDemographicsPRPAMT201303UV02Response";
    public static final String EMULATOR_PROBLEMS_RESPONSE_TYPE = "CareRecordQUPCIN043200UV01Response";
    public static final String EMULATOR_DATA_LOCATION;
    public static final String EMULATOR_FIND_PATIENTS_TAG = "FIND_PATIENTS";
    public static final String EMULATOR_FIND_PATIENTS_RESPONSE_TYPE ="FindPatientsPRPAMT201310UVResponse";
    public static final String EMULATOR_NO_LAST_NAME_LABEL = "UnknownLastName";
    public static final String EMULATOR_NO_FIRST_NAME_LABEL = "UnknownFirstName";
    public static final String EMULATOR_NO_GENDER_LABEL = "UnknowGender";
    public static final String EMULATOR_NO_DOB_LABEL = "UnknowDOB";
    public static final String EMULATOR_NO_PATIENT_ID_LABEL = "UnknownPatientID";

    static
    {

        String sEMULATOR_DATA_LOCATION = null;
        
        //static data switches
        String sALLERGIES_TEST = null;
        String sPROBLEMS_TEST = null;
        String sPATIENT_INFO_TEST = null;
        String sMEDICATIONS_TEST = null;

        try
        {

            //static data switches
            sALLERGIES_TEST = PropertyAccessor.getProperty(ADAPTER_PROPERTIES_FILENAME,"allergies_test");
            sPROBLEMS_TEST = PropertyAccessor.getProperty(ADAPTER_PROPERTIES_FILENAME,"problems_test");
            sMEDICATIONS_TEST = PropertyAccessor.getProperty(ADAPTER_PROPERTIES_FILENAME,"medications_test");
            sPATIENT_INFO_TEST = PropertyAccessor.getProperty(ADAPTER_PROPERTIES_FILENAME,"patient_info_test");

            sEMULATOR_DATA_LOCATION = PropertyAccessor.getProperty(ADAPTER_PROPERTIES_FILENAME, "emulator_data_location");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        EMULATOR_DATA_LOCATION = sEMULATOR_DATA_LOCATION;
        log.debug("EMULATOR_DATA_LOCATION " + EMULATOR_DATA_LOCATION);
        //static data tests
        ALLERGIES_TEST = sALLERGIES_TEST;
        PROBLEMS_TEST = sPROBLEMS_TEST;
        MEDICATIONS_TEST = sMEDICATIONS_TEST;
        PATIENT_INFO_TEST = sPATIENT_INFO_TEST;
    }
}