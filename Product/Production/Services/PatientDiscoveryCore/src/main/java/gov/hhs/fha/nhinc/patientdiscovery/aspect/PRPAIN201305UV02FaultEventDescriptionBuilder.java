/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import com.google.common.collect.ImmutableList;
import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;

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
        if (arguements.length == 1 && arguements[0] instanceof PRPAIN201305UV02Fault) {
            fault = (PRPAIN201305UV02Fault) arguements[0];
        }
    }

    @Override
    public void setReturnValue(Object returnValue) {
        // TODO Auto-generated method stub

    }
}
