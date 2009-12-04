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