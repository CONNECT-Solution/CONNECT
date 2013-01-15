/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved.
 *
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201307UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UVParameterList;
import org.hl7.v3.PRPAMT201307UVQueryByParameter;
import org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType;
import org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType;
import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;

/**
 *
 * @author kim
 */
public class StaticPatientDemographicsQuery {

    private static Log logger = LogFactory.getLog(StaticPatientDemographicsQuery.class);

    public static PatientDemographicsPRPAMT201303UV02ResponseType createPatientDemographicsResponse(PatientDemographicsPRPAIN201307UV02RequestType request) {
        PatientDemographicsPRPAMT201303UV02ResponseType response = new PatientDemographicsPRPAMT201303UV02ResponseType();

        logger.info("Calling Static Patient Info Data...");

        // check properties file for test/live data mode
        if (AdapterCommonDataLayerConstants.PATIENT_INFO_TEST.equalsIgnoreCase("Y")) {

            String receiverOID = request.getReceiverOID();
            PRPAIN201307UV02QUQIMT021001UV01ControlActProcess query = request.getQuery().getControlActProcess();

            PRPAMT201307UVQueryByParameter queryByParam = query.getQueryByParameter().getValue();
            PRPAMT201307UVParameterList paramList = queryByParam.getParameterList();
            String reqPatientID = paramList.getPatientIdentifier().get(0).getValue().get(0).getExtension();

            logger.debug("Retrieving Emulated Data File for : Patient ID: " + reqPatientID + ", receiverOID: " + receiverOID);
            response = StaticUtil.createPatientDemoResponse(reqPatientID, receiverOID);

        } else {
            logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
            logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
            logger.debug(" Insert Adapter Agency specific dynamic document data accessors here ");
            logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
            logger.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
        }

        return response;
    }
}
