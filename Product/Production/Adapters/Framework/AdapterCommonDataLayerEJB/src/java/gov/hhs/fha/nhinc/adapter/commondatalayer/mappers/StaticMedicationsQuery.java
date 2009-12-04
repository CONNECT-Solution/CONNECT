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
import org.hl7.v3.ActClassSupply;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CEExplicit;
import org.hl7.v3.COCTMT030000UV04Person;
import org.hl7.v3.COCTMT050000UV01Patient;
import org.hl7.v3.COCTMT090000UV01AssignedEntity;
import org.hl7.v3.COCTMT090000UV01Person;
import org.hl7.v3.COCTMT090003UVAssignedEntity;
import org.hl7.v3.COCTMT150000UV02Organization;
import org.hl7.v3.CS;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.INT;
import org.hl7.v3.IVLINT;
import org.hl7.v3.IVLPQ;
import org.hl7.v3.IVLTS;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTS;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.MFMIMT700712UV01Custodian;
import org.hl7.v3.ONExplicit;
import org.hl7.v3.PIVLTS;
import org.hl7.v3.PQ;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01RegistrationEvent;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject1;
import org.hl7.v3.QUPCIN043200UV01MFMIMT700712UV01Subject5;
import org.hl7.v3.QUPCMT040300UV01ParameterList;
import org.hl7.v3.REPCMT000100UV01AdministerableMaterial;
import org.hl7.v3.REPCMT000100UV01Author3;
import org.hl7.v3.REPCMT000100UV01Consumable;
import org.hl7.v3.REPCMT000100UV01Material;
import org.hl7.v3.REPCMT000100UV01Observation;
import org.hl7.v3.REPCMT000100UV01Performer3;
import org.hl7.v3.REPCMT000100UV01SourceOf3;
import org.hl7.v3.REPCMT000100UV01SubstanceAdministration;
import org.hl7.v3.REPCMT000100UV01Supply;
import org.hl7.v3.REPCMT004000UV01CareProvisionEvent;
import org.hl7.v3.REPCMT004000UV01PertinentInformation5;
import org.hl7.v3.REPCMT004000UV01RecordTarget;
import org.hl7.v3.STExplicit;
import org.hl7.v3.SXCMTSExplicit;
import org.hl7.v3.SetOperator;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XClinicalStatementObservationMood;
import org.hl7.v3.XClinicalStatementSubstanceMood;
import org.hl7.v3.XClinicalStatementSupplyMood;

/**
 *
 * @author kim
 */
public class StaticMedicationsQuery {

   private static DODConnectorService service;
   private static Log logger = LogFactory.getLog(StaticMedicationsQuery.class);
   private static org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

