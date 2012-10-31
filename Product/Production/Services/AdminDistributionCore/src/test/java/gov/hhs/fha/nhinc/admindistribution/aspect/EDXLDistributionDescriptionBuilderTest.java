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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;

import javax.activation.DataHandler;

import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.emergency.edxl.de._1.NonXMLContentType;
import oasis.names.tc.emergency.edxl.de._1.XmlContentType;

import org.junit.Test;
import org.springframework.util.CollectionUtils;

import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;
import gov.hhs.fha.nhinc.event.EventDescription;

/**
 * @author zmelnick
 *
 */
public class EDXLDistributionDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    /**
     *
     */
    private static final String INITIATING_HCID = "1.1";

    @Test
    public void emptyBuild() {
        EDXLDistributionDescriptionBuilder builder = new EDXLDistributionDescriptionBuilder(null);
        EventDescription eventDescription = getEventDescription(builder);
        assertNotNull(eventDescription);
    }

    @Test
    public void basicBuild() {
        EDXLDistribution alert = new EDXLDistribution();
        setIncomingHCID(alert);
        setNonXmlPayloadWithoutSize(alert);
        EDXLDistributionDescriptionBuilder builder = new EDXLDistributionDescriptionBuilder(alert);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(INITIATING_HCID, eventDescription.getInitiatingHCID());
        assertAlwaysNullAttributes(eventDescription);
    }

    @Test
    public void testPayloadSizeOnSingleNonXMLPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setNonXmlPayloadWithSize(alert, BigInteger.TEN);
        EDXLDistributionDescriptionBuilder builder = new EDXLDistributionDescriptionBuilder(alert);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(BigInteger.TEN.toString(), eventDescription.getPayloadSize());

        assertNull(eventDescription.getInitiatingHCID());
        assertAlwaysNullAttributes(eventDescription);
    }

    @Test
    public void testPayloadSizeMultipleNonXMLPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setNonXmlPayloadWithSize(alert, BigInteger.TEN);

        alert.getContentObject().add(setNonXmlPayloadObject(BigInteger.TEN));

        BigInteger testSize = BigInteger.TEN;
        testSize = testSize.add(BigInteger.TEN);

        EDXLDistributionDescriptionBuilder builder = new EDXLDistributionDescriptionBuilder(alert);
        EventDescription eventDescription = getEventDescription(builder);

        assertEquals(testSize.toString(), eventDescription.getPayloadSize());

        assertNull(eventDescription.getInitiatingHCID());
        assertAlwaysNullAttributes(eventDescription);
    }


    @Test
    public void testPayloadSizeXmlPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setXmlPayload(alert);

        EDXLDistributionDescriptionBuilder builder = new EDXLDistributionDescriptionBuilder(alert);
        EventDescription eventDescription = getEventDescription(builder);

        assertNull(eventDescription.getPayloadSize());
        assertNull(eventDescription.getInitiatingHCID());
        assertAlwaysNullAttributes(eventDescription);
    }

    @Test
    public void testPayloadSizeMixedPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setXmlPayload(alert);
        setNonXmlPayloadWithSize(alert,BigInteger.TEN);

        EDXLDistributionDescriptionBuilder builder = new EDXLDistributionDescriptionBuilder(alert);
        EventDescription eventDescription = getEventDescription(builder);

        assertNull(eventDescription.getPayloadSize());
        assertNull(eventDescription.getInitiatingHCID());
        assertAlwaysNullAttributes(eventDescription);
    }

    /**
     * @param alert
     */
    private void setXmlPayload(EDXLDistribution alert) {
        ContentObjectType payload = new ContentObjectType();
        XmlContentType xmlPayload = mock(XmlContentType.class);
        payload.setXmlContent(xmlPayload);
        alert.getContentObject().add(payload);
    }

    /**
     * @param alert the object to set the payload for
     */
    private void setNonXmlPayloadWithSize(EDXLDistribution alert, BigInteger payloadSize) {
        ContentObjectType payload = new ContentObjectType();
        NonXMLContentType payloadContent = createMockNonXmlPayload();

        payloadContent.setSize(payloadSize);

        payload.setNonXMLContent(payloadContent);
        alert.getContentObject().add(payload);
    }

    /**
     * @param alert the object to set the payload for
     */
    private void setNonXmlPayloadWithoutSize(EDXLDistribution alert) {
        ContentObjectType payload = new ContentObjectType();
        NonXMLContentType payloadContent = createMockNonXmlPayload();

        payload.setNonXMLContent(payloadContent);
        alert.getContentObject().add(payload);
    }

    /**
     * @return
     */
    private NonXMLContentType createMockNonXmlPayload() {
        NonXMLContentType payloadContent = new NonXMLContentType();
        payloadContent.setContentData(mock(DataHandler.class));
        return payloadContent;
    }

    /**
     * @param alert
     */
    private void setIncomingHCID(EDXLDistribution alert) {
        alert.setSenderID(INITIATING_HCID);
    }

    private void assertAlwaysNullAttributes(EventDescription eventDescription) {
        assertNull(eventDescription.getTimeStamp());
        assertNull(eventDescription.getStatuses());
        assertNull(eventDescription.getRespondingHCIDs());
        assertTrue(CollectionUtils.isEmpty(eventDescription.getPayloadTypes()));
        assertNull(eventDescription.getNPI());
        assertTrue(CollectionUtils.isEmpty(eventDescription.getErrorCodes()));
    }


    private ContentObjectType setNonXmlPayloadObject(BigInteger size) {
        ContentObjectType payload = new ContentObjectType();
        NonXMLContentType payloadContent = createMockNonXmlPayload();
        if (size != null) {
            payloadContent.setSize(size);
        }
       payload.setNonXMLContent(payloadContent);
       return payload;
    }
}
