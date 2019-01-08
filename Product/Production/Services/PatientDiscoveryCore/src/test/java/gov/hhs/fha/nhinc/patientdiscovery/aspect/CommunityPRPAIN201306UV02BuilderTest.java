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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import java.util.Set;
import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommunityPRPAIN201306UV02BuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void handlesAssertions() {
        assertTrue(AssertionEventDescriptionBuilder.class.isAssignableFrom(CommunityPRPAIN201306UV02Builder.class));
        CommunityPRPAIN201306UV02Builder builder = new CommunityPRPAIN201306UV02Builder();

        AssertionType assertion = mock(AssertionType.class);

        builder.setArguments(new Object[] { assertion });
        assertTrue(builder.getAssertion().isPresent());
    }

    @Test
    public void noReturnValue() {
        CommunityPRPAIN201306UV02Builder builder = new CommunityPRPAIN201306UV02Builder();
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void handlesNullReturnValue() {
        CommunityPRPAIN201306UV02Builder builder = new CommunityPRPAIN201306UV02Builder();
        builder.setReturnValue(null);
        assertEquals(Optional.absent(), builder.getReturnValue());
    }

    @Test
    public void delegatesRespondingHCIDs() {
        PRPAIN201306UV02 value1 = mock(PRPAIN201306UV02.class);
        PRPAIN201306UV02 value2 = mock(PRPAIN201306UV02.class);

        RespondingGatewayPRPAIN201306UV02ResponseType response = createResponse(value1, value2);
        CommunityPRPAIN201306UV02Builder builder = new CommunityPRPAIN201306UV02Builder();

        builder.setHCIDExtractor(createMockHcidExtractor(value1, value2, ImmutableSet.of("foo", "bar"),
                ImmutableSet.of("baz")));
        builder.setReturnValue(response);

        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(3, eventDescription.getRespondingHCIDs().size());
        assertTrue(eventDescription.getRespondingHCIDs().contains("foo"));
        assertTrue(eventDescription.getRespondingHCIDs().contains("bar"));
        assertTrue(eventDescription.getRespondingHCIDs().contains("baz"));
    }

    @Test
    public void delegatesStatuses() {
        PRPAIN201306UV02 value1 = mock(PRPAIN201306UV02.class);
        PRPAIN201306UV02 value2 = mock(PRPAIN201306UV02.class);

        RespondingGatewayPRPAIN201306UV02ResponseType response = createResponse(value1, value2);
        CommunityPRPAIN201306UV02Builder builder = new CommunityPRPAIN201306UV02Builder();

        PRPAIN201306UV02StatusExtractor statusExtractor = mock(PRPAIN201306UV02StatusExtractor.class);
        when(statusExtractor.apply(value1)).thenReturn(ImmutableSet.of("status1", "status2"));
        when(statusExtractor.apply(value2)).thenReturn(ImmutableSet.of("status3"));

        builder.setStatusExtractor(statusExtractor);
        builder.setReturnValue(response);

        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(3, eventDescription.getStatuses().size());
        assertTrue(eventDescription.getStatuses().contains("status1"));
        assertTrue(eventDescription.getStatuses().contains("status2"));
        assertTrue(eventDescription.getStatuses().contains("status3"));
    }

    private RespondingGatewayPRPAIN201306UV02ResponseType createResponse(PRPAIN201306UV02... values) {
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
        for (int i = 0; i < values.length; ++i) {
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02(values[i]);

            response.getCommunityResponse().add(communityResponse);
        }

        return response;
    }

    private PRPAIN201306UV02HCIDExtractor createMockHcidExtractor(PRPAIN201306UV02 value1, PRPAIN201306UV02 value2,
            Set<String> firstAnswer, Set<String> secondAnswer) {
        PRPAIN201306UV02HCIDExtractor mock = mock(PRPAIN201306UV02HCIDExtractor.class);
        when(mock.apply(value1)).thenReturn(firstAnswer);
        when(mock.apply(value2)).thenReturn(secondAnswer);
        return mock;
    }
}
