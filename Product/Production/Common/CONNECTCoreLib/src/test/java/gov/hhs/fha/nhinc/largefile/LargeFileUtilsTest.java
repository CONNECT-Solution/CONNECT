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
package gov.hhs.fha.nhinc.largefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.activation.DataHandler;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

/**
 * @author akong
 *
 */
public class LargeFileUtilsTest {
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testParseBase64DataAsUri() throws IOException, URISyntaxException {
        String URI_STRING = "file:///nhin/temp";

        LargeFileUtils fileUtils = new LargeFileUtils();

        DataHandler dh = fileUtils.convertToDataHandler(URI_STRING);
        URI uri = fileUtils.parseBase64DataAsUri(dh);

        assertEquals(uri.toString(), URI_STRING);
    }

    @Test(expected = URISyntaxException.class)
    public void testParseBase64DataAsUri_badUri() throws IOException, URISyntaxException {
        String BAD_URI_STRING = "::::::";

        LargeFileUtils fileUtils = new LargeFileUtils();

        DataHandler dh = fileUtils.convertToDataHandler(BAD_URI_STRING);
        fileUtils.parseBase64DataAsUri(dh);
    }

    @Test
    public void testSaveDataToFile() throws Exception {
        String RANDOM_DATA = "1234567890";

        LargeFileUtils fileUtils = new LargeFileUtils();

        File tempFile = File.createTempFile("temp", ".tmp");
        tempFile.deleteOnExit();

        DataHandler dh = fileUtils.convertToDataHandler(RANDOM_DATA);
        fileUtils.saveDataToFile(dh, tempFile);

        assertTrue(tempFile.exists());
        assertTrue(tempFile.length() > 0);

        byte[] dataBytes = fileUtils.convertToBytes(dh);
        String string = new String(dataBytes);

        assertEquals(string, RANDOM_DATA);
    }

}
