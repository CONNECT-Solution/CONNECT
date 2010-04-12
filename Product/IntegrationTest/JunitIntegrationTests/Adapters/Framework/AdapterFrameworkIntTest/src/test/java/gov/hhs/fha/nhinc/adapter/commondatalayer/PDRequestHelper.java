/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201307UV02MCCIMT000100UV01Message;
import org.hl7.v3.PRPAIN201307UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UVParameterList;
import org.hl7.v3.PRPAMT201307UVPatientIdentifier;
import org.hl7.v3.PRPAMT201307UVQueryByParameter;
import org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;

/**
 *
 * @author kim
 */
public class PDRequestHelper {

   private static String extractName(PNExplicit name) {
      String nameString = "";
      Boolean hasName = false;
      List<Serializable> choice = name.getContent();
      Iterator<Serializable> iterSerialObjects = choice.iterator();

      EnExplicitFamily familyName = new EnExplicitFamily();
      EnExplicitGiven givenName = new EnExplicitGiven();

      while (iterSerialObjects.hasNext()) {
         Serializable contentItem = iterSerialObjects.next();

         if (contentItem instanceof JAXBElement) {
            JAXBElement oJAXBElement = (JAXBElement) contentItem;
            if (oJAXBElement.getValue() instanceof EnExplicitFamily) {
               familyName = (EnExplicitFamily) oJAXBElement.getValue();
               hasName = true;
            } else if (oJAXBElement.getValue() instanceof EnExplicitGiven) {
               givenName = (EnExplicitGiven) oJAXBElement.getValue();
               hasName = true;
            }
         }
      }

      if (hasName == true) {
         nameString = familyName.getContent() + " " + givenName.getContent();
         System.out.println(nameString);
      }

      return nameString;
   }

//			<m:query>
//				<m:id extension="20090920011010.005"/>
//				<m:creationTime value="20090920011010.005"/>
//				<m:interactionId extension="PRPA_IN201307UV02" root="2.16.840.1.113883.5"/>
//				<m:processingCode/>
//				<m:processingModeCode/>
//				<m:acceptAckCode/>
//				<!-- Receiving Application and Receiving Facility. i.e. application = Adapter Assembly Service, Facility = OID -->
//				<m:receiver typeCode="RCV">
//					<m:device classCode="DEV" determinerCode="INSTANCE">
//						<m:id extension="Common Data Layer Service"/>
//					</m:device>
//				</m:receiver>
//				<!-- Sending Application and Sending Facility. i.e. application = Adapter Assembly Service, Facility = OID -->
//				<m:sender typeCode="SND">
//					<m:device classCode="DEV" determinerCode="INSTANCE">
//						<m:id extension="Adapter Assembly Service"/>
//					</m:device>
//				</m:sender>
//			</m:query>
   public static PRPAIN201307UV02MCCIMT000100UV01Message build201307(II subjectId) {
      PRPAIN201307UV02MCCIMT000100UV01Message query = new PRPAIN201307UV02MCCIMT000100UV01Message();

      // Set up message header fields
//        msg.setITSVersion("XML_1.0");
//
      II id = new II();
      id.setRoot("2.16.840.1.113883.3.198");
      id.setExtension("20090920011010.005");
      query.setId(id);

      TSExplicit creationTime = new TSExplicit();
      creationTime.setValue("20090920011010.005");
      query.setCreationTime(creationTime);

      II interactionId = new II();
      interactionId.setRoot("2.16.840.1.113883.5");
      interactionId.setExtension("PRPA_IN201307UV02");
      query.setInteractionId(interactionId);

      CS processingCode = new CS();
      processingCode.setCode("P");
      query.setProcessingCode(processingCode);

      CS processingModeCode = new CS();
      processingModeCode.setCode("R");
      query.setProcessingModeCode(processingModeCode);

      CS ackCode = new CS();
      ackCode.setCode("AL");
      query.setAcceptAckCode(ackCode);

      // Set the receiver and sender
      query.getReceiver().add(createReceiver());
      query.setSender(createSender());

      query.setControlActProcess(createControlActProcess(subjectId));

      return query;
   }

