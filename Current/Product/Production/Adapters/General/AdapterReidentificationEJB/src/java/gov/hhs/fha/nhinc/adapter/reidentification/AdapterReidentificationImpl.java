/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.reidentification;

import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201310UVRequestType;
import org.hl7.v3.PRPAIN201309UV;
import org.hl7.v3.PRPAIN201310UV;
import org.hl7.v3.PRPAIN201309UVQUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UVQueryByParameter;
import org.hl7.v3.PRPAMT201307UVParameterList;
import org.hl7.v3.PRPAMT201307UVPatientIdentifier;
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

            PRPAIN201310UV response201310 = null;
            PRPAIN201309UV request201309 = realIdentifierRequest.getPRPAIN201309UV();
            if (request201309 != null) {
                // extract information from the 201309 request message
                // extract sender OID and set to the receiver OID for the 201310
                String receiverOID = extractSenderOID(request201309);

                // extract receiver OID and set to the sender OID for the 201310
                String senderOID = extractReceiverOID(request201309);

                if (senderOID != null && receiverOID != null) {
                    // extract queryByParameter
                    PRPAMT201307UVQueryByParameter queryByParameter = extractQueryByParameter(request201309);

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
            ret201310RequestType.setPRPAIN201310UV(response201310);
        }
        return ret201310RequestType;
    }

    private II extractPseudoPatientIds(PRPAMT201307UVQueryByParameter queryByParameter) {
        II pseudoPatientItem = null;
        if (queryByParameter != null) {
            PRPAMT201307UVParameterList parameterList = queryByParameter.getParameterList();
            if (parameterList != null) {
                PRPAMT201307UVPatientIdentifier patientIdentifier = null;
                List<PRPAMT201307UVPatientIdentifier> patientIdentifierList = parameterList.getPatientIdentifier();
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

    private PRPAMT201307UVQueryByParameter extractQueryByParameter(PRPAIN201309UV request201309) {
        PRPAMT201307UVQueryByParameter queryByParameter = null;
        PRPAIN201309UVQUQIMT021001UV01ControlActProcess controlAct = request201309.getControlActProcess();
        if (controlAct != null) {
            JAXBElement<PRPAMT201307UVQueryByParameter> queryByParameterElem = controlAct.getQueryByParameter();
            if (queryByParameterElem != null) {
                queryByParameter = queryByParameterElem.getValue();
            }
        }
        return queryByParameter;
    }

    private String extractReceiverOID(PRPAIN201309UV request201309) {
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

    private String extractSenderOID(PRPAIN201309UV request201309) {
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
