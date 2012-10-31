package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;

import com.google.common.collect.ImmutableList;

public class PRPAIN201305UV02FaultEventDescriptionBuilder extends BaseEventDescriptionBuilder {

    private PRPAIN201305UV02Fault fault;

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
    public void buildPayloadSize() {
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
}
