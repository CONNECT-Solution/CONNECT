/*
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
package gov.hhs.fha.nhinc.event.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;

import org.junit.Test;

/**
 * @author akong
 *
 */
public class AssertionDescriptionExtractorTest extends BaseDescriptionBuilderTest {
    
    @Test
    public void noAssertion() {
        AssertionDescriptionExtractor extractor = new AssertionDescriptionExtractor();
        
        assertNull(extractor.getNPI(null));
        assertNull(extractor.getInitiatingHCID(null));
    }

    @Test
    public void emptyAssertion() {
        AssertionType assertion = new AssertionType();

        AssertionDescriptionExtractor extractor = new AssertionDescriptionExtractor();
                
        assertNull(extractor.getNPI(assertion));
        assertNull(extractor.getInitiatingHCID(assertion));
    }
    
    @Test
    public void validAssertion() {        
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId("1.1");
        
        AssertionType assertion = new AssertionType();
        assertion.setHomeCommunity(homeCommunity);
        assertion.setNationalProviderId("npi");
        
        AssertionDescriptionExtractor extractor = new AssertionDescriptionExtractor();
        
        assertEquals("1.1", extractor.getInitiatingHCID(assertion));
        assertEquals("npi", extractor.getNPI(assertion));        
    }
}
