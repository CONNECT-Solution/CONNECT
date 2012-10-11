/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved.
 *
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;

import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;
import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.exceptions.SimpleErrorHandler;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;
import org.hl7.v3.FindPatientsPRPAMT201310UV02ResponseType;
import org.hl7.v3.FindDocumentWithContentRCMRIN000032UV01ResponseType;
import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.bind.*;
import javax.xml.parsers.*;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;

/**
 * 
 * @author kim
 */
public class StaticUtil {
    private static String dataPath = AdapterCommonDataLayerConstants.EMULATOR_DATA_LOCATION;
    private static Log logger = LogFactory.getLog(StaticUtil.class);

    public static PatientDemographicsPRPAMT201303UV02ResponseType createPatientDemoResponse(String patientID, String receiverOID)
    {
        PatientDemographicsPRPAMT201303UV02ResponseType response = new PatientDemographicsPRPAMT201303UV02ResponseType();
        response = null;

        // Get PATIENT_INFO_TAG from the properties file
        String patientInfoTag = AdapterCommonDataLayerConstants.EMULATOR_PATIENT_INFO_TAG;

        // Get PATIENT_INFO_RESPONSE_TYPE from the properties file
        String patientInfoResponseType = AdapterCommonDataLayerConstants.EMULATOR_PATIENT_INFO_RESPONSE_TYPE;

        String dataFile = _getResponseFile(receiverOID, patientID, null, null, null, null, null, patientInfoTag, patientInfoResponseType);

        if(dataFile != null)
        {
            try
            {
                org.hl7.v3.ObjectFactory obf = new org.hl7.v3.ObjectFactory();
                JAXBElement element = _getDataFromFile(dataFile);
                JAXBElement<PatientDemographicsPRPAMT201303UV02ResponseType> crr = obf.createPatientDemographicsPRPAMT201303UV02Response((PatientDemographicsPRPAMT201303UV02ResponseType)element.getValue());
                logger.debug("Response of type "  + crr.getClass().getName() + " has been created from Emulated Data File");

                //set response equal to Care Record created from Object Factory
                response = crr.getValue();

                logger.debug("Response Creation Successful for: " + crr.getClass().getName());
            }
            catch (Exception ex)
            {
                logger.error("Error extracting data from " + dataFile + "--" + ex);
            }
        }

        return response;
    }

    public static CareRecordQUPCIN043200UV01ResponseType createCareRecordResponse(String patientID, String receiverOID, String tag, String responseType)
    {
        CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();
        response = null;

        String dataFile = _getResponseFile(receiverOID, patientID, null, null, null, null, null, tag, responseType);

        try
        {
            org.hl7.v3.ObjectFactory obf = new org.hl7.v3.ObjectFactory();
            JAXBElement element = _getDataFromFile(dataFile);
            JAXBElement<CareRecordQUPCIN043200UV01ResponseType> crr = obf.createCareRecordQUPCIN043200UV01Response((CareRecordQUPCIN043200UV01ResponseType)element.getValue());
            logger.debug("Response of type "  + crr.getClass().getName() + " has been created from Emulated Data File");

            //set response equal to Care Record created from Object Factory
            response = crr.getValue();

            logger.debug("Response Creation Successful for: " + crr.getClass().getName());
        }
        catch (Exception ex)
        {
            logger.error("Error extracting data from " + dataFile + "--" + ex);
        }

        return response;
    }

    public static FindPatientsPRPAMT201310UV02ResponseType createFindPatientsResponse(String receiverOID, String lastName, String firstName, String gender, String dob)
    {
        FindPatientsPRPAMT201310UV02ResponseType response = new FindPatientsPRPAMT201310UV02ResponseType();
        response = null;

        // Get FIND_PATIENTS_TAG from the properties file
        String findPatientsTag = AdapterCommonDataLayerConstants.EMULATOR_FIND_PATIENTS_TAG;

        // Get FIND_PATIENTS_RESPONSE_TYPE from the properties file
        String findPatientsResponseType = AdapterCommonDataLayerConstants.EMULATOR_FIND_PATIENTS_RESPONSE_TYPE;

        String dataFile = _getResponseFile(receiverOID, null, lastName, firstName, gender, dob, null, findPatientsTag, findPatientsResponseType);

        try
        {
            org.hl7.v3.ObjectFactory obf = new org.hl7.v3.ObjectFactory();
            JAXBElement element = _getDataFromFile(dataFile);
            JAXBElement<FindPatientsPRPAMT201310UV02ResponseType> crr = obf.createFindPatientsPRPAMT201310UV02Response((FindPatientsPRPAMT201310UV02ResponseType)element.getValue());
            logger.debug("Response of type "  + crr.getClass().getName() + " has been created from Emulated Data File");

            //set response equal to Care Record created from Object Factory
            response = crr.getValue();

            logger.debug("Response Creation Successful for: " + crr.getClass().getName());
        }
        catch (Exception ex)
        {
            logger.error("Error extracting data from " + dataFile + "--" + ex);
        }

        return response;

    }

