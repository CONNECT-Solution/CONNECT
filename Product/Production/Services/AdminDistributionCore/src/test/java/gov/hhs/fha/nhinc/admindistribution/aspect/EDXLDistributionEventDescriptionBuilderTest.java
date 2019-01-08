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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.emergency.edxl.de._1.StatusValues;
import org.junit.Before;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class EDXLDistributionEventDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private EDXLDistributionEventDescriptionBuilder builder;

    @Before
    public void before() {
        builder = new EDXLDistributionEventDescriptionBuilder();
    }

    @Test
    public void handlesAssertionCorrectly() {
        assertTrue(
                AssertionEventDescriptionBuilder.class.isAssignableFrom(EDXLDistributionEventDescriptionBuilder.class));
        builder.setArguments(mock(AssertionType.class));
        assertTrue(builder.getAssertion().isPresent());
    }

    @Test
    public void hasDefaultPayloadSizeExtractor() {
        assertNotNull(builder.getPayloadSizeExtractor());
    }

    @Test
    public void handlesEmptyArguments() {
        builder.setArguments((Object[]) null);
    }

    @Test
    public void basicBuild() {

        EDXLDistributionPayloadSizeExtractor edxldExtractor = mock(EDXLDistributionPayloadSizeExtractor.class);
        List<String> payloadSizes = ImmutableList.of();
        when(edxldExtractor.getPayloadSizes(any(EDXLDistribution.class))).thenReturn(payloadSizes);

        EDXLDistribution alertMessage = mock(EDXLDistribution.class);

        builder.setPayloadSizeExtractor(edxldExtractor);
        builder.setArguments(alertMessage);

        EventDescription eventDescription = getEventDescription(builder);

        verify(edxldExtractor).getPayloadSizes(eq(alertMessage));
        assertEquals(payloadSizes, eventDescription.getPayloadSizes());
    }

    @Test
    public void extractsTimestamp() {
        EDXLDistribution edxl = new EDXLDistribution();
        XMLGregorianCalendar value = mock(XMLGregorianCalendar.class);
        when(value.toString()).thenReturn("foo");
        edxl.setDateTimeSent(value);
        builder.setArguments(edxl);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals("foo", eventDescription.getTimeStamp());
    }

    @Test
    public void extractsStatus() {
        EDXLDistribution edxl = new EDXLDistribution();
        builder.setArguments(edxl);

        StatusValues statusValue = StatusValues.ACTUAL;
        edxl.setDistributionStatus(statusValue);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals(statusValue.toString(), eventDescription.getStatuses().get(0));
    }
}
