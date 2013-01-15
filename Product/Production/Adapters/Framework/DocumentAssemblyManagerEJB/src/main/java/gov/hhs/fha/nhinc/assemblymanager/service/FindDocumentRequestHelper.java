/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.assemblymanager.service;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.utils.DateUtil;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import java.util.Date;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.FindDocumentRCMRIN000031UV01RequestType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.RCMRIN000031UV01MCCIMT000100UV01Message;
import org.hl7.v3.RCMRIN000031UV01QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.RCMRMT000003UV01QueryByParameter;
import org.hl7.v3.RCMRMT000003UV01ClinicalDocumentCode;
import org.hl7.v3.RCMRMT000003UV01PatientId;
import org.hl7.v3.TS;
import org.hl7.v3.XActMoodIntentEvent;
import org.hl7.v3.FindDocumentWithContentRCMRIN000031UV01RequestType;

/**
 * This
 * @author kim
 */
public class FindDocumentRequestHelper {

    private static ObjectFactory factory = null;
    private static String homeOID = "";

    static {
        homeOID = AssemblyConstants.ORGANIZATION_OID;
        factory = new ObjectFactory();
    }

    public static FindDocumentRCMRIN000031UV01RequestType findDocumentRequest(II subjectId) {
        FindDocumentRCMRIN000031UV01RequestType msg = new FindDocumentRCMRIN000031UV01RequestType();

        msg.setQuery(build000031(subjectId, null, null, null));

        return msg;
    }

    public static FindDocumentRCMRIN000031UV01RequestType findDocumentRequest(II subjectId, String dataStartDate, String dataEndDate) {
        FindDocumentRCMRIN000031UV01RequestType msg = new FindDocumentRCMRIN000031UV01RequestType();

        if (dataStartDate == null) {
            msg.setQuery(build000031(subjectId, null, null, null));
        } else {
            msg.setQuery(build000031(subjectId, dataStartDate, dataEndDate, null));
        }

        return msg;
    }

    public static FindDocumentWithContentRCMRIN000031UV01RequestType findDocumentWithContentRequest(II subjectId, String documentClassCode) {
        FindDocumentWithContentRCMRIN000031UV01RequestType msg = new FindDocumentWithContentRCMRIN000031UV01RequestType();

        msg.setQuery(build000031(subjectId, null, null, documentClassCode));

        return msg;
    }

    public static FindDocumentWithContentRCMRIN000031UV01RequestType findDocumentWithContentRequest(II subjectId, String dataStartDate, String dataEndDate) {
        FindDocumentWithContentRCMRIN000031UV01RequestType msg = new FindDocumentWithContentRCMRIN000031UV01RequestType();

        if (dataStartDate == null) {
            msg.setQuery(build000031(subjectId, null, null, null));
        } else {
            msg.setQuery(build000031(subjectId, dataStartDate, dataEndDate, null));
        }

        return msg;
    }

    private static RCMRIN000031UV01MCCIMT000100UV01Message build000031(II subjectId, String dataStartDate, String dataEndDate, String documentClassCode) {

        RCMRIN000031UV01MCCIMT000100UV01Message query = new RCMRIN000031UV01MCCIMT000100UV01Message();

        II id = new II();
        id.setRoot(homeOID);
        id.setExtension(UUIDGenerator.generateRandomUUID());
        query.setId(id);

        TS creationTime = new TS();
        creationTime.setValue(DateUtil.convertToCDATime(new Date()));
        query.setCreationTime(creationTime);

        II interactionId = new II();
        interactionId.setRoot("2.16.840.1.113883.5");
        interactionId.setExtension(AssemblyConstants.CARE_RECORD_QUERY_INTERACTION_ID);
        query.setInteractionId(interactionId);

        // Set the receiver and sender
        query.getReceiver().add(createReceiver());
        query.setSender(createSender());

        // create ControlActProcess object
        RCMRIN000031UV01QUQIMT021001UV01ControlActProcess controlActProcess = createControlActProcess();

        // create QueryByParameter object
        RCMRMT000003UV01QueryByParameter queryParams = createQueryParams();

        RCMRMT000003UV01PatientId patId = new RCMRMT000003UV01PatientId();
        patId.setValue(subjectId);
        queryParams.getPatientId().add(patId);

        CE docCodeId = new CE();
        //set the correct classcode
        // docCodeId.setCode(AssemblyConstants.C62_CLASS_CODE);
        if (documentClassCode != null) {
            docCodeId.setCode(documentClassCode);
        } else {
            docCodeId.setCode(AssemblyConstants.C62_CLASS_CODE);
        }

        docCodeId.setCodeSystem(CDAConstants.LOINC_CODE_SYS_OID);
        docCodeId.setCodeSystemName(CDAConstants.LOINC_CODE_SYS_NAME);

        if (documentClassCode != null && documentClassCode.equals(AssemblyConstants.C62_RR_CLASS_CODE)) {
            docCodeId.setDisplayName(AssemblyConstants.C62_RR_DISPLAY_NAME);
        } else {
            docCodeId.setDisplayName(AssemblyConstants.C62_DISPLAY_NAME);
        }

        RCMRMT000003UV01ClinicalDocumentCode docCode = new RCMRMT000003UV01ClinicalDocumentCode();
        docCode.setValue(docCodeId);

        queryParams.setClinicalDocumentCode(factory.createRCMRMT000003UV01QueryByParameterClinicalDocumentCode(docCode));

        // set QueryByParameter in ControlActProcess object
        controlActProcess.setQueryByParameter(factory.createRCMRIN000031UV01QUQIMT021001UV01ControlActProcessQueryByParameter(queryParams));

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

    private static RCMRIN000031UV01QUQIMT021001UV01ControlActProcess createControlActProcess() {
        RCMRIN000031UV01QUQIMT021001UV01ControlActProcess controlActProcess =
            new RCMRIN000031UV01QUQIMT021001UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setClassCode(ActClassControlAct.CACT);

        CD code = new CD();
        code.setCode(AssemblyConstants.CARE_RECORD_QUERY_TRIGGER);
        controlActProcess.setCode(code);

        CE priority = new CE();
        priority.setCode("R");
        controlActProcess.getPriorityCode().add(priority);

        return controlActProcess;
    }

    private static RCMRMT000003UV01QueryByParameter createQueryParams() {
        RCMRMT000003UV01QueryByParameter queryParams = new RCMRMT000003UV01QueryByParameter();

        II id = new II();
        id.setExtension(UUIDGenerator.generateRandomUUID());
        id.setRoot(homeOID);
        queryParams.setQueryId(id);

        CS statusCode = new CS();
        statusCode.setCode("new");
        queryParams.setStatusCode(statusCode);


        return queryParams;
    }
}
