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
package gov.hhs.fha.nhinc.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.builder.AssertionDescriptionExtractor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AssertionEventDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private AssertionEventDescriptionBuilder builder;
    private AssertionType assertion;
    private AssertionDescriptionExtractor assertionExtractor;

    @Before
    public void before() {
        assertion = mock(AssertionType.class);

        assertionExtractor = mock(AssertionDescriptionExtractor.class);
        when(assertionExtractor.getAssertion(assertion)).thenReturn(assertion);
        when(assertionExtractor.getInitiatingHCID(assertion)).thenReturn("hcid");
        when(assertionExtractor.getNPI(assertion)).thenReturn("npi");

        builder = mock(AssertionEventDescriptionBuilder.class, Mockito.CALLS_REAL_METHODS);
        doNothing().when(builder).buildTimeStamp();
        doNothing().when(builder).buildStatuses();
        doNothing().when(builder).buildRespondingHCIDs();
        doNothing().when(builder).buildPayloadTypes();
        doNothing().when(builder).buildPayloadSizes();
        doNothing().when(builder).buildErrorCodes();
    }

    @Test
    public void correctType() {
        assertTrue(BaseEventDescriptionBuilder.class.isAssignableFrom(AssertionEventDescriptionBuilder.class));
    }

    @Test
    public void extractsAssertion() {
        builder.setAssertionExtractor(assertionExtractor);
        Object[] params = new Object[]{new Object(), new Object(), assertion};
        builder.extractAssertion(params);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals("hcid", eventDescription.getInitiatingHCID());
        assertEquals("npi", eventDescription.getNPI());
    }

    @Test
    public void worksWithNoAssertion() {
        builder.setAssertionExtractor(assertionExtractor);
        Object[] params = new Object[]{};
        builder.extractAssertion(params);

        EventDescription eventDescription = getEventDescription(builder);
        assertNull(eventDescription.getInitiatingHCID());
        assertNull(eventDescription.getNPI());
    }

    @Test
    public void worksWithEmptyParams() {
        builder.setAssertionExtractor(assertionExtractor);
        builder.extractAssertion((Object[]) null);

        EventDescription eventDescription = getEventDescription(builder);
        assertNull(eventDescription.getInitiatingHCID());
        assertNull(eventDescription.getNPI());
    }
}
