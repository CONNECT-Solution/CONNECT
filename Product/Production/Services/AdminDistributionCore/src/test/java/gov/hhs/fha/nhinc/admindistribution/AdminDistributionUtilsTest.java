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
package gov.hhs.fha.nhinc.admindistribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.activation.DataHandler;
import oasis.names.tc.emergency.edxl.de._1.ContentObjectType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.emergency.edxl.de._1.NonXMLContentType;
import org.apache.cxf.attachment.ByteDataSource;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class AdminDistributionUtilsTest {

    final String URI_STRING = "file:///nhin/temp";
    final String URI_SAVED_FILE_STRING = "file:///nhin/temp/saved";

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final LargeFileUtils mockFileUtils = context.mock(LargeFileUtils.class);

    @Test
    public void testConvertFileLocationToDataIfEnabled() throws Exception {
        AdminDistributionUtils adminUtils = createAdminDistributionUtils();

        context.checking(new Expectations() {
            {
                oneOf(mockFileUtils).isParsePayloadAsFileLocationEnabled();
                will(returnValue(true));

                oneOf(mockFileUtils).parseBase64DataAsUri(with(any(DataHandler.class)));
                will(returnValue(new URI(URI_SAVED_FILE_STRING)));

                oneOf(mockFileUtils).convertToDataHandler(with(any(File.class)));
                will(returnValue(createDataHandler()));
            }
        });

        EDXLDistribution request = createEDXLDistribution();
        adminUtils.convertFileLocationToDataIfEnabled(request);

        DataHandler dh = request.getContentObject().get(0).getNonXMLContent().getContentData();

        assertNotNull(dh);
    }

    @Test
    public void testConvertDataToFileLocationIfEnabled() throws Exception {
        AdminDistributionUtils adminUtils = createAdminDistributionUtils();

        context.checking(new Expectations() {
            {
                oneOf(mockFileUtils).isSavePayloadToFileEnabled();
                will(returnValue(true));

                oneOf(mockFileUtils).generateAttachmentFile();
                will(returnValue(new File(new URI(URI_SAVED_FILE_STRING))));

                oneOf(mockFileUtils).saveDataToFile(with(any(DataHandler.class)), with(any(File.class)));

                oneOf(mockFileUtils).convertToDataHandler(with(any(String.class)));
                will(returnValue(LargeFileUtils.getInstance().convertToDataHandler(URI_SAVED_FILE_STRING)));

            }
        });

        EDXLDistribution request = createEDXLDistribution();
        adminUtils.convertDataToFileLocationIfEnabled(request);

        URI contentUri = LargeFileUtils.getInstance()
                .parseBase64DataAsUri(request.getContentObject().get(0).getNonXMLContent().getContentData());

        assertEquals(URI_SAVED_FILE_STRING, contentUri.toString());
    }

    private EDXLDistribution createEDXLDistribution() throws IOException {
        LargeFileUtils fileUtils = LargeFileUtils.getInstance();

        EDXLDistribution request = new EDXLDistribution();

        ContentObjectType co = new ContentObjectType();
        NonXMLContentType nonXmlContentType = new NonXMLContentType();
        DataHandler dh = fileUtils.convertToDataHandler(URI_STRING);

        nonXmlContentType.setContentData(dh);
        co.setNonXMLContent(nonXmlContentType);
        request.getContentObject().add(co);

        return request;
    }

    private DataHandler createDataHandler() {
        ByteDataSource bds = new ByteDataSource(new byte[0]);
        return new DataHandler(bds);
    }

    private AdminDistributionUtils createAdminDistributionUtils() {
        return new AdminDistributionUtils() {
            @Override
            protected LargeFileUtils getLargeFileUtils() {
                return mockFileUtils;
            }
        };
    }

}
