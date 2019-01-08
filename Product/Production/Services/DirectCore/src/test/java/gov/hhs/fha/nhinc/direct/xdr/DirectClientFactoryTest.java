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
package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectAdapterFactory;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import java.net.URISyntaxException;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test {@link DirectAdapterFactory}.
 */
public class DirectClientFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(DirectClientFactoryTest.class);

    /**
     * Tear down keystore created in setup.
     */
    @AfterClass
    public static void tearDownClass() {
        DirectUnitTestUtil.removeSmtpAgentConfig();
    }

    /**
     * Test {@link DirectAdapterFactory#getDirectAdapter()}. Note: This test fails when run as part of the suite - it
     * seems that the config is loaded in another test before we are setting the system property for the
     * nhinc.properties.dir. Ignoring for now til more time can be spent on it.
     *
     * @throws URISyntaxException
     */
    @Test
    @Ignore
    public void canGetDirectClientFromFactory() throws URISyntaxException {

        LOG.info("nhinc.properties.dir...");
        String propertiesDir = DirectUnitTestUtil.getClassPath().getPath();
        System.setProperty("nhinc.properties.dir", propertiesDir);
        LOG.info("nhinc.properties.dir: " + propertiesDir);

        DirectAdapterFactory testDirectFactory = new DirectAdapterFactory();
        assertNotNull(testDirectFactory.getDirectReceiver());
        assertNotNull(testDirectFactory.getDirectSender());

    }

}
