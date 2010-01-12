/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.utils.DocumentIdGenerator;
import gov.hhs.fha.nhinc.template.TemplateConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.POCDMT000040Participant2;
import org.hl7.v3.POCDMT000040PlayingEntity;
import org.hl7.v3.POCDMT000040EntryRelationship;
import org.hl7.v3.REPCMT000100UV01SourceOf3;
import org.hl7.v3.POCDMT000040Observation;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.XActMoodDocumentObservation;
import org.hl7.v3.POCDMT000040Act;
import org.hl7.v3.XActClassDocumentEntryAct;
import org.hl7.v3.XDocumentActMood;
import org.hl7.v3.CD;
import org.hl7.v3.XActRelationshipEntryRelationship;
import org.hl7.v3.POCDMT000040ParticipantRole;
import org.hl7.v3.CS;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.REPCMT000100UV01AdministerableMaterial;
import org.hl7.v3.REPCMT000100UV01Consumable;
import org.hl7.v3.REPCMT000100UV01Material;
import org.hl7.v3.STExplicit;
import org.hl7.v3.XActRelationshipEntry;

/**
 * This module defines a patient's current medications and pertinent medication history.
 *
 *
 */
public class AllergiesModule extends ModuleImpl {

   private static Log log = LogFactory.getLog(AllergiesModule.class);

   public AllergiesModule(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse) {
      super(template, careRecordResponse);
   }

   @Override
   public List<POCDMT000040Entry> build() throws DocumentBuilderException {
      System.out.println("Inside the AllergiesModule build()");
      List<POCDMT000040Entry> entries = new ArrayList<POCDMT000040Entry>();

      QUPCIN043200UV01MFMIMT700712UV01Subject1 careRecord = careRecordResponse.getCareRecord();
      REPCMT004000UV01CareProvisionEvent careProvisionEvent =
              careRecord.getRegistrationEvent().getSubject2().getCareProvisionEvent();

      List<REPCMT004000UV01PertinentInformation5> allergyEvents = careProvisionEvent.getPertinentInformation3();

      log.info("*******************  # of Allergy EVENTS: " + allergyEvents.size());

      for (REPCMT004000UV01PertinentInformation5 allergyEvent : allergyEvents) {
         entries.add(buildAllergy(allergyEvent));
      }

      // allergy
      return entries;
   }

   private POCDMT000040Entry buildAllergy(REPCMT004000UV01PertinentInformation5 allergyEvent) {
      // CDA
      POCDMT000040Entry allergyEntry = new POCDMT000040Entry();
      allergyEntry.setTypeCode(XActRelationshipEntry.DRIV);

      POCDMT000040Act act = new POCDMT000040Act();
      act.setClassCode(XActClassDocumentEntryAct.ACT);
      act.setMoodCode(XDocumentActMood.EVN);

      // REQUIRED! Set template identifiers to identify that this is an allergy entry
      List<II> templateIdList = getConformingTemplateIds();
      for (II templateId : templateIdList) {
         act.getTemplateId().add(templateId);
      }

      CD actCode = new CD();
      actCode.getNullFlavor().add("NA");
      act.setCode(actCode);

      POCDMT000040EntryRelationship actEntryRelationship = new POCDMT000040EntryRelationship();
      actEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.SUBJ);

      //CAL
      REPCMT000100UV01Observation allergyObservation = allergyEvent.getObservation().getValue();
      //CDA
      POCDMT000040Observation actEntryRelationshipObs = new POCDMT000040Observation();
      actEntryRelationshipObs.getClassCode().add("OBS");
      actEntryRelationshipObs.setMoodCode(XActMoodDocumentObservation.EVN);

      // set template identifiers for Adverse Event Entry
      II template1 = new II();
      template1.setRoot(TemplateConstants.ccdAlertObservationTemplateId);
      template1.setAssigningAuthorityName("CCD");
      actEntryRelationshipObs.getTemplateId().add(template1);

      II template2 = new II();
      template2.setRoot(TemplateConstants.iheAlertObservationTemplateId);
      template2.setAssigningAuthorityName("IHE");
      actEntryRelationshipObs.getTemplateId().add(template2);

