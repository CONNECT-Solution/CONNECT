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
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;

import java.math.BigInteger;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.COCTMT030000UV04Person;
import org.hl7.v3.COCTMT050000UV01Patient;
import org.hl7.v3.COCTMT090003UVAssignedEntity;
import org.hl7.v3.CS;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType; // .CareRecordREPCMT000400UV01ResponseType;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.MFMIMT700712UV01Custodian;
import org.hl7.v3.PQ;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCMT040300UV01ParameterList;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject5;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.REPCMT000100UV01ObservationRange;
import org.hl7.v3.REPCMT000100UV01ReferenceRange2;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.REPCMT004000UV01RecordTarget;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.XClinicalStatementObservationMood;

/**
 *
 * @author tmn
 */
public class StaticTestResultsQuery {

   private static Log log = LogFactory.getLog(StaticTestResultsQuery.class);
   private static org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

   public static CareRecordQUPCIN043200UV01ResponseType createTestResultsResponse(CareRecordQUPCIN043100UV01RequestType request) {

      CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();

      QUPCIN043100UV01QUQIMT020001UV01ControlActProcess query = request.getQuery().getControlActProcess();
      QUPCIN043100UV01QUQIMT020001UV01QueryByParameter queryByParam = query.getQueryByParameter().getValue();
      List<QUPCMT040300UV01ParameterList> paramList = queryByParam.getParameterList();

      String reqPatientID = paramList.get(0).getPatientId().getValue().getExtension();

      response.setCareRecord(createSubject(reqPatientID));

      return response;
   }

   private static QUPCIN043200UV01MFMIMT700712UV01Subject1 createSubject(String reqPatientID) {

      QUPCIN043200UV01MFMIMT700712UV01Subject1 subject = new QUPCIN043200UV01MFMIMT700712UV01Subject1();
      subject.getTypeCode().add("SUBJ");

      QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent value = new QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent();
      value.getClassCode().add("REG");
      value.getMoodCode().add("EVN");

      CS statusCode = new CS();
      statusCode.setCode("active");
      value.setStatusCode(statusCode);

      // ---- SETTING Clinical Data Source ----------------
      value.setCustodian(createCustodianData("CST"));

      // --- SETTING data content requested from query -------
      value.setSubject2(createSubject2(reqPatientID));

      // --- FINAL ---
      subject.setRegistrationEvent(value);
      return subject;
   }

   private static QUPCIN043200UV01MFMIMT700712UV01Subject5 createSubject2(String reqPatientID) {

      QUPCIN043200UV01MFMIMT700712UV01Subject5 subject2 = new QUPCIN043200UV01MFMIMT700712UV01Subject5();
      subject2.getTypeCode().add("SUBJ");

      REPCMT004000UV01CareProvisionEvent careProvisEvent = new REPCMT004000UV01CareProvisionEvent();
      careProvisEvent.setClassCode(org.hl7.v3.ActClassCareProvision.ENC);
      careProvisEvent.getMoodCode().add("EVN");

      // -------- RECORD TARGET ------------------------------------
      careProvisEvent.setRecordTarget(factory.createREPCMT004000UV01CareProvisionEventRecordTarget(createRecTarget(reqPatientID)));

      // -------- DOMAIN CONTENT ------------------------------------
      REPCMT004000UV01PertinentInformation5 p3 = new REPCMT004000UV01PertinentInformation5();
      p3.getTypeCode().add("SUBJ");
      p3.setContextControlCode("OP");
      BigInteger seqValue = new BigInteger("1");
      org.hl7.v3.INT seq = new INT();
      seq.setValue(seqValue);
      p3.setSequenceNumber(seq);
      p3.setObservation(factory.createREPCMT004000UV01PertinentInformation5Observation(createObservations()));

      // ------------------------------
      careProvisEvent.getPertinentInformation3().add(p3);
      subject2.setCareProvisionEvent(careProvisEvent);

      return subject2;

   }