   public static PatientDemographicsPRPAIN201307UV02RequestType createPatientDemographicsRequest(II subjectId) {
      PatientDemographicsPRPAIN201307UV02RequestType msg = new PatientDemographicsPRPAIN201307UV02RequestType();

      msg.setLocalDeviceId("1.1");
      msg.setReceiverOID("1.1");
      msg.setSenderOID("1.1");
      msg.setQuery(build201307(subjectId));

      return msg;
   }

   private static PRPAIN201307UV02QUQIMT021001UV01ControlActProcess createControlActProcess(II subjectId) {
      PRPAIN201307UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201307UV02QUQIMT021001UV01ControlActProcess();

      controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

      CD code = new CD();
      code.setCode("PRPA_TE201307UV02");
      code.setCodeSystem("2.16.840.1.113883.1.6");
      controlActProcess.setCode(code);

      CE priority = new CE();
      priority.setCode("R");
      controlActProcess.getPriorityCode().add(priority);

      controlActProcess.setQueryByParameter(createQueryParams(subjectId));

      return controlActProcess;
   }

   private static JAXBElement<PRPAMT201307UVQueryByParameter> createQueryParams(II subjectId) {
      PRPAMT201307UVQueryByParameter params = new PRPAMT201307UVQueryByParameter();

      II id = new II();
      id.setRoot("12345");
      params.setQueryId(id);

      CS statusCode = new CS();
      statusCode.setCode("new");
      params.setStatusCode(statusCode);

      params.setParameterList(createParamList(subjectId));

      javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
      JAXBElement<PRPAMT201307UVQueryByParameter> queryParams = new JAXBElement<PRPAMT201307UVQueryByParameter>(xmlqname, PRPAMT201307UVQueryByParameter.class, params);

      return queryParams;
   }

   private static PRPAMT201307UVParameterList createParamList(II subjectId) {
      PRPAMT201307UVParameterList paramList = new PRPAMT201307UVParameterList();

      // Set the Subject Gender Code
      //paramList.getLivingSubjectAdministrativeGender().add(createGender(gender));

      // Set the Subject Birth Time
      //paramList.getLivingSubjectBirthTime().add(createBirthTime(birthTime));

      // Set the Subject Name
      //paramList.getLivingSubjectName().add(createName(firstName, lastName));

      // Set the subject Id
      paramList.getPatientIdentifier().add(createPatientIdentifier(subjectId));

      return paramList;
   }

//    private static PRPAMT201306UVLivingSubjectId createSubjectId(II subjectId) {
//        PRPAMT201306UVLivingSubjectId id = new PRPAMT201306UVLivingSubjectId();
//        if (subjectId != null) {
//            id.getValue().add(subjectId);
//        }
//
//        return id;
//    }
   private static PRPAMT201307UVPatientIdentifier createPatientIdentifier(II subjectId) {
      PRPAMT201307UVPatientIdentifier id = new PRPAMT201307UVPatientIdentifier();
      if (subjectId != null) {
         id.getValue().add(subjectId);
      }

      return id;
   }

//   private static PRPAMT201306UVLivingSubjectName createName(String firstName, String lastName) {
//      PRPAMT201306UVLivingSubjectName subjectName = new PRPAMT201306UVLivingSubjectName();
//      org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
//      ENExplicit name = (ENExplicit) (factory.createENExplicit());
//      List namelist = name.getContent();
//
//      if (lastName != null &&
//              lastName.length() > 0) {
//         EnExplicitFamily familyName = new EnExplicitFamily();
//         familyName.setPartType("FAM");
//         familyName.setContent(lastName);
//
//         namelist.add(factory.createENExplicitFamily(familyName));
//      }
//
//      if (firstName != null &&
//              firstName.length() > 0) {
//         EnExplicitGiven givenName = new EnExplicitGiven();
//         givenName.setPartType("GIV");
//         givenName.setContent(firstName);
//
//         namelist.add(factory.createENExplicitGiven(givenName));
//      }
//
//      subjectName.getValue().add(name);
//
//      return subjectName;
//   }
//
//   private static PRPAMT201306UVLivingSubjectBirthTime createBirthTime(String birthTime) {
//      PRPAMT201306UVLivingSubjectBirthTime subjectBirthTime = new PRPAMT201306UVLivingSubjectBirthTime();
//      IVLTSExplicit bday = new IVLTSExplicit();
//
//      if (birthTime != null &&
//              birthTime.length() > 0) {
//         bday.setValue(birthTime);
//         subjectBirthTime.getValue().add(bday);
//      }
//
//      return subjectBirthTime;
//   }
//
//   private static PRPAMT201306UVLivingSubjectAdministrativeGender createGender(String gender) {
//      PRPAMT201306UVLivingSubjectAdministrativeGender adminGender = new PRPAMT201306UVLivingSubjectAdministrativeGender();
//      CE genderCode = new CE();
//
//      if (gender != null &&
//              gender.length() > 0) {
//         genderCode.setCode(gender);
//         adminGender.getValue().add(genderCode);
//      }
//
//      return adminGender;
//   }
   private static MCCIMT000100UV01Receiver createReceiver() {
      MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

      receiver.setTypeCode(CommunicationFunctionType.RCV);

      MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
      device.setDeterminerCode("INSTANCE");

      II id = new II();
      id.setRoot("2.16.840.1.113883.3.200");
      device.getId().add(id);

      TELExplicit url = new TELExplicit();
      url.setValue("http://localhost:8080/NhinConnect/CommonDataLayerService");
      device.getTelecom().add(url);

      receiver.setDevice(device);

      return receiver;
   }

