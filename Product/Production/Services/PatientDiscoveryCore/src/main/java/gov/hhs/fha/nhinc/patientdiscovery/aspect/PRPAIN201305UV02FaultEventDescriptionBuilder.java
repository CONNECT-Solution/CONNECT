package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;

import com.google.common.collect.ImmutableList;

public class PRPAIN201305UV02FaultEventDescriptionBuilder extends BaseEventDescriptionBuilder {

    private PRPAIN201305UV02Fault fault;

    public PRPAIN201305UV02FaultEventDescriptionBuilder(PRPAIN201305UV02Fault fault) {
        this.fault = fault;
    }

    public void buildTimeStamp() {
    }

    public void buildStatus() {
        setStatus("FAULT");
    }

    public void buildRespondingHCID() {
    }

    public void buildPayloadType() {
    }

    public void buildPayloadSize() {
    }

    public void buildNPI() {
    }

    public void buildInitiatingHCID() {
    }

    public void buildErrorCode() {
        setErrorCodes(ImmutableList.of(fault.getFaultInfo().getErrorCode()));
    }

}
