/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;

import gov.hhs.fha.nhinc.adapter.commondatalayer.DODConnectorPortType;
import gov.hhs.fha.nhinc.adapter.commondatalayer.DODConnectorService;
import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CD;
import org.hl7.v3.COCTMT030000UV04Person;
import org.hl7.v3.COCTMT050000UV01Patient;
import org.hl7.v3.COCTMT090000UV01AssignedEntity;
import org.hl7.v3.COCTMT090000UV01Person;
import org.hl7.v3.COCTMT090003UVAssignedEntity;
import org.hl7.v3.CS;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.MFMIMT700712UV01Custodian;
import org.hl7.v3.ParticipationPhysicalPerformer;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject5;
import org.hl7.v3.QUPCMT040300UV01ParameterList;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.REPCMT004000UV01RecordTarget;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.XClinicalStatementObservationMood;
import org.hl7.v3.REPCMT000100UV01Performer3;

/**
 *
 * @author kim
 */
public class StaticProblemsQuery {

   private static DODConnectorService service;
   private static Log logger = LogFactory.getLog(StaticProblemsQuery.class);
   private static org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

   public static CareRecordQUPCIN043200UV01ResponseType createProblemsResponse(CareRecordQUPCIN043100UV01RequestType request) {
      CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();

      //check for static/live data flag in properties file
      if (AdapterCommonDataLayerConstants.PROBLEMS_TEST.equalsIgnoreCase("Y")) {

         logger.info("Calling Static Problems Data...");

         QUPCIN043100UV01QUQIMT020001UV01ControlActProcess query = request.getQuery().getControlActProcess();
         QUPCIN043100UV01QUQIMT020001UV01QueryByParameter queryByParam = query.getQueryByParameter().getValue();
         List<QUPCMT040300UV01ParameterList> paramList = queryByParam.getParameterList();

         String reqPatientID = paramList.get(0).getPatientId().getValue().getExtension();

         response.setCareRecord(createSubject(reqPatientID));
      } else {
         //make call to DODConnector
         String COMMON_DATA_LAYER_QNAME = AdapterCommonDataLayerConstants.CDL_QNAME;
         String wsdlUrl = AdapterCommonDataLayerConstants.DOD_CONNECTOR_WSDL;

         try {
            logger.info("Instantiating DOD Connector Service (" + wsdlUrl + ")...");
            service = new DODConnectorService(new URL(wsdlUrl), new QName(COMMON_DATA_LAYER_QNAME, AdapterCommonDataLayerConstants.DOD_CONNECTOR_NAME));
            logger.info("Retrieving the port from the following service: " + service);

            DODConnectorPortType port = service.getCommonDataLayerPort();

            CareRecordQUPCIN043200UV01ResponseType fdmrresponse = port.getProblems(request);

            if (fdmrresponse != null) {
               response = fdmrresponse;
               logger.info("Response =" + fdmrresponse.toString());
            }

         } catch (Exception e) {
            logger.error("Exception in Problems client: " + e);
         }

      }

      return response;
   }

   private static QUPCIN043200UV01MFMIMT700712UV01Subject1 createSubject(String reqPatientID) {

      QUPCIN043200UV01MFMIMT700712UV01Subject1 subject = new QUPCIN043200UV01MFMIMT700712UV01Subject1();
      subject.getTypeCode().add("SUBJ");

      QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent regEvent = new QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent();
      regEvent.getClassCode().add("REG");
      regEvent.getMoodCode().add("EVN");

      CS statusCode = new CS();
      statusCode.setCode("active");
      regEvent.setStatusCode(statusCode);

      // ---- SETTING Clinical Data Source ----------------
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
      p3.setContextControlCode("true");
      BigInteger seqValue = new BigInteger("1");
      org.hl7.v3.INT seq = new INT();
      seq.setValue(seqValue);
      p3.setSequenceNumber(seq);

      p3.setObservation(factory.createREPCMT000100UV01SourceOf3Observation(createObservation()));
      //p3.setSubstanceAdministration(factory.createREPCMT004000UV01PertinentInformation5SubstanceAdministration(createSubstanceAdministration()));

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
      obsTemplateID.setRoot("1.3.6.1.4.1.19376.1.5.3.1.4.5.2");
      observation.getTemplateId().add(obsTemplateID);

      //--Adverse Event Type --
      observation.setCode(createObservationCode());

      //--Adverse Event Date --
      SXCMTSExplicit effectiveTime = new SXCMTSExplicit();
      effectiveTime.setValue("20090119");

      observation.getEffectiveTime().add(effectiveTime);

      // TODO :: text
      EDExplicit text = new EDExplicit();
      text.getContent().add("Migraine");
      //TELExplicit reference = new TELExplicit();
      //reference.setValue("#problem #1");
      //text.getContent().add(factory.createEDExplicitReference(reference));
      observation.setText(text);

      // value
      CD val = new CD();
      val.setCode("37796009");
      val.setDisplayName("Migraine");
      val.setCodeSystem("2.16.840.1.113883.96");
      val.setCodeSystemName("SNOMED CT");
      observation.setValue(val);

      // Treating Provider
      REPCMT000100UV01Performer3 tp = new REPCMT000100UV01Performer3();

      tp.setTypeCode(ParticipationPhysicalPerformer.PRF);

      COCTMT090000UV01AssignedEntity assignedEnt1 = new COCTMT090000UV01AssignedEntity();
      assignedEnt1.setClassCode("ASSIGNED");

      // id
      II ii = new II();
      ii.setExtension("PROVIDER-1");
      assignedEnt1.getId().add(ii);

      // assigned person
      COCTMT090000UV01Person assignedPerson = new COCTMT090000UV01Person();
      assignedPerson.getClassCode().add("PSN");
      assignedPerson.setDeterminerCode("INSTANCE");


      ENExplicit name = new ENExplicit();
      EnExplicitFamily lastname = new EnExplicitFamily();
      lastname.setContent("Cross");
      EnExplicitGiven firstname = new EnExplicitGiven();
      firstname.setContent("Mark");
      EnExplicitPrefix prefix = new EnExplicitPrefix();
      prefix.setContent("Dr");
      name.getContent().add(factory.createENExplicitFamily(lastname));
      name.getContent().add(factory.createENExplicitGiven(firstname));
      name.getContent().add(factory.createENExplicitPrefix(prefix));
      assignedPerson.getName().add(name);

      assignedEnt1.setAssignedPerson(factory.createCOCTMT090000UV01AssignedEntityAssignedPerson(assignedPerson));

      tp.setAssignedEntity1(factory.createREPCMT000100UV01Author3AssignedEntity1(assignedEnt1));

      observation.getPerformer().add(tp);


      return observation;
   }

   private static CD createObservationCode() {

      CD code = new CD();
      code.setCode("404684003");
      code.setDisplayName("Finding");
      code.setCodeSystemVersion("2.16.840.1.113883.6.96");
      code.setCodeSystemName("SNOMED CT");

      return code;
   }

   private static MFMIMT700712UV01Custodian createCustodianData(String typeCode) {

      MFMIMT700712UV01Custodian custodianValue = new MFMIMT700712UV01Custodian();
      custodianValue.getTypeCode().add(typeCode);

      COCTMT090003UVAssignedEntity assignedEnt = new COCTMT090003UVAssignedEntity();
      assignedEnt.setClassCode("ASSIGNED");
      II assignedEntID = new II();
      assignedEntID.setRoot("2.16.840.1.113883.3.198");
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
}
