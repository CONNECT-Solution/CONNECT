/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.assemblymanager.service;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import gov.hhs.fha.nhinc.assemblymanager.utils.DateUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.DocumentIdGenerator;
import java.util.Date;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.QUPCIN043100UV01MCCIMT000100UV01Message;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCMT040300UV01CareProvisionCode;
import org.hl7.v3.QUPCMT040300UV01CareRecordTimePeriod;
import org.hl7.v3.QUPCMT040300UV01ParameterList;
import org.hl7.v3.QUPCMT040300UV01PatientId;
import org.hl7.v3.TSExplicit;
import org.hl7.v3.XActMoodIntentEvent;

/**
 * This
 * @author kim
 */
public class CareRecordRequestHelper {

   private static ObjectFactory factory = null;
   private static String homeOID = "";

   static {
      homeOID = PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.ORGANIZATION_OID, true);
      factory = new ObjectFactory();
   }

   public static CareRecordQUPCIN043100UV01RequestType createCareRecordRequest(II subjectId, String careProvisionCode) {
      CareRecordQUPCIN043100UV01RequestType msg = new CareRecordQUPCIN043100UV01RequestType();

      msg.setLocalDeviceId(homeOID);
      msg.setReceiverOID(homeOID);
      msg.setSenderOID("1.1");
      
      msg.setQuery(build043100(subjectId, careProvisionCode, null, null));

      return msg;
   }

   public static CareRecordQUPCIN043100UV01RequestType createCareRecordRequest(II subjectId, String careProvisionCode, String dataStartDate, String dataEndDate) {
      CareRecordQUPCIN043100UV01RequestType msg = new CareRecordQUPCIN043100UV01RequestType();

      msg.setLocalDeviceId(homeOID);
      msg.setReceiverOID(homeOID);
      msg.setSenderOID("1.1");

      if (dataStartDate == null) {
         msg.setQuery(build043100(subjectId, careProvisionCode, null, null));
      } else {
         msg.setQuery(build043100(subjectId, careProvisionCode, dataStartDate, dataEndDate));
      }

      return msg;
   }

  private static QUPCIN043100UV01MCCIMT000100UV01Message build043100(II subjectId, String careProvisionCode, String dataStartDate, String dataEndDate) {
     
      QUPCIN043100UV01MCCIMT000100UV01Message query = new QUPCIN043100UV01MCCIMT000100UV01Message();

      II id = new II();
      id.setRoot(homeOID);
      id.setExtension(DocumentIdGenerator.generateDocumentId());
      query.setId(id);

      TSExplicit creationTime = new TSExplicit();
      creationTime.setValue(DateUtil.convertToCDATime(new Date()));
      query.setCreationTime(creationTime);

      II interactionId = new II();
      interactionId.setRoot("2.16.840.1.113883.5");
      interactionId.setExtension(AssemblyConstants.CARE_RECORD_QUERY_INTERACTION_ID);
      query.setInteractionId(interactionId);

      //CS processingCode = new CS();
      //processingCode.setCode("");
      //query.setProcessingCode(processingCode);

      //CS processingModeCode = new CS();
      //processingModeCode.setCode("");
      //query.setProcessingModeCode(processingModeCode);

      //CS ackCode = new CS();
      //ackCode.setCode("");
      //query.setAcceptAckCode(ackCode);

      // Set the receiver and sender
      query.getReceiver().add(createReceiver());
      query.setSender(createSender());

      // create ControlActProcess object
      QUPCIN043100UV01QUQIMT020001UV01ControlActProcess controlActProcess = createControlActProcess();

      // create QueryByParameter object
      QUPCIN043100UV01QUQIMT020001UV01QueryByParameter queryParams = createQueryParams();

      // create parameters list and set it in QueryByParameter object
      QUPCMT040300UV01ParameterList paramList =
              createParameterList(subjectId, careProvisionCode, dataStartDate, dataEndDate);
      queryParams.getParameterList().add(paramList);

      // set QueryByParameter in ControlActProcess object
      controlActProcess.setQueryByParameter(factory.createQUPCIN043100UV01QUQIMT020001UV01ControlActProcessQueryByParameter(queryParams));

      // set ControlActProcess in Message object
      query.setControlActProcess(controlActProcess);

      return query;
   }

   private static MCCIMT000100UV01Receiver createReceiver() {
      MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

      receiver.setTypeCode(CommunicationFunctionType.RCV);

      MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
      device.setDeterminerCode("INSTANCE");
      device.setClassCode(EntityClassDevice.DEV);

      II id = new II();
      id.setExtension(AssemblyConstants.CDL_SERVICE);
      id.setRoot(homeOID);
      device.getId().add(id);

      //TELExplicit url = new TELExplicit();
      //url.setValue("http://localhost:8080/NhinConnect/CommonDataLayerService");
      //device.getTelecom().add(url);

      receiver.setDevice(device);

      return receiver;
   }

   private static MCCIMT000100UV01Sender createSender() {
      MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

      sender.setTypeCode(CommunicationFunctionType.SND);

      MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
      device.setDeterminerCode("INSTANCE");
      device.setClassCode(EntityClassDevice.DEV);

      II id = new II();
      id.setExtension(AssemblyConstants.ADAS_SERVICE);
      id.setRoot(homeOID);
      device.getId().add(id);

      sender.setDevice(device);

      return sender;
   }

   private static QUPCIN043100UV01QUQIMT020001UV01ControlActProcess createControlActProcess() {
      QUPCIN043100UV01QUQIMT020001UV01ControlActProcess controlActProcess =
              new QUPCIN043100UV01QUQIMT020001UV01ControlActProcess();

      controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
      controlActProcess.setClassCode(ActClassControlAct.CACT);

      CD code = new CD();
      code.setCode(AssemblyConstants.CARE_RECORD_QUERY_TRIGGER);
      //code.setCodeSystem("2.16.840.1.113883.1.6");
      controlActProcess.setCode(code);

      CE priority = new CE();
      priority.setCode("R");
      controlActProcess.getPriorityCode().add(priority);

      //controlActProcess.setQueryByParameter(createQueryParams(subjectId, careProvisionCode, null, null));

      return controlActProcess;
   }

