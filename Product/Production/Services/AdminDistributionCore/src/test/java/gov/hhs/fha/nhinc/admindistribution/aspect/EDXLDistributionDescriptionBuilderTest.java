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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;

import javax.activation.DataHandler;

import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.emergency.edxl.de._1.NonXMLContentType;
import oasis.names.tc.emergency.edxl.de._1.XmlContentType;

import org.junit.Test;

import gov.hhs.fha.nhinc.event.BaseDescriptionBuilderTest;

/**
 * @author zmelnick
 *
 */
public class EDXLDistributionDescriptionBuilderTest extends BaseDescriptionBuilderTest {

    @Test
    public void emptyBuild() {
        EDXLDistributionDescriptionExtractor extractor = new EDXLDistributionDescriptionExtractor();
        assertNotNull(extractor);
    }

    @Test
    public void testPayloadSizeOnSingleNonXMLPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setNonXmlPayloadWithSize(alert, BigInteger.TEN);
        EDXLDistributionDescriptionExtractor extractor = new EDXLDistributionDescriptionExtractor();

        assertEquals(BigInteger.TEN.toString(), extractor.getPayloadSizes(alert).get(0));
    }

    @Test
    public void testPayloadSizeMultipleNonXMLPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setNonXmlPayloadWithSize(alert, BigInteger.TEN);

        alert.getContentObject().add(setNonXmlPayloadObject(BigInteger.TEN));

        BigInteger testSize = BigInteger.TEN;

        EDXLDistributionDescriptionExtractor extractor = new EDXLDistributionDescriptionExtractor();

        assertTrue(extractor.getPayloadSizes(alert).size() == 2);
        assertEquals(testSize.toString(), extractor.getPayloadSizes(alert).get(0));
        assertEquals(testSize.toString(), extractor.getPayloadSizes(alert).get(1));
    }


    @Test
    public void testPayloadSizeXmlPayload() {
        EDXLDistributionDescriptionExtractor extractor = new EDXLDistributionDescriptionExtractor();
        EDXLDistribution alert = new EDXLDistribution();

        setXmlPayload(alert);
        assertEquals(0, extractor.getPayloadSizes(alert).size());
    }

    @Test
    public void testPayloadSizeMixedPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setXmlPayload(alert);
        setNonXmlPayloadWithSize(alert,BigInteger.TEN);

        EDXLDistributionDescriptionExtractor extractor = new EDXLDistributionDescriptionExtractor();

        assertEquals(0, extractor.getPayloadSizes(alert).size());
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
     * @return
     */
    private NonXMLContentType createMockNonXmlPayload() {
        NonXMLContentType payloadContent = new NonXMLContentType();
        payloadContent.setContentData(mock(DataHandler.class));
        return payloadContent;
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
