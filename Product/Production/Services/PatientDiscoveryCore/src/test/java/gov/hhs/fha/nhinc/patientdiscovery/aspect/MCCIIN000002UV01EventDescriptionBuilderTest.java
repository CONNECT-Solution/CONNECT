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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import org.hl7.v3.CS;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.springframework.util.CollectionUtils;

public class MCCIIN000002UV01EventDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void handlesAssertions() {
        assertTrue(AssertionEventDescriptionBuilder.class
                .isAssignableFrom(MCCIIN000002UV01EventDescriptionBuilder.class));
        MCCIIN000002UV01EventDescriptionBuilder builder = new MCCIIN000002UV01EventDescriptionBuilder();

        AssertionType assertion = mock(AssertionType.class);

        builder.setArguments(new Object[] { assertion });
        assertTrue(builder.getAssertion().isPresent());
    }

    @Test
    public void noReturnValue() {
        MCCIIN000002UV01EventDescriptionBuilder builder = new MCCIIN000002UV01EventDescriptionBuilder();
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void basicExtraction() {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        MCCIMT000200UV01Acknowledgement ack = addAcknowledgement(response);
        addTypeCode(ack, "success");

        EventDescription eventDescription = getEventDescription(response);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals("success", eventDescription.getStatuses().get(0));
    }

    @Test
    public void typeCodeHasNoCode() {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        MCCIMT000200UV01Acknowledgement ack = addAcknowledgement(response);
        addTypeCode(ack, null);

        EventDescription eventDescription = getEventDescription(response);
        assertTrue(CollectionUtils.isEmpty(eventDescription.getStatuses()));
    }

    @Test
    public void noTypeCode() {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        addAcknowledgement(response);

        EventDescription eventDescription = getEventDescription(response);
        assertTrue(CollectionUtils.isEmpty(eventDescription.getStatuses()));
    }

    @Test
    public void multipleAcks() {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        MCCIMT000200UV01Acknowledgement ack = addAcknowledgement(response);
        addTypeCode(ack, "success");

        MCCIMT000200UV01Acknowledgement ack2 = addAcknowledgement(response);
        addTypeCode(ack2, "failure");

        EventDescription eventDescription = getEventDescription(response);
        assertEquals(2, eventDescription.getStatuses().size());
        assertEquals("success", eventDescription.getStatuses().get(0));
        assertEquals("failure", eventDescription.getStatuses().get(1));
    }

    @Test
    public void nullReturnValue() {
        MCCIIN000002UV01EventDescriptionBuilder builder = new MCCIIN000002UV01EventDescriptionBuilder();
        builder.setReturnValue(null);
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void invalidReturnType() {
        MCCIIN000002UV01EventDescriptionBuilder builder = new MCCIIN000002UV01EventDescriptionBuilder();
        builder.setReturnValue(new Object());
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    private EventDescription getEventDescription(MCCIIN000002UV01 response) {
        MCCIIN000002UV01EventDescriptionBuilder builder = new MCCIIN000002UV01EventDescriptionBuilder();
        builder.setReturnValue(response);

        EventDescription eventDescription = getEventDescription(builder);
        return eventDescription;
    }

    private MCCIMT000200UV01Acknowledgement addAcknowledgement(MCCIIN000002UV01 response) {
        MCCIMT000200UV01Acknowledgement ack = new MCCIMT000200UV01Acknowledgement();
        response.getAcknowledgement().add(ack);
        return ack;
    }

    private void addTypeCode(MCCIMT000200UV01Acknowledgement ack, String code) {
        CS typeCode = new CS();
        ack.setTypeCode(typeCode);
        if (code != null) {
            typeCode.setCode(code);
        }
    }
}
