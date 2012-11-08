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
package gov.hhs.fha.nhinc.direct;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test {@link DirectMailClient} with Spring Framework Configuration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test.direct.appcontext.xml")
public class DirectMailClientSpringTest {

    @Autowired
    private DirectMailClient intDirectMailClient;

    @Autowired
    private DirectMailClient extDirectMailClient;
    
    /**
     * Set up keystore for test.
     */
    @BeforeClass
    public static void setUpClass() {
        writeSmtpAgentConfig();
    }

    /**
     * Test that we can get an external mail client with spring.
     */
    @Test
    public void canGetExternalMailClient() {
        assertNotNull(extDirectMailClient);
    }
    
    /**
     * Test that we can get an internal mail client with spring.
     */
    @Test
    public void canGetInternalMailClient() {
        assertNotNull(intDirectMailClient);
    }
    
    /**
     * Test that the two mail clients are distinct instances.
     */
    @Test
    public void canDistinguishInternalExternal() {
        assertNotSame(intDirectMailClient, extDirectMailClient);        
    }
    
    /**
     * Tear down keystore created in setup.
     */
    @AfterClass
    public static void tearDownClass() {
        removeSmtpAgentConfig();
    }
    
    /**
     * The keystores references in smtp.agent.config.xml are fully qualified, so we have to make an absolute path
     * for them from a relative path in order to use inside a junit test. The template config file references the 
     * keystore with a placeholder {jks.keystore.path} which we will replace with the classpath used by this test.
     */
    private static void writeSmtpAgentConfig() {
        String classpath = getClassPath();
        try {
            String smtpAgentConfigTmpl = FileUtils.readFileToString(new File(classpath + "smtp.agent.config.tmpl.xml"));
            FileUtils.writeStringToFile(new File(classpath + "smtp.agent.config.xml"),
                    smtpAgentConfigTmpl.replaceAll("\\{jks.keystore.path\\}", classpath));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Delete the auto-generated smtp.agent.config.xml once the test is complete.
     */
    private static void removeSmtpAgentConfig() {
        FileUtils.deleteQuietly(new File(getClassPath() + "smtp.agent.config.xml"));
    }
    
    private static String getClassPath() {
        return DirectMailClientSpringTest.class.getProtectionDomain().getCodeSource().getLocation().getPath()
                .replaceAll("/", File.separator);
    }
}
