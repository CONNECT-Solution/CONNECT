/*
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
    public static final String DOD_CONNECTOR_NAME;
    public static final String DOD_CONNECTOR_WSDL;

    public static final String CDL_QNAME;

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
    public static final String EMULATOR_DATA_LOCATION = "C:\\Sun\\AppServer\\domains\\domain1\\config\\nhin\\CAL_Emulator\\xml\\";
    public static final String EMULATOR_FIND_PATIENTS_TAG = "FIND_PATIENTS";
    public static final String EMULATOR_FIND_PATIENTS_RESPONSE_TYPE ="FindPatientsPRPAMT201310UVResponse";
    public static final String EMULATOR_NO_LAST_NAME_LABEL = "UnknownLastName";
    public static final String EMULATOR_NO_FIRST_NAME_LABEL = "UnknownFirstName";
    public static final String EMULATOR_NO_GENDER_LABEL = "UnknowGender";
    public static final String EMULATOR_NO_DOB_LABEL = "UnknowDOB";
    public static final String EMULATOR_NO_PATIENT_ID_LABEL = "UnknownPatientID";

    static
    {
        String sDOD_CONNECTOR_NAME = null;
        String sDOD_CONNECTOR_WSDL = null;

        String sCDL_QNAME = null;

        //static data switches
        String sALLERGIES_TEST = null;
        String sPROBLEMS_TEST = null;
        String sPATIENT_INFO_TEST = null;
        String sMEDICATIONS_TEST = null;

        try
        {
            Properties oProps = PropertyLoader.getProperties("gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.properties", "adapter_common_datalayer");

            sDOD_CONNECTOR_NAME = oProps.getProperty("dod_connector.name");
            sDOD_CONNECTOR_WSDL = oProps.getProperty("dod_connector.wsdl");
            sCDL_QNAME = oProps.getProperty("common_datalayer.qname");

            //static data switches
            sALLERGIES_TEST = oProps.getProperty("allergies_test");
            sPROBLEMS_TEST = oProps.getProperty("problems_test");
            sMEDICATIONS_TEST = oProps.getProperty("medications_test");
            sPATIENT_INFO_TEST = oProps.getProperty("patient_info_test");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        DOD_CONNECTOR_NAME = sDOD_CONNECTOR_NAME;
        DOD_CONNECTOR_WSDL = sDOD_CONNECTOR_WSDL;
        CDL_QNAME = sCDL_QNAME;

        //static data tests
        ALLERGIES_TEST = sALLERGIES_TEST;
        PROBLEMS_TEST = sPROBLEMS_TEST;
        MEDICATIONS_TEST = sMEDICATIONS_TEST;
        PATIENT_INFO_TEST = sPATIENT_INFO_TEST;
    }
}