      // unique id for this module entry
      if (allergyObservation.getId().size() > 0) {
         act.getId().add(allergyObservation.getId().get(0));
      } else {
         II id = new II();
         id.setExtension(DocumentIdGenerator.generateDocumentId());
      }

      // ------------------  Adverse Event Type  ------------------
      CD obsCode = allergyObservation.getCode();
      actEntryRelationshipObs.setCode(obsCode);

      // ------------------  Adverse Event Date  ------------------
      // needs to be report in the "low" element of effectiveTime
      if (allergyObservation.getEffectiveTime().size() > 0) {
         IVLTSExplicit newt = new IVLTSExplicit();
         SXCMTSExplicit allergyEffectiveTime = allergyObservation.getEffectiveTime().get(0);

         IVXBTSExplicit lowVal = new IVXBTSExplicit();
         lowVal.setValue(allergyEffectiveTime.getValue());
         newt.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

         actEntryRelationshipObs.setEffectiveTime(newt);
      }

      List<REPCMT000100UV01SourceOf3> allergySourceOf3 = allergyObservation.getSourceOf();

      for (REPCMT000100UV01SourceOf3 thisAllergySourceOf3 : allergySourceOf3) {
         // check type code
         String typeCode = thisAllergySourceOf3.getTypeCode().get(0);
         if (typeCode.equalsIgnoreCase(CDAConstants.TYPE_CODE_MFST)) {
            // ------------------  Product  ------------------
            if (thisAllergySourceOf3.getSubstanceAdministration() != null) {
               log.debug("***********  ALLERGY SUBSTANCE  ***********");

               REPCMT000100UV01Consumable calConsumable = thisAllergySourceOf3.getSubstanceAdministration().getValue().getConsumable();

               if (calConsumable != null) {
                  POCDMT000040Participant2 CDAParticipant2 = new POCDMT000040Participant2();
                  CDAParticipant2.getTypeCode().add(CDAConstants.TYPE_CODE_CSM);

                  REPCMT000100UV01AdministerableMaterial calAdministerableMaterial = calConsumable.getAdministerableMaterial();
                  if (calAdministerableMaterial != null) {
                     actEntryRelationshipObs.getParticipant().add(getProduct(calConsumable.getAdministerableMaterial()));
                  }
               }
            } // ------------------  Reaction  ------------------
            else if (thisAllergySourceOf3.getObservation() != null) {
               log.debug("***********  ALLERGY REACTION OBSERVATION  ***********");

               REPCMT000100UV01Observation calAllergyObs = thisAllergySourceOf3.getObservation().getValue();

               if (calAllergyObs != null) {
                  POCDMT000040EntryRelationship reaction = getReaction(calAllergyObs);
                  if (reaction != null)
                     actEntryRelationshipObs.getEntryRelationship().add(reaction);
               }
            } else {
               log.error("Unknown allergy information with typeCode=" + typeCode);
            }
         } // ------------------  Severity  ------------------
         else if (typeCode.equalsIgnoreCase(CDAConstants.TYPE_CODE_SUBJ)) {
            if (thisAllergySourceOf3.getObservation() != null) {
               log.debug("***********  ALLERGY SEVERITY OBSERVATION  ***********");

               REPCMT000100UV01Observation calSeverityObs = thisAllergySourceOf3.getObservation().getValue();

               if (calSeverityObs != null) {
                  POCDMT000040EntryRelationship severity = getSeverity(calSeverityObs);
                  if (severity != null)
                     actEntryRelationshipObs.getEntryRelationship().add(severity);
               }
            }
         } else {
            log.error("Unknown allergy information with typeCode=" + typeCode);
         }
      }

      actEntryRelationship.setObservation(actEntryRelationshipObs);
      act.getEntryRelationship().add(actEntryRelationship);

      allergyEntry.setAct(act);

