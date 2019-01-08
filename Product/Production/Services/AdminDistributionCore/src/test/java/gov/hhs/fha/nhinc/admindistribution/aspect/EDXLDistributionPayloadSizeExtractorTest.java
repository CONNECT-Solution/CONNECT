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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.List;
import javax.activation.DataHandler;
import oasis.names.tc.emergency.edxl.de._1.AnyXMLType;
import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.emergency.edxl.de._1.NonXMLContentType;
import oasis.names.tc.emergency.edxl.de._1.XmlContentType;
import org.junit.Test;

/**
 * @author zmelnick
 *
 */
public class EDXLDistributionPayloadSizeExtractorTest {

    @Test
    public void emptyBuild() {
        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();
        assertNotNull(extractor);
    }

    @Test
    public void testPayloadSizeOnSingleNonXMLPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setNonXmlPayloadWithSize(alert, BigInteger.TEN);
        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();

        assertEquals(BigInteger.TEN.toString(), extractor.getPayloadSizes(alert).get(0));
    }

    @Test
    public void testPayloadSizeOnSingleNonXMLEmptyPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        ContentObjectType payload = new ContentObjectType();
        NonXMLContentType payloadContent = createMockNonXmlPayload();
        payload.setNonXMLContent(payloadContent);
        alert.getContentObject().add(payload);
        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();

        assertTrue(extractor.getPayloadSizes(alert).size() == 1);
        assertEquals("0", extractor.getPayloadSizes(alert).get(0));
    }

    @Test
    public void testPayloadSizeMultipleNonXMLPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setNonXmlPayloadWithSize(alert, BigInteger.TEN);

        alert.getContentObject().add(setNonXmlPayloadObject(BigInteger.TEN));

        BigInteger testSize = BigInteger.TEN;

        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();

        assertTrue(extractor.getPayloadSizes(alert).size() == 2);
        assertEquals(testSize.toString(), extractor.getPayloadSizes(alert).get(0));
        assertEquals(testSize.toString(), extractor.getPayloadSizes(alert).get(1));
    }

    @Test
    public void testPayloadSizeXmlPayload() {
        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();
        EDXLDistribution alert = new EDXLDistribution();

        setXmlPayload(alert);
        List<String> payloadSizes = extractor.getPayloadSizes(alert);
        assertEquals(1, payloadSizes.size());
        assertEquals("0", payloadSizes.get(0));
    }

    @Test
    public void testPayloadSizeMixedPayload() {
        EDXLDistribution alert = new EDXLDistribution();
        setXmlPayload(alert);
        setNonXmlPayloadWithSize(alert, BigInteger.TEN);

        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();

        List<String> payloadSizes = extractor.getPayloadSizes(alert);
        assertEquals(2, payloadSizes.size());
        assertEquals("0", payloadSizes.get(0));
        assertEquals("10", payloadSizes.get(1));
    }

    @Test
    public void testPayloadSizeContentXMLbothXMLContentTypesEmpty() {
        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();
        EDXLDistribution alert = new EDXLDistribution();

        final List<AnyXMLType> emptyList = mock(List.class);
        XmlContentType contentType = new XmlContentType() {
            @Override
            public List<AnyXMLType> getKeyXMLContent() {
                return emptyList;
            }

            @Override
            public List<AnyXMLType> getEmbeddedXMLContent() {
                return emptyList;
            }
        };

        when(emptyList.size()).thenReturn(0, 0);

        ContentObjectType payload = new ContentObjectType();
        payload.setXmlContent(contentType);
        alert.getContentObject().add(payload);

        List<String> payloadSizes = extractor.getPayloadSizes(alert);
        assertEquals(1, payloadSizes.size());
        assertEquals("0", payloadSizes.get(0));
    }

    @Test
    public void testPayloadSizeContentXMLMixedXMLContentTypeSizes() {
        EDXLDistributionPayloadSizeExtractor extractor = new EDXLDistributionPayloadSizeExtractor();
        EDXLDistribution alert = new EDXLDistribution();

        final List<AnyXMLType> keyList = mock(List.class, "keyList");
        final List<AnyXMLType> embeddedList = mock(List.class, "embeddedList");

        XmlContentType contentType = new XmlContentType() {
            @Override
            public List<AnyXMLType> getKeyXMLContent() {
                return keyList;
            }

            @Override
            public List<AnyXMLType> getEmbeddedXMLContent() {
                return embeddedList;
            }
        };

        when(keyList.size()).thenReturn(4);
        when(embeddedList.size()).thenReturn(5);

        ContentObjectType payload = new ContentObjectType();
        payload.setXmlContent(contentType);
        alert.getContentObject().add(payload);

        List<String> payloadSizes = extractor.getPayloadSizes(alert);
        assertEquals(1, payloadSizes.size());
        assertEquals("9", payloadSizes.get(0));
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
    private void setNonXmlPayloadWithSize(EDXLDistribution alert, BigInteger... payloadSizes) {
        for (BigInteger payloadSize : payloadSizes) {
            ContentObjectType payload = new ContentObjectType();
            NonXMLContentType payloadContent = createMockNonXmlPayload();

            payloadContent.setSize(payloadSize);

            payload.setNonXMLContent(payloadContent);
            alert.getContentObject().add(payload);
        }
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
