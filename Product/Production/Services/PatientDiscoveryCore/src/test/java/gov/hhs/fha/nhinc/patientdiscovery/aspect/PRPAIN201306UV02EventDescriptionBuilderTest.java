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
package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.util.Base64Coder;

import java.io.Serializable;
import java.util.List;

import org.hl7.v3.BinaryDataEncoding;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01AcknowledgementDetail;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Before;
import org.junit.Test;

public class PRPAIN201306UV02EventDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    private PRPAIN201306UV02 body;
    private List<MCCIMT000300UV01Acknowledgement> acknowledgementList;

    @Before
    public void before() {
        body = new PRPAIN201306UV02();
        acknowledgementList = body.getAcknowledgement();
    }

    @Test
    public void handlesAssertions() {
        assertTrue(AssertionEventDescriptionBuilder.class
                .isAssignableFrom(PRPAIN201306UV02EventDescriptionBuilder.class));
        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();

        PRPAIN201306UV02 body = mock(PRPAIN201306UV02.class);
        AssertionType assertion = mock(AssertionType.class);

        builder.setArguments(new Object[] { assertion, body });
        assertTrue(builder.getAssertion().isPresent());
    }

    @Test
    public void basicStatus() {
        addAcknowledgements(1, createDetail("Test Content"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setArguments(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals("Test Content", eventDescription.getStatuses().get(0));
    }

    @Test
    public void twoAcknowledgements() {
        addAcknowledgements(2, createDetail("Test Content"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setArguments(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(2, eventDescription.getStatuses().size());
        assertEquals("Test Content", eventDescription.getStatuses().get(0));
        assertEquals("Test Content", eventDescription.getStatuses().get(1));
    }

    @Test
    public void multipleDetailsInAcknowledgement() {
        addAcknowledgements(1, createDetail("Test Content"), createDetail("Test Content2"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setArguments(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(2, eventDescription.getStatuses().size());
        assertEquals("Test Content", eventDescription.getStatuses().get(0));
        assertEquals("Test Content2", eventDescription.getStatuses().get(1));
    }

    @Test
    public void base64StatusContent() {
        addAcknowledgements(1, createDetail(BinaryDataEncoding.B_64, "Test Content"));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setArguments(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(1, eventDescription.getStatuses().size());
        assertEquals("Test Content", eventDescription.getStatuses().get(0));
    }

    @Test
    public void nullFlavoredStatus() {
        MCCIMT000300UV01AcknowledgementDetail detail = new MCCIMT000300UV01AcknowledgementDetail();
        detail.getNullFlavor().add("NI");

        addAcknowledgements(1, detail);

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setArguments(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(0, eventDescription.getStatuses().size());
    }

    @Test
    public void missingSerializedContent() {
        addAcknowledgements(1, createDetail(BinaryDataEncoding.TXT, null));

        PRPAIN201306UV02EventDescriptionBuilder builder = new PRPAIN201306UV02EventDescriptionBuilder();
        builder.setArguments(body);

        EventDescription eventDescription = getEventDescription(builder);
        assertEquals(0, eventDescription.getStatuses().size());
    }

    private void addAcknowledgements(int count, MCCIMT000300UV01AcknowledgementDetail... details) {
        for (int i = 0; i < count; ++i) {
            MCCIMT000300UV01Acknowledgement acknowledgement = new MCCIMT000300UV01Acknowledgement();
            acknowledgementList.add(acknowledgement);

            List<MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetailList = acknowledgement
                    .getAcknowledgementDetail();
            for (int j = 0; j < details.length; ++j) {
                acknowledgementDetailList.add(details[j]);
            }
        }
    }

    private MCCIMT000300UV01AcknowledgementDetail createDetail(String rawContent) {
        return createDetail(BinaryDataEncoding.TXT, rawContent);
    }

    private MCCIMT000300UV01AcknowledgementDetail createDetail(BinaryDataEncoding encoding, String rawContent) {
        EDExplicit ed = new EDExplicit();
        ed.setRepresentation(encoding);
        if (rawContent != null) {
            List<Serializable> content = ed.getContent();
            if (encoding.equals(BinaryDataEncoding.TXT)) {
                content.add(rawContent);
            } else {
                content.add(Base64Coder.encodeString(rawContent));
            }
        }
        MCCIMT000300UV01AcknowledgementDetail detail = new MCCIMT000300UV01AcknowledgementDetail();
        detail.setText(ed);

        return detail;
    }
}
