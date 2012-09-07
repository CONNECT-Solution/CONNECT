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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.section;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.template.dao.TemplateManagerDAO;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.POCDMT000040Component3;

/**
 *
 * @author kim
 */
public abstract class SectionImpl implements CDASection {

   protected static Log log = LogFactory.getLog(SectionImpl.class);
   //public static final String TITLE_TAG = "title";
   protected CdaTemplate template = null;
   protected List<CdaTemplate> moduleTemplates = null;
   protected POCDMT000040Component3 sectionComponent = null;
   protected CareRecordQUPCIN043200UV01ResponseType careRecordResponse = null;
   protected ObjectFactory objectFactory = null;

   public SectionImpl(CdaTemplate sectionTemplate) {
      this.template = sectionTemplate;
      if (objectFactory == null) {
         objectFactory = new ObjectFactory();
      }

      initialize();
   }

   public CdaTemplate getSectionTemplate() {
      return template;
   }

   public List<CdaTemplate> getModuleTemplates() {
      if (moduleTemplates == null) {
         moduleTemplates = new ArrayList<CdaTemplate>();
      }

      return moduleTemplates;
   }

   public POCDMT000040Component3 getSectionComponent() {
      if (sectionComponent == null) {
         sectionComponent = new POCDMT000040Component3();
      }

      return sectionComponent;
   }

   public CareRecordQUPCIN043200UV01ResponseType getCareRecordResponse() {
      return careRecordResponse;
   }

   @Override
   public void setCareRecordResponse(CareRecordQUPCIN043200UV01ResponseType careRecordResponse) {
      this.careRecordResponse = careRecordResponse;
   }

   // defer to implementer calss
   @Override
   public abstract POCDMT000040Component3 build() throws DocumentBuilderException;

   private void initialize() {
      try {
         moduleTemplates =
                 TemplateManagerDAO.getInstance().getModuleTemplatesForSection(template.getId(), true);
      } catch (Exception ex) {
         log.error(ex);
      }
   }

   protected List<II> getConformingTemplateIds() {
      List<II> templateIdList = new ArrayList<II>();

      if (!AssemblyConstants.usePre25Templates()) {
         if (template.getHitspTemplateId() != null &&
                 template.getHitspTemplateId().length() > 0) {
            II templateId1 = new II();
            templateId1.setRoot(template.getHitspTemplateId());
            templateId1.setAssigningAuthorityName("HITSP/C83");
            templateIdList.add(templateId1);
         }

         // may have to remove -- caused VA + Kaiser stylesheets to render section twice???
         if (template.getIheTemplateId() != null &&
                 template.getIheTemplateId().length() > 0) {
            II templateId2 = new II();
            templateId2.setRoot(template.getIheTemplateId());
            templateId2.setAssigningAuthorityName("IHE");
            templateIdList.add(templateId2);
         }
      }

      if (template.getCdaTemplateId() != null &&
              template.getCdaTemplateId().length() > 0) {
         II templateId3 = new II();
         templateId3.setRoot(template.getCdaTemplateId());
         templateId3.setAssigningAuthorityName("CCD");
         templateIdList.add(templateId3);
      }

      return templateIdList;
   }
}
