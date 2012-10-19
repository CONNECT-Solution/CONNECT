package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import org.hl7.v3.PRPAIN201306UV02;

import gov.hhs.fha.nhinc.event.BaseEventDescription;
import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;

public class PRPAIN201306UV02EventDescriptionBuilder implements EventDescriptionBuilder {
    
    private BaseEventDescriptionBuilder decorated;
    private PRPAIN201306UV02 body;
    
    public PRPAIN201306UV02EventDescriptionBuilder(BaseEventDescriptionBuilder builder, PRPAIN201306UV02 body) {
        this.decorated = builder;
        this.body = body;
    }

    public void buildAction() {
        decorated.buildAction();
    }

    public void buildErrorCode() {
        decorated.buildErrorCode();
    }

    public void buildInitiatingHCID() {
        BaseEventDescription description = (BaseEventDescription)decorated.getEventDescription();
        description.setInitiatingHCID(body.getSender().getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot());
    }

    public void buildMessageId() {
        decorated.buildMessageId();
    }

    public void buildNPI() {
        decorated.buildNPI();
    }

    public void buildPayloadSize() {
        decorated.buildPayloadSize();
    }

    public void buildPayloadType() {
        decorated.buildPayloadType();
    }

    public void buildRespondingHCID() {
        decorated.buildRespondingHCID();
    }

    public void buildResponseMsgIdList() {
        decorated.buildResponseMsgIdList();
    }

    public void buildServiceType() {
        decorated.buildServiceType();
    }

    public void buildStatus() {
        decorated.buildStatus();
    }

    public void buildTimeStamp() {
        decorated.buildTimeStamp();
    }

    public void buildTransactionId() {
        decorated.buildTransactionId();
    }

    public void createEventDescription() {
        decorated.createEventDescription();
    }

    public boolean equals(Object obj) {
        return decorated.equals(obj);
    }

    public EventDescription getEventDescription() {
        return decorated.getEventDescription();
    }

    public int hashCode() {
        return decorated.hashCode();
    }

    public String toString() {
        return decorated.toString();
    }
    
    

}
