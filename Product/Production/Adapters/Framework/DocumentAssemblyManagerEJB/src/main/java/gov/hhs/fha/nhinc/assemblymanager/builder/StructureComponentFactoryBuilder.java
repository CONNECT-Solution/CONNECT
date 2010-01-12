/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.AllergiesSectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.CDASection;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.MedicationsSectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.ProblemsSectionImpl;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import gov.hhs.fha.nhinc.assemblymanager.service.DataService;
import gov.hhs.fha.nhinc.assemblymanager.utils.DateUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.template.TemplateConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Component3;

/**
 *
 * @author kim
 */
public class StructureComponentFactoryBuilder {

   private static Log log = LogFactory.getLog(StructureComponentFactoryBuilder.class);

   /**
    *
    * @param templateId HITSP template identifier
    * @return
    */
   public static POCDMT000040Component3 createHITSPComponent(II subjectId, CdaTemplate template) {
      String hitspTemplateId = template.getHitspTemplateId();
      CDASection sectionBuilder = null;

      String endpoint = PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.DAS_DATASERVICE_ENDPOINT, true);
      DataService dataService = new DataService(endpoint);

      CareRecordQUPCIN043200UV01ResponseType careRecord = null;

      // convert start and end dates (defined in template) from T-format to CDA format
      String cdaStartDate = null;
      String cdaEndDate = null;

      try {
         if (template.getDataDateRangeStart() != null) {
            // if end date is null, assume it's current date/time
            if (template.getDataDateRangeEnd() != null) {
               cdaStartDate = DateUtil.convertTFormatToCDATime(cdaEndDate);
            } else {
               cdaEndDate = DateUtil.convertToCDATime(new Date());
            }
         }
      } catch (Exception e) {
         log.error(e.getMessage());
         cdaStartDate = null;
         cdaEndDate = null;
      }

      // query patient registry for clinical information
      try {
         // medications section
         if (hitspTemplateId.equalsIgnoreCase(TemplateConstants.MEDICATIONS_SECTION_HITSP_TEMPLATE_ID)) {
            sectionBuilder = new MedicationsSectionImpl(template);

            // get medications data
            String careProvisionCode = "RXCAT";
            if (template.getDataStatus() == null || template.getDataStatus() == 'A') {
               //All active medications
               careProvisionCode = "CURMEDLIST";
            }

            careRecord =
                    dataService.getMedications(subjectId, careProvisionCode, cdaStartDate, cdaStartDate, endpoint);

            log.debug("******************  CARE RECORD RESPONSE for getMedications() *************");
            log.debug(XMLUtil.toPrettyXML(careRecord));

            if (careRecord != null) {
               sectionBuilder.setCareRecordResponse(careRecord);
            }
         } // allergies section
         else if (hitspTemplateId.equalsIgnoreCase(TemplateConstants.ALLERGIES_SECTION_HITSP_TEMPLATE_ID)) {
            sectionBuilder = new AllergiesSectionImpl(template);
            String careProvisionCode = "INTOLIST";
            
            // get Allergies Data
            careRecord =
                    dataService.getAllergies(subjectId, careProvisionCode, cdaStartDate, cdaStartDate, endpoint);

            log.debug("******************  CARE RECORD RESPONSE for getAllergies() *************");
            log.debug(XMLUtil.toPrettyXML(careRecord));

            if (careRecord != null) {
               sectionBuilder.setCareRecordResponse(careRecord);
            }
         }
         // problems section
         else if (hitspTemplateId.equalsIgnoreCase(TemplateConstants.PROBLEMS_SECTION_HITSP_TEMPLATE_ID)) {
            sectionBuilder = new ProblemsSectionImpl(template);
            String careProvisionCode = "PROBLIST";

            // get problem data
            careRecord = dataService.getProblems(subjectId, careProvisionCode, cdaStartDate, cdaStartDate, endpoint);

            log.debug("******************  CARE RECORD RESPONSE for getProblems() *************");
            log.debug(XMLUtil.toPrettyXML(careRecord));

            if (careRecord != null) {
               sectionBuilder.setCareRecordResponse(careRecord);
            }
         }
         // unknown section
         else {
            log.error("Template: \"" + hitspTemplateId + "\" - Not implemented yet.");
            return new POCDMT000040Component3();
         }

         return sectionBuilder.build();
      } catch (DocumentBuilderException ex) {
         log.error("Failed to build template section \"" + hitspTemplateId + "\".", ex);
         ex.printStackTrace();
         return new POCDMT000040Component3();
      }
   }
}