//   private static JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> createQueryParams(II subjectId, String careProvisionCode) {
//      QUPCIN043100UV01QUQIMT020001UV01QueryByParameter params = new QUPCIN043100UV01QUQIMT020001UV01QueryByParameter();
//
//      II id = new II();
//      id.setExtension(DocumentIdGenerator.generateDocumentId());
//      id.setRoot(homeOID);
//      params.setQueryId(id);
//
//      CS statusCode = new CS();
//      statusCode.setCode("new");
//      params.setStatusCode(statusCode);
//
//      params.getParameterList().add(createParameterList(subjectId, careProvisionCode));
//
//      javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
//      JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> queryParams = new JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter>(xmlqname, QUPCIN043100UV01QUQIMT020001UV01QueryByParameter.class, params);
//
//      return queryParams;
//   }

   private static QUPCIN043100UV01QUQIMT020001UV01QueryByParameter createQueryParams() {
      QUPCIN043100UV01QUQIMT020001UV01QueryByParameter queryParams = new QUPCIN043100UV01QUQIMT020001UV01QueryByParameter();

      II id = new II();
      id.setExtension(DocumentIdGenerator.generateDocumentId());
      id.setRoot(homeOID);
      queryParams.setQueryId(id);

      CS statusCode = new CS();
      statusCode.setCode("new");
      queryParams.setStatusCode(statusCode);

      //queryParams.getParameterList().add(createParameterList(subjectId, careProvisionCode));

      //javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
      //JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> queryParams = new JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter>(xmlqname, QUPCIN043100UV01QUQIMT020001UV01QueryByParameter.class, params);

      return queryParams;
   }

   private static QUPCMT040300UV01ParameterList createParameterList(II subjectId, String careProvisionCode, String startDate, String endDate) {
      QUPCMT040300UV01ParameterList paramList = new QUPCMT040300UV01ParameterList();

      // -------- Provision Code -------
      QUPCMT040300UV01CareProvisionCode careCode = new QUPCMT040300UV01CareProvisionCode();
      CD code = new CD();
      code.setCode(careProvisionCode);
      code.setCodeSystem("1.3.5.1.4.1.19376.1.5.3.2");
      code.setCodeSystemName("IHEActCode");
      careCode.setValue(code);
      paramList.setCareProvisionCode(factory.createQUPCMT040300UV01ParameterListCareProvisionCode(careCode));

      // -------- Record Time -------
      QUPCMT040300UV01CareRecordTimePeriod careTime = new QUPCMT040300UV01CareRecordTimePeriod();
      IVLTSExplicit time = new IVLTSExplicit();
      IVXBTSExplicit lowValue = new IVXBTSExplicit();
      lowValue.setValue(startDate);
      IVXBTSExplicit hiValue = new IVXBTSExplicit();
      hiValue.setValue(endDate);

      time.getContent().add(factory.createIVLTSExplicitLow(lowValue));
      time.getContent().add(factory.createIVLTSExplicitHigh(hiValue));
      careTime.setValue(time);
      paramList.setCareRecordTimePeriod(factory.createQUPCMT040300UV01ParameterListCareRecordTimePeriod(careTime));

      // -------- patient ID -------
      QUPCMT040300UV01PatientId patientID = new QUPCMT040300UV01PatientId();
      patientID.setValue(subjectId);
      paramList.setPatientId(patientID);

      return paramList;
   }
}
