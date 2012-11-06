package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Agent;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Organization;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;

public class PRPAIN201305UV02EventDescriptionBuilder extends BaseEventDescriptionBuilder {

    private PRPAIN201305UV02 body;
    
    public PRPAIN201305UV02EventDescriptionBuilder() {
        
    }

    public PRPAIN201305UV02EventDescriptionBuilder(PRPAIN201305UV02 body) {
        this.body = body;
    }

    @Override
    public void buildRespondingHCIDs() {
        MCCIMT000100UV01Sender sender = body.getSender();
        // added to help in testing
        // odd to be added here.. there needs to be another way.
        if (sender != null) {
            MCCIMT000100UV01Device device = sender.getDevice();
            JAXBElement<MCCIMT000100UV01Agent> jaxbAgent = device.getAsAgent();
            MCCIMT000100UV01Agent agent = jaxbAgent.getValue();
            JAXBElement<MCCIMT000100UV01Organization> jaxbRepresentedOrganization = agent.getRepresentedOrganization();
            MCCIMT000100UV01Organization representedOrganization = jaxbRepresentedOrganization.getValue();
            List<II> ids = representedOrganization.getId();
            List<String> hcids = new ArrayList<String> ();
            for(II ii : ids) {
                 hcids.add(ii.getRoot());
            }
            setRespondingHCIDs(hcids); 
        }
    }

    @Override
    public void buildTimeStamp() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buildStatuses() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buildPayloadTypes() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buildPayloadSizes() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buildNPI() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buildInitiatingHCID() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buildErrorCodes() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setArguments(Object... arguements) {
        if (arguements.length == 1) {
            if ( arguements[0] instanceof PRPAIN201305UV02) {
                this.body = (PRPAIN201305UV02)arguements[0];
            }
        }
    }

}
