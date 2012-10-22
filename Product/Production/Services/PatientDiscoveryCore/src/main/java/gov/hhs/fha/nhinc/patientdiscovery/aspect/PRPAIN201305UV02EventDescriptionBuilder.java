package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import java.util.List;

import javax.xml.bind.JAXBElement;

import gov.hhs.fha.nhinc.event.BaseEventDescription;
import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;

import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;

public class PRPAIN201305UV02EventDescriptionBuilder implements EventDescriptionBuilder {

    private BaseEventDescriptionBuilder decorated;
    private PRPAIN201305UV02 body;
    private BaseEventDescription description;

    public PRPAIN201305UV02EventDescriptionBuilder(BaseEventDescriptionBuilder eventDesciptionBuilder,
            PRPAIN201305UV02 body) {
        this.decorated = eventDesciptionBuilder;
        this.body = body;
    }

    public EventDescription getEventDescription() {
        return decorated.getEventDescription();
    }

    public void createEventDescription() {
        decorated.createEventDescription();
        description = (BaseEventDescription) decorated.getEventDescription();
    }

    public void buildMessageId() {
        decorated.buildMessageId();
    }

    public void buildTransactionId() {
        decorated.buildTransactionId();
    }

    public void buildTimeStamp() {
        decorated.buildTimeStamp();
    }

    public void buildStatus() {
        decorated.buildStatus();
    }

    public void buildServiceType() {
        decorated.buildServiceType();
    }

    public void buildResponseMsgIdList() {
        decorated.buildResponseMsgIdList();
    }

    public void buildRespondingHCID() {
        MCCIMT000100UV01Sender sender = body.getSender();
        //added to help in testing
        //odd to be added here.. there needs to be another way.
        if (sender != null) {
            MCCIMT000100UV01Device device = sender.getDevice();
            JAXBElement<MCCIMT000100UV01Agent> jaxbAgent = device.getAsAgent();
            MCCIMT000100UV01Agent agent = jaxbAgent.getValue();
            JAXBElement<MCCIMT000100UV01Organization> jaxbRepresentedOrganization = agent.getRepresentedOrganization();
            MCCIMT000100UV01Organization representedOrganization = jaxbRepresentedOrganization.getValue();
            List<II> ids = representedOrganization.getId();
            II ii = ids.get(0);
            String root = ii.getRoot();

            description.setInitiatingHCID(root);
        }
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
        decorated.buildErrorCode();
    }

    public void buildAction() {
        decorated.buildAction();
    }

    public boolean equals(Object obj) {
        return decorated.equals(obj);
    }

    public String toString() {
        return decorated.toString();
    }

}
