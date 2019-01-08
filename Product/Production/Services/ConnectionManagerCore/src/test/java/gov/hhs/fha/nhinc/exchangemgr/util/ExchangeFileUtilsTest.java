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
package gov.hhs.fha.nhinc.exchangemgr.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class ExchangeFileUtilsTest {

    private static final String FILE_NAME = "exchangeInfo.xml";
    private static final String FILE_NAME_WITH_PATH = "/config/ExchangeFileUtilsTest/exchangeInfo.xml";
    private static final String DIRECTORY_NAME = "/config/ExchangeFileUtilsTest";

    @Test
    public void deleteBackupsWhenFilesExceedMaxNo() throws IOException, InterruptedException {
        ExchangeFileUtils fileUtils = new ExchangeFileUtils();
        createBackupFiles(fileUtils, 3);
        URL url = ExchangeFileUtilsTest.class.getClass().getResource(FILE_NAME_WITH_PATH);
        String exFile = URLDecoder.decode(url.getFile(), "UTF-8");
        fileUtils.deleteOldBackups((new File(exFile)).getParentFile(), 2);
        URL dir = ExchangeFileUtilsTest.class.getClass().getResource(DIRECTORY_NAME);
        File targetFolder = new File(URLDecoder.decode(dir.getFile(), "UTF-8"));
        assertEquals("Target folder should have 2 files", 2, targetFolder.
            listFiles().length);
    }

    private void createBackupFiles(ExchangeFileUtils fileUtils, int noOfBackups) throws IOException,
        InterruptedException {
        for (int i = 0; i < noOfBackups; i++) {
            createFile(fileUtils.generateUniqueFilename(FILE_NAME));
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private void createFile(String filename) throws UnsupportedEncodingException, IOException {
        URL url = ExchangeFileUtilsTest.class.getClass().getResource(DIRECTORY_NAME);
        File file = new File(URLDecoder.decode(url.getFile(), "UTF-8"), filename);
        file.createNewFile();
    }
}
