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
package gov.hhs.fha.nhinc.admindistribution.aspect;

import com.google.common.collect.ImmutableList;
import gov.hhs.fha.nhinc.event.TargetEventDescriptionBuilder;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

/**
 * @author zmelnick
 *
 */
public class EDXLDistributionEventDescriptionBuilder extends TargetEventDescriptionBuilder {

    private EDXLDistributionPayloadSizeExtractor payloadSizeExtractor = new EDXLDistributionPayloadSizeExtractor();

    private EDXLDistribution alertMessage;

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildTimeStamp()
     */
    @Override
    public void buildTimeStamp() {
        if (alertMessage == null || alertMessage.getDateTimeSent() == null) {
            return;
        }
        setTimeStamp(alertMessage.getDateTimeSent().toString());
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildStatuses()
     */
    @Override
    public void buildStatuses() {
        if (alertMessage == null || alertMessage.getDistributionStatus() == null) {
            return;
        }
        setStatuses(ImmutableList.of(alertMessage.getDistributionStatus().toString()));
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadTypes()
     */
    @Override
    public void buildPayloadTypes() {
        // PayloadType not available from EDXLDistribution object. However, type will always be t63.
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadSize()
     */
    @Override
    public void buildPayloadSizes() {
        setPayloadSizes(payloadSizeExtractor.getPayloadSizes(alertMessage));
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildErrorCodes()
     */
    @Override
    public void buildErrorCodes() {
        // No error codes in spec
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder#setArguments(java.lang.Object[])
     */
    @Override
    public void setArguments(Object... arguements) {
        extractAssertion(arguements);
        extractAlertMessage(arguements);
        extractTarget(arguements);
    }

    private void extractAlertMessage(Object... arguments) {
        if (arguments == null) {
            return;
        }
        for (Object argument : arguments) {
            if (argument instanceof EDXLDistribution) {
                alertMessage = (EDXLDistribution) argument;
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder#setReturnValue(java.lang.Object)
     */
    @Override
    public void setReturnValue(Object returnValue) {
        // no action needed. Return value is "void" for AD. The object "return value" is a null object.
    }

    EDXLDistributionPayloadSizeExtractor getPayloadSizeExtractor() {
        return payloadSizeExtractor;
    }

    void setPayloadSizeExtractor(EDXLDistributionPayloadSizeExtractor edxldExtractor) {
        payloadSizeExtractor = edxldExtractor;
    }
}
