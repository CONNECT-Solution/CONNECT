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
package gov.hhs.fha.nhinc.documentassembly.ebxml.parsers;

import gov.hhs.fha.nhinc.documentassembly.ebxml.EBXMLConstants;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kim
 */
public class AdhocQueryRequestParser extends EBXMLConstants {

   private static Log log = LogFactory.getLog(AdhocQueryRequestParser.class);
   private static final String DATE_FORMAT_FULL = "yyyyMMddhhmmssZ";
   public LinkedList<String> requiredQueryParametersList;
   public LinkedList<String> queryParametersList;
   private AdhocQueryRequest adhocQueryReq = null;
   private HashMap<String, ValueListType> querySlotHashMap = null;

   /**
    * Constructor
    * @param adhocQuery
    */
   public AdhocQueryRequestParser(AdhocQueryRequest adhocQueryReq) {
      initialize();
      this.adhocQueryReq = adhocQueryReq;
   }

   private void initialize() {

      if (requiredQueryParametersList == null) {
         requiredQueryParametersList = new LinkedList<String>();
         requiredQueryParametersList.add(EBXML_DOCENTRY_PATIENT_ID);
         requiredQueryParametersList.add(EBXML_DOCENTRY_STATUS);
      }

      if (queryParametersList == null) {
         queryParametersList = new LinkedList<String>();
         queryParametersList.add(EBXML_DOCENTRY_PATIENT_ID);
         queryParametersList.add(EBXML_DOCENTRY_CLASS_CODE);
         queryParametersList.add(EBXML_DOCENTRY_CLASS_CODE_SCHEME);
         queryParametersList.add(EBXML_DOCENTRY_PRACTICE_SETTING_CODE);
         queryParametersList.add(EBXML_DOCENTRY_PRACTICE_SETTING_CODE_SCHEME);
         queryParametersList.add(EBXML_DOCENTRY_CREATION_TIME_FROM);
         queryParametersList.add(EBXML_DOCENTRY_CREATION_TIME_TO);
         queryParametersList.add(EBXML_DOCENTRY_SERVICE_START_TIME_FROM);
         queryParametersList.add(EBXML_DOCENTRY_SERVICE_START_TIME_TO);
         queryParametersList.add(EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM);
         queryParametersList.add(EBXML_DOCENTRY_SERVICE_STOP_TIME_TO);
         queryParametersList.add(EBXML_DOCENTRY_HEALTHCARE_FAC_TYPE_CODE);
         queryParametersList.add(EBXML_DOCENTRY_HEALTHCARE_FAC_TYPE_CODE_SCHEME);
         queryParametersList.add(EBXML_DOCENTRY_EVENT_CODE_LIST);
         queryParametersList.add(EBXML_DOCENTRY_EVENT_CODE_LIST_SCHEME);
         queryParametersList.add(EBXML_DOCENTRY_CONFIDENTIALITY_CODE);
         queryParametersList.add(EBXML_DOCENTRY_FORMAT_CODE);
         queryParametersList.add(EBXML_DOCENTRY_STATUS);
      }
   }

   /**
    * Returns the patient id
    * In the form: Id^^^&OID&ISO where Id is the local patient Id number
    * [Jerry] OID is our Object ID - Need to find out what this is.
    * @return  the patient id
    */
   public String getPatientId() {
      String patientId = null;
      List<String> slotValues = extractSlotValues(adhocQueryReq.getAdhocQuery().getSlot(), EBXML_DOCENTRY_PATIENT_ID);
      if ((slotValues != null) && (!slotValues.isEmpty())) {
         String formattedPatientId = slotValues.get(0);
         patientId = PatientIdFormatUtil.parsePatientId(formattedPatientId);
      }
      return patientId;
   }

   /**
    * Returns the patient id in the form: Id^^^&OID&ISO
    * @return  the patient id
    */
   public String getISOFormatPatientId() {
      String formattedPatientId = "";
      List<String> slotValues = extractSlotValues(adhocQueryReq.getAdhocQuery().getSlot(), EBXML_DOCENTRY_PATIENT_ID);
      if ((slotValues != null) && (!slotValues.isEmpty())) {
         formattedPatientId = slotValues.get(0);
      }
      
      return formattedPatientId;
   }

   /**
    * Returns the document type in the request
    * @return  the document type (LOINC code)
    */
   public String getDocType() {
      List<String> classCodes = null;
      List<String> slotValues = extractSlotValues(adhocQueryReq.getAdhocQuery().getSlot(), EBXML_DOCENTRY_CLASS_CODE);
      if ((slotValues != null) && (!slotValues.isEmpty())) {
         classCodes = new ArrayList<String>();
         for (String slotValue : slotValues) {
            parseParamFormattedString(slotValue, classCodes);
         }
      }

      // returns the first class code in the list
      return (classCodes == null || classCodes.size() == 0) ? null : classCodes.get(0);
   }

   public Date getServiceStartTimeFrom() {
      return extractDate(EBXML_DOCENTRY_SERVICE_START_TIME_FROM);
   }

   public Date getServiceStartTimeTo() {
      return extractDate(EBXML_DOCENTRY_SERVICE_START_TIME_TO);
   }

