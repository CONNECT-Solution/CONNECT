/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;

import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCMT040300UV01ParameterList;
import java.io.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author tmn
 */
public class StaticAllergiesQuery {

   private static Log logger = LogFactory.getLog(StaticAllergiesQuery.class);
   private static ObjectFactory factory = new ObjectFactory();

   public static CareRecordQUPCIN043200UV01ResponseType createAllergiesResponse(CareRecordQUPCIN043100UV01RequestType request) {

      CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();

      //check properties file for test/live data mode
      if (AdapterCommonDataLayerConstants.ALLERGIES_TEST.equalsIgnoreCase("Y")) {
         logger.info("Calling Static Allergies Data...");

         // Get Provider OID from request
         String receiverOID = request.getReceiverOID();

         // Get Patient ID from the request
         QUPCIN043100UV01QUQIMT020001UV01ControlActProcess query = request.getQuery().getControlActProcess();
         QUPCIN043100UV01QUQIMT020001UV01QueryByParameter queryByParam = query.getQueryByParameter().getValue();
         List<QUPCMT040300UV01ParameterList> paramList = queryByParam.getParameterList();
         String reqPatientID = paramList.get(0).getPatientId().getValue().getExtension();

         logger.debug("Retrieving Emulated Data File for : Patient ID: " + reqPatientID + ", receiverOID: " + receiverOID);
         response = _getEmulatedResponse(reqPatientID, receiverOID);

         // 20091221 - Removed Static Data and replaced with Data Files
         //response.setCareRecord(createSubject(reqPatientID));
      } else {
          logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
          logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
          logger.debug(" Insert Adapter Agency specific dynamic document data accessors here ");
          logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
          logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
      }

      return response;
   }

   private static CareRecordQUPCIN043200UV01ResponseType _getEmulatedResponse(String patientID, String receiverOID)
   {
       CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();
       response = null;

       // Get ALLERGIES_TAG from the properties file
       String allergiesTag = AdapterCommonDataLayerConstants.EMULATOR_ALLERGIES_TAG;

       // Get MEDS_RESPONSE_TYPE from the properties file
       String allergiesResponseType = AdapterCommonDataLayerConstants.EMULATOR_ALLERGIES_RESPONSE_TYPE;

       // Get the EMULATOR_DATA_LOCATION from the properties file
       String dataPath = AdapterCommonDataLayerConstants.EMULATOR_DATA_LOCATION;

       // Build URL to file
       String responseFile = null;
       String responseNotFoundFile = null;
       String dataFile = null;
       boolean fileExists = true;

       responseFile = dataPath + receiverOID + "_" + patientID + "_" + allergiesTag + "_" + allergiesResponseType + ".xml";
       dataFile = responseFile;

       File testFile = new File(responseFile);
       if (!testFile.exists())
       {
           String notFoundTag = AdapterCommonDataLayerConstants.EMULATOR_NO_PATIENT_ID_LABEL;
           responseNotFoundFile = dataPath + receiverOID + "_" + notFoundTag + "_" + allergiesTag + "_" + allergiesResponseType + ".xml";
           dataFile = responseNotFoundFile;

           File notFoundFile = new File (responseNotFoundFile);
           if(!notFoundFile.exists())
           {
               fileExists = false;
               logger.error("Emulator Data Files Not Found : 1)" + responseFile + "   2)" + responseNotFoundFile);
           }
       }

       if(fileExists)
       {
          logger.debug("Emulated Data File Found: " + dataFile + "  ... Creating Response");
          try{
              JAXBContext jContext = JAXBContext.newInstance(CareRecordQUPCIN043200UV01ResponseType.class);
              Unmarshaller unmarshaller = jContext.createUnmarshaller();
              JAXBElement<CareRecordQUPCIN043200UV01ResponseType> element = unmarshaller.unmarshal(new StreamSource(dataFile), CareRecordQUPCIN043200UV01ResponseType.class);
              response = element.getValue();
              logger.debug("Response Creation Successful");
             }
             catch (Exception ex)
             {
                logger.error("Error Extracting data from " + dataFile + "  --  " + ex);
             }
       }

       return response;
   } // _getEmulatedResponse

