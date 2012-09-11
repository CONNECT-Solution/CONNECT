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
package gov.hhs.fha.nhinc.patientdiscovery.passthru.proxy;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * @author mflynn02
 */
public class NhincPatientDiscoveryProxyObjectFactoryTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PassthruPatientDiscoveryProxy mockProxy = context.mock(PassthruPatientDiscoveryProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext() {

        @Override
        public Object getBean(String beanName) {
            return mockProxy;
        }
    };

    /**
     * Test of getConfigFileName method, of class NhincPatientDiscoveryProxyObjectFactory.
     */
    @Test
    public void testGetConfigFileName() {
        final ApplicationContext mockContext = context.mock(ApplicationContext.class);
        PassthruPatientDiscoveryProxyObjectFactory proxyFactory = new PassthruPatientDiscoveryProxyObjectFactory() {
            @Override
            protected Log createLogger() {
                return mockLog;
            }

            @Override
            protected String getConfigFileName() {
                return "TEST_CONFIG_FILE_NAME";
            }

            @Override
            protected ApplicationContext getContext() {
                return mockContext;
            }
        };
        try {
            assertEquals("Config file name", "TEST_CONFIG_FILE_NAME", proxyFactory.getConfigFileName());
        } catch (Throwable t) {
            System.out.println("Error running testGetConfigFileName test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetConfigFileName test: " + t.getMessage());
        }
    }

    /**
     * Test of getNhincPatientDiscoveryProxy method, of class NhincPatientDiscoveryProxyObjectFactory.
     */
    @Test
    public void testGetNhincPatientDiscoveryProxyHappy() {
        PassthruPatientDiscoveryProxyObjectFactory proxyFactory = new PassthruPatientDiscoveryProxyObjectFactory() {
            @Override
            protected Log createLogger() {
                return mockLog;
            }

            @Override
            protected <T extends Object> T getBean(String beanName, Class<T> type) {
                return type.cast(mockProxy);
            }
        };
        try {
            PassthruPatientDiscoveryProxy proxy = proxyFactory.getNhincPatientDiscoveryProxy();
            assertNotNull("NhincPatientDiscoveryProxy was null", proxy);
        } catch (Throwable t) {
            System.out.println("Error running testGetNhincPatientDiscoveryProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetNhincPatientDiscoveryProxyHappy test: " + t.getMessage());
        }
    }

}
