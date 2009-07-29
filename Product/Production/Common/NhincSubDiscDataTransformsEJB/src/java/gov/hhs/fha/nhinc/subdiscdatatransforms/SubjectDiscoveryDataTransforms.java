/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subdiscdatatransforms;

import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201302Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201310Transforms;
import gov.hhs.fha.nhinc.nhinccomponentsubdisctransforms.NhincComponentSubDiscTransformsPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Jon Hoppesch
 */
@WebService(serviceName = "NhincComponentSubDiscTransforms", portName = "NhincComponentSubDiscTransformsBindingSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentsubdisctransforms.NhincComponentSubDiscTransformsPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentsubdisctransforms", wsdlLocation = "META-INF/wsdl/SubjectDiscoveryDataTransforms/NhincComponentSubDiscTransforms.wsdl")
@Stateless
public class SubjectDiscoveryDataTransforms implements NhincComponentSubDiscTransformsPortType {

    public org.hl7.v3.MCCIIN000002UV01 createAck(org.hl7.v3.CreateAckMsgRequestType createAckRequest) {
        return HL7AckTransforms.createAckMessage(createAckRequest.getLocalDeviceId(), 
                createAckRequest.getOrigMsgId(), 
                createAckRequest.getMsgText(), 
                createAckRequest.getSenderOID(), 
                createAckRequest.getReceiverOID());
    }

    public org.hl7.v3.PRPAIN201305UV create201305(org.hl7.v3.Create201305RequestType create201305Request) {
        return HL7PRPA201305Transforms.createPRPA201305(create201305Request.getPRPA201301Patient(), 
                create201305Request.getSenderOID(), 
                create201305Request.getReceiverOID(), 
                create201305Request.getLocalDeviceId());
    }

    public org.hl7.v3.PRPAIN201302UV create201302(org.hl7.v3.Create201302RequestType create201302Request) {
        if (create201302Request.getPRPA201301Patient() != null) {
            return HL7PRPA201302Transforms.createPRPA201302(create201302Request.getPRPA201301Patient(), 
                    create201302Request.getRemotePatientId(), 
                    create201302Request.getRemoteDeviceId(), 
                    create201302Request.getSenderOID(), 
                    create201302Request.getReceiverOID());
        }
        else {
            return HL7PRPA201302Transforms.createPRPA201302(create201302Request.getPRPA201310Patient(),
                    create201302Request.getRemotePatientId(), 
                    create201302Request.getRemoteDeviceId(), 
                    create201302Request.getSenderOID(), 
                    create201302Request.getReceiverOID());
        }
    }

    public org.hl7.v3.PRPAIN201310UV create201310(org.hl7.v3.Create201310RequestType create201310Request) {
        return HL7PRPA201310Transforms.createPRPA201310(create201310Request.getPseudoPatientId(), 
                create201310Request.getPseudoAssigningAuthorityId(), 
                create201310Request.getLocalDeviceId(), 
                create201310Request.getSenderOID(), 
                create201310Request.getReceiverOID(), 
                create201310Request.getPRPA201307QueryByParameter());
    }

    public org.hl7.v3.PRPAIN201310UV createFault201310(org.hl7.v3.CreateFault201310RequestType createFault201310Request) {
        return HL7PRPA201310Transforms.createFaultPRPA201310(createFault201310Request.getSenderOID(), 
                createFault201310Request.getReceiverOID());
    }
}
