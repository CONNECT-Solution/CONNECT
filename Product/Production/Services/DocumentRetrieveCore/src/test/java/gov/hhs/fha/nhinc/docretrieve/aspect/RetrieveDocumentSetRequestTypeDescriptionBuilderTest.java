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
package gov.hhs.fha.nhinc.docretrieve.aspect;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.builder.AssertionDescriptionExtractor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.util.CollectionUtils;

public class RetrieveDocumentSetRequestTypeDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private RetrieveDocumentSetRequestTypeDescriptionBuilder builder;
    private RetrieveDocumentSetRequestType request;
    private AssertionType assertion;
    private AssertionDescriptionExtractor assertionExtractor;

    @Before
    public void before() throws PropertyAccessException {
        final PropertyAccessor mockProperties = mock(PropertyAccessor.class);
        builder = new RetrieveDocumentSetRequestTypeDescriptionBuilder() {
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return mockProperties;
            }
        };
        request = new RetrieveDocumentSetRequestType();
        assertion = new AssertionType();
        assertionExtractor = mock(AssertionDescriptionExtractor.class);
        when(assertionExtractor.getAssertion(assertion)).thenReturn(assertion);
        when(mockProperties.getProperty(anyString(), anyString())).thenReturn(null);
        when(assertionExtractor.getInitiatingHCID(assertion)).thenReturn("hcid");
        when(assertionExtractor.getNPI(assertion)).thenReturn("npi");
    }

    @Test
    public void emptyBuild() {
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void noAssertion() {
        builder.setArguments(request);
        EventDescription eventDescription = assertBasicBuild();
        assertNull(eventDescription.getNPI());
        assertNull(eventDescription.getInitiatingHCID());
    }

    @Test
    public void withAssertion() {
        builder.setAssertionExtractor(assertionExtractor);
        Object[] arguments = {request, assertion};
        builder.setArguments(arguments);
        EventDescription eventDescription = assertBasicBuild();
        assertEquals("hcid", eventDescription.getInitiatingHCID());
        assertEquals("npi", eventDescription.getNPI());
    }

    private EventDescription assertBasicBuild() {
        EventDescription eventDescription = getEventDescription(builder);
        assertNull(eventDescription.getTimeStamp());
        assertTrue(CollectionUtils.isEmpty(eventDescription.getStatuses()));
        assertTrue(CollectionUtils.isEmpty(eventDescription.getRespondingHCIDs()));
        assertTrue(CollectionUtils.isEmpty(eventDescription.getPayloadTypes()));
        assertTrue(CollectionUtils.isEmpty(eventDescription.getPayloadSizes()));
        assertTrue(CollectionUtils.isEmpty(eventDescription.getErrorCodes()));
        return eventDescription;
    }
}
