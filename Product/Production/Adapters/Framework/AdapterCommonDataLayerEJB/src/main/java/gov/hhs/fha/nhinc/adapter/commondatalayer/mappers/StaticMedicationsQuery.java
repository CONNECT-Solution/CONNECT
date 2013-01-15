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

import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043100UV01RequestType;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01ControlActProcess;
import org.hl7.v3.QUPCIN043100UV01QUQIMT020001UV01QueryByParameter;
import org.hl7.v3.QUPCMT040300UV01ParameterList;

/**
 * 
 * @author kim
 */
public class StaticMedicationsQuery {

    private static Log logger = LogFactory.getLog(StaticMedicationsQuery.class);

    public static CareRecordQUPCIN043200UV01ResponseType createMedicationsResponse(CareRecordQUPCIN043100UV01RequestType request) {
        CareRecordQUPCIN043200UV01ResponseType response = new CareRecordQUPCIN043200UV01ResponseType();

        logger.info("Calling Static Medications Data...");

        // check properties file for test/live data mode
        if (AdapterCommonDataLayerConstants.MEDICATIONS_TEST.equalsIgnoreCase("Y")) {

            // Get Provider OID from request
            String receiverOID = request.getReceiverOID();

            // Get Patient ID from the request
            QUPCIN043100UV01QUQIMT020001UV01ControlActProcess query = request.getQuery().getControlActProcess();
            QUPCIN043100UV01QUQIMT020001UV01QueryByParameter queryByParam = query.getQueryByParameter().getValue();
            List<QUPCMT040300UV01ParameterList> paramList = queryByParam.getParameterList();
            String reqPatientID = paramList.get(0).getPatientId().getValue().getExtension();

            // Get MEDS_TAG from the properties file
            String medsTag = AdapterCommonDataLayerConstants.EMULATOR_MEDS_TAG;

            // Get MEDS_RESPONSE_TYPE from the properties file
            String medsResponseType = AdapterCommonDataLayerConstants.EMULATOR_MEDS_RESPONSE_TYPE;

            logger.debug("Retrieving Emulated Data File for : Patient ID: " + reqPatientID + ", receiverOID: " + receiverOID);
            response = StaticUtil.createCareRecordResponse(reqPatientID, receiverOID, medsTag, medsResponseType);

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
