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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040Section;
import org.hl7.v3.StrucDocContent;
import org.hl7.v3.StrucDocParagraph;
import org.w3c.dom.Element;
import org.hl7.v3.STExplicit;

/**
 * This class performs the necessary mappings from CareRecordQUPCIN043200UV01ResponseType
 * to POCDMT000040Component3.
 *
 * @author kim
 */
public class ProblemsSectionImpl extends SectionImpl {

   private int problemIndex = -1;
   private Map<String, String> problems = null;

   public ProblemsSectionImpl(CdaTemplate template) {
      super(template);
      problemIndex = 1;
   }

   public Map<String, String> getProblems() {
      if (problems == null) {
         problems = new HashMap<String, String>();
      }

      return problems;
   }

   public int getProblemIndex() {
      if (problemIndex == -1) {
         problemIndex = 1;
      }

      return problemIndex++;
   }

   @Override
   public POCDMT000040Component3 build() throws DocumentBuilderException {

      POCDMT000040Section probSection = new POCDMT000040Section();

      // REQUIRED! Set template ids to identify that this is a medication section
      List<II> templateIdList = getConformingTemplateIds();
      for (II templateId : templateIdList) {
         probSection.getTemplateId().add(templateId);
      }

      // REQUIRED! Must have this also: <code code="48765-2" codeSystem="2.16.840.1.113883.6.1"/>
      CE loincAllergyCode = new CE();
      loincAllergyCode.setCode(CDAConstants.LOINC_PROBLEM_CODE);
      loincAllergyCode.setCodeSystem(CDAConstants.LOINC_CODE_SYS_OID);
      probSection.setCode(loincAllergyCode);

      // REQUIRED! Set title for display
//      try {
//         Element title = XMLUtil.createElement(CDAConstants.TITLE_TAG);
//         title.setTextContent(CDAConstants.PROBLEMS_SECTION_TITLE);
//         probSection.setTitle(title);
//      } catch (Exception e) {
//         log.error("Failed to set POCDMT000040Section.title", e);
//      }
       STExplicit title = new STExplicit();
       title.getContent().add(CDAConstants.PROBLEMS_SECTION_TITLE);
       probSection.setTitle(title);

      // build the relevant module entries
      log.debug("*******************  # of PROBLEM MODULE: " + moduleTemplates.size());

      if (moduleTemplates.size() > 0) {
         CdaTemplate moduleTemplate = moduleTemplates.get(0);

         log.debug(moduleTemplate);

         List<POCDMT000040Entry> entries = ModuleFactoryBuilder.createModule(moduleTemplate, careRecordResponse, this);
         for (POCDMT000040Entry entry : entries) {
            probSection.getEntry().add(entry);
         }
      }

      // set problem references (if any)
      try {
         if (getProblems().size() > 0) {
            Element textElement = XMLUtil.createElement(CDAConstants.TEXT_TAG);
            StringBuffer contentBuffer = new StringBuffer();
            Set<String> keys = null;

            // build problem references, i.e. <content ID='reaction-1'>some reaction</content>
            keys = getProblems().keySet();
            StrucDocParagraph problemTextParagraph = null;
            for (String key : keys) {
               problemTextParagraph = objectFactory.createStrucDocParagraph();
               problemTextParagraph.setID(key);
               problemTextParagraph.getContent().add(getProblems().get(key));
               contentBuffer.append(XMLUtil.toCanonicalXML(objectFactory.createStrucDocTextParagraph(problemTextParagraph)));
            }

            if (contentBuffer.toString().length() > 1) {
               textElement.setTextContent(contentBuffer.toString());
               probSection.setText(textElement);
            }
         }
      } catch (Exception e) {
         log.error("Failed to set Problem Section.text", e);
      }

      getSectionComponent().setSection(probSection);

      return getSectionComponent();
   }
}
