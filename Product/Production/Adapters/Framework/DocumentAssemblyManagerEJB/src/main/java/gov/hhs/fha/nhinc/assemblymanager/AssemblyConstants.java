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
package gov.hhs.fha.nhinc.assemblymanager;

import java.util.Properties;
import gov.hhs.fha.nhinc.assemblymanager.properties.PropertyLoader;

/**
 *
 * @author kim
 */
public class AssemblyConstants {

    public static final String DAS_DATASERVICE_ENDPOINT;
    public static final String DAS_DEMO;
    public static final String DAS_DEMO_DATA_DIRECTORY;
    public static final String DAS_DEMO_DOC_DATE;
    public static final String DAS_DEMO_PATIENT;
    public static final String DAS_DOCMGRSERVICE_ENDPOINT;

    public static final String DAS_HITSP_PRE25_TEMPLATES;
    public static final String DAS_PERSISTENCE_UNIT;
    public static final String DAS_TERMINOLOGY_ENDPOINT;
    public static final String DAS_TEST;
    public static final String DAS_VERSION;

    public static final String HL7_DELIMITER;
    public static final String LANGUAGE;

    public static final String ORGANIZATION_OID;
    public static final String ORGANIZATION_NAME;

    // DISPLAY NAMES
    public static final String NDC_CODE_SYSTEM_DISPLAY_NAME;
    public static final String SNOMED_CT_CODE_SYS_DISPLAY_NAME;
    public static final String LOINC_STATUS_CODE_DISPLAY_NAME;
    public static final String ICD9_CODE_SYS_DISPLAY_NAME;

    // C32 Specific Properties
    public static final String C32_DISPLAY_NAME;
    public static final String C32_CLASS_CODE;
    public static final String C32_STYLESHEET;
    public static final String C32_ORGANIZATION_SYS;
    public static final String C32_ORGANIZATION_SYS_TYPE;
    public static final String C32_CONFIDENTIAL_CODE;
    public static final String C32_CONFIDENTIAL_CODE_DESCR;
    public static final String C32_FORMAT_CODE;
    public static final String C32_FORMAT_CODE_DESCR;
    public static final String C32_HCFT_CODE;
    public static final String C32_HCFT_CODE_DESCR;
    public static final String C32_ICD9_CODE_SYS_NAME;
    public static final String C32_NDC_CODE_SYS_NAME;
    public static final String C32_PRACTICE_SETTING_CODE;
    public static final String C32_PRACTICE_SETTING_CODE_DESCR;
    public static final String C32_PRACTICE_SETTING_CODE_SYS_NAME;
    public static final String C32_RXNORM_CODE_SYS_NAME;
    public static final String C32_SNOMED_CODE_SYS_NAME;
    public static final String C32_VERSION;

    // C62 Specific Properties
    public static final String C62_DISPLAY_NAME;
    public static final String C62_CLASS_CODE;
    public static final String C62_ORGANIZATION_SYS;
    public static final String C62_ORGANIZATION_SYS_TYPE;
    public static final String C62_CONFIDENTIAL_CODE;
    public static final String C62_CONFIDENTIAL_CODE_DESCR;
    public static final String C62_FORMAT_CODE;
    public static final String C62_FORMAT_CODE_DESCR;
    public static final String C62_HCFT_CODE;
    public static final String C62_HCFT_CODE_DESCR;
    public static final String C62_ICD9_CODE_SYS_NAME;
    public static final String C62_NDC_CODE_SYS_NAME;
    public static final String C62_PRACTICE_SETTING_CODE;
    public static final String C62_PRACTICE_SETTING_CODE_DESCR;
    public static final String C62_PRACTICE_SETTING_CODE_SYS_NAME;
    public static final String C62_RXNORM_CODE_SYS_NAME;
    public static final String C62_SNOMED_CODE_SYS_NAME;
    public static final String C62_VERSION;

    // C62 Specific Properties - Discharge Summary only
    public static final String C62_DS_DISPLAY_NAME;
    public static final String C62_DS_HCFT_CODE;
    public static final String C62_DS_HCFT_CODE_DESCR;
    public static final String C62_DS_PRACTICE_SETTING_CODE;
    public static final String C62_DS_PRACTICE_SETTING_CODE_DESCR;

   public static final String DAS_PU_VALUE = "docassemblyPU";
   public static final String TEMPLATE_MANAGER_PU = "TemplateManagerPU";
   public static final String CARE_RECORD_QUERY_INTERACTION_ID = "QUPC_IN043100UV";
   public static final String CARE_RECORD_QUERY_TRIGGER = "QUPC_TE043100UV01";
   public static final String CDL_SERVICE = "Common Data Layer Service";
   public static final String ADAS_SERVICE = "Adapter Document Assembly Service";