    public static FindDocumentWithContentRCMRIN000032UV01ResponseType createFindDocumentWithContentResponse(String patientID, String receiverOID, String documentType)
    {

        FindDocumentWithContentRCMRIN000032UV01ResponseType response = new FindDocumentWithContentRCMRIN000032UV01ResponseType();
        response = null;

        // Get FIND_DOCUMENT_TAG from the properties file
        String findDocumentWithContentTag = AdapterCommonDataLayerConstants.EMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG + "_" + documentType;

        // Get FIND_DOCUMENT_RESPONSE_TYPE fro the properties file
        String findDocumentWithContentResponseType = AdapterCommonDataLayerConstants.EMULATOR_FIND_DOCUMENT_WITH_CONTENT_RESPONSE_TYPE;

        String dataFile = _getResponseFile(receiverOID, patientID, null, null, null, null, null, findDocumentWithContentTag, findDocumentWithContentResponseType);

        try
        {
            org.hl7.v3.ObjectFactory obf = new org.hl7.v3.ObjectFactory();
            JAXBElement element = _getDataFromFile(dataFile);
            JAXBElement<FindDocumentWithContentRCMRIN000032UV01ResponseType> crr = obf.createFindDocumentWithContentRCMRIN000032UV01Response((FindDocumentWithContentRCMRIN000032UV01ResponseType)element.getValue());
            logger.debug("Response of type " + crr.getClass().getName() + " has been created from Emulated Data File");

            //set response equal to Find Document created from Object Factory
            response = crr.getValue();

            logger.debug("Response Creation Successful for : " + crr.getClass().getName());

        }
        catch (Exception ex)
        {
            logger.error("Error extracting data from " + dataFile + "--" + ex);
        }

        return response;
    }