   public Date getServiceStopTimeFrom() {
      return extractDate(EBXML_DOCENTRY_SERVICE_STOP_TIME_FROM);
   }

   public Date getServiceStopTimeTo() {
      return extractDate(EBXML_DOCENTRY_SERVICE_STOP_TIME_TO);
   }

   public Date extractCreationTimeFrom() {
      return extractDate(EBXML_DOCENTRY_CREATION_TIME_FROM);
   }

   public Date extractCreationTimeTo(List<SlotType1> slots) {
      return extractDate(EBXML_DOCENTRY_CREATION_TIME_TO);
   }

   public boolean validate() {
      if (adhocQueryReq == null) {
         return false;
      }

      if (adhocQueryReq.getResponseOption().getReturnType().compareTo("LeafClass") != 0) {
         log.warn("query:ResponseOption returnType=" + adhocQueryReq.getResponseOption().getReturnType() +
                 " changed to LeafClass");
         adhocQueryReq.getResponseOption().setReturnType("LeafClass");
      }

      if (!adhocQueryReq.getResponseOption().isReturnComposedObjects()) {
         log.warn("query:ResponseOption returnComposedObjects = false, changing to true");
         adhocQueryReq.getResponseOption().setReturnComposedObjects(true);
      }

      // check for generally required fields -- need further info from Jerry???
      // at the very least we will need to know the patient id
      if (this.getPatientId() == null || getPatientId().length() < 1) {
         return false;
      }

      return true;
   }

   private List<String> extractSlotValues(List<SlotType1> slots, String slotName) {
      List<String> returnValues = null;
      for (SlotType1 slot : slots) {
         if ((slot.getName() != null) &&
                 (slot.getName().length() > 0) &&
                 (slot.getValueList() != null) &&
                 (slot.getValueList().getValue() != null) &&
                 (slot.getValueList().getValue().size() > 0)) {

            if (slot.getName().equals(slotName)) {
               ValueListType valueListType = slot.getValueList();
               List<String> slotValues = valueListType.getValue();
               returnValues = new ArrayList<String>();
               for (String slotValue : slotValues) {
                  returnValues.add(slotValue);
               }
            }
         }
      }

      return returnValues;
   }

   public void parseParamFormattedString(String paramFormattedString, List<String> resultCollection) {
      if ((paramFormattedString != null) && (resultCollection != null)) {
         if (paramFormattedString.startsWith("(")) {
            String working = paramFormattedString.substring(1);
            int endIndex = working.indexOf(")");
            if (endIndex != -1) {
               working = working.substring(0, endIndex);
            }
            String[] multiValueString = working.split(",");
            if (multiValueString != null) {
               for (int i = 0; i < multiValueString.length; i++) {
                  String singleValue = multiValueString[i];
                  if (singleValue != null) {
                     singleValue = singleValue.trim();
                  }
                  if (singleValue.startsWith("'")) {
                     singleValue = singleValue.substring(1);
                     int endTickIndex = singleValue.indexOf("'");
                     if (endTickIndex != -1) {
                        singleValue = singleValue.substring(0, endTickIndex);
                     }
                  }
                  resultCollection.add(singleValue);
                  if (log.isDebugEnabled()) {
                     log.debug("Added single value: " + singleValue + " to query parameters");
                  }
               }
            }
         } else {
            resultCollection.add(paramFormattedString);
            if (log.isDebugEnabled()) {
               log.debug("No wrapper on status - adding status: " + paramFormattedString + " to query parameters");
            }
         }
      }
   }

   /**
    * Prepare a date format string based on the length of the date string
    * to be parsed.
    * @see gov.hhs.fha.nhinc.repository.util.DocumentLoadUtil
    * @param dateFormat Date format string (ex. yyyyMMddhhmmssZ)
    * @param dateString Date string to be parsed (ex. 19990205)
    * @return Modified format string based on the date string length (ex. yyyyMMdd)
    */
   public static String prepareDateFormatString(String dateFormat, String dateString) {
      String formatString = dateFormat;
      if ((dateString != null) && (dateFormat != null) && (dateString.length() > 0) && (dateString.length() < dateFormat.length())) {
         formatString = dateFormat.substring(0, dateString.length());
         if (log.isDebugEnabled()) {
            log.debug("New dateFormat: " + dateFormat);
         }
      }
      return formatString;
   }

   private Date parseDate(String dateString, String dateFormat) {
      Date parsed = null;
      if ((dateString != null) && (dateFormat != null)) {
         try {
            String formatString = prepareDateFormatString(dateFormat, dateString);
            parsed = new SimpleDateFormat(formatString).parse(dateString);
         } catch (Throwable t) {
            log.warn("Error parsing '" + dateString + "' using format: '" + dateFormat + "'", t);
         }
      }
      return parsed;
   }

   private Date extractDate(String key) {
      Date extractedDate = null;
      List<String> slotValues = extractSlotValues(adhocQueryReq.getAdhocQuery().getSlot(), key);
      if ((slotValues != null) && (!slotValues.isEmpty())) {
         extractedDate = parseDate(slotValues.get(0), DATE_FORMAT_FULL);
      }
      return extractedDate;
   }
}
