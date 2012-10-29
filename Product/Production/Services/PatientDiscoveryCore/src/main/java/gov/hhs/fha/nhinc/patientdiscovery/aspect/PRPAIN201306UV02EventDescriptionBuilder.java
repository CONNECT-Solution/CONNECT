package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;

import org.hl7.v3.PRPAIN201306UV02;

public class PRPAIN201306UV02EventDescriptionBuilder extends BaseEventDescriptionBuilder {
    
    private PRPAIN201306UV02 body;
    
    public PRPAIN201306UV02EventDescriptionBuilder(PRPAIN201306UV02 body) {
        this.body = body;
    }

   
    public void buildErrorCode() {
    }

    public void buildInitiatingHCID() {
        setInitiatingHCID(body.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
    }

   
    public void buildNPI() {
    }

    public void buildPayloadSize() {
    }

    public void buildPayloadType() {
    }

    public void buildRespondingHCID() {
    }

       public void buildStatus() {
    }

    public void buildTimeStamp() {
    }

    public void createEventDescription() {
    }

}
