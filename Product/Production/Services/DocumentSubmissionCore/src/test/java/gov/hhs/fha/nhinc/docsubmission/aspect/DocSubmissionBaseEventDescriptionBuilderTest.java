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
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.builder.AssertionDescriptionExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class DocSubmissionBaseEventDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void basicBuild() {
        ProvideAndRegisterDocumentSetDescriptionExtractor requestExtractor = mock(ProvideAndRegisterDocumentSetDescriptionExtractor.class);
        AssertionDescriptionExtractor assertionExtractor = mock(AssertionDescriptionExtractor.class);
        RegistryResponseDescriptionExtractor responseExtractor = mock(RegistryResponseDescriptionExtractor.class);

        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        RegistryResponseType response = new RegistryResponseType();

        List<String> payloadSizes = new ArrayList<>();
        payloadSizes.add("payloadSize");
        when(requestExtractor.getPayloadSize(request)).thenReturn(payloadSizes);
        when(assertionExtractor.getAssertion(assertion)).thenReturn(assertion);
        List<String> payloadTypes = new ArrayList<>();
        payloadTypes.add("payloadType");
        when(requestExtractor.getPayloadTypes(request)).thenReturn(payloadTypes);

        when(assertionExtractor.getInitiatingHCID(assertion)).thenReturn("hcid");
        when(assertionExtractor.getNPI(assertion)).thenReturn("npi");

        List<String> errorCodes = new ArrayList<>();
        errorCodes.add("errorCode");
        when(responseExtractor.getErrorCodes(response)).thenReturn(errorCodes);

        ImmutableList<String> statuses = ImmutableList.of("status");
        when(responseExtractor.getStatuses(response)).thenReturn(statuses);

        DocSubmissionBaseEventDescriptionBuilder builder = new DocSubmissionBaseEventDescriptionBuilder(
            requestExtractor, assertionExtractor, responseExtractor);
        builder.setArguments(request, assertion);
        builder.setReturnValue(response);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(1, eventDescription.getPayloadSizes().size());
        assertEquals("payloadSize", eventDescription.getPayloadSizes().get(0));

        assertEquals(1, eventDescription.getPayloadTypes().size());
        assertEquals("payloadType", eventDescription.getPayloadTypes().get(0));

        assertEquals("hcid", eventDescription.getInitiatingHCID());
        assertEquals("npi", eventDescription.getNPI());

        assertEquals(1, eventDescription.getErrorCodes().size());
        assertEquals("errorCode", eventDescription.getErrorCodes().get(0));

        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals("status", eventDescription.getStatuses().get(0));
    }

    @Test
    public void robustArgumentExtraction() {
        DocSubmissionBaseEventDescriptionBuilder builder = new DocSubmissionBaseEventDescriptionBuilder();
        ProvideAndRegisterDocumentSetRequestType request = mock(ProvideAndRegisterDocumentSetRequestType.class);
        AssertionType assertion = mock(AssertionType.class);

        builder.setArguments(new Object[]{new Object(), request, new Object(), assertion});

        assertEquals(request, builder.getRequest());
        assertEquals(assertion, builder.getAssertion().get());
    }
}
