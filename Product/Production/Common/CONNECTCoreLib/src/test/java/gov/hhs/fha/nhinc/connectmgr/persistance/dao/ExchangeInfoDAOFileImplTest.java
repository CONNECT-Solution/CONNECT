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
package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class ExchangeInfoDAOFileImplTest {

    private static final String TEST_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<exchangeInfo xmlns=\"urn:gov:hhs:fha:nhinc:exchange\" xmlns:ns2=\"urn:gov:hhs:fha:nhinc:exchange:directory\">"
        + "<exchanges><exchange type=\"uddi\"><organizationList/><disabled>false</disabled>"
        + "<lastUpdated>2017-10-23T16:10:41.968Z</lastUpdated></exchange>"
        + "<exchange type=\"fhir\"><organizationList/><disabled>false</disabled>"
        + "<lastUpdated>2017-10-23T16:10:41.968Z</lastUpdated></exchange>"
        + "</exchanges></exchangeInfo>";

    private static boolean ignoreWhitespaceSavedValue;
    private File tempFile = null;

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeInfoDAOFileImplTest.class);

    private String readFile(String file) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                stringBuilder.append(buffer);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return stringBuilder.toString();
    }

    private void writeFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter w = new OutputStreamWriter(fos);
        w.write(TEST_CONTENT);
        w.close();
    }

    @Before
    public void createTempFile() {
        try {
            tempFile = File.createTempFile(this.getClass().getSimpleName(), ".xml");
            LOG.info("Temp file was created. " + tempFile.getPath());
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

    @BeforeClass
    public static void saveXMLUnitWhitespaceSetting() {
        ignoreWhitespaceSavedValue = XMLUnit.getIgnoreWhitespace();
        XMLUnit.setIgnoreWhitespace(true);
    }

    @AfterClass
    public static void restoreXMLUnitWhitespaceSetting() {
        XMLUnit.setIgnoreWhitespace(ignoreWhitespaceSavedValue);
    }

    @Test
    public void readWriteTest() throws IOException, Exception {
        writeFile(tempFile.getPath());
        ExchangeInfoDAOFileImpl dao = ExchangeInfoDAOFileImpl.getInstance();
        dao.setFileName(tempFile.getPath());
        dao.saveExchangeInfo(dao.loadExchangeInfo());
        String fileContent = readFile(tempFile.getPath());
        XMLAssert.assertXMLEqual(TEST_CONTENT, fileContent);
    }

    @Test(expected = Exception.class)
    public void failOfNonexistentFileTest() throws Exception {
        ExchangeInfoDAOFileImpl dao = ExchangeInfoDAOFileImpl.getInstance();
        dao.setFileName("wrong-file-name");
        dao.loadExchangeInfo();
    }

    @After
    public void cleanup() {
        tempFile.delete();
        LOG.info("Temp file was deleted. " + tempFile.getPath());
    }
}
