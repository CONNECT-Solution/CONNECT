/*
 * 
 */

package gov.hhs.fha.nhinc.adapter.commondatalayer;

import javax.xml.bind.JAXBElement;
import org.hl7.v3.XActMoodIntentEvent;
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
import org.hl7.v3.QUPCIN043100UV01MCCIMT000100UV01Message;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCMT040300UV01CareProvisionCode;
import org.hl7.v3.QUPCMT040300UV01CareRecordTimePeriod;
import org.hl7.v3.QUPCMT040300UV01ParameterList;
import org.hl7.v3.QUPCMT040300UV01PatientId;
import org.hl7.v3.TSExplicit;


/**
 *
 * @author tmn
 */
public class MedicationsRequestHelper {


   public static CareRecordQUPCIN043100UV01RequestType createMedicationsRequest(II subjectId) {
      CareRecordQUPCIN043100UV01RequestType msg = new CareRecordQUPCIN043100UV01RequestType();

      msg.setLocalDeviceId("1.1");
      msg.setReceiverOID("1.1");
      msg.setSenderOID("1.1");
      msg.setQuery(build043100(subjectId));

      return msg;
   }

  public static QUPCIN043100UV01MCCIMT000100UV01Message build043100(II subjectId) {
      QUPCIN043100UV01MCCIMT000100UV01Message query = new QUPCIN043100UV01MCCIMT000100UV01Message();

      II id = new II();
      id.setRoot("1.1");
      id.setExtension("20090920011010.005");
      query.setId(id);

      TSExplicit creationTime = new TSExplicit();
      creationTime.setValue("20090920011010.005");
      query.setCreationTime(creationTime);

      II interactionId = new II();
      interactionId.setRoot("2.16.840.1.113883.5");
      interactionId.setExtension("QUPC_IN043100UV");
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

      query.setControlActProcess(createControlActProcess(subjectId));

      return query;
   }
  
   private static MCCIMT000100UV01Receiver createReceiver() {
      MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();

      receiver.setTypeCode(CommunicationFunctionType.RCV);

      MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();
      device.setDeterminerCode("INSTANCE");
      device.setClassCode(EntityClassDevice.DEV);

      II id = new II();
      id.setExtension("Common Data Layer Service");
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
      id.setExtension("Adapter Assembly Service");
      device.getId().add(id);

      sender.setDevice(device);

      return sender;
   }
   
   private static QUPCIN043100UV01QUQIMT020001UV01ControlActProcess createControlActProcess(II subjectId) {
      QUPCIN043100UV01QUQIMT020001UV01ControlActProcess controlActProcess = new QUPCIN043100UV01QUQIMT020001UV01ControlActProcess();

      controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
      controlActProcess.setClassCode(ActClassControlAct.CACT);

      CD code = new CD();
      code.setCode("QUPC_TE043100UV01");
      //code.setCodeSystem("2.16.840.1.113883.1.6");
      controlActProcess.setCode(code);

      CE priority = new CE();
      priority.setCode("R");
      controlActProcess.getPriorityCode().add(priority);

      controlActProcess.setQueryByParameter(createQueryParams(subjectId));

      return controlActProcess;
   }

   private static JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> createQueryParams(II subjectId) {
      QUPCIN043100UV01QUQIMT020001UV01QueryByParameter params = new QUPCIN043100UV01QUQIMT020001UV01QueryByParameter();

      II id = new II();
      id.setExtension("20090920010010");
      params.setQueryId(id);

      CS statusCode = new CS();
      statusCode.setCode("new");
      params.setStatusCode(statusCode);

      params.getParameterList().add(createParameterList(subjectId));

      javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "queryByParameter");
      JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> queryParams = new JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter>(xmlqname, QUPCIN043100UV01QUQIMT020001UV01QueryByParameter.class, params);

      return queryParams;
   }
   
   private static QUPCMT040300UV01ParameterList createParameterList(II subjectId) {
      QUPCMT040300UV01ParameterList paramList = new QUPCMT040300UV01ParameterList();
      org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

      // -------- Provision Code -------
      QUPCMT040300UV01CareProvisionCode careCode = new QUPCMT040300UV01CareProvisionCode();
      CD code = new CD();
      code.setCode("RXCAT");
      code.setCodeSystem("1.3.5.1.4.1.19376.1.5.3.2");
      code.setCodeSystemName("IHEActCode");
      careCode.setValue(code);
      paramList.setCareProvisionCode(factory.createQUPCMT040300UV01ParameterListCareProvisionCode(careCode));

      // -------- Record Time -------
      QUPCMT040300UV01CareRecordTimePeriod careTime = new QUPCMT040300UV01CareRecordTimePeriod();
      IVLTSExplicit time = new IVLTSExplicit();
      IVXBTSExplicit lowValue = new IVXBTSExplicit();
      lowValue.setValue("20090101");
      IVXBTSExplicit hiValue = new IVXBTSExplicit();
      hiValue.setValue("20090630");
      
      time.getContent().add(factory.createIVLTSExplicitLow(lowValue));
      time.getContent().add(factory.createIVLTSExplicitHigh(hiValue));
      careTime.setValue(time);
      paramList.setCareRecordTimePeriod(factory.createQUPCMT040300UV01ParameterListCareRecordTimePeriod(careTime));

      // -------- patient ID -------
      QUPCMT040300UV01PatientId patientID = new QUPCMT040300UV01PatientId();     
      II id = new II();
      id.setRoot("1.1");
      id.setExtension("8237363");
      patientID.setValue(id);
      paramList.setPatientId(patientID);
      
      return paramList;
   }








}
