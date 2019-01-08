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
package gov.hhs.fha.nhinc.docsubmission.aspect;

import com.google.common.collect.ImmutableList;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeferredResponseDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private DeferredResponseDescriptionBuilder builder;

    @Before
    public void before() {
        builder = new DeferredResponseDescriptionBuilder();
    }

    @Test
    public void exists() {
        assertTrue(AssertionEventDescriptionBuilder.class.isAssignableFrom(builder.getClass()));
    }

    @Test
    public void hasRegistryResponseDescriptionExtractor() {
        assertTrue(builder.getResponseExtractor() instanceof RegistryResponseDescriptionExtractor);
    }

    @Test
    public void delegatesToExtractor() {
        RegistryResponseType response = new RegistryResponseType();
        RegistryResponseDescriptionExtractor responseExtractor = mock(RegistryResponseDescriptionExtractor.class);
        List<String> errorCodes = new ArrayList<>();
        errorCodes.add("errorCode");
        when(responseExtractor.getErrorCodes(response)).thenReturn(errorCodes);

        ImmutableList<String> statuses = ImmutableList.of("status");
        when(responseExtractor.getStatuses(response)).thenReturn(statuses);

        builder.setExtractor(responseExtractor);

        builder.setArguments(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(1, eventDescription.getErrorCodes().size());
        assertEquals("errorCode", eventDescription.getErrorCodes().get(0));

        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals("status", eventDescription.getStatuses().get(0));
    }

    @Test
    public void extractsAssertion() {
        AssertionType mock = mock(AssertionType.class);
        builder.setArguments(mock);
        assertEquals(mock, builder.getAssertion().get());

    }

    @Test
    public void nullArgumentsNoException() {
        builder.setArguments((Object[]) null);
    }
}
