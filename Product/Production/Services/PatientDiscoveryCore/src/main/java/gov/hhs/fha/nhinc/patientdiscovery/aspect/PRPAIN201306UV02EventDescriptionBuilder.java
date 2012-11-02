package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;

import org.hl7.v3.PRPAIN201306UV02;

public class PRPAIN201306UV02EventDescriptionBuilder extends BaseEventDescriptionBuilder {

    private PRPAIN201306UV02 body;
    
    public PRPAIN201306UV02EventDescriptionBuilder() {
    }

    public PRPAIN201306UV02EventDescriptionBuilder(PRPAIN201306UV02 body) {
        this.body = body;
    }

    @Override
    public void buildErrorCodes() {
    }

    @Override
    public void buildInitiatingHCID() {
        setInitiatingHCID(body.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue()
                .getId().get(0).getRoot());
    }

    @Override
    public void buildNPI() {
    }

    @Override
    public void buildPayloadSize() {
    }

    @Override
    public void buildPayloadTypes() {
    }

    @Override
    public void buildRespondingHCIDs() {
    }

    @Override
    public void buildStatuses() {
    }

    @Override
    public void buildTimeStamp() {
    }

    @Override
    public void createEventDescription() {
    }

    @Override
    public void setArguments(Object... arguements) {
        if (arguements.length == 1) {
            if (arguements[0] instanceof PRPAIN201306UV02) {
                this.body = (PRPAIN201306UV02)arguements[0];
            }
        }
        
    }

    @Override
    public void setReturnValue(Object returnValue) {
        // TODO Auto-generated method stub
        
    }
}
