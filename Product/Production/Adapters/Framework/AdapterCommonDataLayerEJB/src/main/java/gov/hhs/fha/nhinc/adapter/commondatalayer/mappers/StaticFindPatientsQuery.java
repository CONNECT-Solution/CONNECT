/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
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

import gov.hhs.fha.nhinc.adapter.commondatalayer.parsers.PRPAIN201305UVParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.FindPatientsPRPAIN201305UV02RequestType;
import org.hl7.v3.FindPatientsPRPAMT201310UV02ResponseType;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PRPAIN201305UV02MCCIMT000100UV01Message;
import java.io.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;

/**
 * 
 * @author kim
 */
public class StaticFindPatientsQuery {

    private static Log logger = LogFactory.getLog(StaticFindPatientsQuery.class);
    private static ObjectFactory factory = new ObjectFactory();

    public static FindPatientsPRPAMT201310UV02ResponseType createFindPatientsResponse(
            FindPatientsPRPAIN201305UV02RequestType request) {
        logger.debug("Creating Static Find Patients Data");
        FindPatientsPRPAMT201310UV02ResponseType response = new FindPatientsPRPAMT201310UV02ResponseType();
        String ptDOB = null;
        String ptFirstName = null;
        String ptLastName = null;
        String ptGender = null;

        if (request != null) {
            String receiverOID = request.getReceiverOID();

            PRPAIN201305UV02MCCIMT000100UV01Message query = request.getQuery();
            if (query != null) {
                PRPAIN201305UVParser parser = new PRPAIN201305UVParser(query);

                // get patient's first name
                ptLastName = parser.getSubjectLastName();

                // get patient's first name
                ptFirstName = parser.getSubjectFirstName();

                // get patient's gender
                ptGender = parser.getSubjectGender();

                // get patient's date of birth (if any)
                ptDOB = parser.getDateOfBirth();

                response = _getEmulatedResponse(receiverOID, ptLastName, ptFirstName, ptGender, ptDOB);
                // 20091221 - Removed Static Data and replaces with Data Files
                // response = createResponse(ptFirstName, ptLastName, ptDob,
                // ptGender);
            }
        }

        return response;
    }

    private static FindPatientsPRPAMT201310UV02ResponseType _getEmulatedResponse(String receiverOID, String lastName,
            String firstName, String gender, String dob) {
        FindPatientsPRPAMT201310UV02ResponseType response = new FindPatientsPRPAMT201310UV02ResponseType();
        response = null;

        // Get FIND_PATIENTS_TAG from the properties file
        String findPatientsTag = AdapterCommonDataLayerConstants.EMULATOR_FIND_PATIENTS_TAG;

        // Get FIND_PATIENTS_RESPONSE_TYPE from the properties file
        String findPatientsResponseType = AdapterCommonDataLayerConstants.EMULATOR_FIND_PATIENTS_RESPONSE_TYPE;

        // Get the EMULATOR_DATA_LOCATION from the properties file
        String dataPath = AdapterCommonDataLayerConstants.EMULATOR_DATA_LOCATION;

        // Build URL to file
        String responseFile = null;
        String responseNotFoundFile = null;
        String dataFile = null;
        boolean fileExists = true;

        String fileLastName = null;
        String fileFirstName = null;
        String fileGender = null;
        String fileDOB = null;

        if (lastName.length() > 0) {
            fileLastName = lastName;
        } else {
            fileLastName = AdapterCommonDataLayerConstants.EMULATOR_NO_LAST_NAME_LABEL;
        }

        if (firstName.length() > 0) {
            fileFirstName = firstName;
        } else {
            fileFirstName = AdapterCommonDataLayerConstants.EMULATOR_NO_FIRST_NAME_LABEL;
        }

        if (gender != null) {
            fileGender = gender;
        } else {
            fileGender = AdapterCommonDataLayerConstants.EMULATOR_NO_GENDER_LABEL;
        }

        if (dob != null) {
            fileDOB = dob;
        } else {
            fileDOB = AdapterCommonDataLayerConstants.EMULATOR_NO_DOB_LABEL;
        }

        responseFile = dataPath + receiverOID + "_" + fileLastName + "_" + fileFirstName + "_" + fileDOB + "_"
                + fileGender + "_" + findPatientsTag + "_" + findPatientsResponseType + ".xml";
        dataFile = responseFile;

        File testFile = new File(responseFile);
        if (!testFile.exists()) {
            logger.debug("Emulator Data File not Found..." + dataFile + "... Checking for default response data file");
            responseNotFoundFile = dataPath + receiverOID + "_"
                    + AdapterCommonDataLayerConstants.EMULATOR_NO_LAST_NAME_LABEL + "_"
                    + AdapterCommonDataLayerConstants.EMULATOR_NO_FIRST_NAME_LABEL + "_"
                    + AdapterCommonDataLayerConstants.EMULATOR_NO_DOB_LABEL + "_"
                    + AdapterCommonDataLayerConstants.EMULATOR_NO_GENDER_LABEL + "_" + findPatientsTag + "_"
                    + findPatientsResponseType + ".xml";
            dataFile = responseNotFoundFile;

            File notFoundFile = new File(responseNotFoundFile);
            if (!notFoundFile.exists()) {
                fileExists = false;
                logger.error("Emulator Data Files Not Found : 1)" + responseFile + "  2)" + responseNotFoundFile);

            }
        }

        if (fileExists) {
            logger.debug("Emulated Data File Found: " + dataFile + "  ... Creating Response");
            try {
                JAXBContext jContext = JAXBContext.newInstance(FindPatientsPRPAMT201310UV02ResponseType.class);
                Unmarshaller unmarshaller = jContext.createUnmarshaller();
                JAXBElement<FindPatientsPRPAMT201310UV02ResponseType> element = unmarshaller.unmarshal(
                        new StreamSource(dataFile), FindPatientsPRPAMT201310UV02ResponseType.class);
                response = element.getValue();
                logger.debug("Response Creation Successful");
            } catch (Exception ex) {
                logger.error("Error Extracting data from " + dataFile + "  --  " + ex);
            }
        }
        return response;
    } // _getEmulatedResponse

}