   private static MCCIMT000100UV01Sender createSender() {
      MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

      sender.setTypeCode(CommunicationFunctionType.SND);

      MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
      device.setDeterminerCode("INSTANCE");

      II id = new II();
      id.setRoot("2.16.840.1.113883.3.200");
      device.getId().add(id);

      sender.setDevice(device);

      return sender;
   }

//    public static Patient createMpiPatient(String firstName, String lastName, String gender, String birthTime, Identifier subjectId) {
//        Patient result = new Patient();
//
//        // Set the patient name
//        PersonName name = new PersonName();
//        name.setFirstName(firstName);
//        name.setLastName(lastName);
//        result.setName(name);
//
//        // Set the patient gender
//        result.setGender(gender);
//
//        // Set the patient birth time
//        result.setDateOfBirth(birthTime);
//
//        // Set the patient Id
//        Identifiers ids = new Identifiers();
//        ids.add(subjectId);
//        result.setIdentifiers(ids);
//
//        return result;
//    }


   /**
    * Uses JXB to marshal a message to an XML string.
    *
    * This operation will remove unused namespaces so if namespaces must be
    * retained, the message must be captured from the soap message.
    *
    * @param subscribe
    * @return
    */
//   public static String marshalPatientDemographics(PatientDemographicsPRPAIN201307UV02RequestType request) {
//      System.out.println("Begin marshal PatientDemographics");
//      String sXML = "";
//
//      if (request != null) {
//         try {
//            JAXBContext jc = JAXBContext.newInstance("org.hl7.v3");
//            Marshaller marshaller = jc.createMarshaller();
//            StringWriter swXML = new StringWriter();
//            System.out.println("Calling marshal");
//            marshaller.marshal(request, swXML);
//            sXML = swXML.toString();
//            System.out.println("Marshaled request: " + sXML);
//         } catch (Exception e) {
//            System.err.println("Failed to marshall PatientDemographicsPRPAIN201307UV02RequestType to XML: " + e.getMessage());
//         }
//      }
//
//      return sXML;
//   }
}
