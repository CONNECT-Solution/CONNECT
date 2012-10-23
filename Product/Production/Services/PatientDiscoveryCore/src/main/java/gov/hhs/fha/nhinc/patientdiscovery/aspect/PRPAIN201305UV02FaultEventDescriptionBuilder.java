package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;
import gov.hhs.fha.nhinc.event.BaseEventDescription;
import gov.hhs.fha.nhinc.event.ContextEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;

public class PRPAIN201305UV02FaultEventDescriptionBuilder implements EventDescriptionBuilder {

    private ContextEventDescriptionBuilder decorated;
    private PRPAIN201305UV02Fault fault;
    private BaseEventDescription description;

    public PRPAIN201305UV02FaultEventDescriptionBuilder(ContextEventDescriptionBuilder eventDesciptionBuilder,
            PRPAIN201305UV02Fault fault) {
        this.decorated = eventDesciptionBuilder;
        this.fault = fault;
    }

    public void buildMessageId() {
        decorated.buildMessageId();
    }

    public void buildTransactionId() {
        decorated.buildTransactionId();
    }

    public void buildResponseMsgIdList() {
        decorated.buildResponseMsgIdList();
    }

    public EventDescription getEventDescription() {
        return decorated.getEventDescription();
    }

    public void createEventDescription() {
        decorated.createEventDescription();
        description = (BaseEventDescription) decorated.getEventDescription();
    }

    public void buildTimeStamp() {
        decorated.buildTimeStamp();
    }

    public void buildStatus() {
        description.setStatus("FAULT");
    }

    public void buildServiceType() {
        decorated.buildServiceType();
    }

    public void buildRespondingHCID() {
        decorated.buildRespondingHCID();
    }

    public void buildPayloadType() {
        decorated.buildPayloadType();
    }

    public void buildPayloadSize() {
        decorated.buildPayloadSize();
    }

    public void buildNPI() {
        decorated.buildNPI();
    }

    public void buildInitiatingHCID() {
        decorated.buildInitiatingHCID();
    }

    public void buildErrorCode() {
        description.setErrorCode(fault.getFaultInfo().getErrorCode());
    }

    public void buildAction() {
        decorated.buildAction();
    }

    

    

}
