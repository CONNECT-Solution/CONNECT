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
package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectionManagerDAOFileImplTest {

    private static final String TEST_CONTENT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<businessDetail xmlns=\"urn:uddi-org:api_v3\" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\">" +
            "<businessEntity businessKey=\"key\">" +
            "<name xml:lang=\"EN\">BusinessEntity 1</name>" +
            "</businessEntity>" +
            "<businessEntity businessKey=\"key\">" +
            "<name xml:lang=\"EN\">BusinessEntity 2</name>" +
            "</businessEntity>" +
            "</businessDetail>";
    private static final String TEST_CONTENT_2 =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<businessDetail xmlns=\"urn:uddi-org:api_v3\" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\">" +
            "	<businessEntity businessKey=\"uddiztestnhieonenodezum:oid:2.1 6.840.1 .1 13883.3.166.4\">" +
            "			  <name xml:lang=\"en\">Test NHIE One</name>" +
            "			  <name xml:lang=\"en\">TestNHIE_1</name>" +
            "			    <contacts>" +
            "			      <contact useType=\"\">" +
            "			        <personName xml:lang=\"en\">John Doe</personName>" +
            "			        <phone>111-111-1111</phone>" +
            "			        <email>john.doe@testnhieone.oom</email>" +
            "			        <address xml:lang=\"en\" useType=\"\" sortCode=\"\">" +
            "			          <addressLine>112 W. Main Street</addressLine>" +
            "			          <addressLine>Somewhere, AL 21 11 1</addressLine>" +
            "			        </address>" +
            "			      </contact>" +
            "			    </contacts>" +
            "			    <businessServices>" +
            "			      <businessService servioeKey=\"uddi:testnhieonenode:PatientDiscovery\"" +
            "			        businessKey=\"uddi:tes1nhieonenode:um:oid:2.16.840.1.113883.3.166.4\">" +
            "			        <name xml:lang=\"en\">PatientDisoovery</name>" +
            "			        <bindingTemplates>" +
            "			          <bindingTemplate bindingKey=\"uddi:00f27b20-c31e-1 1de-a851-5ad17990a84c\"" +
            "			            sarvioeKey=\"uddi:testnhieonenode:PatientDiscovery\">" +
            "			            <accessPoint useType=\"endPoint\">https:/testonenhie.comlPa1ientDiscoveryRespondingGatawayService" +
            "			          </accessPoint>" +
            "			            <categoryBag>" +
            "			              <keyedReferenoe tModelKey=\"uddi:nhin:versionofservioe\" keyName=\"\" keyValue=\"2.0\"/>" +
            "			            </categoryBag>" +
            "			          </bindingTemplate>" +
            "			        </bindingTemplates>" +
            "			      </businessService>" +
            "			    </businessServices>" +
            "			    <identityBag>" +
            "			      <keyedReferenoe tModelKey=\"uddi:nhinznhiemomeoommunityid\" keyName=\"\" keyValue=\"um:oid:2.16.840.1.113883.3.166.4\"/>" +
            "			    </identityBag>" +
            "			    <categoryBag>" +
            "			      <keyedReference tModelKey=\"uddi:nhin:nhie:publickey\" keyName=\"\" keyValue=\"305C300D06092AB64886F70D01010105" +
            "			        00034B003048024100D1565D698FA304 19CED840497E346 FOOFSF451 D6C43315 8508130639AE25EEE009FD9A574251 12" +
            "			        7A2F9A14A047ADBB2DC39253D2658E8F 4AOBC6DDF!D09DEE7F020301 0001\"/>" +
            "			        <keyedReferenoe tModelKey=\"uddi:uddi.org:ubr:categorization:iso3166\" keyName=\"Alabama\" keyValue=\"US-AL\"/>" +
            "			    </categoryBag>" +
            "		</businessEntity>" +
            "	</businessDetail>";
    private File tempFile = null;
    private Log log = null;

    private Log getLog() {
        if (log == null) {
            log = LogFactory.getLog(this.getClass());
        }
        return log;
    }

    private String readFile(String file) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String buffer = null;
            while ((buffer = reader.readLine()) != null) {
                stringBuilder.append(buffer);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return stringBuilder.toString();
    }

    private void writeFileWithDelay(String fileName, long delay) throws IOException {
        File f = new File(fileName);
        FileOutputStream fos = new FileOutputStream(f);
        OutputStreamWriter w = new OutputStreamWriter(fos);
        w.write(TEST_CONTENT);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            log.error(ex);
        }
        w.close();
    }

    private void startWritingThread(final long delay) {
        Runnable worker = new Runnable() {

            public void run() {
                try {
                    writeFileWithDelay(tempFile.getPath(), delay);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(worker);
        thread.start();
    }

    @Before
    public void createTempFile() {
        try {
            tempFile = File.createTempFile(this.getClass().getSimpleName(), ".xml");
            getLog().debug("Temp file was created. " + tempFile.getPath());
        } catch (IOException e) {
            getLog().error("Unable to create temp file.");
        }
    }

    @Test
    @Ignore
    public void readWriteTest() throws IOException, Exception {
        writeFileWithDelay(tempFile.getPath(), 0);
        InternalConnectionInfoDAOFileImpl dao = InternalConnectionInfoDAOFileImpl.getInstance();
        dao.setFileName(tempFile.getPath());
        dao.saveBusinessDetail(dao.loadBusinessDetail());
        String fileContent = readFile(tempFile.getPath());
        assertEquals(TEST_CONTENT, fileContent);
    }

    @Test(expected = Exception.class)
    public void failOfNonexistentFileTest() throws Exception {
        InternalConnectionInfoDAOFileImpl dao = InternalConnectionInfoDAOFileImpl.getInstance();
        dao.setFileName("wrong-file-name");
        dao.loadBusinessDetail();
    }

    @After
    public void cleanup() {
        tempFile.delete();
        getLog().debug("Temp file was deleted. " + tempFile.getPath());
    }
}
