/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
   public final static String DAS_C32_STYLESHEET= "das.c32.stylesheet";


   private static String pre25TemplatesSupport = "N";   // default to HITSP 2.5 templates

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
