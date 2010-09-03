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

import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCMT040300UV01ParameterList;
import java.io.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author kim
 */
public class StaticMedicationsQuery {

   private static Log logger = LogFactory.getLog(StaticMedicationsQuery.class);
   private static org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

   public static CareRecordQUPCIN043200UV01ResponseType createMedicationsResponse(CareRecordQUPCIN043100UV01RequestType request) {
      CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();

      //static vs. live data check
      if (AdapterCommonDataLayerConstants.MEDICATIONS_TEST.equalsIgnoreCase("Y")) {
         logger.info("Calling Static Medications Data...");

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

       // Get MEDS_TAG from the properties file
       String medsTag = AdapterCommonDataLayerConstants.EMULATOR_MEDS_TAG;

       // Get MEDS_RESPONSE_TYPE from the properties file
       String medsResponseType = AdapterCommonDataLayerConstants.EMULATOR_MEDS_RESPONSE_TYPE;

       // Get the EMULATOR_DATA_LOCATION from the properties file
       String dataPath = AdapterCommonDataLayerConstants.EMULATOR_DATA_LOCATION;

       // Build URL to file
       String responseFile = null;
       String responseNotFoundFile = null;
       String dataFile = null;
       boolean fileExists = true;

       responseFile = dataPath + receiverOID + "_" + patientID + "_" + medsTag + "_" + medsResponseType + ".xml";
       dataFile = responseFile;

       File testFile = new File(responseFile);
       if (!testFile.exists())
       {
           String notFoundTag = AdapterCommonDataLayerConstants.EMULATOR_NO_PATIENT_ID_LABEL;
           responseNotFoundFile = dataPath + receiverOID + "_" + notFoundTag + "_" + medsTag + "_" + medsResponseType + ".xml";
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



}