   public static final String REPOSITORY_PROPERTY_FILE = "repository";
   public static final String DOCUMENT_UNIQUE_OID_PROP = "documentUniqueOID";

   private static String pre25TemplatesSupport = "N";   // default to HITSP 2.5 templates

   public static boolean usePre25Templates() {
      pre25TemplatesSupport = AssemblyConstants.DAS_HITSP_PRE25_TEMPLATES;
      
      // default to HITSP 2.5 templates
      if (pre25TemplatesSupport == null || pre25TemplatesSupport.length() == 0) {
         return false;
      }

      if (pre25TemplatesSupport.equalsIgnoreCase("Y")) {
         return true;
      }

      return false;
   }

   static
   {
        String sDAS_DATASERVICE_ENDPOINT = null;
        String sDAS_DEMO = null;
        String sDAS_DEMO_DATA_DIRECTORY = null;
        String sDAS_DEMO_DOC_DATE = null;
        String sDAS_DEMO_PATIENT = null;
        String sDAS_DOCMGRSERVICE_ENDPOINT = null;
        String sDAS_HITSP_PRE25_TEMPLATES = null;
        String sDAS_PERSISTENCE_UNIT = null;
        String sDAS_TERMINOLOGY_ENDPOINT = null;
        String sDAS_TEST = null;
        String sDAS_VERSION = null;

        String sHL7_DELIMITER = null;
        String sLANGUAGE = null;

        String sORGANIZATION_OID = null;
        String sORGANIZATION_NAME = null;

        // DISPLAY NAMES
        String sNDC_CODE_SYSTEM_DISPLAY_NAME = null;
        String sSNOMED_CT_CODE_SYS_DISPLAY_NAME = null;
        String sLOINC_STATUS_CODE_DISPLAY_NAME = null;
        String sICD9_CODE_SYS_DISPLAY_NAME = null;

        // C32 Specific Properties
        String sC32_DISPLAY_NAME = null;
        String sC32_CLASS_CODE = null;
        String sC32_STYLESHEET = null;
        String sC32_ORGANIZATION_SYS = null;
        String sC32_ORGANIZATION_SYS_TYPE = null;
        String sC32_CONFIDENTIAL_CODE = null;
        String sC32_CONFIDENTIAL_CODE_DESCR = null;
        String sC32_FORMAT_CODE = null;
        String sC32_FORMAT_CODE_DESCR = null;
        String sC32_HCFT_CODE = null;
        String sC32_HCFT_CODE_DESCR = null;
        String sC32_ICD9_CODE_SYS_NAME = null;
        String sC32_NDC_CODE_SYS_NAME = null;
        String sC32_PRACTICE_SETTING_CODE = null;
        String sC32_PRACTICE_SETTING_CODE_DESCR = null;
        String sC32_PRACTICE_SETTING_CODE_SYS_NAME = null;
        String sC32_RXNORM_CODE_SYS_NAME = null;
        String sC32_SNOMED_CODE_SYS_NAME = null;
        String sC32_VERSION = null;

        // C62 Specific Properties
        String sC62_DISPLAY_NAME = null;
        String sC62_CLASS_CODE = null;
        String sC62_ORGANIZATION_SYS = null;
        String sC62_ORGANIZATION_SYS_TYPE = null;
        String sC62_CONFIDENTIAL_CODE = null;
        String sC62_CONFIDENTIAL_CODE_DESCR = null;
        String sC62_FORMAT_CODE = null;
        String sC62_FORMAT_CODE_DESCR = null;
        String sC62_HCFT_CODE = null;
        String sC62_HCFT_CODE_DESCR = null;
        String sC62_ICD9_CODE_SYS_NAME = null;
        String sC62_NDC_CODE_SYS_NAME = null;
        String sC62_PRACTICE_SETTING_CODE = null;
        String sC62_PRACTICE_SETTING_CODE_DESCR = null;
        String sC62_PRACTICE_SETTING_CODE_SYS_NAME = null;
        String sC62_RXNORM_CODE_SYS_NAME = null;
        String sC62_SNOMED_CODE_SYS_NAME = null;
        String sC62_VERSION = null;

        // C62 Specific Properties - Discharge Summaries only
        String sC62_DS_DISPLAY_NAME = null;
        String sC62_DS_HCFT_CODE = null;
        String sC62_DS_HCFT_CODE_DESCR = null;
        String sC62_DS_PRACTICE_SETTING_CODE = null;
        String sC62_DS_PRACTICE_SETTING_CODE_DESCR = null;

        try
        {
            Properties oProps = PropertyLoader.getProperties("properties", "docassembly");

            sDAS_DATASERVICE_ENDPOINT = oProps.getProperty("DAS_DATASERVICE_ENDPOINT");
            sDAS_DEMO = oProps.getProperty("DAS_DEMO");
            sDAS_DEMO_DATA_DIRECTORY = oProps.getProperty("DAS_DEMO_DATA_DIRECTORY");
            sDAS_DEMO_DOC_DATE = oProps.getProperty("DAS_DEMO_DOC_DATE");
            sDAS_DEMO_PATIENT = oProps.getProperty("DAS_DEMO_PATIENT");
            sDAS_DOCMGRSERVICE_ENDPOINT = oProps.getProperty("DAS_DOCMGRSERVICE_ENDPOINT");
            sDAS_HITSP_PRE25_TEMPLATES = oProps.getProperty("DAS_HITSP_PRE25_TEMPLATES");
            sDAS_PERSISTENCE_UNIT = oProps.getProperty("DAS_PERSISTENCE_UNIT");
            sDAS_TERMINOLOGY_ENDPOINT = oProps.getProperty("DAS_TERMINOLOGY_ENDPOINT");
            sDAS_TEST = oProps.getProperty("DAS_TEST");
            sDAS_VERSION = oProps.getProperty("DAS_VERSION");

            sHL7_DELIMITER = oProps.getProperty("HL7_DELIMITER");
            sLANGUAGE = oProps.getProperty("LANGUAGE");

            sORGANIZATION_OID = oProps.getProperty("ORGANIZATION_OID");
            sORGANIZATION_NAME = oProps.getProperty("ORGANIZATION_NAME");

            // DISPLKAY NAMES
            sNDC_CODE_SYSTEM_DISPLAY_NAME = oProps.getProperty("NDC_CODE_SYSTEM_DISPLAY_NAME");
            sSNOMED_CT_CODE_SYS_DISPLAY_NAME = oProps.getProperty("SNOMED_CT_CODE_SYS_DISPLAY_NAME");
            sLOINC_STATUS_CODE_DISPLAY_NAME = oProps.getProperty("LOINC_STATUS_CODE_DISPLAY_NAME");
            sICD9_CODE_SYS_DISPLAY_NAME = oProps.getProperty("ICD9_CODE_SYS_DISPLAY_NAME");

            // C32 Specific Properties
            sC32_DISPLAY_NAME = oProps.getProperty("C32_DISPLAY_NAME");
            sC32_CLASS_CODE = oProps.getProperty("C32_CLASS_CODE");
            sC32_STYLESHEET = oProps.getProperty("C32_STYLESHEET");
            sC32_ORGANIZATION_SYS = oProps.getProperty("C32_ORGANIZATION_SYS");
            sC32_ORGANIZATION_SYS_TYPE = oProps.getProperty("C32_ORGANIZATION_SYS_TYPE");
            sC32_CONFIDENTIAL_CODE = oProps.getProperty("C32_CONFIDENTIAL_CODE");
            sC32_CONFIDENTIAL_CODE_DESCR = oProps.getProperty("C32_CONFIDENTIAL_CODE_DESCR");
            sC32_FORMAT_CODE = oProps.getProperty("C32_FORMAT_CODE");
            sC32_FORMAT_CODE_DESCR = oProps.getProperty("C32_FORMAT_CODE_DESCR");
            sC32_HCFT_CODE = oProps.getProperty("C32_HCFT_CODE");
            sC32_HCFT_CODE_DESCR = oProps.getProperty("C32_HCFT_CODE_DESCR");
            sC32_ICD9_CODE_SYS_NAME = oProps.getProperty("C32_ICD9_CODE_SYS_NAME");
            sC32_NDC_CODE_SYS_NAME = oProps.getProperty("C32_NDC_CODE_SYS_NAME");
            sC32_PRACTICE_SETTING_CODE = oProps.getProperty("C32_PRACTICE_SETTING_CODE");
            sC32_PRACTICE_SETTING_CODE_DESCR = oProps.getProperty("C32_PRACTICE_SETTING_CODE_DESCR");
            sC32_PRACTICE_SETTING_CODE_SYS_NAME = oProps.getProperty("C32_PRACTICE_SETTING_CODE_SYS_NAME");
            sC32_RXNORM_CODE_SYS_NAME = oProps.getProperty("C32_RXNORM_CODE_SYS_NAME");
            sC32_SNOMED_CODE_SYS_NAME = oProps.getProperty("C32_SNOMED_CODE_SYS_NAME");
            sC32_VERSION = oProps.getProperty("C32_VERSION");

            // C62 Specific Properties
            sC62_DISPLAY_NAME = oProps.getProperty("C62_DISPLAY_NAME");
            sC62_CLASS_CODE = oProps.getProperty("C62_CLASS_CODE");
            sC62_ORGANIZATION_SYS = oProps.getProperty("C62_ORGANIZATION_SYS");
            sC62_ORGANIZATION_SYS_TYPE = oProps.getProperty("C62_ORGANIZATION_SYS_TYPE");
            sC62_CONFIDENTIAL_CODE = oProps.getProperty("C62_CONFIDENTIAL_CODE");
            sC62_CONFIDENTIAL_CODE_DESCR = oProps.getProperty("C62_CONFIDENTIAL_CODE_DESCR");
            sC62_FORMAT_CODE = oProps.getProperty("C62_FORMAT_CODE");
            sC62_FORMAT_CODE_DESCR = oProps.getProperty("C62_FORMAT_CODE_DESCR");
            sC62_HCFT_CODE = oProps.getProperty("C62_HCFT_CODE");
            sC62_HCFT_CODE_DESCR = oProps.getProperty("C62_HCFT_CODE_DESCR");
            sC62_ICD9_CODE_SYS_NAME = oProps.getProperty("C62_ICD9_CODE_SYS_NAME");
            sC62_NDC_CODE_SYS_NAME = oProps.getProperty("C62_NDC_CODE_SYS_NAME");
            sC62_PRACTICE_SETTING_CODE = oProps.getProperty("C62_PRACTICE_SETTING_CODE");
            sC62_PRACTICE_SETTING_CODE_DESCR = oProps.getProperty("C62_PRACTICE_SETTING_CODE_DESCR");
            sC62_PRACTICE_SETTING_CODE_SYS_NAME = oProps.getProperty("C62_PRACTICE_SETTING_CODE_SYS_NAME");
            sC62_RXNORM_CODE_SYS_NAME = oProps.getProperty("C62_RXNORM_CODE_SYS_NAME");
            sC62_SNOMED_CODE_SYS_NAME = oProps.getProperty("C62_SNOMED_CODE_SYS_NAME");
            sC62_VERSION = oProps.getProperty("C62_VERSION");

            // C62 Specific Properties - Discharge Summaries only
            sC62_DS_DISPLAY_NAME = oProps.getProperty("C62_DS_DISPLAY_NAME");
            sC62_DS_HCFT_CODE = oProps.getProperty("C62_DS_HCFT_CODE");
            sC62_DS_HCFT_CODE_DESCR = oProps.getProperty("C62_DS_HCFT_CODE_DESCR");
            sC62_DS_PRACTICE_SETTING_CODE = oProps.getProperty("C62_DS_PRACTICE_SETTING_CODE");
            sC62_DS_PRACTICE_SETTING_CODE_DESCR = oProps.getProperty("C62_DS_PRACTICE_SETTING_CODE_DESCR");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        DAS_DATASERVICE_ENDPOINT = sDAS_DATASERVICE_ENDPOINT;
        DAS_DEMO = sDAS_DEMO;
        DAS_DEMO_DATA_DIRECTORY = sDAS_DEMO_DATA_DIRECTORY;
        DAS_DEMO_DOC_DATE = sDAS_DEMO_DOC_DATE;
        DAS_DEMO_PATIENT = sDAS_DEMO_PATIENT;
        DAS_DOCMGRSERVICE_ENDPOINT = sDAS_DOCMGRSERVICE_ENDPOINT;
        DAS_HITSP_PRE25_TEMPLATES = sDAS_HITSP_PRE25_TEMPLATES;
        DAS_PERSISTENCE_UNIT = sDAS_PERSISTENCE_UNIT;
        DAS_TERMINOLOGY_ENDPOINT = sDAS_TERMINOLOGY_ENDPOINT;
        DAS_TEST = sDAS_TEST;
        DAS_VERSION = sDAS_VERSION;

        HL7_DELIMITER = sHL7_DELIMITER;
        LANGUAGE = sLANGUAGE;

        ORGANIZATION_OID = sORGANIZATION_OID;
        ORGANIZATION_NAME = sORGANIZATION_NAME;

        // Display Names
        NDC_CODE_SYSTEM_DISPLAY_NAME = sNDC_CODE_SYSTEM_DISPLAY_NAME;
        SNOMED_CT_CODE_SYS_DISPLAY_NAME = sSNOMED_CT_CODE_SYS_DISPLAY_NAME;
        LOINC_STATUS_CODE_DISPLAY_NAME = sLOINC_STATUS_CODE_DISPLAY_NAME;
        ICD9_CODE_SYS_DISPLAY_NAME = sICD9_CODE_SYS_DISPLAY_NAME;

        // C32 Specific Properties
        C32_DISPLAY_NAME = sC32_DISPLAY_NAME;
        C32_CLASS_CODE = sC32_CLASS_CODE;
        C32_STYLESHEET = sC32_STYLESHEET;
        C32_ORGANIZATION_SYS = sC32_ORGANIZATION_SYS;
        C32_ORGANIZATION_SYS_TYPE = sC32_ORGANIZATION_SYS_TYPE;
        C32_CONFIDENTIAL_CODE = sC32_CONFIDENTIAL_CODE;
        C32_CONFIDENTIAL_CODE_DESCR = sC32_CONFIDENTIAL_CODE_DESCR;
        C32_FORMAT_CODE = sC32_FORMAT_CODE;
        C32_FORMAT_CODE_DESCR = sC32_FORMAT_CODE_DESCR;
        C32_HCFT_CODE = sC32_HCFT_CODE;
        C32_HCFT_CODE_DESCR = sC32_HCFT_CODE_DESCR;
        C32_ICD9_CODE_SYS_NAME = sC32_ICD9_CODE_SYS_NAME;
        C32_NDC_CODE_SYS_NAME = sC32_NDC_CODE_SYS_NAME;
        C32_PRACTICE_SETTING_CODE = sC32_PRACTICE_SETTING_CODE;
        C32_PRACTICE_SETTING_CODE_DESCR = sC32_PRACTICE_SETTING_CODE_DESCR;
        C32_PRACTICE_SETTING_CODE_SYS_NAME = sC32_PRACTICE_SETTING_CODE_SYS_NAME;
        C32_RXNORM_CODE_SYS_NAME = sC32_RXNORM_CODE_SYS_NAME;
        C32_SNOMED_CODE_SYS_NAME = sC32_SNOMED_CODE_SYS_NAME;
        C32_VERSION = sC32_VERSION;

        // C62 Specific Properties
        C62_DISPLAY_NAME = sC62_DISPLAY_NAME;
        C62_CLASS_CODE = sC62_CLASS_CODE;
        C62_ORGANIZATION_SYS = sC62_ORGANIZATION_SYS;
        C62_ORGANIZATION_SYS_TYPE = sC62_ORGANIZATION_SYS_TYPE;
        C62_CONFIDENTIAL_CODE = sC62_CONFIDENTIAL_CODE;
        C62_CONFIDENTIAL_CODE_DESCR = sC62_CONFIDENTIAL_CODE_DESCR;
        C62_FORMAT_CODE = sC62_FORMAT_CODE;
        C62_FORMAT_CODE_DESCR = sC62_FORMAT_CODE_DESCR;
        C62_HCFT_CODE = sC62_HCFT_CODE;
        C62_HCFT_CODE_DESCR = sC62_HCFT_CODE_DESCR;
        C62_ICD9_CODE_SYS_NAME = sC62_ICD9_CODE_SYS_NAME;
        C62_NDC_CODE_SYS_NAME = sC62_NDC_CODE_SYS_NAME;
        C62_PRACTICE_SETTING_CODE = sC62_PRACTICE_SETTING_CODE;
        C62_PRACTICE_SETTING_CODE_DESCR = sC62_PRACTICE_SETTING_CODE_DESCR;
        C62_PRACTICE_SETTING_CODE_SYS_NAME = sC62_PRACTICE_SETTING_CODE_SYS_NAME;
        C62_RXNORM_CODE_SYS_NAME = sC62_RXNORM_CODE_SYS_NAME;
        C62_SNOMED_CODE_SYS_NAME = sC62_SNOMED_CODE_SYS_NAME;
        C62_VERSION = sC62_VERSION;

        // C62 Specific Properties - Discharge Summaries only
        C62_DS_DISPLAY_NAME = sC62_DS_DISPLAY_NAME;
        C62_DS_HCFT_CODE = sC62_DS_HCFT_CODE;
        C62_DS_HCFT_CODE_DESCR = sC62_DS_HCFT_CODE_DESCR;
        C62_DS_PRACTICE_SETTING_CODE = sC62_DS_PRACTICE_SETTING_CODE;
        C62_DS_PRACTICE_SETTING_CODE_DESCR = sC62_DS_PRACTICE_SETTING_CODE_DESCR;
   }
}
