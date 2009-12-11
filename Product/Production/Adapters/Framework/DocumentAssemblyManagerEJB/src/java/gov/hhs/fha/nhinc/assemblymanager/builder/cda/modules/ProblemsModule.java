/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.utils.DocumentIdGenerator;
import gov.hhs.fha.nhinc.template.TemplateConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CS;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040Observation;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.XActMoodDocumentObservation;
import org.hl7.v3.POCDMT000040Act;
import org.hl7.v3.XActClassDocumentEntryAct;
import org.hl7.v3.XDocumentActMood;
import org.hl7.v3.CD;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040EntryRelationship;
import org.hl7.v3.XActRelationshipEntryRelationship;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.XActRelationshipEntry;

/**
 * This module defines a patient's current medications and pertinent medication history.
 *
 * @author kim
 */
public class ProblemsModule extends ModuleImpl {

   private static Log log = LogFactory.getLog(ProblemsModule.class);

   public ProblemsModule(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse) {
      super(template, careRecordResponse);
   }

   @Override
   public List<POCDMT000040Entry> build() throws DocumentBuilderException {
      List<POCDMT000040Entry> entries = new ArrayList<POCDMT000040Entry>();

      QUPCIN043200UV01MFMIMT700712UV01Subject1 careRecord = careRecordResponse.getCareRecord();
      REPCMT004000UV01CareProvisionEvent careProvisionEvent =
              careRecord.getRegistrationEvent().getSubject2().getCareProvisionEvent();

      List<REPCMT004000UV01PertinentInformation5> problemEvents = careProvisionEvent.getPertinentInformation3();

      log.info("*******************  # of PROBLEM EVENTS: " + problemEvents.size());

      for (REPCMT004000UV01PertinentInformation5 problemEvent : problemEvents) {
         entries.add(buildProblems(problemEvent));
      }

      System.out.println("Entries:");
      for (int i = 0; i < entries.size(); i++) {
         System.out.println(entries.get(i));
      }

      // problems
      return entries;
   }

   private POCDMT000040Entry buildProblems(REPCMT004000UV01PertinentInformation5 problemEvent) {
      log.info("Inside buildProblems");

      POCDMT000040Entry problemEntry = new POCDMT000040Entry();
      problemEntry.setTypeCode(XActRelationshipEntry.DRIV);

      // Problem Entry
      POCDMT000040Act act = new POCDMT000040Act();
      act.setClassCode(XActClassDocumentEntryAct.ACT);
      act.setMoodCode(XDocumentActMood.EVN);

      // REQUIRED! Set template ids to identify that this is a problem entry
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
      REPCMT000100UV01Observation problemObservation = problemEvent.getObservation().getValue();
      //CDA
      POCDMT000040Observation actEntryRelationshipObs = new POCDMT000040Observation();
      actEntryRelationshipObs.getClassCode().add("OBS");
      actEntryRelationshipObs.setMoodCode(XActMoodDocumentObservation.EVN);

      // problem observation template identifier
      II template1 = new II();
      template1.setRoot(TemplateConstants.ccdProblemObservationTemplateId);
      template1.setAssigningAuthorityName("CCD");
      actEntryRelationshipObs.getTemplateId().add(template1);

      // unique id for this module entry
      if (problemObservation.getId().size() > 0) {
         act.getId().add(problemObservation.getId().get(0));
      } else {
         II id = new II();
         id.setExtension(DocumentIdGenerator.generateDocumentId());
         act.getId().add(id);
      }

      // status code
      CS statusCode = new CS();
      statusCode.setCode("completed");
      actEntryRelationshipObs.setStatusCode(statusCode);

      // ------------------  Problem Date  ------------------
      // needs to be report in the "low" element of effectiveTime
      if (problemObservation.getEffectiveTime().size() > 0) {
         IVLTSExplicit effectiveTime = new IVLTSExplicit();
         SXCMTSExplicit problemEffectiveTime = problemObservation.getEffectiveTime().get(0);

         IVXBTSExplicit lowVal = new IVXBTSExplicit();
         lowVal.setValue(problemEffectiveTime.getValue());
         effectiveTime.getContent().add(this.objectFactory.createIVLTSExplicitLow(lowVal));

         actEntryRelationshipObs.setEffectiveTime(effectiveTime);
      }

      // Problem Type
      actEntryRelationshipObs.setCode(problemObservation.getCode());

      //Problem Name
      actEntryRelationshipObs.setText(problemObservation.getText());

      //Problem Code
      actEntryRelationshipObs.getValue().add(problemObservation.getValue());

      //Treating Provider
      //pre.setAssignedEntity(problemObservation.getPerformer());

      actEntryRelationship.setObservation(actEntryRelationshipObs);

      act.getEntryRelationship().add(actEntryRelationship);

      problemEntry.setAct(act);

      return problemEntry;
   }
}
