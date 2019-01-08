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
package gov.hhs.fha.nhinc.patientdiscovery.messaging.director.impl;

import gov.hhs.fha.nhinc.messaging.director.AbstractMessageDirector;
import gov.hhs.fha.nhinc.patientdiscovery.messaging.builder.PRPAIN201305UV02Builder;
import gov.hhs.fha.nhinc.patientdiscovery.messaging.director.PatientDiscoveryMessageDirector;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author tjafri
 */
public class PatientDiscoveryMessageDirectorImpl extends AbstractMessageDirector implements
    PatientDiscoveryMessageDirector {

    /**
     * The message.
     */
    private RespondingGatewayPRPAIN201305UV02RequestType message = null;

    /**
     * The pd builder.
     */
    private PRPAIN201305UV02Builder pdBuilder = null;

    /*
     * (non-Javadoc)
     *
     * @see org.cahih.messaging.builder.EntityPatientDiscoveryMessageBuilder#getMessage()
     */
    @Override
    public RespondingGatewayPRPAIN201305UV02RequestType getMessage() {
        return message;
    }

    @Override
    public void build() {
        message = new RespondingGatewayPRPAIN201305UV02RequestType();
        if (pdBuilder != null) {
            pdBuilder.build();
            message.setPRPAIN201305UV02(pdBuilder.getMessage());
        }

        if (assertion != null) {
            message.setAssertion(assertion);
        }

        if (targetBuilder != null) {
            targetBuilder.build();
            message.setNhinTargetCommunities(targetBuilder.getNhinTargetCommunities());
        }
    }

    @Override
    public void setPRPAIN201305UV02Builder(PRPAIN201305UV02Builder pdBuilder) {
        this.pdBuilder = pdBuilder;
    }

}
