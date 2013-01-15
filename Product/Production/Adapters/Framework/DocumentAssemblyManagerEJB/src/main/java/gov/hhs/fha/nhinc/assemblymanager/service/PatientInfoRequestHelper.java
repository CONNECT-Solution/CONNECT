/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.assemblymanager.service;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.utils.UUIDGenerator;
import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.CD;

import org.hl7.v3.CE;

import org.hl7.v3.CS;

import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;

import org.hl7.v3.MCCIMT000100UV01Device;

import org.hl7.v3.MCCIMT000100UV01Receiver;

import org.hl7.v3.MCCIMT000100UV01Sender;

import org.hl7.v3.ObjectFactory;

import org.hl7.v3.PRPAIN201307UV02MCCIMT000100UV01Message;

import org.hl7.v3.PRPAIN201307UV02QUQIMT021001UV01ControlActProcess;

import org.hl7.v3.PRPAMT201307UVParameterList;

import org.hl7.v3.PRPAMT201307UVPatientIdentifier;

import org.hl7.v3.PRPAMT201307UVQueryByParameter;

import org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType;

import org.hl7.v3.TELExplicit;

import org.hl7.v3.TSExplicit;

import org.hl7.v3.XActMoodIntentEvent;

/**
 * 
 * 
 * 
 * @author kim
 */
public class PatientInfoRequestHelper {

    public final static String PATIENT_INFO_INTERACTION = "PRPA_IN201307UV02";

    public static PatientDemographicsPRPAIN201307UV02RequestType createPatientDemographicsRequest(II subjectId) {

        PatientDemographicsPRPAIN201307UV02RequestType msg = new PatientDemographicsPRPAIN201307UV02RequestType();

        msg.setLocalDeviceId(AssemblyConstants.ORGANIZATION_OID);
        msg.setReceiverOID(AssemblyConstants.ORGANIZATION_OID);
        msg.setSenderOID(AssemblyConstants.ORGANIZATION_OID);
        msg.setQuery(build201307(subjectId));

        return msg;

    }

    public static PRPAIN201307UV02MCCIMT000100UV01Message build201307(II subjectId) {

        PRPAIN201307UV02MCCIMT000100UV01Message query = new PRPAIN201307UV02MCCIMT000100UV01Message();

        II id = new II();
        id.setRoot(AssemblyConstants.ORGANIZATION_OID);
        id.setExtension(UUIDGenerator.generateRandomUUID());
        query.setId(id);

        TSExplicit creationTime = new TSExplicit();

        creationTime.setValue("20090920011010.005");

        query.setCreationTime(creationTime);

        II interactionId = new II();

        interactionId.setRoot("2.16.840.1.113883.5");

        interactionId.setExtension(PATIENT_INFO_INTERACTION);

        query.setInteractionId(interactionId);

        CS processingCode = new CS();

        processingCode.setCode("P");

        query.setProcessingCode(processingCode);

        CS processingModeCode = new CS();

        processingModeCode.setCode("R");

        query.setProcessingModeCode(processingModeCode);

        CS ackCode = new CS();

        ackCode.setCode("AL");

        query.setAcceptAckCode(ackCode);

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

        //added to work with request to AllScripts
        device.setClassCode(EntityClassDevice.DEV);

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200");
        device.getId().add(id);

        TELExplicit url = new TELExplicit();

        url.setValue("http://localhost:8080/NhinConnect/CommonDataLayerService");

        device.getTelecom().add(url);

        receiver.setDevice(device);

        return receiver;

    }

    private static MCCIMT000100UV01Sender createSender() {

        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();

        sender.setTypeCode(CommunicationFunctionType.SND);

        MCCIMT000100UV01Device device = new MCCIMT000100UV01Device();

        device.setDeterminerCode("INSTANCE");

        //added to work with request to AllScripts
        device.setClassCode(EntityClassDevice.DEV);

        II id = new II();
        id.setRoot("2.16.840.1.113883.3.200");
        device.getId().add(id);

        sender.setDevice(device);

        return sender;

    }

    private static PRPAIN201307UV02QUQIMT021001UV01ControlActProcess createControlActProcess(II subjectId) {

        ObjectFactory hl7Factory = new ObjectFactory();

        PRPAIN201307UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201307UV02QUQIMT021001UV01ControlActProcess();

        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);

        //added to work with request to AllScripts
        controlActProcess.setClassCode(ActClassControlAct.CACT);

        CD code = new CD();
        code.setCode("PRPA_TE201307UV02");
        code.setCodeSystem("2.16.840.1.113883.1.6");
        controlActProcess.setCode(code);

        CE priority = new CE();

        priority.setCode("R");

        controlActProcess.getPriorityCode().add(priority);

        controlActProcess.setQueryByParameter(hl7Factory.createPRPAIN201307UV02QUQIMT021001UV01ControlActProcessQueryByParameter(createQueryParams(subjectId)));

        return controlActProcess;

    }

    private static PRPAMT201307UVQueryByParameter createQueryParams(II subjectId) {

        PRPAMT201307UVQueryByParameter params = new PRPAMT201307UVQueryByParameter();

        II id = new II();

        id.setRoot("12345");

        params.setQueryId(id);

        CS statusCode = new CS();

        statusCode.setCode("new");

        params.setStatusCode(statusCode);

        params.setParameterList(createParamList(subjectId));

        return params;

    }

    private static PRPAMT201307UVParameterList createParamList(II subjectId) {

        PRPAMT201307UVParameterList paramList = new PRPAMT201307UVParameterList();

        // Set the subject Id

        paramList.getPatientIdentifier().add(createPatientIdentifier(subjectId));

        return paramList;

    }

    private static PRPAMT201307UVPatientIdentifier createPatientIdentifier(II subjectId) {

        PRPAMT201307UVPatientIdentifier id = new PRPAMT201307UVPatientIdentifier();

        if (subjectId != null) {

            id.getValue().add(subjectId);

        }

        return id;

    }
}
