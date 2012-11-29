package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import com.google.common.collect.ImmutableList;

import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;
import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;

public class PRPAIN201305UV02FaultEventDescriptionBuilder extends BaseEventDescriptionBuilder {

    private PRPAIN201305UV02Fault fault;
    
    public PRPAIN201305UV02FaultEventDescriptionBuilder() {
        
    }
    

    public PRPAIN201305UV02FaultEventDescriptionBuilder(PRPAIN201305UV02Fault fault) {
        this.fault = fault;
    }

    @Override
    public void buildTimeStamp() {
    }

    @Override
    public void buildStatuses() {
        setStatuses(ImmutableList.of("FAULT"));
    }

    @Override
    public void buildRespondingHCIDs() {
    }

    @Override
    public void buildPayloadTypes() {
    }

    @Override
    public void buildPayloadSizes() {
    }

    @Override
    public void buildNPI() {
    }

    @Override
    public void buildInitiatingHCID() {
    }

    @Override
    public void buildErrorCodes() {
        setErrorCodes(ImmutableList.of(fault.getFaultInfo().getErrorCode()));
    }

    @Override
    public void setArguments(Object... arguements) {
        if (arguements.length == 1) {
            if (arguements[0] instanceof PRPAIN201305UV02Fault) {
                this.fault = (PRPAIN201305UV02Fault)arguements[0];
            }
        }
    }

    @Override
    public void setReturnValue(Object returnValue) {
        // TODO Auto-generated method stub
        
    }
}
