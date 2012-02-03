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
package gov.hhs.fha.nhinc.adapter.reidentification;

import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Device;
import gov.hhs.fha.nhinc.adapter.reidentification.data.PseudonymMap;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Constants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201310Transforms;

/**
 * This provides the implementation of the adapter reidentification web service
 */
class AdapterReidentificationImpl {
    //localDeviceId is currently just set to a default value
    private String localDeviceId = HL7Constants.DEFAULT_LOCAL_DEVICE_ID;

    PIXConsumerPRPAIN201310UVRequestType getRealIdentifier(PIXConsumerPRPAIN201309UVRequestType realIdentifierRequest) {
        PIXConsumerPRPAIN201310UVRequestType ret201310RequestType = new PIXConsumerPRPAIN201310UVRequestType();

        if (realIdentifierRequest != null) {
            ret201310RequestType.setAssertion(realIdentifierRequest.getAssertion());

            PRPAIN201310UV02 response201310 = null;
            PRPAIN201309UV02 request201309 = realIdentifierRequest.getPRPAIN201309UV02();
            if (request201309 != null) {
                // extract information from the 201309 request message
                // extract sender OID and set to the receiver OID for the 201310
                String receiverOID = extractSenderOID(request201309);

                // extract receiver OID and set to the sender OID for the 201310
                String senderOID = extractReceiverOID(request201309);

                if (senderOID != null && receiverOID != null) {
                    // extract queryByParameter
                    JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = extractQueryByParameter(request201309);

                    // extract pseudonymPatientId and pseudonymPatientIdAssigningAuthority
                    // and translate to real patient IDs
                    String realPatientId = null;
                    String realAssigningAuthority = null;
                    if (queryByParameter != null) {
                        II pseudoPatientItem = extractPseudoPatientIds(queryByParameter);
                        if (pseudoPatientItem != null) {
                            String pseudonymPatientIdAssigningAuthority = pseudoPatientItem.getRoot();
                            String pseudonymPatientId = pseudoPatientItem.getExtension();
                            if (pseudonymPatientIdAssigningAuthority != null && pseudonymPatientId != null) {
                                // look up real patient id and assigning authority
                                PseudonymMapManager.readMap();
                                PseudonymMap pseudonymMap = PseudonymMapManager.findPseudonymMap(pseudonymPatientIdAssigningAuthority, pseudonymPatientId);
                                if (pseudonymMap != null) {
                                    realPatientId = pseudonymMap.getRealPatientId();
                                    realAssigningAuthority = pseudonymMap.getRealPatientIdAssigningAuthority();
                                }
                            }
                        }
                    }
                    if (realPatientId != null && realAssigningAuthority != null) {
                        response201310 = HL7PRPA201310Transforms.createPRPA201310(realPatientId, realAssigningAuthority, localDeviceId, senderOID, receiverOID, queryByParameter);
                    } else {
                        response201310 = HL7PRPA201310Transforms.createFaultPRPA201310(senderOID, receiverOID);
                    }
                } else {
                    response201310 = HL7PRPA201310Transforms.createFaultPRPA201310();
                }
            } else {
                //create error response
                response201310 = HL7PRPA201310Transforms.createFaultPRPA201310();
            }
            ret201310RequestType.setPRPAIN201310UV02(response201310);
        }
        return ret201310RequestType;
    }

    private II extractPseudoPatientIds(JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter) {
        II pseudoPatientItem = null;
        if (queryByParameter != null) {
            PRPAMT201307UV02ParameterList parameterList = queryByParameter.getValue().getParameterList();
            if (parameterList != null) {
                PRPAMT201307UV02PatientIdentifier patientIdentifier = null;
                List<PRPAMT201307UV02PatientIdentifier> patientIdentifierList = parameterList.getPatientIdentifier();
                if (patientIdentifierList != null && !patientIdentifierList.isEmpty()) {
                    patientIdentifier = patientIdentifierList.get(0);
                    List<II> iiList = patientIdentifier.getValue();
                    if (iiList != null && !iiList.isEmpty()) {
                        pseudoPatientItem = iiList.get(0);
                    }
                }
            }
        }
        return pseudoPatientItem;
    }

    private JAXBElement<PRPAMT201307UV02QueryByParameter> extractQueryByParameter(PRPAIN201309UV02 request201309) {
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = null;
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlAct = request201309.getControlActProcess();
        if (controlAct != null) {
            queryByParameter = controlAct.getQueryByParameter();
        }
        return queryByParameter;
    }

    private String extractReceiverOID(PRPAIN201309UV02 request201309) {
        String receiverOID201309 = null;
        List<MCCIMT000100UV01Receiver> receiver201309List = request201309.getReceiver();
        if (receiver201309List != null && !receiver201309List.isEmpty()) {
            MCCIMT000100UV01Receiver receiver201309 = receiver201309List.get(0);
            MCCIMT000100UV01Device receiverDevice = receiver201309.getDevice();
            if (receiverDevice != null) {
                List<II> listDevices = receiverDevice.getId();
                if (listDevices != null && !listDevices.isEmpty()) {
                    for (II item : listDevices) {
                        receiverOID201309 = item.getRoot();
                    }
                }
            }
        }
        return receiverOID201309;
    }

    private String extractSenderOID(PRPAIN201309UV02 request201309) {
        String senderOID201309 = null;
        MCCIMT000100UV01Sender sender201309 = request201309.getSender();
        if (sender201309 != null) {
            MCCIMT000100UV01Device senderDevice = sender201309.getDevice();
            if (senderDevice != null) {
                List<II> listDevices = senderDevice.getId();
                if (listDevices != null && !listDevices.isEmpty()) {
                    for (II item : listDevices) {
                        senderOID201309 = item.getRoot();
                    }
                }
            }
        }
        return senderOID201309;
    }
}