    private static String _getResponseFile(String receiverOID, String patientID, String lastName, String firstName, String gender, String dob, String providerID, String tag, String responseType)
    {
        String dataFile = null;

        // Build URL to file
        String responseFile = null;
        String responseNotFoundFile = null;
        boolean fileExists = true;

        if(tag.equals(AdapterCommonDataLayerConstants.EMULATOR_ALLERGIES_TAG) ||
           tag.equals(AdapterCommonDataLayerConstants.EMULATOR_MEDS_TAG) ||
           tag.equals(AdapterCommonDataLayerConstants.EMULATOR_PROBLEMS_TAG) ||
           tag.equals(AdapterCommonDataLayerConstants.EMULATOR_PATIENT_INFO_TAG) ||
           tag.equals(AdapterCommonDataLayerConstants.EMULATOR_FIND_DOCUMENT_TAG) ||
           tag.contains(AdapterCommonDataLayerConstants.EMULATOR_FIND_DOCUMENT_WITH_CONTENT_TAG))
        {
            responseFile = dataPath + receiverOID + "_" + patientID + "_" + tag + "_" + responseType + ".xml";
            responseNotFoundFile = dataPath + receiverOID + "_" + AdapterCommonDataLayerConstants.EMULATOR_NO_PATIENT_ID_LABEL + "_" + tag + "_" + responseType + ".xml";
        }
        else if(tag.equals(AdapterCommonDataLayerConstants.EMULATOR_FIND_PATIENTS_TAG))
        {
            String fileLastName = null;
            String fileFirstName = null;
            String fileGender = null;
            String fileDOB = null;

            if (lastName != null)
            {
                if(lastName.length() > 0)
                {
                    fileLastName = lastName;
                }
                else
                {
                    fileLastName = AdapterCommonDataLayerConstants.EMULATOR_NO_LAST_NAME_LABEL;
                }
            }
            else
            {
                fileLastName = AdapterCommonDataLayerConstants.EMULATOR_NO_LAST_NAME_LABEL;
            }

            if (firstName != null)
            {
                if (firstName.length() > 0)
                {
                    fileFirstName = firstName;
                }
                else
                {
                    fileFirstName = AdapterCommonDataLayerConstants.EMULATOR_NO_FIRST_NAME_LABEL;
                }
            }
            else
            {
                fileFirstName = AdapterCommonDataLayerConstants.EMULATOR_NO_FIRST_NAME_LABEL;
            }

            if (gender != null)
            {
                if (gender.length() > 0)
                {
                    fileGender = gender;
                }
                else
                {
                    fileGender = AdapterCommonDataLayerConstants.EMULATOR_NO_GENDER_LABEL;
                }
            }
            else
            {
                fileGender = AdapterCommonDataLayerConstants.EMULATOR_NO_GENDER_LABEL;
            }

            if (dob != null)
            {
                if (dob.length() > 0 && dob != null)
                {
                    fileDOB = dob;
                }
                else
                {
                    fileDOB = AdapterCommonDataLayerConstants.EMULATOR_NO_DOB_LABEL;
                }
            }
            else
            {
                fileDOB = AdapterCommonDataLayerConstants.EMULATOR_NO_DOB_LABEL;
            }

            responseFile = dataPath + receiverOID + "_" + fileLastName + "_" + fileFirstName + "_" + fileDOB + "_" + fileGender + "_" + tag + "_" + responseType + ".xml";

            responseNotFoundFile = dataPath + receiverOID + "_" +
                    AdapterCommonDataLayerConstants.EMULATOR_NO_LAST_NAME_LABEL + "_" +
                    AdapterCommonDataLayerConstants.EMULATOR_NO_FIRST_NAME_LABEL + "_" +
                    AdapterCommonDataLayerConstants.EMULATOR_NO_DOB_LABEL + "_" +
                    AdapterCommonDataLayerConstants.EMULATOR_NO_GENDER_LABEL + "_" +
                    tag + "_" + responseType + ".xml";
        }
        else if(tag.equals(AdapterCommonDataLayerConstants.EMULATOR_FIND_PROVIDERS_TAG))
        {
            responseFile = dataPath + receiverOID + "_" + providerID + "_" + tag + "_" + responseType + ".xml";
            responseNotFoundFile = dataPath + receiverOID + "_" + AdapterCommonDataLayerConstants.EMULATOR_NO_PROVIDER_ID_LABEL + "_" + tag + "_" + responseType + ".xml";
        }

        File testFile = new File(responseFile);
        if (!testFile.exists())
        {

            File notFoundFile = new File(responseNotFoundFile);
            if(!notFoundFile.exists())
            {
                fileExists = false;
                dataFile = null;
                logger.error("Emulator Data Files Not Found : 1)" + responseFile + " 2)" + responseNotFoundFile);
            }
            else
            {
                dataFile = responseNotFoundFile;
            }
        }
        else
        {
            dataFile = responseFile;
        }

        if(fileExists)
        {
            logger.debug("Emulated Data File Found: " + dataFile + " ... Creating Response...");
        }

        return dataFile;
    }

    private static JAXBElement _getDataFromFile(String dataFile)
    {
        JAXBElement element = null;

        try
        {
            //read file in line by line using a String Builder to build the xml doc from the FileInputStream
            FileInputStream fis = new FileInputStream(dataFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();

            while(true)
            {
                //read line of xml file
                String line = br.readLine();
                if(line == null)
                {
                    break;
                }

                //trim line to remove carriage returns and line terminations
                sb.append(line.trim());
            }

            //set temporary string to string buffer
            String tempString = sb.toString();

            //get JAXBContext instance
            JAXBContextHandler jch = new JAXBContextHandler();
            JAXBContext jContext = jch.getJAXBContext("org.hl7.v3");
            
            //create unmarshaller
            Unmarshaller unmarshaller = jContext.createUnmarshaller();
            UnmarshallerHandler umh = unmarshaller.getUnmarshallerHandler();

            //create new SAX XML parser
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);

            //get parser from factory
            SAXParser parser = spf.newSAXParser();

            //create XML reader with error handler - Errors will display in log
            XMLReader xmlReader = parser.getXMLReader();
            xmlReader.setErrorHandler(new SimpleErrorHandler());
            xmlReader.setContentHandler(umh);

            //parse the XML response from the temp String
            xmlReader.parse(new InputSource(new StringReader(tempString)));

            element = (JAXBElement)umh.getResult();
        }
        catch (Exception ex)
        {
            logger.error("Problem parsing data file: " + ex.getMessage());
        }

        return element;
    }
}