      return allergyEntry;
   }

   private POCDMT000040Participant2 getProduct(REPCMT000100UV01AdministerableMaterial administerableMaterial) {
      POCDMT000040Participant2 product = new POCDMT000040Participant2();
      if (administerableMaterial != null) {
         product.getTypeCode().add(CDAConstants.TYPE_CODE_CSM);

         REPCMT000100UV01Material calMaterial = administerableMaterial.getAdministerableMaterial();
         if (calMaterial != null) {
            POCDMT000040ParticipantRole productDetail = new POCDMT000040ParticipantRole();
            productDetail.getClassCode().add(CDAConstants.CLASS_CODE_MANU);

            POCDMT000040PlayingEntity productDetailPlayingEntity = new POCDMT000040PlayingEntity();
            productDetailPlayingEntity.getClassCode().add(CDAConstants.CLASS_CODE_MMAT);

            // Product Coded
            if (calMaterial.getCode() != null) {
               productDetailPlayingEntity.setCode(calMaterial.getCode());
            }

            // Product Free-Text
            if (calMaterial.getDesc() != null) {
               STExplicit productDesc = calMaterial.getDesc();
               if (productDesc.getContent().size() > 0) {
                  PNExplicit productFreeText = new PNExplicit();
                  productFreeText.getContent().add(productDesc.getContent().get(0));
                  productDetailPlayingEntity.getName().add(productFreeText);
               }
            }

            productDetail.setPlayingEntity(productDetailPlayingEntity);
            product.setParticipantRole(productDetail);
         }
      }

      return product;
   }

   private POCDMT000040EntryRelationship getReaction(REPCMT000100UV01Observation obs) {
      POCDMT000040EntryRelationship reactionEntryRelationship = null;

      if (obs != null) {
         reactionEntryRelationship = new POCDMT000040EntryRelationship();
         POCDMT000040Observation entryObs = new POCDMT000040Observation();

         reactionEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.MFST);
         reactionEntryRelationship.setInversionInd(true);

         // reaction observation template id
         II reactionTemplate = new II();
         reactionTemplate.setRoot(TemplateConstants.ccdReactionObservationTemplateId);
         reactionTemplate.setAssigningAuthorityName("CCD");
         entryObs.getTemplateId().add(reactionTemplate);

         // status code
         CS statusCode = new CS();
         statusCode.setCode(CDAConstants.STATUS_CODE_COMPLETED);
         entryObs.setStatusCode(statusCode);

         // Reaction Coded
         if (obs.getValue() != null) {
            entryObs.getValue().add(obs.getValue());
         }

         // Reaction Free-Text
         if (obs.getText() != null) {
            entryObs.setText(obs.getText());
         }

         reactionEntryRelationship.setObservation(entryObs);
      }

      return reactionEntryRelationship;
   }

   private POCDMT000040EntryRelationship getSeverity(REPCMT000100UV01Observation obs) {
      POCDMT000040EntryRelationship severityEntryRelationship = null;

      if (obs != null) {
         severityEntryRelationship = new POCDMT000040EntryRelationship();
         severityEntryRelationship.setTypeCode(XActRelationshipEntryRelationship.SUBJ);

         POCDMT000040Observation cdaSeverityObs = new POCDMT000040Observation();

         // severity observation template id
         II severityTemplate = new II();
         severityTemplate.setRoot(TemplateConstants.ccdSeverityObservationTemplateId);
         severityTemplate.setAssigningAuthorityName("CCD");
         cdaSeverityObs.getTemplateId().add(severityTemplate);

         // status code
         CS statusCode = new CS();
         statusCode.setCode(CDAConstants.STATUS_CODE_COMPLETED);
         cdaSeverityObs.setStatusCode(statusCode);

         // severity code
         CD severityCode = new CD();
         severityCode.setCode(CDAConstants.ACT_CODE_SEVERITY);
         severityCode.setDisplayName(CDAConstants.ACT_CODE_SEVERITY_LABEL);
         severityCode.setCodeSystem(CDAConstants.ACT_CODE_SYSTEM_OID);
         severityCode.setCodeSystemName(CDAConstants.ACT_CODE_SYSTEM);

         // Severity Coded
         if (obs.getValue() != null) {
            cdaSeverityObs.getValue().add(obs.getValue());
         }

         // Severity Free-Text
         if (obs.getText() != null) {
            cdaSeverityObs.setText(obs.getText());
         }

         severityEntryRelationship.setObservation(cdaSeverityObs);
      }

      return severityEntryRelationship;
   }
}