   public static CareRecordQUPCIN043200UV01ResponseType createMedicationsResponse(CareRecordQUPCIN043100UV01RequestType request) {
      CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();

      //static vs. live data check
      if (AdapterCommonDataLayerConstants.MEDICATIONS_TEST.equalsIgnoreCase("Y")) {
         logger.info("Calling Static Medications Data...");

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

            CareRecordQUPCIN043200UV01ResponseType fdmrresponse = port.getMedications(request);

            if (fdmrresponse != null) {
               response = fdmrresponse;
               logger.info("Response =" + fdmrresponse.toString());
            }

         } catch (Exception e) {
            logger.error("Exception in Medications client: " + e);
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

      p3.setSubstanceAdministration(factory.createREPCMT004000UV01PertinentInformation5SubstanceAdministration(createSubstanceAdministration()));

      // ------------------------------
      careProvisEvent.getPertinentInformation3().add(p3);
      subject2.setCareProvisionEvent(careProvisEvent);
      regEvent.setSubject2(subject2);
      subject.setRegistrationEvent(regEvent);
      return subject;
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

   private static REPCMT000100UV01SubstanceAdministration createSubstanceAdministration() {
      REPCMT000100UV01SubstanceAdministration subs = new REPCMT000100UV01SubstanceAdministration();

      subs.getClassCode().add("SABM");
      subs.setMoodCode(XClinicalStatementSubstanceMood.INT);

      II id = new II();
      id.setExtension("cdbd5b05-6cde-11db-9fe1-0800200c9a66");
      subs.getId().add(id);

      // set status"
      CS statusCode = new CS();
      statusCode.setCode("active");
      subs.setStatusCode(statusCode);

      // set start + stop time of medication
      IVLTS effectiveTime1 = new IVLTS();

      // when medication was started or to be start
      IVXBTS startTime = new IVXBTS();
      startTime.setValue("20090104");
      effectiveTime1.getRest().add(factory.createIVLTSLow(startTime));

      // when medication was stopped or to be stop
      IVXBTS stopTime = new IVXBTS();
      stopTime.setValue("20090314");
      effectiveTime1.getRest().add(factory.createIVLTSHigh(stopTime));
      subs.getEffectiveTime().add(effectiveTime1);

      // duration/frequescy
      PIVLTS administerTiming = new PIVLTS();
      PQ period = new PQ();
      period.setValue("12");
      period.setUnit("h");
      administerTiming.setPeriod(period);
      administerTiming.setOperator(SetOperator.A);
      administerTiming.setInstitutionSpecified(true);
      subs.getEffectiveTime().add(administerTiming);

      // set Route of Administration
      CE routeCode = new CE();
      routeCode.setCode("C38288");
      routeCode.setCodeSystem("2.16.840.1.113883.3.26.1.1");
      routeCode.setCodeSystemName("RouteOfAdministration");
      routeCode.setDisplayName("Oral");
      subs.setRouteCode(routeCode);

      // dose quantity
      IVLPQ doseQuantity = new IVLPQ();
      doseQuantity.setValue("1");
      doseQuantity.setUnit("{TABLET}");
      subs.setDoseQuantity(doseQuantity);

      // administration unit code
      CE administrationUnitCode = new CE();
      administrationUnitCode.setCode("C42998");
      administrationUnitCode.setCodeSystem("2.16.840.1.113883.3.26.1.1");
      administrationUnitCode.setDisplayName("TABLET");
      administrationUnitCode.setCodeSystemName("NCI Thesaurus");

      // set Medication Information
      subs.setConsumable(createMedicationInfo());

      // set Type of Medication
      subs.getSourceOf().add(createTypeOfMedication());

      // set Status of Medication
      subs.getSourceOf().add(createStatusOfMedication());

      // set Indication
      subs.getSourceOf().add(createIndication());

      // set Order Information with supply event
      subs.getSourceOf().add(createOrderInfo());

      // set fullfillment information
      for (long i = 1; i < 3; i++) {
         subs.getSourceOf().add(createFullfillments(i, "20090" + String.valueOf(i) + "04"));
      }

      return subs;
   }

   private static REPCMT000100UV01Consumable createMedicationInfo() {
      REPCMT000100UV01Consumable consumable = new REPCMT000100UV01Consumable();

      consumable.getTypeCode().add("CSM");

      REPCMT000100UV01AdministerableMaterial admm = new REPCMT000100UV01AdministerableMaterial();
      admm.getClassCode().add("ADMM");

      REPCMT000100UV01Material mmat = new REPCMT000100UV01Material();
      mmat.getClassCode().add("MMAT");
      mmat.setDeterminerCode("KIND");

      // Coded Product Name 
      CEExplicit code = new CEExplicit();
      code.setCode("197806");
      code.setCodeSystem("2.16.840.1.113883.6.88");
      code.setCodeSystemName("RxNorm");
      code.setDisplayName("IBUPROFEN, 600MG, TABLET, ORAL");
      EDExplicit originalText = new EDExplicit();
      originalText.getContent().add("IBUPROFEN (MOTRIN)--PO 600MG TAB");
      //originalText.getNullFlavor().add("IBUPROFEN (MOTRIN)--PO 600MG TAB");
      code.setOriginalText(originalText);

      CD translation = new CD();
      translation.setCode("201152");
      translation.setDisplayName("Ibuprofen 600mg, (Motrin), Tablet, Oral");
      translation.setCodeSystem("2.16.840.1.113883.6.88");
      translation.setCodeSystemName("RxNorm");
      code.getTranslation().add(translation);

      mmat.setCode(code);

      // Free Text Product Name
      STExplicit desc = new STExplicit();
      desc.getContent().add("MOTRIN 600MG ORAL TAB");
      mmat.setDesc(desc);

      admm.setAdministerableMaterial(mmat);
      consumable.setAdministerableMaterial(admm);

      return consumable;
   }

   private static REPCMT000100UV01SourceOf3 createTypeOfMedication() {
      REPCMT000100UV01SourceOf3 typeOfMed = new REPCMT000100UV01SourceOf3();

      typeOfMed.getTypeCode().add("SUBJ");

      REPCMT000100UV01Observation obs = new REPCMT000100UV01Observation();
      obs.getClassCode().add("OBS");
      obs.setMoodCode(XClinicalStatementObservationMood.EVN);

      CD code = new CD();
      code.setCode("73639000");
      code.setDisplayName("Prescription Drug");
      code.setCodeSystemName("SNOMED CT");
      code.setCodeSystem("2.16.840.1.113883.6.96");
      obs.setCode(code);

      CS statusCode = new CS();
      statusCode.setCode("completed");
      obs.setStatusCode(statusCode);

      typeOfMed.setObservation(factory.createREPCMT000100UV01SourceOf3Observation(obs));

      return typeOfMed;
   }

   private static REPCMT000100UV01SourceOf3 createStatusOfMedication() {
      REPCMT000100UV01SourceOf3 statusofMed = new REPCMT000100UV01SourceOf3();

      statusofMed.getTypeCode().add("REFR");

      REPCMT000100UV01Observation obs = new REPCMT000100UV01Observation();
      obs.getClassCode().add("OBS");
      obs.setMoodCode(XClinicalStatementObservationMood.EVN);

      CD code = new CD();
      code.setCode("33999-4");
      code.setDisplayName("Status");
      code.setCodeSystemName("LOINC");
      code.setCodeSystem("2.16.840.1.113883.6.1");
      obs.setCode(code);

      CS statusCode = new CS();
      statusCode.setCode("completed");
      obs.setStatusCode(statusCode);

      CE value = new CE();
      value.setCode("55561003");
      value.setCodeSystem("2.16.840.1.113883.6.96");
      value.setCodeSystemName("SNOMED CT");
      value.setDisplayName("Active");
      obs.setValue(value);

      statusofMed.setObservation(factory.createREPCMT000100UV01SourceOf3Observation(obs));

      return statusofMed;
   }

   private static REPCMT000100UV01SourceOf3 createIndication() {
      REPCMT000100UV01SourceOf3 indication = new REPCMT000100UV01SourceOf3();

      indication.getTypeCode().add("RSON");

      REPCMT000100UV01Observation obs = new REPCMT000100UV01Observation();
      obs.getClassCode().add("OBS");
      obs.setMoodCode(XClinicalStatementObservationMood.EVN);

      CD code = new CD();
      code.setCode("12345");
      code.setDisplayName("some-indocation-code");
      code.setCodeSystemName("SNOMED CT");
      code.setCodeSystem("2.16.840.1.113883.6.96");
      obs.setCode(code);

      EDExplicit text = new EDExplicit();
      TELExplicit reference = new TELExplicit();
      reference.setValue("#indication-1");
      text.getContent().add(factory.createEDExplicitReference(reference));
      obs.setText(text);

      CS statusCode = new CS();
      statusCode.setCode("completed");
      obs.setStatusCode(statusCode);

      SXCMTSExplicit effectiveTime = new SXCMTSExplicit();
      effectiveTime.setValue("20090114");
      obs.getEffectiveTime().add(effectiveTime);

      indication.setObservation(factory.createREPCMT000100UV01SourceOf3Observation(obs));

      return indication;
   }

   private static REPCMT000100UV01SourceOf3 createOrderInfo() {
      REPCMT000100UV01SourceOf3 orderInfo = new REPCMT000100UV01SourceOf3();

      orderInfo.getTypeCode().add("REFR");

      REPCMT000100UV01Supply supply = new REPCMT000100UV01Supply();
      supply.setClassCode(ActClassSupply.SPLY);
      supply.setMoodCode(XClinicalStatementSupplyMood.INT);

      // order number
      II ordernum = new II();
      ordernum.setExtension("prescription12");
      ordernum.setRoot("2.16.840.1.113883.3.198");
      supply.getId().add(ordernum);

      // fills
      IVLINT repeatNumber = new IVLINT();
      repeatNumber.setValue(BigInteger.valueOf(1));
      supply.setRepeatNumber(repeatNumber);

      // quantity
      PQ quantity = new PQ();
      quantity.setValue("30");
      supply.setQuantity(quantity);

      //  order expiration date/time
      IVLTSExplicit expireDate = new IVLTSExplicit();
      IVXBTSExplicit hiTime = new IVXBTSExplicit();
      hiTime.setValue("20090114");
      expireDate.getContent().add(factory.createIVLTSExplicitHigh(hiTime));
      supply.setExpectedUseTime(expireDate);

      // ordering provider
      REPCMT000100UV01Author3 author = new REPCMT000100UV01Author3();
      author.getTypeCode().add("AUT");
      TSExplicit orderDateTime = new TSExplicit();
      orderDateTime.setValue("20090102");
      author.setTime(orderDateTime);

      COCTMT090000UV01AssignedEntity provider = new COCTMT090000UV01AssignedEntity();
      II provId = new II();
      provId.setExtension("PROV2");
      provider.getId().add(provId);

      COCTMT090000UV01Person providerInfo = new COCTMT090000UV01Person();
      ENExplicit name = new ENExplicit();
      EnExplicitFamily lastname = new EnExplicitFamily();
      lastname.setContent("Smith");
      EnExplicitGiven firstname = new EnExplicitGiven();
      firstname.setContent("Jonathan");
      EnExplicitSuffix suffix = new EnExplicitSuffix();
      suffix.setContent("MD");
      name.getContent().add(factory.createENExplicitGiven(firstname));
      name.getContent().add(factory.createENExplicitFamily(lastname));
      name.getContent().add(factory.createENExplicitSuffix(suffix));
      providerInfo.getName().add(name);

      provider.setAssignedPerson(factory.createCOCTMT090000UV01AssignedEntityAssignedPerson(providerInfo));
      author.setAssignedEntity1(factory.createREPCMT000100UV01Author3AssignedEntity1(provider));
      supply.getAuthor().add(author);

      orderInfo.setSupply(factory.createREPCMT000100UV01SourceOf3Supply(supply));

      return orderInfo;
   }

   private static REPCMT000100UV01SourceOf3 createFullfillments(long fillNo, String fillDate) {
      REPCMT000100UV01SourceOf3 fullfillmentInfo = new REPCMT000100UV01SourceOf3();

      fullfillmentInfo.getTypeCode().add("COMP");

      // <!-- Fulfillment History: fill number -->
      INT seqNo = new INT();
      seqNo.setValue(BigInteger.valueOf(fillNo));
      fullfillmentInfo.setSequenceNumber(seqNo);

      REPCMT000100UV01Supply supply = new REPCMT000100UV01Supply();
      supply.setClassCode(ActClassSupply.SPLY);
      supply.setMoodCode(XClinicalStatementSupplyMood.EVN);

      // dispensed activity template
      II templateid = new II();
      templateid.setRoot("2.16.840.1.113883.10.20.1.34");

      // <!-- Fulfillment History: The prescription identifier assigned by the pharmacy -->
      II prescriptionId = new II();
      prescriptionId.setExtension("14ED7742-" + fillNo);
      prescriptionId.setRoot("2.16.840.1.113883.3.198");
      supply.getId().add(prescriptionId);

      // <!-- Fulfillment History: fill status -->
      CS statusCode = new CS();
      statusCode.setCode("dispensed");
      supply.setStatusCode(statusCode);

      // <!-- Fulfillment History: The date of this dispense  -->
      SXCMTSExplicit dispensedDate = new SXCMTSExplicit();
      dispensedDate.setValue(fillDate);
      supply.getEffectiveTime().add(dispensedDate);

      // <!-- Fulfillment History: The actual quantity of product supplied in this dispense -->
      PQ dispensedQt = new PQ();
      dispensedQt.setValue("30");
      dispensedQt.setUnit("{TABLET}");
      supply.setQuantity(dispensedQt);

      //<!-- Fulfillment History: pharmacy -->
      supply.getPerformer().add(createFullfillmentLocation());

      fullfillmentInfo.setSupply(factory.createREPCMT000100UV01SourceOf3Supply(supply));

      return fullfillmentInfo;
   }

   private static REPCMT000100UV01Performer3 createFullfillmentLocation() {
      REPCMT000100UV01Performer3 performer = new REPCMT000100UV01Performer3();

      COCTMT090000UV01AssignedEntity pharmacy = new COCTMT090000UV01AssignedEntity();
      pharmacy.setClassCode("ASSIGNED");

      // <!-- Fulfillment History: pharmacy location -->
      pharmacy.getAddr().add(StaticUtil.createAddress("21 Dawn Road", "Blue Bell", "MA", "00000", "US"));
      pharmacy.getTelecom().add(StaticUtil.createTelecom("tel:+1-888-345-8888", "WP"));

      // <!-- Fulfillment History: organization -->
      COCTMT150000UV02Organization org = new COCTMT150000UV02Organization();
      II orgId = new II();
      orgId.setRoot("2.16.840.1.113883.3.198");
      org.getId().add(orgId);

      ONExplicit orgName = new ONExplicit();
      orgName.getContent().add("Department of Defense");
      org.getName().add(orgName);

      pharmacy.setRepresentedOrganization(factory.createCOCTMT090400UVAssignedPartyRepresentedOrganization(org));

      // <!-- Fulfillment History: pharmacy -->
      COCTMT090000UV01Person dispensedPharm = new COCTMT090000UV01Person();
      ENExplicit pharmacyName = new ENExplicit();
      pharmacyName.getContent().add("Blue Bell Pharmacy");
      dispensedPharm.getName().add(pharmacyName);

      pharmacy.setAssignedPerson(factory.createCOCTMT090000UV01AssignedEntityAssignedPerson(dispensedPharm));

      performer.setAssignedEntity1(factory.createREPCMT000100UV01Performer3AssignedEntity1(pharmacy));

      return performer;
   }
}
