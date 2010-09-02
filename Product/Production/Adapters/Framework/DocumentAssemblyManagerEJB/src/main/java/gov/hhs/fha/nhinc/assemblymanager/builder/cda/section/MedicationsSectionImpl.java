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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.section;

import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.builder.ModuleFactoryBuilder;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.List;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040Section;
import org.hl7.v3.STExplicit;


/**
 * This class performs the necessary mappings from CareRecordQUPCIN043200UV01ResponseType
 * to POCDMT000040Component3.
 * 
 * @author kim
 */
public class MedicationsSectionImpl extends SectionImpl {

   public MedicationsSectionImpl(CdaTemplate template) {
      super(template);
   }

   @Override
   public POCDMT000040Component3 build() throws DocumentBuilderException {

      POCDMT000040Section rxSection = new POCDMT000040Section();

      // REQUIRED! Set template ids to identify that this is a medication section
      List<II> templateIdList = getConformingTemplateIds();
      for (II templateId : templateIdList) {
         rxSection.getTemplateId().add(templateId);
      }

      // REQUIRED! Must have this also: <code code="10160-0" codeSystem="2.16.840.1.113883.6.1"/>
      CE loincMedCode = new CE();
      loincMedCode.setCode(CDAConstants.LOINC_MED_CODE);
      loincMedCode.setCodeSystem(CDAConstants.LOINC_CODE_SYS_OID);
      rxSection.setCode(loincMedCode);

      // REQUIRED! Set title for display
//      try {
//         Element title = XMLUtil.createElement(TITLE_TAG);
//         title.setTextContent(CDAConstants.MED_SECTION_TITLE);
          STExplicit title = new STExplicit();
          title.getContent().add(CDAConstants.ALLERGIES_SECTION_TITLE);
         rxSection.setTitle(title);
//      } catch (Exception e) {
//         log.error("Failed to set POCDMT000040Section.title", e);
//      }

      // build the relevant module entries
      log.debug("********  # of MEDICATION MODULES: " + moduleTemplates.size());

      if (moduleTemplates.size() > 0) {
         CdaTemplate moduleTemplate = moduleTemplates.get(0);

         log.debug(moduleTemplate);

         List<POCDMT000040Entry> entries = ModuleFactoryBuilder.createModule(moduleTemplate, careRecordResponse, this);
         for (POCDMT000040Entry entry : entries) {
            rxSection.getEntry().add(entry);
         }
      }

      getSectionComponent().setSection(rxSection);

      return getSectionComponent();
   }
}
