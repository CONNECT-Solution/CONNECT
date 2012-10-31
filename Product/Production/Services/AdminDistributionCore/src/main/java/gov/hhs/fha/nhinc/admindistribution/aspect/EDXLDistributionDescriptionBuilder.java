/**
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventContextAccessor;
import gov.hhs.fha.nhinc.event.MessageRoutingAccessor;

/**
 * @author zmelnick
 *
 */
public class EDXLDistributionDescriptionBuilder extends BaseEventDescriptionBuilder {

    EDXLDistribution alert;
    /**
     * Public constructor.
     */
    public EDXLDistributionDescriptionBuilder(EDXLDistribution alert) {
        this.alert = alert;
    }

    /**
     * @param msgRouting
     * @param msgContext
     */
    public EDXLDistributionDescriptionBuilder(MessageRoutingAccessor msgRouting, EventContextAccessor msgContext) {
        super(msgRouting, msgContext);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildTimeStamp()
     */
    @Override
    public void buildTimeStamp() {
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildStatus()
     */
    @Override
    public void buildStatus() {

    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildRespondingHCID()
     */
    @Override
    public void buildRespondingHCID() {

    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadType()
     */
    @Override
    public void buildPayloadType() {

    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadSize()
     */
    @Override
    public void buildPayloadSize() {

    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildNPI()
     */
    @Override
    public void buildNPI() {

    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildInitiatingHCID()
     */
    @Override
    public void buildInitiatingHCID() {

    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildErrorCode()
     */
    @Override
    public void buildErrorCode() {

    }

}
