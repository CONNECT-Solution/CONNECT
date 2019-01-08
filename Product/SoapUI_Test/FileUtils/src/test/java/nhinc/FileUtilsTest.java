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
package nhinc;

import static nhinc.FileUtils.getCheckpoint;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.internal.verification.AtLeast;

/**
 * @author Tran Tang
 *
 */
public class FileUtilsTest {
    private final Logger log = mock(Logger.class);
    private static final String RESOURCE_FOLDER = "src/test/resources/FileUtils";
    private static final String BACKUP_FOLDER = "src/test/resources/FileUtils/backupHere";
    private static final String EXCHANGE_FILE = "exchangeInfo.xml";
    private static final String EXCHANGE_FILE_ORG = "exchangeInfoOrg.xml";
    private static final String TEST_FILE = "testFile.properties";
    private static final String TEST_FILE_ORG = "testFileOrg.properties";

    @Test
    public void testFileRead() {
        String result = FileUtils.readFile(getResourceBy(TEST_FILE_ORG), log);
        verify(log).debug(getCheckpoint("readFile"));
    }

    @Test
    public void testFileCopyDelete() {
        String fileDestination = "copyTestFile.properties";

        FileUtils.copyFile(RESOURCE_FOLDER, TEST_FILE_ORG, BACKUP_FOLDER, fileDestination, log);
        verify(log).debug("File 'testFileOrg.properties' copied to 'copyTestFile.properties'.");

        FileUtils.deleteFile(BACKUP_FOLDER, fileDestination, log);
        verify(log).debug("file deleted: copyTestFile.properties");

        try{
            FileUtils.deleteFolder(getFileBy(BACKUP_FOLDER), log);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        verify(log).debug(getCheckpoint("deleteFolder"));

    }

    @Test
    public void testPropertyReadUpdate() {
        FileUtils.copyFile(RESOURCE_FOLDER, TEST_FILE_ORG, RESOURCE_FOLDER, TEST_FILE, log);

        String propertyKey = "updateProperty";
        String propertyValueOld = FileUtils.readProperty(RESOURCE_FOLDER, TEST_FILE, propertyKey, log);
        assertNotNull(propertyValueOld);

        String propertyValueUpdate = "update-to-this-value";
        FileUtils.updateProperty(RESOURCE_FOLDER, TEST_FILE, propertyKey, propertyValueUpdate, log);
        String propertyValue = FileUtils.readProperty(RESOURCE_FOLDER, TEST_FILE, propertyKey, log);
        assertEquals(propertyValue, propertyValueUpdate);

        FileUtils.updateProperty(RESOURCE_FOLDER, TEST_FILE, propertyKey, propertyValueOld, log);
        String propertyValueReturn = FileUtils.readProperty(RESOURCE_FOLDER, TEST_FILE, propertyKey, log);
        assertEquals(propertyValueReturn, propertyValueOld);

        FileUtils.deleteFile(RESOURCE_FOLDER, TEST_FILE, log);
    }

    @Test
    public void testSpringConfigUpdate() {
        String alias = "alias3";
        String oldName = "name3";
        String newName = "nameThree";
        FileUtils.updateSpringConfig(RESOURCE_FOLDER, "configFile.xml", alias, oldName, newName, log);
        verify(log).debug(getCheckpoint("updateSpringConfig-found"));

        FileUtils.updateSpringConfig(RESOURCE_FOLDER, "configFile.xml", alias, newName, oldName, log);
        verify(log, new AtLeast(1)).debug(getCheckpoint("updateSpringConfig"));
    }

    @Test
    public void testConnectionCreateOrUpdate() {
        String communityId = "2.2";
        String serviceName = "PatientDiscovery";
        String serviceUrl = "https://localhost:8181/Gateway/PatientDiscovery/1_0/NhinService/NhinPatientDiscovery";
        String defaultVersion = "1.0";

        FileUtils.copyFile(RESOURCE_FOLDER, EXCHANGE_FILE_ORG, RESOURCE_FOLDER, EXCHANGE_FILE, log);

        FileUtils.createOrUpdateConnection(EXCHANGE_FILE, RESOURCE_FOLDER, communityId, serviceName, serviceUrl,
            defaultVersion, log);
        verify(log).debug(getCheckpoint("checkServiceEndpoint-found: PatientDiscovery"));

        serviceName = "ServiceDoesNotExist";
        serviceUrl = "https://localhost:8181/Gateway/ServiceDoesNotExist/1_0/NhinService/NhinPatientDiscovery";
        FileUtils.createOrUpdateConnection(EXCHANGE_FILE, RESOURCE_FOLDER, communityId, serviceName, serviceUrl,
            defaultVersion, log);
        verify(log).debug(getCheckpoint("createOrUpdateConnection-createElementEndpointBy: ServiceDoesNotExist"));

        FileUtils.deleteFile(RESOURCE_FOLDER, EXCHANGE_FILE, log);

        verify(log, new AtLeast(2)).debug("Done createorupdate: exchangeInfo.xml");
    }

    @Test
    public void testConnectionConfigure() {
        String communityId = "2.2";
        String serviceName = "PatientDiscovery";
        String serviceUrl = "https://localhost:8181/Gateway/PatientDiscovery/1_0/NhinService/NhinPatientDiscovery";
        String defaultVersion = "1.0";

        FileUtils.copyFile(RESOURCE_FOLDER, EXCHANGE_FILE_ORG, RESOURCE_FOLDER, EXCHANGE_FILE, log);

        FileUtils.configureConnection(EXCHANGE_FILE, RESOURCE_FOLDER, communityId, serviceName, serviceUrl,
            defaultVersion,
            log);
        verify(log).debug(getCheckpoint("checkServiceEndpoint-found: PatientDiscovery"));

        serviceName = "ServiceDoesNotExist_configurationConnection";
        serviceUrl = "https://localhost:8181/Gateway/ServiceDoesNotExist/1_0/NhinService/NhinPatientDiscovery";
        FileUtils.configureConnection(EXCHANGE_FILE, RESOURCE_FOLDER, communityId, serviceName, serviceUrl,
            defaultVersion,
            log);
        verify(log).debug(getCheckpoint(
            "configureConnection-createElementEndpointBy: ServiceDoesNotExist_configurationConnection"));

        FileUtils.deleteFile(RESOURCE_FOLDER, EXCHANGE_FILE, log);

        verify(log, new AtLeast(2)).debug("Done configureConnection: exchangeInfo.xml");
    }

    private String getResourceBy(String fileName) {
        return MessageFormat.format("{0}/{1}", RESOURCE_FOLDER, fileName);
    }

    private File getFileBy(String fileOrFolder) {
        return new File(fileOrFolder);
    }

}
