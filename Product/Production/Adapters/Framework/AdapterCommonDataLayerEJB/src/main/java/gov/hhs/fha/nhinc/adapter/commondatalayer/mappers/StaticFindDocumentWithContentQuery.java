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

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.FindDocumentWithContentRCMRIN000031UV01RequestType;
import org.hl7.v3.FindDocumentWithContentRCMRIN000032UV01ResponseType;
import org.hl7.v3.RCMRIN000031UV01QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.RCMRMT000003UV01QueryByParameter;
import org.hl7.v3.RCMRMT000003UV01PatientId;
import gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.constants.AdapterCommonDataLayerConstants;

/**
 *
 * @author Jason
 */
public class StaticFindDocumentWithContentQuery {

    private static Log logger = LogFactory.getLog(StaticFindDocumentWithContentQuery.class);

    public static FindDocumentWithContentRCMRIN000032UV01ResponseType createFindDocumentWithContentResponse(FindDocumentWithContentRCMRIN000031UV01RequestType request) {
        FindDocumentWithContentRCMRIN000032UV01ResponseType response = new FindDocumentWithContentRCMRIN000032UV01ResponseType();

        logger.info("Calling Static Find Document With Content Data...");

        // check properties file for test/live data mode
        if (AdapterCommonDataLayerConstants.FDWC_TEST.equalsIgnoreCase("Y")) {

            // Get Provider OID from request
            String receiverOID = request.getQuery().getId().getRoot();

            // Get Patient ID from the request
            RCMRIN000031UV01QUQIMT021001UV01ControlActProcess query = request.getQuery().getControlActProcess();
            RCMRMT000003UV01QueryByParameter queryByParam = query.getQueryByParameter().getValue();
            List<RCMRMT000003UV01PatientId> patientIdList = queryByParam.getPatientId();
            String reqPatientID = patientIdList.get(0).getValue().getExtension();
            String documentType = request.getQuery().getControlActProcess().getQueryByParameter().getValue().getClinicalDocumentCode().getValue().getValue().getCode();

            logger.debug("Retrieving Emulated Data File for : Patient ID " + reqPatientID + ",receiverOID: " + receiverOID + ", DocType: " + documentType);
            response = StaticUtil.createFindDocumentWithContentResponse(reqPatientID, receiverOID, documentType);

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