   /** 20091221 - Commenting out Static Data
   private static QUPCIN043200UV01MFMIMT700712UV01Subject1 createSubject(String reqPatientID) {

      QUPCIN043200UV01MFMIMT700712UV01Subject1 subject = new QUPCIN043200UV01MFMIMT700712UV01Subject1();
      subject.getTypeCode().add("SUBJ");

      QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent regEvent = new QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent();
      regEvent.getClassCode().add("REG");
      regEvent.getMoodCode().add("EVN");

      CS statusCode = new CS();
      statusCode.setCode("active");
      regEvent.setStatusCode(statusCode);

      // ---- SETTING Allergy Data Source ----------------
      regEvent.setCustodian(createCustodianData("CST"));

      // --- SETTING data content requested from query -------      
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

      p3.setObservation(factory.createREPCMT000100UV01SourceOf3Observation(createObservation()));

      // ------------------------------
      careProvisEvent.getPertinentInformation3().add(p3);
      subject2.setCareProvisionEvent(careProvisEvent);
      regEvent.setSubject2(subject2);
      subject.setRegistrationEvent(regEvent);
      return subject;
   }

   private static REPCMT000100UV01Observation createObservation() {

      REPCMT000100UV01Observation observation = new REPCMT000100UV01Observation();

      observation.getClassCode().add("OBS");
      observation.setMoodCode(XClinicalStatementObservationMood.EVN);

      II obsTemplateID = new II();
      obsTemplateID.setRoot("2.16.840.1.113883.3.88.11.83.6");
      observation.getTemplateId().add(obsTemplateID);

      //--Adverse Event Type --
      observation.setCode(createObservationCode());

      //--Adverse Event Date --
      SXCMTSExplicit effectiveTime = new SXCMTSExplicit();
      effectiveTime.setValue("20090119");
      observation.getEffectiveTime().add(effectiveTime);

      //--Product
      observation.getSourceOf().add(createProduct());

      //--Reaction
      observation.getSourceOf().add(createReaction());

      //--Severity
      observation.getSourceOf().add(createSeverity());

      return observation;
   }

   private static REPCMT000100UV01SourceOf3 createProduct() {
      REPCMT000100UV01SourceOf3 product = new REPCMT000100UV01SourceOf3();
      product.getTypeCode().add("MFST");
      product.setSubstanceAdministration(factory.createREPCMT004000UV01PertinentInformation5SubstanceAdministration(createSubstanceAdministration()));

      return product;
   }

   private static REPCMT000100UV01SourceOf3 createReaction() {
      REPCMT000100UV01SourceOf3 reaction = new REPCMT000100UV01SourceOf3();
      reaction.getTypeCode().add("MFST");
      
      REPCMT000100UV01Observation reactionObs = new REPCMT000100UV01Observation();

      reactionObs.getClassCode().add("OBS");
      reactionObs.setMoodCode(XClinicalStatementObservationMood.EVN);

      II obsTemplateID = new II();
      obsTemplateID.setRoot("2.16.840.1.113883.10.20.1.54");
      reactionObs.getTemplateId().add(obsTemplateID);

      EDExplicit text = new EDExplicit();
      text.getContent().add("Hives");
      //TELExplicit; reference = new TELExplicit();
      //reference.setValue("#reaction-1");
      //text.getContent().add(factory.createEDExplicitReference(reference));
      reactionObs.setText(text);

      CD value = new CD();
      value.setCode("247472004");
      value.setCodeSystem("2.16.840.1.113883.6.96");
      value.setCodeSystemName("SNOMED CT");
      value.setDisplayName("Hives");
      reactionObs.setValue(value);

      reaction.setObservation(factory.createREPCMT000100UV01SourceOf3Observation(reactionObs));
      //getObservation().setValue(reactionObs);

      return reaction;
   }

   private static REPCMT000100UV01SourceOf3 createSeverity() {
      REPCMT000100UV01SourceOf3 severity = new REPCMT000100UV01SourceOf3();
      severity.getTypeCode().add("SUBJ");

      REPCMT000100UV01Observation severityObs = new REPCMT000100UV01Observation();

      severityObs.getClassCode().add("OBS");
      severityObs.setMoodCode(XClinicalStatementObservationMood.EVN);

      II obsTemplateID = new II();
      obsTemplateID.setRoot("2.16.840.1.113883.10.20.1.55");
      severityObs.getTemplateId().add(obsTemplateID);

      II obsTemplateID2 = new II();
      obsTemplateID2.setRoot("1.3.6.1.4.1.19376.1.5.3.1.4.1");
      severityObs.getTemplateId().add(obsTemplateID2);

      CD severityCode = new CD();
      severityCode.setCode("SEV");
      severityCode.setCodeSystem("2.16.840.1.113883.5.4");
      severityCode.setCodeSystemName("ActCode");
      severityCode.setDisplayName("Severity");

      severityObs.setCode(severityCode);

      //EDExplicit text = new EDExplicit();
      //TELExplicit reference = new TELExplicit();
      //reference.setValue("#severity-1");
      //text.getContent().add(factory.createEDExplicitReference(reference));
      //severityObs.setText(text);

      EDExplicit text = new EDExplicit();
      text.getContent().add("Very Severe");
      //TELExplicit; reference = new TELExplicit();
      //reference.setValue("#reaction-1");
      //text.getContent().add(factory.createEDExplicitReference(reference));
      severityObs.setText(text);


      CS statusCode = new CS();
      statusCode.setCode("completed");
      severityObs.setStatusCode(statusCode);

      CD value = new CD();
      value.setCode("24484000");
      value.setCodeSystem("2.16.840.1.113883.6.96");
      value.setCodeSystemName("SNOMED CT");
      value.setDisplayName("24484000");
      severityObs.setValue(value);

      severity.setObservation(factory.createREPCMT000100UV01SourceOf3Observation(severityObs));

      //getObservation().setValue(severityObs);

      return severity;
   }

   private static CD createObservationCode() {
      CD obsCode = new CD();
      obsCode.setCode("416098002");
      obsCode.setCodeSystem("2.16.840.1.113883.6.96");
      obsCode.setCodeSystemName("SNOMED CT");
      obsCode.setDisplayName("drug allergy");

      return obsCode;
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

      COCTMT030000UV04Person patientPerson = new COCTMT030000UV04Person();
      patientPerson.getClassCode().add("PSN");
      patientPerson.setDeterminerCode("INSTANCE");

      COCTMT050000UV01Patient patient = new COCTMT050000UV01Patient();
      patient.getClassCode().add("PAT");
      org.hl7.v3.II patientID = new II();
      patientID.setExtension(reqPatientID); // setting response with request's patient ID.
      patient.getId().add(patientID);
      patient.setPatientPerson(factory.createCOCTMT050000UV01PatientPatientPerson(patientPerson));

      REPCMT004000UV01RecordTarget recTarget = new REPCMT004000UV01RecordTarget();
      recTarget.getTypeCode().add("RCT");

      recTarget.setPatient(factory.createREPCMT004000UV01RecordTargetPatient(patient));


      return recTarget;
   }

   private static REPCMT000100UV01SubstanceAdministration createSubstanceAdministration() {
      REPCMT000100UV01SubstanceAdministration subs = new REPCMT000100UV01SubstanceAdministration();

      subs.getClassCode().add("SABM");
      subs.setMoodCode(XClinicalStatementSubstanceMood.EVN);

      REPCMT000100UV01Consumable consumable = createConsumable();
      subs.setConsumable(consumable);

      return subs;
   }

   private static REPCMT000100UV01Consumable createConsumable() {
      REPCMT000100UV01Consumable consumable = new REPCMT000100UV01Consumable();

      consumable.getTypeCode().add("CSM");

      REPCMT000100UV01AdministerableMaterial admm = new REPCMT000100UV01AdministerableMaterial();
      admm.getClassCode().add("ADMM");

      REPCMT000100UV01Material mmat = new REPCMT000100UV01Material();
      mmat.getClassCode().add("MMAT");
      mmat.setDeterminerCode("INSTANCE");

      // Coded Product Name
      CEExplicit code = new CEExplicit();
      code.setCode("70618");
      code.setCodeSystem("2.16.840.1.113883.6.88");
      code.setCodeSystemName("RxNorm");
      code.setDisplayName("Penicillin");
      EDExplicit originalText = new EDExplicit();
      originalText.getContent().add("PENICILLIN VK 500 MG");
      code.setOriginalText(originalText);
      
      
      //CD translation = new CD();
      //translation.setCode("201152");
      //translation.setDisplayName("Ibuprofen 600mg, (Motrin), Tablet, Oral");
      //translation.setCodeSystem("2.16.840.1.113883.6.88");
      //translation.setCodeSystemName("RxNorm");
      //code.getTranslation().add(translation);
      

      mmat.setCode(code);

      // Free Text Product Name
      STExplicit desc = new STExplicit();
      desc.getContent().add("Penicillin");
      mmat.setDesc(desc);

      admm.setAdministerableMaterial(mmat);
      consumable.setAdministerableMaterial(admm);

      return consumable;
   }
*/
}
