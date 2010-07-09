/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;

/**
 *
 * @author rayj
 */
public class FacadeAckBuilder {
    public static org.hl7.v3.CreateAckResponseType createAck(org.hl7.v3.CreateAckRequestType createAckRequest) {
        org.hl7.v3.CreateAckResponseType response = new org.hl7.v3.CreateAckResponseType();
        AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage(createAckRequest.getMessage());
        response.setAcknowledgement(ack);
        return response;
    }
}
