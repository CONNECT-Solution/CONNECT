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

package gov.hhs.fha.nhinc.assemblymanager;

import gov.hhs.fha.nhinc.assemblymanager.builder.CDADocumentBuilder;
import gov.hhs.fha.nhinc.template.dao.TemplateManagerDAO;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author kim
 */
public class AssemblerFactory {

   private static Log log = LogFactory.getLog(AssemblerFactory.class);
  
   public static CDADocumentBuilder cdaBuilder(String docType) {
      List<CdaTemplate> templates;
      CDADocumentBuilder builder = null;
      try {
         templates = TemplateManagerDAO.getInstance().getSectionTemplatesForDocument(docType, true);
         log.debug(templates.size() + " templates for document type " + docType);
         
         builder = new CDADocumentBuilder(templates);
      } catch (Exception ex) {                  
         log.error("No templates located - error: " + ex.getMessage());
         builder = new CDADocumentBuilder();
      }

      builder.setDocumentType(docType);
      return builder;
   }
}