   private static MFMIMT700712UV01Custodian createCustodianData(String typeCode) {

      MFMIMT700712UV01Custodian custodianValue = new MFMIMT700712UV01Custodian();
      custodianValue.getTypeCode().add(typeCode);

      COCTMT090003UVAssignedEntity assignedEnt = new COCTMT090003UVAssignedEntity();
      assignedEnt.setClassCode("ASSIGNED");
      II assignedEntID = new II();
      assignedEntID.setRoot("1.1");
      assignedEntID.setExtension("AHLTA");
      assignedEnt.getId().add(assignedEntID);
      custodianValue.setAssignedEntity(assignedEnt);

      return custodianValue;
   }

   private static REPCMT004000UV01RecordTarget createRecTarget(String reqPatientID) {

      REPCMT004000UV01RecordTarget recTarget = new REPCMT004000UV01RecordTarget();

      recTarget.getTypeCode().add("RCT");

      COCTMT050000UV01Patient patient = new COCTMT050000UV01Patient();
      patient.getClassCode().add("PAT");
      org.hl7.v3.II patientID = new II();
      patientID.setRoot("2.16.840.1.113883.3.198");
      patientID.setExtension(reqPatientID); // setting response with request's patient ID.
      patient.getId().add(patientID);

      COCTMT030000UV04Person patientPerson = new COCTMT030000UV04Person();
      patientPerson.getClassCode().add("PSN");
      patientPerson.setDeterminerCode("INSTANCE");
      patient.setPatientPerson(factory.createCOCTMT050000UV01PatientPatientPerson(patientPerson));

      recTarget.setPatient(factory.createREPCMT004000UV01RecordTargetPatient(patient));

      return recTarget;
   }

   private static REPCMT000100UV01Observation createObservations() {

      REPCMT000100UV01Observation obs = new REPCMT000100UV01Observation();

      obs.getClassCode().add("OBS");
      obs.setMoodCode(XClinicalStatementObservationMood.EVN);
      II templID = new II();
      templID.setRoot("1.3.6.1.4.1.19376.1.5.3.1.4.13");
      obs.getTemplateId().add(templID);
      CD obscode = new CD();
      obscode.setCode("404684003");
      obscode.setDisplayName("Lab Test 100");
      obscode.setCodeSystem("2.16.840.1.113883.6.1");
      obscode.setCodeSystemName("'LOINC");
      obs.setCode(obscode);

      CS obsStatusCode = new CS();
      obsStatusCode.setCode("N");
      obs.setStatusCode(obsStatusCode);

      SXCMTSExplicit effectiveTime = new SXCMTSExplicit();
      effectiveTime.setValue("20090119");
      obs.getEffectiveTime().add(effectiveTime);

      PQ obsValue = new PQ();
      obsValue.setValue("100");
      obsValue.setUnit("g/dl");
      obs.setValue(obsValue);

      CE obsInterpretCode = new CE();
      obsInterpretCode.setCode("N");
      obsInterpretCode.setCodeSystem("2.16.840.1.113883.5.83");
      obs.getInterpretationCode().add(obsInterpretCode);

      REPCMT000100UV01ReferenceRange2 obsRefRange = new REPCMT000100UV01ReferenceRange2();
      obsRefRange.getTypeCode().add("REFV");
      REPCMT000100UV01ObservationRange observationRange = new REPCMT000100UV01ObservationRange();
      observationRange.getClassCode().add("OBS");
      observationRange.getMoodCode().add("ENV.CRT");

      TELExplicit text = new TELExplicit();
      text.setValue("M 13-18 g/dl; F 12-16 g/dl");
      EDExplicit observationRangeText = new EDExplicit();
      observationRangeText.getContent().add(factory.createEDExplicitReference(text));
      observationRange.setText(observationRangeText);

      // not complete b/c sample don't have
      //CD obsRangeValue = new CD();
      //obsRangeValue.set....
      //obsRefRangeObs.setValue(obsValue)

      obsRefRange.setObservationRange(observationRange);

      obs.getReferenceRange().add(obsRefRange);

      return obs;
   }
}